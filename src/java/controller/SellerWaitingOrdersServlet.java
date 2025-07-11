package controller;

import dal.DAOOrder;
import dal.DAOOrderStatus;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.Order;
import model.User;

public class SellerWaitingOrdersServlet extends HttpServlet {

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

            List<Order> waitingOrders = dao.getOrdersByStatusIn(List.of(3));
            request.setAttribute("waitingOrders", waitingOrders);
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching orders: " + e.getMessage());
            e.printStackTrace();
        }
        request.getRequestDispatcher("/view/sellerWaitingOrders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}