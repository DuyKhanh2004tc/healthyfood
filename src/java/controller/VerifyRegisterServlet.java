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
import utils.Mail;
import utils.PasswordUtil;

/**
 *
 * @author Hoa
 */
public class VerifyRegisterServlet extends HttpServlet {
   
    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
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
            out.println("<title>Servlet VerifyRegister</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet VerifyRegister at " + request.getContextPath () + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    } 

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
        request.getRequestDispatcher("view/verifyRegister.jsp").forward(request, response);
    } 

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        //processRequest(request, response);
          String inputOtp = request.getParameter("otp");
        HttpSession session = request.getSession();

        String sessionOtp = (String) session.getAttribute("otp");
        Long otpTime = (Long) session.getAttribute("otp_time");
        long currentTime = System.currentTimeMillis();


        if (sessionOtp == null || otpTime == null || currentTime - otpTime > 5 * 60 * 1000) {
            request.setAttribute("error", "OTP is expired or invalid.");
            request.getRequestDispatcher("view/verifyRegister.jsp").forward(request, response);
            return;
        }

        if (!inputOtp.equals(sessionOtp)) {
            request.setAttribute("error", "Incorrect OTP.");
            request.getRequestDispatcher("view/verifyRegister.jsp").forward(request, response);
            return;
        }

        User customer = (User) session.getAttribute("pendingUser");
        if (customer == null) {
            request.setAttribute("error", "Session expired. Please register again.");
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
            return;
        }
        
        //String hashedPasswoord = PasswordUtil.hashPassword(customer.getPassword());
        //customer.setPassword(hashedPasswoord);

        DAOUser dao = new DAOUser();
        dao.addAccount(customer);
        Mail.sendMail(customer.getEmail(), "Welcome to HealthyFood", "Congratulations! You have successfully registered.");
        session.removeAttribute("otp");
        session.removeAttribute("otp_time");
        session.removeAttribute("pendingUser");

        request.setAttribute("success", "Registration successful! You can now login.");
        request.getRequestDispatcher("view/login.jsp").forward(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
