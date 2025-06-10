package controller;

import model.Product;
import dal.DAOProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ManageProductServlet extends HttpServlet {
    private DAOProduct productDAO;
    private static final Logger LOGGER = Logger.getLogger(ManageProductServlet.class.getName());

    @Override
    public void init() throws ServletException {
        productDAO = DAOProduct.INSTANCE;
        LOGGER.log(Level.INFO, "ManageProductServlet initialized, DAO status: {0}", productDAO.getStatus());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Product> products = productDAO.getAllProduct();
            LOGGER.log(Level.INFO, "Fetched {0} products for ProductManager.jsp", products != null ? products.size() : 0);
            if (products == null || products.isEmpty()) {
                LOGGER.log(Level.WARNING, "No products returned from getAllProduct()");
                request.setAttribute("errorMessage", "No products available to display.");
            } else {
                for (Product p : products) {
                    LOGGER.log(Level.INFO, "Product: ID={0}, Name={1}, Category={2}",
                        new Object[]{p.getId(), p.getName(), p.getCategory() != null ? p.getCategory().getName() : "null"});
                }
            }
            request.setAttribute("allProducts", products);
            request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing GET request: {0}", e.getMessage());
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
        }
    }
}