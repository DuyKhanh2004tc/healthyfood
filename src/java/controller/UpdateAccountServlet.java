package controller;

import dal.DAOUser;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.User;
import model.Role;

public class UpdateAccountServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userId = request.getParameter("id");
            if (userId == null || userId.trim().isEmpty()) {
                request.setAttribute("error", "User ID is missing");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }
            int id = Integer.parseInt(userId);
            DAOUser daoUser = DAOUser.INSTANCE;
            ArrayList<User> userList = daoUser.getUser();
            User user = null;
            for (User u : userList) {
                if (u.getId() == id) {
                    user = u;
                    break;
                }
            }
            if (user == null) {
                request.setAttribute("error", "User not found");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }
            request.setAttribute("user", user);
            RequestDispatcher dispatcher = request.getRequestDispatcher("view/updateAccount.jsp");
            dispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid User ID");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String userId = request.getParameter("id");
            if (userId == null || userId.trim().isEmpty()) {
                request.setAttribute("error", "User ID is missing");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }
            int id = Integer.parseInt(userId);
            
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String dobStr = request.getParameter("dob");
            String address = request.getParameter("address");
            String genderStr = request.getParameter("gender");
            String roleIdStr = request.getParameter("roleId");

            // Validate required fields
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("error", "Email is required");
                request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
                return;
            }

            // Parse and validate data
            Date dob = null;
            if (dobStr != null && !dobStr.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = sdf.parse(dobStr);
                    dob = new Date(parsedDate.getTime());
                } catch (ParseException e) {
                    request.setAttribute("error", "Invalid date format for DOB. Use YYYY-MM-DD");
                    request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
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
                    request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
                    return;
                }
            }

            // Create User object
            User user = new User();
            user.setId(id);
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

            // Update user in database
            DAOUser daoUser = DAOUser.INSTANCE;
            boolean updated = daoUser.updateUser(user);
            if (updated) {
                response.sendRedirect("DisplayAccount?idRole=" + roleId);
            } else {
                request.setAttribute("error", "Failed to update user: " + daoUser.getStatus());
                request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid User ID or Role ID");
            request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Servlet for updating user account information";
    }
}