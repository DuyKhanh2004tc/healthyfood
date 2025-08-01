/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

package controller;

import dal.DAOBlog;
import dal.DAOTag;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Blog;
import model.Tag;
import model.User;
import utils.Pagination;

/**
 *
 * @author Hoa
 */
public class SearchBlogServlet extends HttpServlet {
   
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
            out.println("<title>Servlet SearchBlogServlet</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SearchBlogServlet at " + request.getContextPath () + "</h1>");
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
        DAOBlog daoBlog = new DAOBlog(); 
        DAOTag daoTag = new DAOTag(); 
        String keyword = request.getParameter("keyword"); 

        List<Blog> blogList; 
        if (keyword != null && !keyword.isEmpty()) {
            blogList = daoBlog.searchBlogsByTitle(keyword); 
        } else {
            blogList = daoBlog.getAllBlogsByNewest(); 
        }

        List<Tag> tagList = daoTag.listAllTag(); 

        HttpSession session = request.getSession(); 
        User u = (User) session.getAttribute("user"); 

        int page = 1; 
        int pageSize = 6; 
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1; 
            }
        }

        List<Blog> pagedList = Pagination.paginate(blogList, page, pageSize); 
        int totalPages = (int) Math.ceil((double) blogList.size() / pageSize); 

        // Gửi dữ liệu đến JSP
        request.setAttribute("blogList", pagedList); 
        request.setAttribute("tagList", tagList); 
        request.setAttribute("currentPage", page); 
        request.setAttribute("totalPage", totalPages); 
        request.setAttribute("keyword", keyword); 

        request.getRequestDispatcher("/view/nutritionBlog.jsp").forward(request, response); 
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
