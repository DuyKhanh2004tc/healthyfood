/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOBlog;
import dal.DAOCategory;
import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Blog;
import model.Category;
import model.Product;

/**
 *
 * @author ASUS
 */
public class ChatbotDataServlet extends HttpServlet {

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
            out.println("<title>Servlet ChatbotDataServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ChatbotDataServlet at " + request.getContextPath() + "</h1>");
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
        DAOProduct daoProduct = new DAOProduct();
        DAOCategory daoCategory = new DAOCategory();
        DAOBlog daoBlog = new DAOBlog();
        List<Product> productList = daoProduct.getAllProduct();
        List<Category> categoryList = daoCategory.getAllCategory();
        List<Blog> blogList = daoBlog.getAllBlog();
        
        StringBuilder sb = new StringBuilder("Product List:\n");
        for (Product p : productList) {
            sb.append("- Name: ").append(p.getName()).append("\n")
                    .append("  Description: ").append(p.getDescription()).append("\n")
                    .append("  Price: ").append(p.getPrice()).append("đ\n")
                    .append("  In Stock: ").append(p.getStock()).append("\n")
                    .append("  Shelf Life Hours: ").append(p.getShelfLifeHours()).append(" giờ\n")
                    .append("  Rating: ").append(p.getRate()).append("\n")
                    .append("  In Category: ").append(p.getCategory().getName()).append("\n\n");
        }
        sb.append("Category List:\n");
        for (Category c : categoryList) {
            sb.append("- ").append(c.getName()).append(": ").append(c.getId()).append("\n\n");
        }
        sb.append("Blog List:\n");
        for(Blog b : blogList){
            sb.append("- Blog Name:").append(b.getTitle()).append("\n")
                    .append("Content:").append(b.getDescription());
        }
        
        
        response.setContentType("text/plain");
        response.getWriter().write(sb.toString());
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
