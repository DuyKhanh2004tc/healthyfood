package controller;

import dal.DAOOrder;
import model.Order;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class SellerOrderHistory extends HttpServlet {
    private static final int PAGE_SIZE = 15;
    private DAOOrder orderDAO;

    @Override
    public void init() {
        orderDAO = DAOOrder.INSTANCE;
        System.out.println("SellerOrderHistory initialized at " + new java.util.Date());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("No user in session, redirecting to login.jsp at " + new java.util.Date());
            response.sendRedirect("view/login.jsp");
            return;
        }

        User seller = (User) session.getAttribute("user");
        int sellerId = seller.getId();
        request.setAttribute("sellerId", sellerId);

        // Check if orderId is provided for viewing details
        String orderIdParam = request.getParameter("orderId");
        if (orderIdParam != null) {
            try {
                int orderId = Integer.parseInt(orderIdParam);
                System.out.println("Fetching order details for order ID: " + orderId + " at " + new java.util.Date());
                Order order = orderDAO.getOrderById(orderId);
                if (order == null) {
                    System.out.println("No order found for order ID: " + orderId + " at " + new java.util.Date());
                    request.setAttribute("errorMessage", "No order details found for Order ID: " + orderId + ". Please ensure the order exists.");
                } else {
                    request.setAttribute("order", order);
                }
                request.getRequestDispatcher("view/SellerOrderDetail.jsp").forward(request, response);
                return;
            } catch (NumberFormatException e) {
                System.err.println("Invalid orderId format: " + orderIdParam + " at " + new java.util.Date());
                request.setAttribute("errorMessage", "Invalid Order ID format.");
                request.getRequestDispatcher("view/SellerOrderDetail.jsp").forward(request, response);
                return;
            } catch (SQLException e) {
                System.err.println("Error fetching order details for orderId: " + orderIdParam + ": " + e.getMessage());
                e.printStackTrace();
                request.setAttribute("errorMessage", "An error occurred while fetching order details: " + e.getMessage());
                request.getRequestDispatcher("view/SellerOrderDetail.jsp").forward(request, response);
                return;
            }
        }

        // Default: Show order history with pagination
        System.out.println("Fetching all orders for seller ID: " + sellerId + ", Name: " + seller.getName() + " at " + new java.util.Date());
        int page = 1;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
                if (page < 1) page = 1;
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        try {
            List<Order> orders = orderDAO.getAllOrders(page, PAGE_SIZE);
            int totalOrders = orderDAO.getTotalOrders();
            int totalPages = (int) Math.ceil((double) totalOrders / PAGE_SIZE);

            if (orders.isEmpty()) {
                System.out.println("No orders found at " + new java.util.Date());
                request.setAttribute("errorMessage", "No orders found. Please ensure orders exist in the system.");
            }

            request.setAttribute("orders", orders);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
        } catch (SQLException e) {
            System.err.println("Error fetching orders: " + e.getMessage());
            e.printStackTrace();
            request.setAttribute("errorMessage", "An error occurred while fetching orders: " + e.getMessage());
        }

        request.getRequestDispatcher("view/OrderHistory.jsp").forward(request, response);
    }
}