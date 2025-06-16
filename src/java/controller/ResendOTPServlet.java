/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;
import utils.Mail;

/**
 *
 * @author Hoa
 */
public class ResendOTPServlet extends HttpServlet {

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
            out.println("<title>Servlet ResendOTPServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ResendOTPServlet at " + request.getContextPath() + "</h1>");
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

        String type = request.getParameter("type");
        HttpSession session = request.getSession();
        String email = null;

        // Check for pendingUser (registration flow) or email (password reset flow)
        User user = (User) session.getAttribute("pendingUser");
        if (user != null) {
            email = user.getEmail();
        } else {
            email = (String) session.getAttribute("email");
        }

        if (email != null) {
            String otp = String.valueOf((int) (Math.random() * 900000) + 100000);
            session.setAttribute("otp", otp);
            session.setAttribute("otp_time", System.currentTimeMillis());

            String subject = "register".equals(type) ? "Verify your registration" : "Password Reset Code";
            Mail.sendMail(email, subject, "Your verification code is: " + otp);


        } else {
            request.setAttribute("error", "No email found in session. Unable to resend verification code.");
        }

        // Redirect to appropriate page based on type
        if ("register".equals(type)) {
            request.getRequestDispatcher("view/verifyRegister.jsp").forward(request, response);
        } else if ("reset".equals(type)) {
            request.getRequestDispatcher("view/resetPassword.jsp").forward(request, response);
        } else {
            request.setAttribute("error", "Invalid request type provided.");
            request.getRequestDispatcher("view/error.jsp").forward(request, response);
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
