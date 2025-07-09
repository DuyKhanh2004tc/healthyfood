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
import model.User;

/**
 * @author Cuong
 */
@WebServlet("/ShipperUpdateStatusServlet")
public class ShipperUpdateStatusServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (user.getRole().getId() != 6) { // Kiểm tra role_id = 6 (Shipper)
            response.sendRedirect("login");
            return;
        }

        int shipperId = user.getId();
        int orderId = Integer.parseInt(request.getParameter("orderId"));
        int newStatusId = Integer.parseInt(request.getParameter("statusId"));

        DAOOrder dao = DAOOrder.INSTANCE;
        try {
            // Cập nhật trạng thái và shipper_id nếu chưa có
            dao.updateOrderStatus(orderId, newStatusId, shipperId);

            // Chuyển hướng dựa trên trạng thái mới
            if (newStatusId == 5) { // Delivering
                response.sendRedirect("DeliveringOrders");
            } else if (newStatusId == 6) { // Delivered
                response.sendRedirect("DeliveredOrders");
            } else {
                response.sendRedirect("WaitingOrders"); // Quay lại trang chờ nếu không phải Delivering hoặc Delivered
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error updating status: " + e.getMessage());
            request.getRequestDispatcher("/view/waitingOrders.jsp").forward(request, response);
        }
    }


}