package controller;

import dal.DAOFeedback;
import dal.DAOOrder;
import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.Feedback;
import model.Product;
import model.User;

/**
 *
 * @author HP
 */
public class ProductDetailServlet extends HttpServlet {

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
            out.println("<title>Servlet productDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet productDetailServlet at " + request.getContextPath() + "</h1>");
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
        String productIdStr = request.getParameter("productId");
        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID.");
            request.getRequestDispatcher("view/productDetail.jsp").forward(request, response);
            return;
        }

        DAOProduct dao = new DAOProduct();
        Product product = dao.getProductById(productId);
        if (product == null) {
            request.setAttribute("error", "Product not found.");
            request.getRequestDispatcher("view/productDetail.jsp").forward(request, response);
            return;
        }
        DAOFeedback feedbackDAO = new DAOFeedback();
        List<Feedback> feedbackList = feedbackDAO.getFeedbackByProductId(productId);
        List<Product> p = dao.getAllProduct();
        int prevId = 0;
        int nextId = 0;
        for (int i = 0; i < p.size(); i++) {
            if (p.get(i).getId() == productId) {
                if (i > 0) {
                    prevId = p.get(i - 1).getId();
                }
                if (i < p.size() - 1) {
                    nextId = p.get(i + 1).getId();
                }
                break;
            }
        }

        request.setAttribute("productId", product.getId());
        request.setAttribute("productName", product.getName());
        request.setAttribute("description", product.getDescription());
        request.setAttribute("price", product.getPrice());
        request.setAttribute("stock", product.getStock());
        request.setAttribute("img", product.getImgUrl());
        request.setAttribute("time", product.getShelfLifeHours());
        request.setAttribute("categoryName", product.getCategory().getName());
        request.setAttribute("categoryId", product.getCategory().getId());
        request.setAttribute("feedbackList", feedbackList);
        request.setAttribute("rate", product.getRate());
        request.setAttribute("prevId", prevId != 0 ? prevId : p.get(p.size() - 1).getId());
        request.setAttribute("nextId", nextId != 0 ? nextId : p.get(0).getId());
        
        request.getRequestDispatcher("view/productDetail.jsp").forward(request, response);

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
        if ("comment".equals(action)) {

            User user = (User) request.getSession().getAttribute("user");
            if (user == null || user.getRole().getId() != 3) {
                response.sendRedirect("login.jsp");
                return;
            }

            String content = request.getParameter("content");
            if (content == null || content.trim().isEmpty()) {
                request.setAttribute("error", "Comment cannot be empty.");
                doGet(request, response);
                return;
            }

            Feedback feedback = new Feedback();
            feedback.setUser(user);
            Product product = new Product();
            product.setId(Integer.parseInt(request.getParameter("productId")));
            feedback.setProduct(product);

            feedback.setContent(content);
            feedback.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));

            DAOFeedback daoFeedback = new DAOFeedback();
            daoFeedback.insertFeedback(feedback);

            request.setAttribute("message", "Comment submitted successfully.");
            doGet(request, response); // Load lại feedback list và trang chi tiết
            return;
        }

        String productIdStr = request.getParameter("productId");
        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID.");
            doGet(request, response); // Re-fetch product details
            return;
        }
        String numberStr = request.getParameter("number");
        int number;
        try {
            number = Integer.parseInt(numberStr);
            if (number <= 0) {
                throw new NumberFormatException("Number must be greater than 0.");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid quantity.");
            doGet(request, response); // Re-fetch product details
            return;
        }

        DAOProduct dao = new DAOProduct();
        Product product = dao.getProductById(productId);

        if (product == null) {
            request.setAttribute("error", "Product not found.");
            doGet(request, response);
            return;
        }

        if (number > product.getStock()) {
            request.setAttribute("error", "Requested quantity exceeds available stock.");
            doGet(request, response);
            return;
        }
        double totalPrice = number * product.getPrice();
        DAOOrder daoOrder = new DAOOrder();
//        boolean success = daoOrder.orderProduct(user.getId(), productId, number, totalPrice);
//        
//        if (success) {
//            request.setAttribute("message", "Product added to cart successfully!");
//        } else {
//            request.setAttribute("error", "Failed to add product to cart. Please try again.");
//        }

        request.getRequestDispatcher("view/productDetail.jsp").forward(request, response);
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
