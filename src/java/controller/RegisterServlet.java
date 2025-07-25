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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import model.User;
import utils.Mail;
import utils.PasswordUtil;

/**
 *
 * @author Hoa
 */
public class RegisterServlet extends HttpServlet {

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
            out.println("<title>Servlet RegisterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RegisterServlet at " + request.getContextPath() + "</h1>");
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
        //rocessRequest(request, response);
        request.getRequestDispatcher("view/register.jsp").forward(request, response);
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
        try {
            String fullName = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phonenumber");
            String dateOfBirth = request.getParameter("dateofbirth");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");
            String password = request.getParameter("password");
            String confirmPassword = request.getParameter("confirmpassword");
            
            if (fullName != null) {
                fullName = fullName.trim().replaceAll("\\s+", " ");
            }
            if (email != null) {
                email = email.trim().replaceAll("\\s+", "");
            }
            if (phoneNumber != null) {
                phoneNumber = phoneNumber.trim().replaceAll("\\s+", "");
            }
            if (dateOfBirth != null) {
                dateOfBirth = dateOfBirth.trim();
            }
            if (address != null) {
                address = address.trim().replaceAll("\\s+", " ");
            }

            HttpSession session = request.getSession();

            DAOUser dao = new DAOUser();
            
            boolean hasError = false;

            if (dao.checkEmailExists(email, -1)) {
                request.setAttribute("errorEmail", "Email already exists");
                hasError = true;
            }
            
            if (fullName == null || fullName.isEmpty() || fullName.length() < 2 || fullName.length() > 50 || !fullName.matches("^[\\p{L}\\s]+$")) {
                request.setAttribute("errorName", "Full name must be 2-50 characters and contain only letters and spaces.");
                hasError = true;
            }

            if (!phoneNumber.matches("^(0|\\+84)[0-9]{9}$")) {
                request.setAttribute("errorPhone", "Invalid phone number. It must start with 0 or +84 and contain 9 digits after.");
                hasError = true;
            }

            Date dob = null;
            if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
                request.setAttribute("errorDOB", "Date of birth is required.");
                hasError = true;
            } else {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.setLenient(false);
                    java.util.Date parsedDate = sdf.parse(dateOfBirth);
                    dob = new Date(parsedDate.getTime());

                    LocalDate birthDate = LocalDate.parse(dateOfBirth);
                    if (birthDate.plusYears(13).isAfter(LocalDate.now())) {
                        request.setAttribute("errorDOB", "You must be at least 13 years old.");
                        hasError = true;
                    }
                } catch (ParseException e) {
                    request.setAttribute("errorDOB", "Invalid date of birth format. Use YYYY-MM-DD.");
                    hasError = true;
                }
            }
            
            if (address == null || address.isEmpty() || address.length() < 5 || address.length() > 100) {
                request.setAttribute("errorAddress", "Address must be 5-100 characters.");
                hasError = true;
            }
            
            if (gender == null || (!gender.equals("0") && !gender.equals("1"))) {
                request.setAttribute("errorGender", "Please select a gender.");
                hasError = true;
            }
            
            if (password.length() <= 8 || password.length() >= 32) {
                request.setAttribute("errorPassword", "Password must be between 8 and 32 characters long.");
                hasError = true;
            }
            
            if (password == null || password.contains(" ")) {
                request.setAttribute("errorPassword", "The password must not contain any spaces.");
                hasError = true;
            }           
            
            if (!password.equals(confirmPassword)) {
                    request.setAttribute("error", "Password and Confirm Password do not match.");
                    hasError = true;
                }
            
            if(hasError){
                request.getRequestDispatcher("view/register.jsp").forward(request, response);
                return;
            }
            
 
            
            boolean genderSQL = "1".equals(gender);

            User customer = new User();
            customer.setName(fullName);
            customer.setEmail(email);
            customer.setPassword(PasswordUtil.hashPassword(password));
            customer.setPhone(phoneNumber);
            customer.setDob(dob);
            customer.setAddress(address);
            customer.setGender(genderSQL);
            
            
            session.setAttribute("pendingUser", customer);

            String otp = String.valueOf((int) (Math.random() * 900000) + 100000);

            session.setAttribute("otp", otp);
            session.setAttribute("otp_time", System.currentTimeMillis());

            Mail.sendMail(email, "Verify your registration", "Your verification code is: " + otp);

            response.sendRedirect("verifyRegister");
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("view/register.jsp").forward(request, response);
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