/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOBlog;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;

import model.Blog;
import model.User;

/**
 *
 * @author HP
 */
public class BlogDetailServlet extends HttpServlet {

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
            out.println("<title>Servlet BlogDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet BlogDetailServlet at " + request.getContextPath() + "</h1>");
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
        String blogIdStr = request.getParameter("blogId");
        int blogId;
        try {
            blogId = Integer.parseInt(blogIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid Blog ID.");
            request.getRequestDispatcher("view/blogDetail.jsp").forward(request, response);
            return;
        }
        DAOBlog dao = new DAOBlog();
        Blog blog = dao.getBlogById(blogId);
        List<Blog> b = dao.getAllBlog();
        int prevId = 0;
        int nextId = 0;
        for (int i = 0; i < b.size(); i++) {
            if (b.get(i).getId() == blogId) {
                if (i > 0) {
                    prevId = b.get(i - 1).getId();
                }
                if (i < b.size() - 1) {
                    nextId = b.get(i + 1).getId();
                }
                break;
            }
        }

        request.setAttribute("blogId", blog.getId());
        request.setAttribute("title", blog.getTitle());
        request.setAttribute("description", blog.getDescription());
        request.setAttribute("created_at", blog.getCreated_at());
        request.setAttribute("image", blog.getImage());
        request.setAttribute("createBy", blog.getUser().getName());
        request.setAttribute("prevId", prevId != 0 ? prevId : b.get(b.size() - 1).getId());
        request.setAttribute("nextId", nextId != 0 ? nextId : b.get(0).getId());
        request.getRequestDispatcher("view/blogDetail.jsp").forward(request, response);
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
        String action = request.getParameter("action");
        User user = (User) request.getSession().getAttribute("user");
        if("editBlog".equals(action)){
        String blogIdstr = request.getParameter("blogId");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        
        int blogId = Integer.parseInt(blogIdstr);
        Blog blog = new Blog();
        blog.setId(blogId);
        blog.setCreated_at(new java.sql.Timestamp(System.currentTimeMillis()));
        blog.setDescription(description);
        blog.setTitle(title);
        blog.setUser(user);
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
