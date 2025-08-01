/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOCart;
import dal.DAOProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import model.CartItem;
import model.Product;
import model.User;

/**
 *
 * @author ASUS
 */
public class CartServlet extends HttpServlet {

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
            out.println("<title>Servlet CartServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet CartServlet at " + request.getContextPath() + "</h1>");
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
        HttpSession session = request.getSession();
        User u = (User) session.getAttribute("user");
        DAOProduct dao = new DAOProduct();
        DAOCart daoCart = new DAOCart();
        String cartURL = request.getParameter("cartUrl");
        String stockError = (String) session.getAttribute("stockError");
        if (stockError != null) {
            request.setAttribute("stockError", stockError);
            session.removeAttribute("stockError");
        }
        if (u != null) {
            if (request.getParameter("number") != null && request.getParameter("id") != null) {
                try {
                    int number = Integer.parseInt(request.getParameter("number"));
                    int productId = Integer.parseInt(request.getParameter("id"));
                    int userId = u.getId();
                    daoCart.updateCartItemQuantity(userId, productId, number);
                    response.sendRedirect("cart");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (request.getParameter("productId") != null) {
                try {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    if (u.getRole().getId() == 3) {
                        int userId = u.getId();

                        if (request.getParameter("number1") != null) {
                            String number1 = request.getParameter("number1");
                            dao.addToCart(userId, productId, Integer.parseInt(number1));
                            response.sendRedirect(request.getContextPath() + "/productDetail?productId=" + productId);
                            return;
                        } else {
                            dao.addToCart(userId, productId, 1);
                            if (cartURL != null && !cartURL.isEmpty()) {
                                response.sendRedirect(cartURL);
                            } else {
                                response.sendRedirect("home"); // fallback
                            }
                            return;
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (u.getRole().getId() == 3) {
                List<CartItem> itemList = daoCart.getCartItemsByUserId(u.getId());
                String sort = request.getParameter("sort");
                sortItemList(itemList, sort);
                List<Product> productList = new ArrayList<>();
                boolean outOfStock = false;
                for (CartItem i : itemList) {
                    Product p = dao.getProductById(i.getProduct().getId());
                    if (i.getQuantity() > p.getStock()) {
                        outOfStock = true;
                    }
                    productList.add(p);
                }
                if (outOfStock) {
                    request.setAttribute("stockError", "Some items in your cart are out of stock.");
                }
                request.setAttribute("newProductList", dao.getNewProduct(5));
                request.setAttribute("productList", productList);
                request.setAttribute("itemList", itemList);
                request.getRequestDispatcher("/view/cart.jsp").forward(request, response);
                return;
            }
        } else {
            if (request.getParameter("productId") != null) {
                try {
                    int productId = Integer.parseInt(request.getParameter("productId"));
                    List<CartItem> itemList = (List<CartItem>) session.getAttribute("itemList");

                    if (itemList == null) {
                        itemList = new ArrayList<>();
                        session.setAttribute("itemList", itemList);
                    }

                    boolean exist = false;
                    for (CartItem ci : itemList) {
                        if (ci.getProduct().getId() == productId) {
                            ci.setQuantity(ci.getQuantity() + 1);
                            exist = true;
                            break;
                        }
                    }

                    if (!exist) {
                        Product p = dao.getProductById(productId);
                        CartItem item = new CartItem();
                        item.setProduct(p);
                        item.setQuantity(1);
                        itemList.add(item);
                    }
                    session.setAttribute("itemList", itemList);
                    if (cartURL != null && !cartURL.isEmpty()) {
                        response.sendRedirect(cartURL);
                    } else {
                        response.sendRedirect("home");
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (request.getParameter("number") != null && request.getParameter("id") != null) {
                try {
                    int productId = Integer.parseInt(request.getParameter("id"));
                    int number = Integer.parseInt(request.getParameter("number"));

                    List<CartItem> itemList = (List<CartItem>) session.getAttribute("itemList");
                    if (itemList != null) {
                        for (int i = 0; i < itemList.size(); i++) {
                            CartItem ci = itemList.get(i);
                            if (ci.getProduct().getId() == productId) {
                                int newQuantity = ci.getQuantity() + number;
                                if (newQuantity <= 0) {
                                    itemList.remove(i);
                                } else {
                                    ci.setQuantity(newQuantity);
                                }
                                break;
                            }
                        }
                    }

                    session.setAttribute("itemList", itemList);
                    response.sendRedirect("cart");
                    return;

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            List<CartItem> itemList = (List<CartItem>) session.getAttribute("itemList");
            boolean hasOutOfStock = false;
            String sort = request.getParameter("sort");
            sortItemList(itemList, sort);

            if (itemList != null) {
                for (CartItem ci : itemList) {
                    Product p = dao.getProductById(ci.getProduct().getId());
                    if (ci.getQuantity() > p.getStock()) {
                        hasOutOfStock = true;
                        break;
                    }
                }
            }

            if (hasOutOfStock) {
                request.setAttribute("stockError", "Some items in your cart are out of stock.");
            }
            request.setAttribute("newProductList", dao.getNewProduct(5));
            request.setAttribute("itemList", itemList);
            request.getRequestDispatcher("/view/cart.jsp").forward(request, response);
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

    private void sortItemList(List<CartItem> itemList, String sort) {
        if (sort == null || itemList == null) {
            return;
        }
        switch (sort) {
            case "idAsc":
                itemList.sort((a, b) -> Integer.compare(a.getId(), b.getId()));
                break;
            case "idDesc":
                itemList.sort((a, b) -> Integer.compare(b.getId(), a.getId())); // đảo ngược
                break; 
            case "nameAsc":
                itemList.sort((a, b) -> a.getProduct().getName().compareToIgnoreCase(b.getProduct().getName()));
                break;
            case "nameDesc":
                itemList.sort((a, b) -> b.getProduct().getName().compareToIgnoreCase(a.getProduct().getName()));
                break;
            case "priceAsc":
                itemList.sort((a, b) -> Double.compare(a.getProduct().getPrice(), b.getProduct().getPrice()));
                break;
            case "priceDesc":
                itemList.sort((a, b) -> Double.compare(b.getProduct().getPrice(), a.getProduct().getPrice()));
                break;
            case "shelfAsc":
                itemList.sort((a, b) -> Double.compare(a.getProduct().getShelfLifeHours(), b.getProduct().getShelfLifeHours()));
                break;
            case "shelfDesc":
                itemList.sort((a, b) -> Double.compare(b.getProduct().getShelfLifeHours(), a.getProduct().getShelfLifeHours()));
                break;
        }
    }
}
