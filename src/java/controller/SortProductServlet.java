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
import java.util.ArrayList;
import java.util.List;
import model.Category;
import model.Product;
import model.User;
import utils.Pagination;

/**
 *
 * @author ASUS
 */
public class SortProductServlet extends HttpServlet {

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
            out.println("<title>Servlet SortProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet SortProductServlet at " + request.getContextPath() + "</h1>");
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
        int userRoleId = (u != null && u.getRole() != null) ? u.getRole().getId() : -1;

        String orderByPrice = request.getParameter("orderBy");
        String orderByName = request.getParameter("nameOrderBy");
        String orderByRate = request.getParameter("rateOrderBy");
        String orderByDate = request.getParameter("dateOrderBy");
        String index_raw = request.getParameter("index");
        int index = 1;
        if (index_raw != null) {
            try {
                index = Integer.parseInt(index_raw);
            } catch (NumberFormatException e) {
                index = 1;
            }
        }

        int categoryId = 0;
        if (session.getAttribute("categoryId") != null) {
            categoryId = (int) session.getAttribute("categoryId");
        }

        List<Product> sortedList;
        if (orderByRate != null && !orderByRate.isEmpty()) {
            sortedList = (categoryId == 0)
                    ? dao.getRatingSorted(orderByRate)
                    : dao.getRatingSortedByCategoryId(orderByRate, categoryId);
            request.setAttribute("rateOrderBy", orderByRate);
        } else if (orderByName != null && !orderByName.isEmpty()) {
            sortedList = (categoryId == 0)
                    ? dao.getNameSorted(orderByName)
                    : dao.getNameSortedByCategoryId(orderByName, categoryId);
            request.setAttribute("nameOrderBy", orderByName);
        } else if (orderByPrice != null && !orderByPrice.isEmpty()) {
            sortedList = (categoryId == 0)
                    ? dao.getPriceSorted(orderByPrice)
                    : dao.getPriceSortedByCategoryId(orderByPrice, categoryId);
            request.setAttribute("orderBy", orderByPrice);
        } else if (orderByDate != null && !orderByDate.isEmpty()) {
            sortedList = (categoryId == 0)
                    ? dao.getTimeSorted(orderByDate)
                    : dao.getTimeSortedByCategoryId(orderByDate, categoryId);
            request.setAttribute("dateOrderBy", orderByDate);
        } else {
            sortedList = (categoryId == 0)
                    ? dao.getAllProduct()
                    : dao.getProductByCategory(categoryId);
        }

        int pageSize = 12;
        int totalPage = (int) Math.ceil((double) sortedList.size() / pageSize);
        List<Product> pagedList = Pagination.paginate(sortedList, index, pageSize);

        request.setAttribute("productList", pagedList);
        request.setAttribute("totalPage", totalPage);
        request.setAttribute("currentPage", index);
        request.setAttribute("categoryId", categoryId);
        request.setAttribute("categoryList", dao2.getAllCategory());
        request.setAttribute("newProduct", dao.getNewestProduct());

        if (userRoleId == 4) {
            request.getRequestDispatcher("/view/nutritionistHome.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/view/home.jsp").forward(request, response);
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
