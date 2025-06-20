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
import java.util.Collections;
import java.util.Comparator;
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

        User user = (User) session.getAttribute("user");
        if (!"System admin".equals(user.getRole().getRoleName())) {
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

            String sortBy = request.getParameter("sortBy");
            String sortOrder = request.getParameter("sortOrder");
            if (sortBy != null) {
                Comparator<User> comparator = getComparator(sortBy);
                if (comparator != null) {
                    if ("desc".equals(sortOrder)) {
                        comparator = comparator.reversed();
                    }
                    Collections.sort(uList, comparator);
                }
            }

            request.setAttribute("uList", uList);
            request.setAttribute("roleId", rId);
            request.setAttribute("sortBy", sortBy);
            request.setAttribute("sortOrder", sortOrder != null && "desc".equals(sortOrder) ? "desc" : "asc");
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

            ArrayList<User> uList = DAOUser.INSTANCE.getUsersByRoleId(rId);
            if (uList == null) {
                request.setAttribute("error", "Error retrieving users from database");
                request.getRequestDispatcher("view/error.jsp").forward(request, response);
                return;
            }

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

            String successMessage = (String) session.getAttribute("success");
            if (successMessage != null) {
                request.setAttribute("success", successMessage);
                session.removeAttribute("success");
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

    // Phương thức hỗ trợ để lấy Comparator 
    private Comparator<User> getComparator(String sortBy) {
        switch (sortBy) {
            case "id":
                return Comparator.comparingInt(User::getId);
            case "name":
                return Comparator.comparing(User::getName, String.CASE_INSENSITIVE_ORDER);
            case "email":
                return Comparator.comparing(User::getEmail, String.CASE_INSENSITIVE_ORDER);
            default:
                return null;
        }
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }
}
