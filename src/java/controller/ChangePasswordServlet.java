/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOUser;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

/**
 *
 * @author Hoa
 */
public class ChangePasswordServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ChangePasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChangePasswordServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        if (u.getRole().getId() == 4) {
            request.getRequestDispatcher("view/changePasswordNutritionist.jsp").forward(request, response);
        } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5) {
            request.getRequestDispatcher("view/changePassword.jsp").forward(request, response);
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");

        // Check if user is null to avoid NullPointerException
        if (u == null) {
            request.setAttribute("error", "User not logged in.");
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
            return;
        }

        try {
            String currentPassword = request.getParameter("currentpassword");
            String newPassword = request.getParameter("newpassword");
            String confirmPassword = request.getParameter("confirmpassword");

            String email = u.getEmail();
            String password = u.getPassword();

            boolean hasError = false;

            if (newPassword.equals(currentPassword)) {
                request.setAttribute("error", "The new password must be different from the current password.");
                hasError = true;
            }
            if (newPassword == null || confirmPassword == null || !newPassword.equals(confirmPassword) || newPassword.trim().isEmpty()) {
                request.setAttribute("error", "Password and Confirm Password do not match.");
                hasError = true;
            }
            if (newPassword.length() < 8 || newPassword.length() > 32) {
                request.setAttribute("error", "Password must be between 8 and 32 characters.");
                hasError = true;
            }
            if (currentPassword == null || !currentPassword.equals(password)) {
                request.setAttribute("error", "Incorrect current password.");
                hasError = true;
            }

            if (hasError) {
                if (u.getRole().getId() == 4) {
                    request.getRequestDispatcher("/view/changePasswordNutritionist.jsp").forward(request, response);
                } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5) {
                    request.getRequestDispatcher("/view/changePassword.jsp").forward(request, response);
                }
                return;
            }

            DAOUser dao = new DAOUser();
            boolean updated = dao.updatePassword(email, newPassword);

            if (updated) {
                u.setPassword(newPassword);
                session.setAttribute("user", u);
                request.setAttribute("success", "Password updated successfully.");
            } else {
                request.setAttribute("error", "Failed to update password. Please try again.");
            }

            if (u.getRole().getId() == 4) {
                request.getRequestDispatcher("/view/changePasswordNutritionist.jsp").forward(request, response);
            } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5) {
                request.getRequestDispatcher("/view/changePassword.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            if (u.getRole().getId() == 4) {
                request.getRequestDispatcher("/view/changePasswordNutritionist.jsp").forward(request, response);
            } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5) {
                request.getRequestDispatcher("/view/changePassword.jsp").forward(request, response);
            }
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
