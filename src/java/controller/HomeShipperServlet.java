package controller;

import dal.DAOOrder;
import dal.DAOOrderStatus;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 * @author Cuong
 */
@WebServlet("/HomeShipperServlet")
public class HomeShipperServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Kiểm tra session
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

        // Chuyển hướng đến trang homeShipper.jsp
        request.getRequestDispatcher("/view/homeShipper.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }


}