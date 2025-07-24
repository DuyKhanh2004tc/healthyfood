package controller;

import dal.DAOOrder;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.sql.SQLException;
import model.Order;
import model.User;
import utils.Mail;

/**
 * @author Cuong
 */
public class SellerUpdateStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRole().getId() != 5) { // Kiểm tra role_id = 5 (Seller)
            response.sendRedirect("login");
            return;
        }
        int sellerId = user.getId();
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int newStatusId = Integer.parseInt(request.getParameter("statusId"));

        DAOOrder dao = DAOOrder.INSTANCE;
        try {
            // Cập nhật trạng thái
            dao.updateOrderStatus(orderId, newStatusId, -1);

            // Lấy thông tin đơn hàng để gửi email
            Order order = dao.getOrderById(orderId);
            if (order != null) {
                // Lấy tên trạng thái mới
                String statusName = dao.getOrderStatusName(newStatusId);

                // Gửi email xác nhận
                String email = order.getReceiverEmail();
                String userName = order.getReceiverName();
                StringBuilder content = new StringBuilder();
                content.append("Dear ").append(userName).append(",\n\n");
                content.append("The status of your order (Order ID: ").append(orderId).append(") has been updated.\n\n");
                content.append("New Status: ").append(statusName).append("\n");
                content.append("Total Amount: $").append(order.getTotalAmount()).append("\n");
                content.append("Shipping Address: ").append(order.getShippingAddress()).append("\n");
                content.append("Phone: ").append(order.getReceiverPhone()).append("\n");
                content.append("Payment Method: ").append(order.getPaymentMethod()).append("\n\n");

                content.append("If you have any questions, feel free to contact us!\n\n");
                content.append("Best regards,\nHealthy Food Team");

                Mail.sendMail(email, "[HealthyFood] Order Status Update - Order #" + orderId, content.toString());
            }

            // Giữ nguyên logic chuyển trang
            switch (newStatusId) {
                case 2:
                    response.sendRedirect("ConfirmedOrders");
                    break;
                case 3:
                    response.sendRedirect("ProcessingOrders");
                    break;
                case 4:
                    response.sendRedirect("SellerWaitingOrders");
                    break;
                case 7:
                    response.sendRedirect("SellerCanceledOrders");
                    break;
                default:
                    response.sendRedirect("ConfirmedOrders");
                    break;
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error updating status: " + e.getMessage());
            request.getRequestDispatcher("/view/confirmedOrders.jsp").forward(request, response);
        }
    }
}