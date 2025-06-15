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
        DAOCategory dao2 = new DAOCategory();
        HttpSession session = request.getSession();

        User u = (User) session.getAttribute("user");
        session.removeAttribute("keyword");
        String categoryId_raw = request.getParameter("categoryId");
        List<Category> categoryList = dao2.getAllCategory();
        request.setAttribute("categoryList", categoryList);
        List<Product> productList;

        try {

            int categoryId = Integer.parseInt(categoryId_raw);
            int userRoleId = -1;
            if (u != null && u.getRole() != null) {
                userRoleId = u.getRole().getId();
            }
            String index_raw = request.getParameter("index");
            if (index_raw == null) {
                index_raw = "1";
            }
            int index = Integer.parseInt(index_raw);
            if (categoryId == 0) {
                int totalProduct = dao.getTotalProduct();
                int pages = totalProduct / 12;
                if (totalProduct % 12 != 0) {
                    pages++;
                }
                request.setAttribute("totalPage", pages);
            } else {
                int totalProduct = dao.getTotalProductByCid(categoryId);
                int pages = totalProduct / 12;
                if (totalProduct % 12 != 0) {
                    pages++;
                }
                request.setAttribute("totalPage", pages);
            }

            if (categoryId == 0) {
                session.removeAttribute("categoryId");
                productList = dao.getProductPagination(index, 12);
                request.setAttribute("productList", productList);
                if (userRoleId == 4) {
                    request.getRequestDispatcher("/view/nutritionistHome.jsp").forward(request, response);
                    return;
                } else {
                    request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                    return;
                }
            } else {
                productList = dao.getProductPaginationByCid(categoryId, index, 12);
                request.setAttribute("productList", productList);
                session.setAttribute("categoryId", categoryId);
                if (userRoleId == 4) {
                    request.getRequestDispatcher("/view/nutritionistHome.jsp").forward(request, response);
                    return;
                } else {
                    request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                    return;
                }
            }

        } catch (Exception e) {

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
