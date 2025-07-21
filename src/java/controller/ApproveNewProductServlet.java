/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOCategory;
import dal.DAOProposedProduct;
import dal.DAOUser;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.Category;
import model.ProposedProduct;
import model.User;
import utils.Pagination;

/**
 *
 * @author ASUS
 */
public class ApproveNewProductServlet extends HttpServlet {

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
            out.println("<title>Servlet ApproveNewProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ApproveNewProductServlet at " + request.getContextPath() + "</h1>");
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
        DAOProposedProduct daoPropose = new DAOProposedProduct();
        DAOCategory daoCategory = new DAOCategory();
        List<Category> categoryList = daoCategory.getAllCategory();
        String orderBy = request.getParameter("btn_sort");
        HttpSession session = request.getSession();
        String categoryId_raw = request.getParameter("categoryId");
        int categoryId = 0;
        String categoryName = "All Products";
        if (categoryId_raw != null && !categoryId_raw.equals("0")) {
            try {
                categoryId = Integer.parseInt(categoryId_raw);
                request.getSession().setAttribute("categoryId", categoryId);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            
        } else {
            session.removeAttribute("categoryId");
        }

        List<ProposedProduct> proposedList;
        if (orderBy != null && orderBy.equalsIgnoreCase("Descending")) {
            proposedList = (categoryId != 0)
                    ? daoPropose.getProposedProductsByCategoryDESC(categoryId)
                    : daoPropose.getAllProposedProductOrderByDESC();
        } else {
            proposedList = (categoryId != 0)
                    ? daoPropose.getAllProposedProductByCategory(categoryName)
                    : daoPropose.getAllProposedProduct();
        }

        request.setAttribute("proposedList", proposedList);

        int page = 1;
        int pageSize = 5;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        List<ProposedProduct> pagedList = Pagination.paginate(proposedList, page, pageSize);
        int totalPages = (int) Math.ceil((double) proposedList.size() / pageSize);
        
        request.setAttribute("proposedList", pagedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("categoryList", categoryList);
        request.getRequestDispatcher("/view/approveNewProduct.jsp").forward(request, response);

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
        DAOProposedProduct daoPropose = new DAOProposedProduct();
        String id_raw = request.getParameter("proposedId");
        String status = request.getParameter("btn_status");

        if (id_raw != null && status != null) {
            try {
                int id = Integer.parseInt(id_raw);
                DAOProposedProduct dao = new DAOProposedProduct();
                dao.updateProposedProductStatusById(id, status);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        doGet(request, response);

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
