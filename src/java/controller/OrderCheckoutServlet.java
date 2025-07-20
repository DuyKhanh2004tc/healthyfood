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
import model.OrderDetail;
import model.OrderStatus;
import model.Product;
import model.User;
import utils.Mail;

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
        String street = request.getParameter("street");
        String ward = request.getParameter("ward");
        String district = request.getParameter("district");
        String province = request.getParameter("province");
        String address = street + ", " + ward + ", " + district + ", " + province;
        String email = request.getParameter("email");
        String paymentMethod = request.getParameter("paymentMethod");
        String productId_raw = request.getParameter("productId");
        String quantity_raw = request.getParameter("quantity");
        String totalAmount_raw = request.getParameter("totalAmount");
        String deliveryMessage = request.getParameter("deliveryMessage");

        if (request.getParameter("userName") != null && request.getParameter("phone") != null && request.getParameter("paymentMethod") != null
                && request.getParameter("email") != null
                && request.getParameter("totalAmount") != null) {
            try {
                List<CartItem> itemList;
                boolean isEnoughStock = true;
                Double totalAmount = Double.parseDouble(totalAmount_raw);
                if (productId_raw != null && quantity_raw != null) {
                    int productId = Integer.parseInt(productId_raw);
                    int quantity = Integer.parseInt(quantity_raw);
                    Product p = daoProduct.getProductById(productId);

                    if (p.getStock() < 1) {
                        session.setAttribute("stockError", "The selected item product is out of stock.");
                        response.sendRedirect("home");
                        return;
                    }
                    CartItem item = new CartItem();
                    item.setProduct(p);
                    item.setQuantity(quantity);
                    itemList = List.of(item);
                } else {

                    itemList = (List<CartItem>) session.getAttribute("itemList");
                    for (CartItem item : itemList) {
                        int productId = item.getProduct().getId();
                        int quantityOrdered = item.getQuantity();
                        int stock = daoProduct.getProductStock(productId);
                        if (stock < quantityOrdered) {
                            isEnoughStock = false;
                            break;
                        }
                    }
                }

                if (!isEnoughStock) {
                    session.setAttribute("stockError", "Some items in your cart are out of stock or not enough stock.");
                    response.sendRedirect("cart");
                    return;
                }

                Order order = new Order();
                if (u != null) {
                    order.setUser(u);
                } else {
                    order.setUser(null);
                }
                order.setPaymentMethod(paymentMethod);
                order.setReceiverEmail(email);
                order.setReceiverName(userName);
                order.setReceiverPhone(phone);
                order.setShippingAddress(address);
                if (deliveryMessage != null) {
                    order.setDeliveryMessage(deliveryMessage);
                } else {
                    order.setDeliveryMessage(null);
                }
                OrderStatus orderStatus = new OrderStatus();
                orderStatus.setId(1);
                order.setStatus(orderStatus);
                order.setShipper(null);
                order.setTotalAmount(totalAmount);
                int orderId = daoOrder.insertOrder(order);
                order.setId(orderId);

                for (CartItem item : itemList) {
                    OrderDetail od = new OrderDetail();
                    od.setOrder(order);
                    od.setProduct(item.getProduct());
                    od.setQuantity(item.getQuantity());
                    od.setPrice(item.getProduct().getPrice());
                    daoOrder.insertOrderDetail(od);
                    daoProduct.reduceStock(item.getProduct().getId(), item.getQuantity());
                }
                if (productId_raw == null && u != null) {
                    daoCart.deleteCartItemsByUserId(u.getId());
                } else if (productId_raw == null && u == null) {
                    session.removeAttribute("itemList");
                }
                if (u == null) {
                    StringBuilder content = new StringBuilder();
                    content.append("Dear ").append(userName).append(",\n\n");
                    content.append("Thank you for shopping with Healthy Food!\n\n");
                    content.append("We have received your order successfully. Below are your order details:\n\n");
                    content.append("Order ID: ").append(order.getId()).append("\n");
                    content.append("Total Amount: $").append(order.getTotalAmount()).append("\n");
                    content.append("Shipping Address: ").append(address).append("\n");
                    content.append("Phone: ").append(phone).append("\n");
                    content.append("Email: ").append(email).append("\n");
                    content.append("Payment Method: ").append(paymentMethod).append("\n\n");

                    content.append("Items Ordered:\n");
                    for (CartItem item : itemList) {
                        content.append("- ")
                                .append(item.getProduct().getName())
                                .append(" x ").append(item.getQuantity())
                                .append(" - $").append(item.getProduct().getPrice()).append("\n");
                    }

                    content.append("\nWe will process and ship your order soon!\n\n");
                    content.append("Best regards,\nHealthy Food Team ");

                    Mail.sendMail(email, "[HealthyFood] Order Confirmation - Order #" + order.getId(), content.toString());
                }

                request.setAttribute("order", order);
                request.setAttribute("itemList", itemList);

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
