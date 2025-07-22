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

public class ProcessingOrdersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAOOrder dao = DAOOrder.INSTANCE;
        DAOOrderStatus daoStatus = DAOOrderStatus.INSTANCE;
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
                    request.getRequestDispatcher("/view/orderDetail.jsp?fromPage=processing").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "Order not found with ID: " + orderId);
                }
            } else {
                List<Integer> statusIds = new ArrayList<>();
                statusIds.add(2); // Processing
                List<Order> processingOrders = dao.getOrdersByStatusIn(statusIds);
                request.setAttribute("processingOrders", processingOrders);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching orders: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid order ID");
            request.getRequestDispatcher("/view/processingOrders.jsp").forward(request, response);
        }
        request.getRequestDispatcher("/view/processingOrders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}