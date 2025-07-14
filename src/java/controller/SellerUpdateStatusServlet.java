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
            dao.updateOrderStatus(orderId, newStatusId, -1);

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
