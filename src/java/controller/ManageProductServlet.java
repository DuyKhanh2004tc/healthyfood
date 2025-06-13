package controller;

import model.Product;
import model.Category;
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
    private Object service;

    @Override
    public void init() throws ServletException {
        productDAO = DAOProduct.INSTANCE;
        LOGGER.log(Level.INFO, "ManageProductServlet initialized, DAO status: {0}", productDAO.getStatus());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String service = request.getParameter("service");
            if (!productDAO.getStatus().equals("OK")) {
                request.setAttribute("errorMessage", "Database connection failed: " + productDAO.getStatus());
                request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
                return;
            }

            if (service == null || service.equals("list")) {
                List<Product> products = productDAO.getAllProduct();
                LOGGER.log(Level.INFO, "Fetched {0} products for ProductManager.jsp", products != null ? products.size() : 0);
                if (products == null || products.isEmpty()) {
                    request.setAttribute("errorMessage", "No products available to display.");
                }
                request.setAttribute("allProducts", products);
                request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
            } else if (service.equals("searchByKeywords")) {
                String keywords = request.getParameter("keywords");
                if (keywords == null || keywords.trim().isEmpty()) {
                    request.setAttribute("errorMessage", "Please enter a search keyword.");
                    request.setAttribute("allProducts", productDAO.getAllProduct());
                    request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
                    return;
                }
                List<Product> products = productDAO.searchProductsByName(keywords.trim());
                LOGGER.log(Level.INFO, "Found {0} products for search query: {1}", new Object[]{products.size(), keywords});
                if (products.isEmpty()) {
                    request.setAttribute("errorMessage", "No products found matching: " + keywords);
                }
                request.setAttribute("allProducts", products);
                request.setAttribute("keywords", keywords);
                request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
            } else if (service.equals("requestInsert")) {
                List<Category> categories = productDAO.getAllCategories();
                request.setAttribute("categoryList", categories);
                request.getRequestDispatcher("/view/InsertProduct.jsp").forward(request, response);
            } else if (service.equals("requestUpdate")) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                Product product = productDAO.getProductById(productId);
                if (product == null) {
                    request.setAttribute("errorMessage", "Product not found for ID: " + productId);
                    request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
                } else {
                    request.setAttribute("product", product);
                    request.setAttribute("categoryList", productDAO.getAllCategories());
                    request.getRequestDispatcher("/view/UpdateProduct.jsp").forward(request, response);
                }
            } else if (service.equals("requestDelete")) {
                int productId = Integer.parseInt(request.getParameter("productId"));
                productDAO.deleteProductById(productId);
                LOGGER.log(Level.INFO, "Deleted product with ID: {0}", productId);
                response.sendRedirect("manageproduct?service=list");
            } else {
                request.setAttribute("errorMessage", "Invalid service request.");
                request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid product ID format: {0}", e.getMessage());
            request.setAttribute("errorMessage", "Invalid product ID.");
            request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing GET request: {0}", e.getMessage());
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String service = request.getParameter("service");
            if (service.equals("insert")) {
                Product product = new Product();
                product.setName(request.getParameter("name"));
                product.setDescription(request.getParameter("description"));
                product.setPrice(Double.parseDouble(request.getParameter("price")));
                product.setStock(Integer.parseInt(request.getParameter("stock")));
                product.setImgUrl(request.getParameter("imgUrl"));
                product.setShelfLifeHours(Double.parseDouble(request.getParameter("shelfLifeHours")));
                Category category = new Category();
                category.setId(Integer.parseInt(request.getParameter("categoryId")));
                product.setCategory(category);

                productDAO.insertProduct(product);
                LOGGER.log(Level.INFO, "Inserted new product: {0}", product.getName());
                response.sendRedirect("manageproduct?service=list");
            } else if (service.equals("update")) {
                Product product = new Product();
                product.setId(Integer.parseInt(request.getParameter("id")));
                product.setName(request.getParameter("name"));
                product.setDescription(request.getParameter("description"));
                product.setPrice(Double.parseDouble(request.getParameter("price")));
                product.setStock(Integer.parseInt(request.getParameter("stock")));
                product.setImgUrl(request.getParameter("imgUrl"));
                product.setShelfLifeHours(Double.parseDouble(request.getParameter("shelfLifeHours")));
                Category category = new Category();
                category.setId(Integer.parseInt(request.getParameter("categoryId")));
                product.setCategory(category);

                if (productDAO.updateProduct(product)) {
                    LOGGER.log(Level.INFO, "Updated product: {0} (ID: {1})", new Object[]{product.getName(), product.getId()});
                    response.sendRedirect("manageproduct?service=list");
                } else {
                    request.setAttribute("errorMessage", "Failed to update product. Please try again.");
                    request.setAttribute("product", product);
                    request.setAttribute("categoryList", productDAO.getAllCategories());
                    request.getRequestDispatcher("/view/UpdateProduct.jsp").forward(request, response);
                }
            } else {
                request.setAttribute("errorMessage", "Invalid service request.");
                request.getRequestDispatcher("/view/ProductManager.jsp").forward(request, response);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid input format: {0}", e.getMessage());
            request.setAttribute("errorMessage", "Invalid input data.");
            request.setAttribute("product", createProductFromRequest(request));
            request.setAttribute("categoryList", productDAO.getAllCategories());
            request.getRequestDispatcher(service.equals("insert") ? "/view/InsertProduct.jsp" : "/view/UpdateProduct.jsp").forward(request, response);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error processing POST request: {0}", e.getMessage());
            request.setAttribute("errorMessage", "An error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/error.jsp").forward(request, response);
        }
    }

    private Product createProductFromRequest(HttpServletRequest request) {
        Product product = new Product();
        try {
            if (request.getParameter("id") != null) {
                product.setId(Integer.parseInt(request.getParameter("id")));
            }
        } catch (NumberFormatException e) {
            // ID might be invalid, leave it unset
        }
        product.setName(request.getParameter("name"));
        product.setDescription(request.getParameter("description"));
        try {
            product.setPrice(Double.parseDouble(request.getParameter("price")));
        } catch (NumberFormatException e) {
            product.setPrice(0.0);
        }
        try {
            product.setStock(Integer.parseInt(request.getParameter("stock")));
        } catch (NumberFormatException e) {
            product.setStock(0);
        }
        product.setImgUrl(request.getParameter("imgUrl"));
        try {
            product.setShelfLifeHours(Double.parseDouble(request.getParameter("shelfLifeHours")));
        } catch (NumberFormatException e) {
            product.setShelfLifeHours(0.0);
        }
        try {
            Category category = new Category();
            category.setId(Integer.parseInt(request.getParameter("categoryId")));
            product.setCategory(category);
        } catch (NumberFormatException e) {
            product.setCategory(new Category());
        }
        return product;
    }
}