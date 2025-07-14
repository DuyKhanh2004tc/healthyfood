package controller;

import dal.DAOOrder;
import model.Order;
import model.User;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class OrderDetailServlet extends HttpServlet {
    private DAOOrder orderDAO;

    @Override
    public void init() {
        // Khởi tạo DAOOrder
        orderDAO = DAOOrder.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra session và user
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            System.out.println("No user in session, redirecting to login.jsp at " + new java.util.Date());
            response.sendRedirect("view/login.jsp");
            return;
        }

        User seller = (User) session.getAttribute("user");
        // Kiểm tra quyền seller (role_id = 5)
        if (seller.getRole().getId() != 5) {
            System.out.println("User is not a seller (role_id=" + seller.getRole().getId() + "), redirecting to login.jsp at " + new java.util.Date());
            response.sendRedirect("view/login.jsp");
            return;
        }

        // Lấy orderId từ tham số
        String orderIdStr = request.getParameter("id");
        int orderId;
        try {
            orderId = Integer.parseInt(orderIdStr);
        } catch (NumberFormatException e) {
            System.out.println("Invalid orderId: " + orderIdStr + " at " + new java.util.Date());
            response.sendRedirect("SellerOrderHistory");
            return;
        }

        // Lấy sellerId từ user
        int sellerId = seller.getId();
        request.setAttribute("sellerId", sellerId); // Gửi sellerId đến JSP để debug

        // Lấy chi tiết đơn hàng
        Order order = orderDAO.getOrderDetailsBySeller(orderId, sellerId);
        if (order == null) {
            System.out.println("No order found for orderId: " + orderId + " and sellerId: " + sellerId + " at " + new java.util.Date());
            request.setAttribute("error", "No order found or you do not have access to this order.");
            request.getRequestDispatcher("view/OrderHistory.jsp").forward(request, response);
            return;
        }

        request.setAttribute("order", order);
        // Chuyển tiếp đến OrderDetail.jsp
        request.getRequestDispatcher("view/OrderDetail.jsp").forward(request, response);
    }
}