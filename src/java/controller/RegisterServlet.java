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
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import model.Role;
import model.User;

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

        String fullName = request.getParameter("fullname");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phonenumber");
        String dateOfBirth = request.getParameter("dateofbirth");
        String address = request.getParameter("address");
        String gender = request.getParameter("gender");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmpassword");
        
        if(password.equals(confirmPassword)){
        DAOUser dao = new DAOUser();
        ArrayList<User> user = dao.getUser();
        
        if (dao.checkEmailExists(email, -1)) {
                request.setAttribute("errorRegiter", "Email already exists");
                request.getRequestDispatcher("view/register.jsp").forward(request, response);
                return;
            }
        
        Date dob = null;
            if (dateOfBirth != null && !dateOfBirth.trim().isEmpty()) {
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = sdf.parse(dateOfBirth);
                    dob = new Date(parsedDate.getTime());
                } catch (ParseException e) {
                    request.setAttribute("error", "Invalid date format for DOB. Use YYYY-MM-DD");
                    request.getRequestDispatcher("view/register.jsp").forward(request, response);
                    return;
                }
            }
        
        boolean genderSQL = "1".equals(gender);
           
        
            User customer = new User();
            customer.setName(fullName);
            customer.setEmail(email);
            customer.setPassword(password);
            customer.setPhone(phoneNumber);
            customer.setDob(dob);
            customer.setAddress(address);
            customer.setGender(genderSQL);


            boolean added = dao.addAccountByRole(customer);
            if (added) {
                //session.setAttribute("success", "User added successfully with role ID " + " at " + new java.util.Date());
                response.sendRedirect("DisplayAccount?idRole=" + "&page=1");
            } else {
                request.setAttribute("error", "Failed to add user: " + dao.getStatus());
                request.getRequestDispatcher("view/addAccount.jsp").forward(request, response);
            }
        
    } else {
            request.setAttribute("error", "Password and Confirm Password do not match.");
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
