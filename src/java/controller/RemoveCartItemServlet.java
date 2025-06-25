/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOCart;
import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.CartItem;
import model.User;

/**
 *
 * @author ASUS
 */
public class RemoveCartItemServlet extends HttpServlet {

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
            out.println("<title>Servlet RemoveCartItemServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RemoveCartItemServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        DAOCart daoCart = new DAOCart();
        if (u != null) {
            if (request.getParameter("removePId") != null) {
                try {
                    int productId = Integer.parseInt(request.getParameter("removePId"));
                    if (u.getRole().getId() == 3) {
                        int userId = u.getId();
                        daoCart.removeCartItem(u.getId(), productId);
                        response.sendRedirect("cart");
                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (request.getParameter("removePId") != null) {
                try {
                    int productId = Integer.parseInt(request.getParameter("removePId"));
                    List<CartItem> itemList = (List<CartItem>) session.getAttribute("itemList");
                    for (int i = 0; i < itemList.size(); i++) {
                        if (itemList.get(i).getProduct().getId() == productId) {
                            itemList.remove(i);
                            break;
                        }
                    }
                    response.sendRedirect("cart"); 
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
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
