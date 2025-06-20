package controller;

import dal.DAOUser;
import dal.DAORole;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.User;
import model.Role;

public class AddAccountServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User users = (User) session.getAttribute("user");
        if (!"System admin".equals(users.getRole().getRoleName())) {
            response.sendRedirect("login");
            return;
        }

        String idRole = request.getParameter("roleId");
        if (idRole == null || idRole.trim().isEmpty()) {
            request.setAttribute("error", "Role ID is missing");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("roleId", idRole);

        ArrayList<Role> roles = DAORole.INSTANCE.getAllRoles();
        request.setAttribute("roles", roles);

        RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/addAccount.jsp");
        requestDispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login");
            return;
        }

        User users = (User) session.getAttribute("user");
        if (!"System admin".equals(users.getRole().getRoleName())) {
            response.sendRedirect("login");
            return;
        }
        try {
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String dobStr = request.getParameter("dob");
            String address = request.getParameter("address");
            String genderStr = request.getParameter("gender");
            String roleIdStr = request.getParameter("roleId");

            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email is required");
                request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
                return;
            }

            DAOUser daoUser = DAOUser.INSTANCE;

            if (daoUser.checkEmailExists(email, 0)) {
                request.setAttribute("error", "Email already exists");
                request.setAttribute("roleId", roleIdStr);
                request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
                return;
            }

            Date dob = null;
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = sdf.parse(dobStr);
                    dob = new Date(parsedDate.getTime());
                } catch (ParseException e) {
                    request.setAttribute("error", "Invalid date format for DOB. Use YYYY-MM-DD");
                    request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
                    return;
                }
            }

            boolean gender = genderStr != null && genderStr.equals("1");
            int roleId = 0;
            if (roleIdStr != null && !roleIdStr.trim().isEmpty()) {
                try {
                    roleId = Integer.parseInt(roleIdStr);
                } catch (NumberFormatException e) {
                    request.setAttribute("error", "Invalid Role ID");
                    request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
                    return;
                }
            }

            User user = new User();
            user.setName(name);
            user.setEmail(email);
            user.setPassword(password);
            user.setPhone(phone);
            user.setDob(dob);
            user.setAddress(address);
            user.setGender(gender);
            Role role = new Role();
            role.setId(roleId);
            user.setRole(role);

            boolean added = daoUser.addAccountByRole(user);
            if (added) {
                session.setAttribute("success", "User added successfully with role ID " + roleId + " at " + new java.util.Date());
                response.sendRedirect("DisplayAccount?idRole=" + roleId + "&page=1");
            } else {
                request.setAttribute("error", "Failed to add user: " + daoUser.getStatus());
                request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for adding a new user account";
    }
}
