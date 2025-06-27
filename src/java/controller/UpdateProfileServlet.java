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

/**
 *
 * @author Hoa
 */
public class UpdateProfileServlet extends HttpServlet {

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
            out.println("<title>Servlet UpdateProfileServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet UpdateProfileServlet at " + request.getContextPath() + "</h1>");
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
        request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
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

            HttpSession session = request.getSession();
            DAOUser dao = new DAOUser();

            if (!phoneNumber.matches("^(0|\\+84)[0-9]{9}$")) {
                request.setAttribute("errorPhone", "Invalid phone number. It must start with 0 or +84 and contain 9 digits after.");
                request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
                return;
            }

            Date dob = null;
            if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = sdf.parse(dateOfBirth);
                    dob = new Date(parsedDate.getTime());

                    LocalDate birthDate = LocalDate.parse(dateOfBirth);
                    if (birthDate.plusYears(13).isAfter(LocalDate.now())) {
                        request.setAttribute("errorDOB", "You must be at least 13 years old to register.");
                        request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
                        return;
                    }
                } catch (ParseException e) {
                    request.setAttribute("errorDOB", "Invalid date format for DOB. Use YYYY-MM-DD");
                    request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
                    return;
                }
            }

            boolean genderSQL = "1".equals(gender);


            boolean updated = dao.updateProfile(fullName, email, phoneNumber, dob, address, genderSQL);
            if (updated) {
                User u = (User) session.getAttribute("user");
                if (u != null) {
                    u.setName(fullName);
                    u.setPhone(phoneNumber);
                    u.setDob(dob);
                    u.setAddress(address);
                    u.setGender(genderSQL);
                }

                request.setAttribute("success", "Profile updated successfully.");
                request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
            } else {
                request.setAttribute("error", "Failed to update profile. Please try again.");
                request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
            }

        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
        }
    }
        /**
         * Returns a short description of the servlet.
         *
         * @return a String containing servlet description
         */
        @Override
        public String getServletInfo
        
            () {
        return "Short description";
        }// </editor-fold>

    }
