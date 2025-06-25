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
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import model.*;

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
        String productIdStr = request.getParameter("productId");
        int productId;
        try {
            productId = Integer.parseInt(productIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID.");
            doGet(request, response);
            return;
        }

        DAOFeedback daoFeedback = new DAOFeedback();
        DAOProduct daoProduct = new DAOProduct();
        Product product = daoProduct.getProductById(productId);

        if (product == null) {
            request.setAttribute("error", "Product not found.");
            doGet(request, response);
            return;
        }
        if ("comment".equals(action)) {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null || user.getRole().getId() != 3) {
                response.sendRedirect("login.jsp");
                return;
            }

            String content = request.getParameter("content");
            String ratingStr = request.getParameter("rating");

            if (ratingStr == null || ratingStr.trim().isEmpty()) {
                request.getSession().setAttribute("errorFeedback", "Please select a rating.");
                doGet(request, response);
                return;
            }

            double rate;
            try {
                rate = Double.parseDouble(ratingStr);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorFeedback", "Invalid rating value.");
                doGet(request, response);
                return;
            }

            Feedback feedback = new Feedback();
            feedback.setUser(user);
            feedback.setProduct(product);
            feedback.setContent(content);
            feedback.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            feedback.setRate(rate);

            daoFeedback.insertFeedback(feedback);
            request.getSession().setAttribute("messageFeedback", "Comment submitted successfully.");
            response.sendRedirect(request.getContextPath() + "/productDetail?productId=" + productId);
            return;

        } else if ("deleteFeedback".equals(action)) {
            String feedbackIdStr = request.getParameter("feedbackId");
            int feedbackId;
            try {
                feedbackId = Integer.parseInt(feedbackIdStr);
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("errorFeedback", "Invalid feedback ID.");
                doGet(request, response);
                return;
            }

            daoFeedback.deleteFeedbackById(feedbackId);
            request.getSession().setAttribute("messageFeedback", "Feedback deleted successfully.");
            response.sendRedirect(request.getContextPath() + "/productDetail?productId=" + productId);
            return;

        } else if ("editFeedback".equals(action)) {
            User user = (User) request.getSession().getAttribute("user");
            if (user == null || user.getRole().getId() != 3) {
                response.sendRedirect("login.jsp");
                return;
            }

            String feedbackIdStr = request.getParameter("feedbackId");
            String content = request.getParameter("content");
            String ratingStr = request.getParameter("rating");

            int feedbackId;
            feedbackId = Integer.parseInt(feedbackIdStr);
            if (ratingStr == null || ratingStr.trim().isEmpty()) {
                request.getSession().setAttribute("errorFeedback", "Please select a rating.");
                doGet(request, response);
                return;
            }
            double rate = Double.parseDouble(ratingStr);
            Feedback feedback = new Feedback();
            feedback.setId(feedbackId);
            feedback.setUser(user);
            feedback.setProduct(product);
            feedback.setContent(content);
            feedback.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            feedback.setRate(rate);

            daoFeedback.updateFeedbackById(feedbackId, feedback);
            request.getSession().setAttribute("messageFeedback", "Feedback edited successfully.");
            response.sendRedirect(request.getContextPath() + "/productDetail?productId=" + productId);
            return;

        } else if ("add".equals(action)) {
            String numberStr = request.getParameter("number");
            int number;
            try {
                number = Integer.parseInt(numberStr);
                if (number <= 0) {
                    request.getSession().setAttribute("error", "Quantity must be greater than 0.");
                    doGet(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.getSession().setAttribute("error", "Invalid quantity.");
                doGet(request, response);
                return;
            }

            if (number > product.getStock()) {
                request.getSession().setAttribute("error", "Requested quantity exceeds available stock.");
                doGet(request, response);
                return;
            }

            request.getSession().setAttribute("message", "Added to cart successfully.");
            response.sendRedirect(request.getContextPath() + "/productDetail?productId=" + productId);
            return;
        }

        
        request.setAttribute("error", "Invalid action.");
        doGet(request, response);
    }

    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
