package controller;

import dal.DAOOrder;
import dal.DAOOrderStatus;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;

public class SellerCanceledOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOOrder dao = DAOOrder.INSTANCE;
        DAOOrderStatus daoStatus = DAOOrderStatus.INSTANCE;
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute("user") == null) {
                response.sendRedirect("login");
                return;
            }

            User user = (User) session.getAttribute("user");
            if (user.getRole().getId() != 5) {
                response.sendRedirect("login");
                return;
            }

            List<Order> canceledOrders = dao.getOrdersByStatusIn(List.of(7));
            request.setAttribute("canceledOrders", canceledOrders);
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching canceled orders: " + e.getMessage());
            e.printStackTrace();
        }
        request.getRequestDispatcher("/view/sellerCanceledOrders.jsp").forward(request, response);
    }
}