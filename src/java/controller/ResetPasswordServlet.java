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
import utils.Mail;

/**
 *
 * @author Hoa
 */
public class ResetPasswordServlet extends HttpServlet {

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
            out.println("<title>Servlet ResetPasswordServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ResetPasswordServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
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

        String inputOtp = request.getParameter("otp");
        String newPassword = request.getParameter("newpassword");
        String confirmPassword = request.getParameter("confirmpassword");

        String sessionOtp = (String) session.getAttribute("otp");
        Long otpTime = (Long) session.getAttribute("otp_time");
        long currentTime = System.currentTimeMillis();
        String email = (String) session.getAttribute("email");

        if (sessionOtp == null || otpTime == null || email == null) {
            request.setAttribute("error", "No OTP session found. Please request a new OTP.");
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
            return;
        }

        
        if (sessionOtp == null || otpTime == null || currentTime - otpTime > 5 * 60 * 1000) {
            request.setAttribute("error", "OTP is expired or invalid.Please click on 'Resend OTP' to receive a new code.");
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
            return;
        }

        if (!sessionOtp.equals(inputOtp)) {
            request.setAttribute("error", "Invalid OTP. Please try again.");
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
            return;
        }

        if (newPassword == null || confirmPassword == null
                || !newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "Password and Confirm Password do not match.");
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
            return;
        }

        if (newPassword.length() < 8 || newPassword.length() > 32) {
            request.setAttribute("error", "Password must be between 8 and 32 characters.");
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
            return;
        }

        DAOUser dao = new DAOUser();
        boolean updated = dao.updatePassword(email, newPassword);

        if (updated) {
            // Xóa session OTP
            session.removeAttribute("otp");
            session.removeAttribute("otp_time");
            session.removeAttribute("email");

            request.setAttribute("success", "Password updated successfully. You can now login.");
            request.getRequestDispatcher("view/login.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Failed to update password. Please try again.");
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
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
