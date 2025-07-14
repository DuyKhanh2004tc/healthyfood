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
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        
        if (u == null) {
        request.setAttribute("error", "User not logged in.");
        request.getRequestDispatcher("view/login.jsp").forward(request, response);
        return;
    }
        
        if (u.getRole().getId() == 4) {
            request.getRequestDispatcher("view/nutritionistProfile.jsp").forward(request, response);
        } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5|| u.getRole().getId() == 6) {
            request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
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
        try {
            String fullName = request.getParameter("fullname");
            String email = request.getParameter("email");
            String phoneNumber = request.getParameter("phonenumber");
            String dateOfBirth = request.getParameter("dateofbirth");
            String address = request.getParameter("address");
            String gender = request.getParameter("gender");

            boolean hasError = false;

            if (fullName != null) {
                fullName = fullName.trim().replaceAll("\\s+", " ");
            }
            if (address != null) {
                address = address.trim().replaceAll("\\s+", " ");
            }
            if (phoneNumber != null) {
                phoneNumber = phoneNumber.trim().replaceAll("\\s+", "");
            }

            if (fullName == null || fullName.isEmpty() || fullName.length() < 2 || fullName.length() > 50 || !fullName.matches("^[\\p{L}\\s]+$")) {
                request.setAttribute("errorName", "Full name must be 2-50 characters and contain only letters and spaces.");
                hasError = true;
            }

            if (phoneNumber == null || phoneNumber.isEmpty() || !phoneNumber.matches("^(0|\\+84)[0-9]{9}$")) {
                request.setAttribute("errorPhone", "Phone number must start with 0 or +84 and contain exactly 9 digits.");
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

            if (hasError) {
                if (u.getRole().getId() == 4) {
                    request.getRequestDispatcher("view/nutritionistProfile.jsp").forward(request, response);
                } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5|| u.getRole().getId() == 6) {
                    request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
                }
                return;
            }

            User currentUser = (User) session.getAttribute("user");
            boolean genderSQL = "1".equals(gender);
            if (currentUser != null
                    && fullName.equals(currentUser.getName())
                    && phoneNumber.equals(currentUser.getPhone())
                    && dateOfBirth.equals(currentUser.getDob() != null ? new SimpleDateFormat("yyyy-MM-dd").format(currentUser.getDob()) : "")
                    && address.equals(currentUser.getAddress())
                    && genderSQL == currentUser.isGender()) {
                request.setAttribute("success", "No changes made.");
                if (u.getRole().getId() == 4) {
                    request.getRequestDispatcher("view/nutritionistProfile.jsp").forward(request, response);
                } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5|| u.getRole().getId() == 6) {
                    request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
                }
                return;
            }

            DAOUser dao = new DAOUser();
            boolean updated = dao.updateProfile(fullName, email, phoneNumber, dob, address, genderSQL);
            if (updated) {
                if (u != null) {
                    u.setName(fullName);
                    u.setPhone(phoneNumber);
                    u.setDob(dob);
                    u.setAddress(address);
                    u.setGender(genderSQL);
                }
                request.setAttribute("success", "Profile updated successfully.");
                if (u.getRole().getId() == 4) {
                    request.getRequestDispatcher("view/nutritionistProfile.jsp").forward(request, response);
                } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5|| u.getRole().getId() == 6) {
                    request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
                }
                return;
            } else {
                request.setAttribute("error", "Failed to update profile. Please try again.");
                hasError = true;
            }
           
        } catch (Exception e) {
            request.setAttribute("error", "Error: " + e.getMessage());
            if (u.getRole().getId() == 4) {
                request.getRequestDispatcher("view/nutritionistProfile.jsp").forward(request, response);
            } else if (u.getRole().getId() == 2 || u.getRole().getId() == 3 || u.getRole().getId() == 5|| u.getRole().getId() == 6) {
                request.getRequestDispatcher("view/userProfile.jsp").forward(request, response);
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
