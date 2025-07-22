package controller;

import dal.DAOOrder;
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

public class ConfirmedOrdersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOOrder dao = DAOOrder.INSTANCE;
        String orderIdParam = request.getParameter("orderId");

        try {
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

            if (orderIdParam != null && !orderIdParam.isEmpty()) {
                int orderId = Integer.parseInt(orderIdParam);
                Order order = dao.getOrderById(orderId);
                if (order != null) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/view/orderDetail.jsp").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "Order not found with ID: " + orderId);
                }
            } else {
                List<Integer> statusIds = new ArrayList<>();
                statusIds.add(1); // Pending
                List<Order> pendingOrders = dao.getOrdersByStatusIn(statusIds);
                request.setAttribute("pendingOrders", pendingOrders);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching orders: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid order ID");
            request.getRequestDispatcher("/view/confirmedOrders.jsp").forward(request, response);
        }
        request.getRequestDispatcher("/view/confirmedOrders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}