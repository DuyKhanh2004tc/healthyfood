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

public class AddAccountServlet extends HttpServlet {

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

        String idRole = request.getParameter("roleId");
        if (idRole == null || idRole.trim().isEmpty()) {
            request.setAttribute("error", "Role ID is missing");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
            return;
        }

        ArrayList<Role> roles = DAORole.INSTANCE.getAllRoles();
        boolean roleExists = roles.stream().anyMatch(role -> String.valueOf(role.getId()).equals(idRole));
        if (!roleExists) {
            request.setAttribute("error", "Invalid Role ID");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
            return;
        }
        request.setAttribute("roleId", idRole);
        request.setAttribute("roles", roles); // Giữ roles để tham chiếu nếu cần

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

        User user = (User) session.getAttribute("user");
        if (!"System admin".equals(user.getRole().getRoleName())) {
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
            String roleIdStr = request.getParameter("roleId"); // Lấy từ hidden input

            // Khởi tạo các lỗi cụ thể cho từng field
            request.setAttribute("nameError", "");
            request.setAttribute("emailError", "");
            request.setAttribute("passwordError", "");
            request.setAttribute("phoneError", "");
            request.setAttribute("dobError", "");
            request.setAttribute("addressError", "");
            request.setAttribute("genderError", "");
            request.setAttribute("roleIdError", "");

            // Validate input
            if (email == null || email.trim().isEmpty()) {
                request.setAttribute("emailError", "Email is required");
            } else if (!Pattern.matches("^[A-Za-z0-9+_.-]+@(.+)$", email)) {
                request.setAttribute("emailError", "Invalid email format");
            }
            if (name == null || name.trim().isEmpty() || name.length() < 2 || name.length() > 50 || !name.matches("^[\\p{L}\\s]+$")) {
                request.setAttribute("nameError", "Full name must be 2-50 characters and contain only letters and spaces.");
            }

            if (password == null || password.trim().isEmpty()) {
                request.setAttribute("passwordError", "Password is required");
            } else if (password.trim().length() < 6) {
                request.setAttribute("passwordError", "Password must be at least 6 characters");
            }

            if (phone == null || phone.isEmpty() || !phone.matches("^(0|\\+84)[0-9]{9}$")) {
                request.setAttribute("phoneError", "Phone number must start with 0 or +84 and contain exactly 9 digits.");
            }

            Date dob = null;
            if (dobStr == null || dobStr.trim().isEmpty()) {
                request.setAttribute("dobError", "Date of birth is required.");
            } else {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    java.util.Date parsedDate = sdf.parse(dobStr);
                    dob = new Date(parsedDate.getTime());

                    LocalDate birthDate = LocalDate.parse(dobStr);
                    if (birthDate.plusYears(13).isAfter(LocalDate.now())) {
                        request.setAttribute("dobError", "You must be at least 13 years old.");
                    }
                } catch (ParseException e) {
                    request.setAttribute("dobError", "Invalid date of birth format. Use YYYY-MM-DD.");
                }
            }
            if (address == null || address.trim().isEmpty() || address.length() < 5 || address.length() > 100) {
                request.setAttribute("addressError", "Address must be 5-100 characters.");
            }
            if (genderStr == null || genderStr.trim().isEmpty()) {
                request.setAttribute("genderError", "Gender is required");
            }

            if (!request.getAttribute("emailError").equals("") || !request.getAttribute("nameError").equals("")
                    || !request.getAttribute("passwordError").equals("") || !request.getAttribute("phoneError").equals("")
                    || !request.getAttribute("dobError").equals("") || !request.getAttribute("addressError").equals("")
                    || !request.getAttribute("genderError").equals("")) {
                forwardToAddAccount(request, response, roleIdStr);
                return;
            }

            DAOUser daoUser = DAOUser.INSTANCE;
            if (daoUser.checkEmailExists(email, 0)) {
                request.setAttribute("emailError", "Email already exists");
                forwardToAddAccount(request, response, roleIdStr);
                return;
            }

            boolean gender = genderStr != null && genderStr.equals("1");
            int roleId = Integer.parseInt(roleIdStr); // Sử dụng roleId từ hidden input

            User newUser = new User();
            newUser.setName(name.trim());
            newUser.setEmail(email.trim());
            newUser.setPassword(password != null ? password.trim() : "");
            newUser.setPhone(phone != null ? phone.trim() : null);
            newUser.setDob(dob);
            newUser.setAddress(address.trim());
            newUser.setGender(gender);
            Role role = new Role();
            role.setId(roleId);
            newUser.setRole(role);

            boolean added = daoUser.addAccountByRole(newUser);
            if (added) {
                session.setAttribute("success", "User added successfully with role ID " + roleId + " at " + new java.util.Date());
                response.sendRedirect("DisplayAccount?idRole=" + roleId + "&page=1");
            } else {
                request.setAttribute("error", "Failed to add user: " + daoUser.getStatus());
                forwardToAddAccount(request, response, roleIdStr);
            }
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            forwardToAddAccount(request, response, null);
        }
    }

    private void forwardToAddAccount(HttpServletRequest request, HttpServletResponse response, String roleIdStr)
            throws ServletException, IOException {
        ArrayList<Role> roles = DAORole.INSTANCE.getAllRoles();
        request.setAttribute("roles", roles);
        if (roleIdStr != null) {
            request.setAttribute("roleId", roleIdStr);
        }
        request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Servlet for adding a new user account";
    }
}
