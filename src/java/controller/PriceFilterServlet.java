/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import model.Product;

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
        List<Product> productList;
        String index_raw = request.getParameter("index");
        if (index_raw == null) {
            index_raw = "1";
        }
        int index = Integer.parseInt(index_raw);
        int totalProduct = dao.getTotalProduct();
        int pages = totalProduct / 12;
        if (totalProduct % 12 != 0) {
            pages++;
        }
        request.setAttribute("totalPage", pages);

        if (request.getParameter("minPrice") != null || request.getParameter("maxPrice") != null) {
            String min_raw = request.getParameter("minPrice");
            String max_raw = request.getParameter("maxPrice");
            double min, max;

            try {
                boolean hasMin = min_raw != null && !min_raw.isEmpty();
                boolean hasMax = max_raw != null && !max_raw.isEmpty();
                if (hasMin && hasMax) {
                    min = Double.parseDouble(min_raw);
                    max = Double.parseDouble(max_raw);
                    if (min > max) {
                        request.setAttribute("eMessage", "Min price must be less than or equal Max price");
                        request.setAttribute("minPrice", min);
                        request.setAttribute("maxPrice", max);
                        productList = dao.getProductPagination(index, 12);
                        request.setAttribute("productList", productList);
                        request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                        return;

                    } else {
                        productList = dao.getPriceInRange(min, max);
                        if (productList == null || productList.isEmpty()) {
                            request.setAttribute("notFoundMessage", "Not found product in entered price range! ");
                            productList = dao.getProductPagination(index, 12);
                            request.setAttribute("minPrice", min);
                            request.setAttribute("maxPrice", max);
                            request.setAttribute("productList", productList);
                            request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                            return;
                        }
                        request.setAttribute("minPrice", min);
                        request.setAttribute("maxPrice", max);
                        request.setAttribute("productList", productList);
                        request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                        return;
                    }

                } else if (hasMin) {
                    min = Double.parseDouble(min_raw);
                    productList = dao.getPriceInRange(min, 1000);
                    request.setAttribute("minPrice", min);
                    request.setAttribute("productList", productList);
                    request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                    return;
                } else if (hasMax) {
                    max = Double.parseDouble(max_raw);
                    productList = dao.getPriceInRange(0, max);
                    request.setAttribute("maxPrice", max);
                    request.setAttribute("productList", productList);
                    request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                    return;

                } else {

                    productList = dao.getProductPagination(index, 12);
                    request.setAttribute("productList", productList);
                    request.getRequestDispatcher("/view/home.jsp").forward(request, response);
                    return;
                }

            } catch (NumberFormatException e) {
//                    productList = dao.getAllProduct();
//                    request.setAttribute("productList", productList);
//                    request.getRequestDispatcher("/view/home.jsp").forward(request, response);
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
