/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DAOCategory;
import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Category;
import model.Product;
import utils.Pagination;

/**
 *
 * @author ASUS
 */
public class NutritionistHomeServlet extends HttpServlet {
   
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
            out.println("<title>Servlet NutritionistHomeServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet NutritionistHomeServlet at " + request.getContextPath () + "</h1>");
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
        DAOProduct dao = new DAOProduct();
        HttpSession session = request.getSession();
        session.removeAttribute("keyword");
        session.removeAttribute("categoryId");
        List<Product> productList = dao.getAllProduct();
        int page = 1;
        int pageSize = 12;
        String index_raw = request.getParameter("index");
        if (index_raw != null) {
            try {
                page = Integer.parseInt(index_raw);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        int totalPages = (int) Math.ceil((double) productList.size() / pageSize);
        List<Product> pagedList = Pagination.paginate(productList, page, pageSize);       
        request.setAttribute("productList", pagedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPages);
        DAOCategory dao2 = new DAOCategory();
        List<Category> categoryList = dao2.getAllCategory();
        request.setAttribute("categoryList",categoryList);
        request.getRequestDispatcher("/view/nutritionistHome.jsp").forward(request, response);
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
