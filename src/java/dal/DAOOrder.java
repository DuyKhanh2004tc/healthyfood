package dal;

import model.Order;
import model.OrderDetail;
import model.OrderStatus;
import model.User;
import model.Product;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class DAOOrder {

    public static DAOOrder INSTANCE = new DAOOrder();
    private static Connection con;
    private String status = "OK";

    public DAOOrder() {
        if (INSTANCE == null) {
            try {
                con = new DBContext().connect;
                if (con == null) {
                    throw new SQLException("Database connection is null");
                }
                System.out.println("Database connection established successfully at " + new java.util.Date());
            } catch (SQLException e) {
                System.err.println("Failed to establish database connection: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            INSTANCE = this;
        }
    }

    public int insertOrder(Order order) {
        int orderId = -1;
        String sql = "INSERT INTO Orders (user_id, total_amount, payment_method, status_id, shipper_id, "
                + "receiver_name, receiver_phone, receiver_email, shipping_address, delivery_message) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            st.setObject(1, order.getUser() != null ? order.getUser().getId() : null, Types.INTEGER);
            st.setDouble(2, order.getTotalAmount());
            st.setString(3, order.getPaymentMethod());
            st.setInt(4, order.getStatus().getId());
            st.setObject(5, order.getShipper() != null ? order.getShipper().getId() : null, Types.INTEGER);
            st.setString(6, order.getReceiverName());
            st.setString(7, order.getReceiverPhone());
            st.setString(8, order.getReceiverEmail());
            st.setString(9, order.getShippingAddress());
            st.setString(10, order.getDeliveryMessage());
            int insertedRows = st.executeUpdate();
            if (insertedRows > 0) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error inserting order: " + e.getMessage());
            e.printStackTrace();
        }
        return orderId;
    }

    public void insertOrderDetail(OrderDetail od) {
        String sql = "INSERT INTO OrderDetail (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, od.getOrder().getId());
            st.setInt(2, od.getProduct().getId());
            st.setInt(3, od.getQuantity());
            st.setDouble(4, od.getPrice());
            st.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error inserting order detail: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByStatusIn(List<Integer> statusIds) throws SQLException {
        List<Order> orders = new ArrayList<>();
        if (statusIds == null || statusIds.isEmpty()) {
            System.out.println("No statusIds provided for getOrdersByStatusIn at " + new java.util.Date());
            return orders;
        }
        String sql = "SELECT o.*, os.status_name, os.description, u.name AS user_name, s.name AS shipper_name, "
                + "o.delivery_message "
                + "FROM [dbo].[Orders] o "
                + "LEFT JOIN [dbo].[OrderStatus] os ON o.status_id = os.id "
                + "LEFT JOIN [dbo].[Users] u ON o.user_id = u.id "
                + "LEFT JOIN [dbo].[Users] s ON o.shipper_id = s.id "
                + "WHERE o.status_id IN (" + String.join(",", statusIds.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (!rs.isBeforeFirst()) {
                System.out.println("No orders found for statusIds: " + statusIds + " at " + new java.util.Date());
            }
            while (rs.next()) {
                Order order = new Order();
                order.setId(rs.getInt("id"));
                order.setOrderDate(rs.getTimestamp("order_date"));
                order.setTotalAmount(rs.getDouble("total_amount"));
                order.setPaymentMethod(rs.getString("payment_method"));
                order.setReceiverName(rs.getString("receiver_name"));
                order.setReceiverPhone(rs.getString("receiver_phone"));
                order.setReceiverEmail(rs.getString("receiver_email"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setDeliveryMessage(rs.getString("delivery_message"));
                order.setStatus(new OrderStatus(rs.getInt("status_id"), rs.getString("status_name"), rs.getString("description")));
                int userId = rs.getInt("user_id");
                User user = rs.wasNull() ? null : DAOUser.getInstance().getUserById(userId);
                if (user != null) {
                    order.setUser(user);
                } else {
                    System.out.println("Warning: No user found for user_id: " + userId);
                }
                int shipperId = rs.getInt("shipper_id");
                User shipper = rs.wasNull() ? null : DAOUser.getInstance().getUserById(shipperId);
                if (shipper != null) {
                    order.setShipper(shipper);
                } else {
                    System.out.println("Warning: No shipper found for shipper_id: " + (rs.wasNull() ? "NULL" : shipperId));
                }
                // Tải chi tiết đơn hàng
                order.setOrderDetails(getOrderDetails(rs.getInt("id")));
                orders.add(order);
                System.out.println("Retrieved order ID: " + order.getId() + ", Status: " + order.getStatus().getStatusName() + ", Delivery Message: " + order.getDeliveryMessage());
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getOrdersByStatusIn: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return orders;
    }

    public void updateOrderStatus(int orderId, int statusId) throws SQLException {
        String checkSql = "SELECT status_id FROM [dbo].[Orders] WHERE id = ?";
        int currentStatusId = 0;
        try (PreparedStatement checkSt = con.prepareStatement(checkSql)) {
            checkSt.setInt(1, orderId);
            try (ResultSet rs = checkSt.executeQuery()) {
                if (rs.next()) {
                    currentStatusId = rs.getInt("status_id");
                } else {
                    throw new SQLException("No order found with ID: " + orderId);
                }
            }
        }

        DAOOrderStatus daoStatus = DAOOrderStatus.INSTANCE;
        if (daoStatus.isValidTransition(currentStatusId, statusId)) {
            String sql = "UPDATE [dbo].[Orders] SET status_id = ? WHERE id = ?";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setInt(1, statusId);
                st.setInt(2, orderId);
                int updatedRows = st.executeUpdate();
                if (updatedRows == 0) {
                    throw new SQLException("Failed to update order with ID: " + orderId);
                }
            }
        } else {
            throw new SQLException("Invalid status transition from " + currentStatusId + " to " + statusId);
        }
    }

    public void updateOrderStatus(int orderId, int statusId, int shipperId) throws SQLException {
        String sql;
        if (shipperId == -1) {
            sql = "UPDATE [dbo].[Orders] SET status_id = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, statusId);
                ps.setInt(2, orderId);
                ps.executeUpdate();
            }
        } else {
            sql = "UPDATE [dbo].[Orders] SET status_id = ?, shipper_id = ? WHERE id = ?";
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, statusId);
                ps.setInt(2, shipperId);
                ps.setInt(3, orderId);
                ps.executeUpdate();
            }
        }
    }

    public List<Order> getOrdersByShipper(int shipperId, int page, int pageSize) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, os.status_name, os.description, u.name AS user_name, s.name AS shipper_name, s.phone AS shipper_phone, "
                + "o.delivery_message "
                + "FROM Orders o "
                + "JOIN OrderStatus os ON o.status_id = os.id "
                + "LEFT JOIN Users u ON o.user_id = u.id "
                + "LEFT JOIN Users s ON o.shipper_id = s.id "
                + "WHERE o.shipper_id = ? AND o.status_id IN (6, 7, 8) "
                + "ORDER BY o.order_date DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, shipperId);
            st.setInt(2, (page - 1) * pageSize);
            st.setInt(3, pageSize);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUser(new User(rs.getInt("user_id"), rs.getString("user_name"), null, null, null, null, null, false, null, null));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setStatus(new OrderStatus(rs.getInt("status_id"), rs.getString("status_name"), rs.getString("description")));
                    if (rs.getInt("shipper_id") != 0) {
                        order.setShipper(new User(rs.getInt("shipper_id"), rs.getString("shipper_name"), null, null, rs.getString("shipper_phone"), null, null, false, null, null));
                    }
                    order.setReceiverName(rs.getString("receiver_name"));
                    order.setReceiverPhone(rs.getString("receiver_phone"));
                    order.setReceiverEmail(rs.getString("receiver_email"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setDeliveryMessage(rs.getString("delivery_message"));
                    order.setOrderDetails(getOrderDetails(rs.getInt("id")));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public int getTotalOrdersByShipper(int shipperId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM Orders WHERE shipper_id = ? AND status_id IN (6, 7, 8)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, shipperId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    private List<OrderDetail> getOrderDetails(int orderId) throws SQLException {
        List<OrderDetail> details = new ArrayList<>();
        if (orderId <= 0) {
            System.err.println("Invalid orderId: " + orderId + " at " + new java.util.Date());
            return details;
        }
        String sql = "SELECT od.id, p.id AS product_id, p.name, od.quantity, od.price "
                + "FROM OrderDetail od JOIN Product p ON od.product_id = p.id "
                + "WHERE od.order_id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, orderId);
            try (ResultSet rs = st.executeQuery()) {
                if (!rs.isBeforeFirst()) {
                    System.out.println("No order details found for orderId: " + orderId + " at " + new java.util.Date());
                }
                while (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    OrderDetail detail = new OrderDetail();
                    detail.setId(rs.getInt("id"));
                    detail.setProduct(product);
                    detail.setQuantity(rs.getInt("quantity"));
                    detail.setPrice(rs.getDouble("price"));
                    details.add(detail);
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getOrderDetails for orderId " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return details;
    }

    public List<Order> getAllOrders(int page, int pageSize) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, os.status_name, os.description, u.name AS user_name, s.name AS shipper_name, s.phone AS shipper_phone, "
                + "o.delivery_message "
                + "FROM Orders o "
                + "JOIN OrderStatus os ON o.status_id = os.id "
                + "LEFT JOIN Users u ON o.user_id = u.id "
                + "LEFT JOIN Users s ON o.shipper_id = s.id "
                + "ORDER BY o.order_date DESC "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, (page - 1) * pageSize);
            st.setInt(2, pageSize);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()) {
                    Order order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUser(new User(rs.getInt("user_id"), rs.getString("user_name"), null, null, null, null, null, false, null, null));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setStatus(new OrderStatus(rs.getInt("status_id"), rs.getString("status_name"), rs.getString("description")));
                    if (rs.getInt("shipper_id") != 0) {
                        order.setShipper(new User(rs.getInt("shipper_id"), rs.getString("shipper_name"), null, null, rs.getString("shipper_phone"), null, null, false, null, null));
                    }
                    order.setReceiverName(rs.getString("receiver_name"));
                    order.setReceiverPhone(rs.getString("receiver_phone"));
                    order.setReceiverEmail(rs.getString("receiver_email"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setDeliveryMessage(rs.getString("delivery_message"));
                    order.setOrderDetails(getOrderDetails(rs.getInt("id")));
                    orders.add(order);
                }
            }
        }
        return orders;
    }

    public int getTotalOrders() throws SQLException {
        String sql = "SELECT COUNT(*) FROM Orders";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }

    public Order getOrderById(int orderId) throws SQLException {
        Order order = null;
        String sql = "SELECT o.*, os.status_name, os.description, u.name AS user_name, s.name AS shipper_name, s.phone AS shipper_phone, "
                + "o.delivery_message "
                + "FROM Orders o "
                + "JOIN OrderStatus os ON o.status_id = os.id "
                + "LEFT JOIN Users u ON o.user_id = u.id "
                + "LEFT JOIN Users s ON o.shipper_id = s.id "
                + "WHERE o.id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, orderId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    order = new Order();
                    order.setId(rs.getInt("id"));
                    order.setUser(new User(rs.getInt("user_id"), rs.getString("user_name"), null, null, null, null, null, false, null, null));
                    order.setOrderDate(rs.getTimestamp("order_date"));
                    order.setTotalAmount(rs.getDouble("total_amount"));
                    order.setPaymentMethod(rs.getString("payment_method"));
                    order.setStatus(new OrderStatus(rs.getInt("status_id"), rs.getString("status_name"), rs.getString("description")));
                    if (rs.getInt("shipper_id") != 0) {
                        order.setShipper(new User(rs.getInt("shipper_id"), rs.getString("shipper_name"), null, null, rs.getString("shipper_phone"), null, null, false, null, null));
                    }
                    order.setReceiverName(rs.getString("receiver_name"));
                    order.setReceiverPhone(rs.getString("receiver_phone"));
                    order.setReceiverEmail(rs.getString("receiver_email"));
                    order.setShippingAddress(rs.getString("shipping_address"));
                    order.setDeliveryMessage(rs.getString("delivery_message"));
                    order.setOrderDetails(getOrderDetails(rs.getInt("id")));
                } else {
                    System.out.println("No order found with ID: " + orderId + " at " + new java.util.Date());
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Error in getOrderById for orderId " + orderId + ": " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return order;
    }

    public boolean isProductOrdered(int productId) {
        String sql = "SELECT 1 FROM OrderDetail od "
                + "JOIN Orders o ON od.order_id = o.id "
                + "WHERE od.product_id = ? AND o.status_id = 6";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
            try (ResultSet rs = st.executeQuery()) {
                return rs.next(); // Trả về true nếu có ít nhất một đơn hàng đã thanh toán chứa sản phẩm này
            }
        } catch (SQLException e) {
            System.err.println("Error checking if product is ordered and paid: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

}
