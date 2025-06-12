package controller;

import dal.DAORole;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import model.User;

public class HomeAdminServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User user = (User) session.getAttribute("user");
        if (!"System admin".equals(user.getRole().getRoleName())) {
            response.sendRedirect("login");
            return;
        }

        request.setAttribute("listRole", DAORole.INSTANCE.getAllRoles());
        request.getRequestDispatcher("/view/homeAdmin.jsp").forward(request, response);
    }
}