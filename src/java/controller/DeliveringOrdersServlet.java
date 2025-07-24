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

public class DeliveringOrdersServlet extends HttpServlet {

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
            if (user.getRole().getId() != 6) { // Kiểm tra role_id = 6 (Shipper)
                response.sendRedirect("login");
                return;
            }

            int shipperId = user.getId();

            if (orderIdParam != null && !orderIdParam.isEmpty()) {
                int orderId = Integer.parseInt(orderIdParam);
                Order order = dao.getOrderById(orderId);
                if (order != null && order.getShipper() != null && order.getShipper().getId() == shipperId) {
                    request.setAttribute("order", order);
                    request.getRequestDispatcher("/view/orderDetailShipper.jsp?fromPage=delivering").forward(request, response);
                    return;
                } else {
                    request.setAttribute("error", "Order not found or not assigned to you with ID: " + orderId);
                }
            } else {
                List<Order> deliveringOrders = dao.getOrdersByStatusIn(List.of(5)); // Delivering
                List<Order> filteredDelivering = new ArrayList<>();

                for (Order order : deliveringOrders) {
                    if (order.getShipper() != null && order.getShipper().getId() == shipperId) {
                        // Đảm bảo validStatuses bao gồm Cancel (status_id = 7)
                        order.setValidStatuses(daoStatus.getValidStatusesFor(order.getStatus().getId()));
                        filteredDelivering.add(order);
                    }
                }

                request.setAttribute("deliveringOrders", filteredDelivering);
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Error fetching orders: " + e.getMessage());
            e.printStackTrace();
        }
        request.getRequestDispatcher("/view/deliveringOrders.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}