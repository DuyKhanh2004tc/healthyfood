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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Pattern;
import model.User;
import model.Role;

public class UpdateAccountServlet extends HttpServlet {

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
        try {
            String userId = request.getParameter("id");
            int id = Integer.parseInt(userId);
            DAOUser daoUser = DAOUser.INSTANCE;
            User user = daoUser.getUserById(id);
            if (user == null) {
                request.setAttribute("error", "User not found");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }

            ArrayList<Role> roles = DAORole.INSTANCE.getAllRoles();
            request.setAttribute("user", user);
            request.setAttribute("roles", roles);
            RequestDispatcher dispatcher = request.getRequestDispatcher("view/updateAccount.jsp");
            dispatcher.forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        }
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
            int id = Integer.parseInt(request.getParameter("id"));

            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            String phone = request.getParameter("phone");
            String dobStr = request.getParameter("dob");
            String address = request.getParameter("address");
            String genderStr = request.getParameter("gender");
            String roleIdStr = request.getParameter("roleId");

            request.setAttribute("nameError", "");
            request.setAttribute("emailError", "");
            request.setAttribute("passwordError", "");
            request.setAttribute("phoneError", "");
            request.setAttribute("dobError", "");
            request.setAttribute("addressError", "");
            request.setAttribute("genderError", "");
            request.setAttribute("roleIdError", "");

            boolean hasError = false;

            if (name == null || name.trim().isEmpty() || name.length() < 2 ||name.length() > 50 || !name.matches("^[\\p{L}\\s]+$")) {
                request.setAttribute("nameError", "Full name must be 2-50 characters and contain only letters and spaces.");
                hasError = true;
            }

            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("emailError", "Email is required");
                hasError = true;
            } else if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
                request.setAttribute("emailError", "Invalid email format");
                hasError = true;
            }

            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("passwordError", "Password is required");
                hasError = true;
            } else if (password.trim().length() < 6) {
                request.setAttribute("passwordError", "Password must be at least 6 characters");
                hasError = true;
            }

            if (phone == null || phone.isEmpty() || !phone.matches("^(0|\\+84)[0-9]{9}$")) {
                request.setAttribute("phoneError", "Phone number must start with 0 or +84 and contain exactly 9 digits.");
                hasError = true;
            }

            Date dob = null;
            if (dobStr == null || dobStr.trim().isEmpty()) {
                request.setAttribute("dobError", "Date of birth is required.");
                hasError = true;
            } else {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    java.util.Date parsedDate = sdf.parse(dobStr);
                    dob = new Date(parsedDate.getTime());

                    LocalDate birthDate = LocalDate.parse(dobStr);
                    if (birthDate.plusYears(13).isAfter(LocalDate.now())) {
                        request.setAttribute("dobError", "You must be at least 13 years old.");
                        hasError = true;
                    }
                } catch (ParseException e) {
                    request.setAttribute("dobError", "Invalid date of birth format. Use YYYY-MM-DD.");
                    hasError = true;
                }
            }

            if (address == null || address.trim().isEmpty() || address.length() < 5 || address.length() > 100) {
                request.setAttribute("addressError", "Address must be 5-100 characters.");
                hasError = true;
            }

            if (genderStr == null || genderStr.trim().isEmpty()) {
                request.setAttribute("genderError", "Gender is required");
                hasError = true;
            }

            if (roleIdStr == null || roleIdStr.trim().isEmpty()) {
                request.setAttribute("roleIdError", "Role is required");
                hasError = true;
            }

            DAOUser daoUser = DAOUser.INSTANCE;
            if (daoUser.checkEmailExists(email, id)) {
                request.setAttribute("emailError", "Email already exists for another user");
                hasError = true;
            }

            if (hasError) {
                User user = daoUser.getUserById(id);
                request.setAttribute("user", user);
                request.setAttribute("roles", DAORole.INSTANCE.getAllRoles());
                request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
                return;
            }

            boolean gender = genderStr.equals("1");
            int roleId = Integer.parseInt(roleIdStr);

            User user = new User();
            user.setId(id);
            user.setName(name.trim());
            user.setEmail(email.trim());
            user.setPassword(password.trim());
            user.setPhone(phone.trim());
            user.setDob(dob);
            user.setAddress(address.trim());
            user.setGender(gender);
            Role role = new Role();
            role.setId(roleId);
            user.setRole(role);

            boolean updated = daoUser.updateUser(user);
            if (updated) {
                session.setAttribute("success", "User updated successfully");
                response.sendRedirect("DisplayAccount?idRole=" + roleId + "&page=1");
            } else {
                request.setAttribute("error", "Failed to update user: " + daoUser.getStatus());
                User u = daoUser.getUserById(id);
                request.setAttribute("user", u);
                request.setAttribute("roles", DAORole.INSTANCE.getAllRoles());
                request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("view/updateAccount.jsp").forward(request, response);
        }
    }

}
