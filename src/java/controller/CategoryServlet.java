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
import model.User;
import utils.Pagination;

/**
 *
 * @author ASUS
 */
public class CategoryServlet extends HttpServlet {

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
            out.println("<title>Servlet CategoryServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CategoryServlet at " + request.getContextPath() + "</h1>");
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
        DAOProduct dao = new DAOProduct();
        DAOCategory daoCategory = new DAOCategory();
        HttpSession session = request.getSession();

        User u = (User) session.getAttribute("user");
        session.removeAttribute("keyword");
        String categoryId_raw = request.getParameter("categoryId");
        List<Category> categoryList = daoCategory.getAllCategory();
        request.setAttribute("categoryList", categoryList);
        List<Product> productList;
        Product newest = dao.getNewestProduct();
        request.setAttribute("newProduct", newest);

        try {

            int categoryId = Integer.parseInt(categoryId_raw);
            int userRoleId = -1;
            if (u != null && u.getRole() != null) {
                userRoleId = u.getRole().getId();
            }

            String index_raw = request.getParameter("index");
            int page = 1;
            int pageSize = 12;
            if (index_raw != null) {
                try {
                    page = Integer.parseInt(index_raw);
                } catch (NumberFormatException e) {
                    page = 1;
                }
            }

            if (categoryId == 0) {
                session.removeAttribute("categoryId");
                productList = dao.getAllProduct();
            } else {
                session.setAttribute("categoryId", categoryId);
                productList = dao.getProductByCategory(categoryId);
            }

            
            int totalProducts = productList.size();
            int totalPages = (int) Math.ceil((double) totalProducts / pageSize);
            List<Product> pagedList = Pagination.paginate(productList, page, pageSize);

            request.setAttribute("totalPage", totalPages);
            request.setAttribute("currentPage", page);
            request.setAttribute("productList", pagedList);

            if (userRoleId == 4) {
                request.getRequestDispatcher("/view/nutritionistHome.jsp").forward(request, response);
            } else {
                request.getRequestDispatcher("/view/home.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
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
//        processRequest(request, response);
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
