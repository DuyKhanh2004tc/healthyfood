/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOCart;
import dal.DAOOrder;
import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import model.CartItem;
import model.Order;
import model.OrderStatus;
import model.User;

/**
 *
 * @author ASUS
 */
public class OrderCheckoutServlet extends HttpServlet {

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
            out.println("<title>Servlet OrderCheckoutServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet OrderCheckoutServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        DAOProduct daoProduct = new DAOProduct();
        DAOOrder daoOrder = new DAOOrder();
        DAOCart daoCart = new DAOCart();
        User u = (User) session.getAttribute("user");
        String userName = request.getParameter("userName");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        String email = request.getParameter("email");
        String paymentMethod = request.getParameter("paymentMethod");
        Double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
        if (request.getParameter("userName") != null && request.getParameter("phone") != null && request.getParameter("paymentMethod") != null
                && request.getParameter("address") != null && request.getParameter("email") != null && request.getParameter("totalAmount") != null) {
            try {
                boolean isEnoughStock = true;
                List<CartItem> itemList = (List<CartItem>) session.getAttribute("itemList");
                for (CartItem item : itemList) {
                    int productId = item.getProduct().getId();
                    int quantityOrdered = item.getQuantity();
                    int stock = daoProduct.getProductStock(productId);
                    if (stock < quantityOrdered) {
                        isEnoughStock = false;
                        break;
                    }
                }

                if (!isEnoughStock) {
                    session.setAttribute("stockError", "Some items in your cart are out of stock or not enough stock.");
                    response.sendRedirect("cart");
                    return;
                }

                Order order = new Order();
                order.setUser(u);
                order.setPaymentMethod(paymentMethod);
                order.setReceiverEmail(email);
                order.setReceiverName(userName);
                order.setReceiverPhone(phone);
                order.setShippingAddress(address);
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setId(1);
                order.setStatus(orderStatus);
                order.setShipper(null);
                order.setTotalAmount(totalAmount);
                int orderId = daoOrder.insertOrder(order);
                order.setId(orderId);

                for (CartItem item : itemList) {
                    int productId = item.getProduct().getId();
                    int quantityOrdered = item.getQuantity();
                    daoProduct.reduceStock(productId, quantityOrdered);
                }

                daoCart.deleteCartItemsByUserId(u.getId());

                request.setAttribute("order", order);
                request.setAttribute("itemList", itemList);
                session.removeAttribute("itemList");
                request.getRequestDispatcher("/view/orderCheckout.jsp").forward(request, response);
                return;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        request.getRequestDispatcher("/view/orderCheckout.jsp").forward(request, response);
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
