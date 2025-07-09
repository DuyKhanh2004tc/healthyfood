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
            List<Order> deliveringOrders = dao.getOrdersByStatusIn(List.of(5)); // Delivering
            List<Order> filteredDelivering = new ArrayList<>();

            for (Order order : deliveringOrders) {
                if (order.getShipper() != null && order.getShipper().getId() == shipperId) {
                    order.setValidStatuses(daoStatus.getValidStatusesFor(order.getStatus().getId()));
                    filteredDelivering.add(order);
                }
            }

            request.setAttribute("deliveringOrders", filteredDelivering);
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