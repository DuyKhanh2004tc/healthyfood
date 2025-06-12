package controller;

import dal.DAOUser;
import jakarta.servlet.RequestDispatcher;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.stream.Collectors;
import model.User;

public class DisplayAccountServlet extends HttpServlet {

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
            String roleId = request.getParameter("idRole");
            if (roleId == null || roleId.trim().isEmpty()) {
                request.setAttribute("error", "Role ID is missing");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }
            int rId = Integer.parseInt(roleId);
            ArrayList<User> uList = DAOUser.INSTANCE.getUsersByRoleId(rId);
            if (uList == null) {
                request.setAttribute("error", "Error retrieving users from database");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }
            request.setAttribute("uList", uList);
            request.setAttribute("roleId", rId);
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/displayAccounts.jsp");
            requestDispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Role ID");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        

        try {
            String keyWord = request.getParameter("keyword");
            String roleId = request.getParameter("idRole");
            if (roleId == null || roleId.trim().isEmpty()) {
                request.setAttribute("error", "Role ID is missing");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }
            int rId = Integer.parseInt(roleId);

            // Lấy toàn bộ danh sách người dùng theo roleId
            ArrayList<User> uList = DAOUser.INSTANCE.getUsersByRoleId(rId);
            if (uList == null) {
                request.setAttribute("error", "Error retrieving users from database");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }

            // Lọc danh sách thủ công dựa trên keyword
            if (keyWord != null && !keyWord.trim().isEmpty()) {
                final String searchKey = keyWord.toLowerCase();
                ArrayList<User> filteredList = new ArrayList<>();

                for (User user : uList) {
                    String name = user.getName();
                    String email = user.getEmail();

                    boolean matchName = name != null && name.toLowerCase().contains(searchKey);
                    boolean matchEmail = email != null && email.toLowerCase().contains(searchKey);

                    if (matchName || matchEmail) {
                        filteredList.add(user);
                    }
                }

                uList = filteredList;
            }

            request.setAttribute("uList", uList);
            request.setAttribute("roleId", rId);

            // Lấy thông báo thành công từ session (nếu có)
            String successMessage = (String) session.getAttribute("success");
            if (successMessage != null) {
                request.setAttribute("success", successMessage);
                session.removeAttribute("success"); // Xóa thông báo sau khi lấy
            }
            RequestDispatcher requestDispatcher = request.getRequestDispatcher("view/displayAccounts.jsp");
            requestDispatcher.forward(request, response);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Role ID");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
