//package controller;
//
//import dal.DAOOrder;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import model.Order;
//import model.OrderStatus;
//
//@WebServlet("/SellerUpdateStatusServlet") // Đảm bảo URL pattern khớp với đường dẫn bạn gọi
//public class SellerUpdateStatusServlet extends HttpServlet {
//
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        DAOOrder dao = DAOOrder.INSTANCE;
//        try {
//            List<Order> orders = dao.getOrdersByStatusIn(List.of(1, 2, 3, 4));
//            List<OrderStatus> allStatuses = new ArrayList<>();
//            allStatuses.add(new OrderStatus(1, "Pending Confirmation", "Order is awaiting seller confirmation"));
//            allStatuses.add(new OrderStatus(2, "Confirmed", "Order has been confirmed by seller"));
//            allStatuses.add(new OrderStatus(3, "Processing", "Order is being prepared by seller"));
//            allStatuses.add(new OrderStatus(4, "Waiting for Delivery", "Order is ready for shipper to pick up"));
//            allStatuses.add(new OrderStatus(5, "Delivering", "Order is being delivered by shipper"));
//            allStatuses.add(new OrderStatus(6, "Delivered", "Order has been delivered"));
//            allStatuses.add(new OrderStatus(7, "Cancelled", "Order has been cancelled"));
//            allStatuses.add(new OrderStatus(8, "Returned", "Order has been returned by customer"));
//
//            List<List<OrderStatus>> validStatusesList = new ArrayList<>();
//            for (Order order : orders) {
//                List<OrderStatus> validStatuses = new ArrayList<>();
//                int currentStatusId = order.getStatus().getId();
//                switch (currentStatusId) {
//                    case 1:
//                        validStatuses.add(allStatuses.get(1)); // Confirmed
//                        validStatuses.add(allStatuses.get(6)); // Cancelled
//                        break;
//                    case 2:
//                        validStatuses.add(allStatuses.get(2)); // Confirmed
//                        validStatuses.add(allStatuses.get(3)); // Processing
//                        validStatuses.add(allStatuses.get(6)); // Cancelled
//                        break;
//                    case 3:
//                        validStatuses.add(allStatuses.get(3)); // Processing
//                        validStatuses.add(allStatuses.get(4)); // Waiting for Delivery
//                        validStatuses.add(allStatuses.get(6)); // Cancelled
//                        break;
//                    case 4:
//                        validStatuses.add(allStatuses.get(4)); // Waiting for Delivery
//                        validStatuses.add(allStatuses.get(5)); // Delivering
//                        break;
//                    case 5:
//                        validStatuses.add(allStatuses.get(5)); // Delivering
//                        validStatuses.add(allStatuses.get(6)); // Delivered
//                        break;
//                    case 6:
//                        validStatuses.add(allStatuses.get(6)); // Delivered
//                        validStatuses.add(allStatuses.get(7)); // Returned
//                        break;
//                    case 7:
//                    case 8:
//                        validStatuses.add(order.getStatus()); // Giữ nguyên
//                        break;
//                }
//                validStatusesList.add(validStatuses);
//            }
//
//            request.setAttribute("orders", orders);
//            request.setAttribute("validStatusesList", validStatusesList);
//            request.setAttribute("allStatuses", allStatuses);
//        } catch (SQLException e) {
//            request.setAttribute("error", "Error fetching orders: " + e.getMessage());
//            e.printStackTrace();
//        }
//        request.getRequestDispatcher("/view/sellerUpdateStatus.jsp").forward(request, response);
//    }
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//        String orderIdParam = request.getParameter("orderId");
//        String statusIdParam = request.getParameter("statusId");
//        if (orderIdParam != null && statusIdParam != null) {
//            try {
//                int orderId = Integer.parseInt(orderIdParam);
//                int statusId = Integer.parseInt(statusIdParam);
//                DAOOrder dao = DAOOrder.INSTANCE;
//                dao.updateOrderStatus(orderId, statusId);
//                request.setAttribute("message", "Status updated successfully for Order ID: " + orderId);
//            } catch (NumberFormatException e) {
//                request.setAttribute("error", "Invalid order ID or status ID format: " + e.getMessage());
//            } catch (SQLException e) {
//                request.setAttribute("error", "Error updating status: " + e.getMessage());
//            }
//        } else {
//            request.setAttribute("error", "Missing order ID or status ID!");
//        }
//        doGet(request, response); // Tải lại trang với thông báo
//    }
//}