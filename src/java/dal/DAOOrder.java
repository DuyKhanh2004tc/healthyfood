package dal;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Order;
import model.OrderDetail;
import model.OrderStatus;
import model.User;

/**
 *
 * @author ASUS
 */
public class DAOOrder {

    public static DAOOrder INSTANCE = new DAOOrder();
    private static Connection con;
    private String status = "OK";

    public DAOOrder() {
        if (INSTANCE == null) {
            con = new DBContext().connect;
        } else {
            INSTANCE = this;
        }
    }

    public int insertOrder(Order order) {
        int orderId = -1;
        String sql = "INSERT INTO Orders (user_id, total_amount, payment_method, status_id, shipper_id, "
                + "receiver_name, receiver_phone, receiver_email, shipping_address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            if (order.getUser() != null) {
                st.setInt(1, order.getUser().getId());
            } else {
                st.setNull(1, Types.INTEGER);
            }
            st.setDouble(2, order.getTotalAmount());
            st.setString(3, order.getPaymentMethod());
            st.setInt(4, order.getStatus().getId());
            if (order.getShipper() != null) {
                st.setInt(5, order.getShipper().getId());
            } else {
                st.setNull(5, Types.INTEGER);
            }
            st.setString(6, order.getReceiverName());
            st.setString(7, order.getReceiverPhone());
            st.setString(8, order.getReceiverEmail());
            st.setString(9, order.getShippingAddress());

            int insertedRows = st.executeUpdate();
            if (insertedRows > 0) {
                try (ResultSet rs = st.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }

    public void insertOrderDetail(OrderDetail od) {
        String sql = "INSERT INTO OrderDetail (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1,od.getOrder().getId());
            st.setInt(2,od.getProduct().getId());
            st.setInt(3,od.getQuantity());
            st.setDouble(4,od.getPrice());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Order> getOrdersByStatusIn(List<Integer> statusIds) throws SQLException {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, os.status_name, os.description, u.name AS user_name, s.name AS shipper_name " +
                     "FROM [dbo].[Orders] o " +
                     "LEFT JOIN [dbo].[OrderStatus] os ON o.status_id = os.id " +
                     "LEFT JOIN [dbo].[Users] u ON o.user_id = u.id " +
                     "LEFT JOIN [dbo].[Users] s ON o.shipper_id = s.id " +
                     "WHERE o.status_id IN (" + String.join(",", statusIds.stream().map(String::valueOf).toArray(String[]::new)) + ")";
        try (PreparedStatement st = con.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {
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
                order.setStatus(new OrderStatus(rs.getInt("status_id"), rs.getString("status_name"), rs.getString("description")));
                order.setUser(new User(rs.getInt("user_id"), rs.getString("user_name"), null, null, null, null, null, false, null, null));
                if (rs.getInt("shipper_id") != 0) {
                    order.setShipper(new User(rs.getInt("shipper_id"), rs.getString("shipper_name"), null, null, null, null, null, false, null, null));
                }
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            status = "Error: Unable to retrieve orders - " + e.getMessage();
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

}
