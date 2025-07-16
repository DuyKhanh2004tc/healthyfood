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
import model.Product;
import utils.Pagination;

/**
 *
 * @author ASUS
 */
public class PriceFilterServlet extends HttpServlet {

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
            out.println("<title>Servlet PriceFilterServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet PriceFilterServlet at " + request.getContextPath() + "</h1>");
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
//        processRequest(request, response);
        DAOProduct dao = new DAOProduct();
        DAOCategory daoCategory = new DAOCategory();
        List<Product> productList;
        request.setAttribute("categoryList", daoCategory.getAllCategory());
        Product newest = dao.getNewestProduct();
        request.setAttribute("newProduct", newest);

        HttpSession session = request.getSession();
        int categoryId = 0;
        if (session.getAttribute("categoryId") != null) {
            categoryId = (int) session.getAttribute("categoryId");
        }

            String min_raw = request.getParameter("minPrice");
            String max_raw = request.getParameter("maxPrice");
            double min, max;
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

            try {
                boolean hasMin = min_raw != null && !min_raw.isEmpty();
                boolean hasMax = max_raw != null && !max_raw.isEmpty();
                min = hasMin ? Double.parseDouble(min_raw) : 0;
                max = hasMax ? Double.parseDouble(max_raw) : 1000;
                if (hasMin && hasMax) {
                    if (min > max) {
                        request.setAttribute("eMessage", "Min price must be less than or equal Max price");
                        request.setAttribute("minPrice", min);
                        request.setAttribute("maxPrice", max);
                        productList = (categoryId == 0)
                                ? dao.getAllProduct()
                                : dao.getProductByCategory(categoryId);
                    } else {
                        productList = (categoryId == 0)
                                ? dao.getPriceInRange(min, max)
                                : dao.getPriceInRangeByCategoryId(min, max, categoryId);

                        request.setAttribute("minPrice", min);
                        request.setAttribute("maxPrice", max);

                        if (productList == null || productList.isEmpty()) {
                            request.setAttribute("notFoundMessage", "Not found product in entered price range!");
                            productList = (categoryId == 0)
                                    ? dao.getAllProduct()
                                    : dao.getProductByCategory(categoryId);
                        }
                    }

                } else if (hasMin) {
                    productList = (categoryId == 0)
                            ? dao.getPriceInRange(min, 1000)
                            : dao.getPriceInRangeByCategoryId(min, 1000, categoryId);
                    request.setAttribute("minPrice", min);
                } else if (hasMax) {
                    productList = (categoryId == 0)
                            ? dao.getPriceInRange(0, max)
                            : dao.getPriceInRangeByCategoryId(0, max, categoryId);
                    request.setAttribute("maxPrice", max);

                } else {
                    productList = (categoryId == 0)
                            ? dao.getAllProduct()
                            : dao.getProductByCategory(categoryId);
                }

                int totalPages = (int) Math.ceil((double) productList.size() / pageSize);
                List<Product> pagedList = Pagination.paginate(productList, page, pageSize);
                request.setAttribute("productList", pagedList);
                request.setAttribute("totalPage", totalPages);
                request.setAttribute("currentPage", page);

                request.getRequestDispatcher("/view/home.jsp").forward(request, response);

            } catch (NumberFormatException e) {
//                    productList = dao.getAllProduct();
//                    request.setAttribute("productList", productList);
//                    request.getRequestDispatcher("/view/home.jsp").forward(request, response);
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
