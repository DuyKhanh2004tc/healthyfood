package controller;

import dal.DAOOrder;
import model.Order;
import model.User;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class SellerOrderHistory extends HttpServlet {
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
            // Không có user trong session, chuyển hướng đến login
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

        // Lấy sellerId từ user
        int sellerId = seller.getId();
        request.setAttribute("sellerId", sellerId); // Gửi sellerId đến JSP để debug

        // Lấy danh sách đơn hàng của seller
        List<Order> orders = orderDAO.getOrdersBySeller(sellerId);
        request.setAttribute("orders", orders);

        // Chuyển tiếp đến OrderHistory.jsp
        request.getRequestDispatcher("view/OrderHistory.jsp").forward(request, response);
    }
}