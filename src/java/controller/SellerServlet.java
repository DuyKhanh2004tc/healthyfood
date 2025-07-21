package controller;

import dal.DAOSeller;
import dal.DAOCategory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import model.Order;

@WebServlet(name = "SellerServlet", urlPatterns = {"/seller"})
public class SellerServlet extends HttpServlet {
    private DAOSeller DAOSeller;
    private static final Logger LOGGER = Logger.getLogger(SellerServlet.class.getName());
    private static final int PRODUCTS_PER_PAGE = 5;
    private static final int ORDERS_PER_PAGE = 12;

    @Override
    public void init() throws ServletException {
        DAOSeller = DAOSeller.getInstance();
        LOGGER.log(Level.INFO, "SellerServlet initialized, DAO status: {0}", DAOSeller.getStatus());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        int currentPage = 1;
        try {
            String pageStr = request.getParameter("page");
            if (pageStr != null) {
                currentPage = Integer.parseInt(pageStr);
                if (currentPage < 1) currentPage = 1;
            }
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Số trang không hợp lệ.");
        }

        if ("requestDelete".equals(service)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                DAOSeller.deleteProductById(productId);
                session.setAttribute("message", "Xóa sản phẩm thành công!");
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Định dạng ID sản phẩm không hợp lệ.");
                LOGGER.log(Level.WARNING, "Định dạng ID sản phẩm không hợp lệ: {0}", e.getMessage());
            } catch (RuntimeException e) {
                session.setAttribute("errorMessage", "Xóa thất bại: " + e.getMessage());
                LOGGER.log(Level.SEVERE, "Xóa thất bại: {0}", e.getMessage());
            }
            displayProductList(request, response, null, currentPage);
        } else if ("searchByKeywords".equals(service)) {
            String keywords = request.getParameter("keywords");
            displayProductList(request, response, keywords, currentPage);
        } else {
            displayProductList(request, response, null, currentPage);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        if (service == null) {
            LOGGER.log(Level.WARNING, "Service parameter is null. URL: {0}, Params: {1}",
                    new Object[]{request.getRequestURL(), request.getParameterMap()});
            session.setAttribute("errorMessage", "Yêu cầu không hợp lệ: Thiếu tham số service.");
            response.sendRedirect("seller?service=list");
            return;
        }

        session.setAttribute("errorMessage", "Yêu cầu không hợp lệ: " + service);
        response.sendRedirect("seller?service=list");
    }

    private void displayProductList(HttpServletRequest request, HttpServletResponse response, String keywords, int currentPage)
            throws ServletException, IOException {
        int productsPerPage = PRODUCTS_PER_PAGE;
        List<Product> productList;
        int totalRows;
        String service = "list";
        if (keywords != null && !keywords.trim().isEmpty()) {
            productList = DAOSeller.searchProductsByNamePaginated(keywords.trim(), currentPage, productsPerPage);
            totalRows = DAOSeller.getSearchProductsByNameCount(keywords.trim());
            service = "searchByKeywords";
        } else {
            productList = DAOSeller.getProductPagination(currentPage, productsPerPage);
            totalRows = DAOSeller.getTotalProductCount();
        }

        int totalPages = (int) Math.ceil((double) totalRows / productsPerPage);
        if (currentPage > totalPages && totalPages > 0) {
            currentPage = totalPages;
            if (keywords != null && !keywords.trim().isEmpty()) {
                productList = DAOSeller.searchProductsByNamePaginated(keywords.trim(), currentPage, productsPerPage);
            } else {
                productList = DAOSeller.getProductPagination(currentPage, productsPerPage);
            }
        }

        request.setAttribute("allProducts", productList);
        request.setAttribute("keywords", keywords);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("service", service);
        request.getRequestDispatcher("view/ProductManagement.jsp").forward(request, response);
    }

    
}