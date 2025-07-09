/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import dal.DAOSeller;
import dal.DAOCategory;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Product;
import model.Category;
import model.Order;

@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class SellerServlet extends HttpServlet {
    private DAOSeller DAOSeller;
    private DAOCategory DAOCategory;
    private static final Logger LOGGER = Logger.getLogger(SellerServlet.class.getName());
    private static final String UPLOAD_DIR = "images/uploads";
    private static final int PRODUCTS_PER_PAGE = 5;

    @Override
    public void init() throws ServletException {
        DAOSeller = DAOSeller.getInstance();
        DAOCategory = DAOCategory.INSTANCE;
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
            session.setAttribute("errorMessage", "Invalid page number.");
        }

        if ("requestInsert".equals(service)) {
            List<Category> categories = DAOCategory.getAllCategory();
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("view/InsertProduct.jsp").forward(request, response);
        } else if ("requestUpdate".equals(service)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                Product product = DAOSeller.getProductById(productId);
                if (product != null) {
                    List<Category> categories = DAOCategory.getAllCategory();
                    request.setAttribute("categories", categories);
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("view/UpdateProduct.jsp").forward(request, response);
                } else {
                    session.setAttribute("errorMessage", "Product not found.");
                    displayProductList(request, response, null, currentPage);
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid product ID.");
                displayProductList(request, response, null, currentPage);
            }
        } else if ("requestDelete".equals(service)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                DAOSeller.deleteProductById(productId);
                session.setAttribute("message", "Product deleted successfully!");
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "Invalid product ID format.");
                LOGGER.log(Level.WARNING, "Invalid product ID format: {0}", e.getMessage());
            } catch (RuntimeException e) {
                session.setAttribute("errorMessage", "Deletion failed: " + e.getMessage());
                LOGGER.log(Level.SEVERE, "Deletion failed: {0}", e.getMessage());
            }
            displayProductList(request, response, null, currentPage);
        } else if ("searchByKeywords".equals(service)) {
            String keywords = request.getParameter("keywords");
            displayProductList(request, response, keywords, currentPage);
        } 
        
        
        
        
//        else if ("orderHistory".equals(service)) {
//    int ordersPerPage = 5; // Consistent with PRODUCTS_PER_PAGE
//    List<Order> orders = DAOSeller.getOrderHistoryPaginated(currentPage, ordersPerPage);
//    int totalOrders = DAOSeller.getTotalOrderCount();
//    int totalPages = (int) Math.ceil((double) totalOrders / ordersPerPage);
//    request.setAttribute("orders", orders);
//    request.setAttribute("currentPage", currentPage);
//    request.setAttribute("totalPages", totalPages);
//    request.setAttribute("service", "orderHistory");
//    request.getRequestDispatcher("view/OrderHistory.jsp").forward(request, response);
//}
        
        
        
        else {
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
            session.setAttribute("errorMessage", "Invalid request: service parameter is missing.");
            response.sendRedirect("seller?service=list");
            return;
        }

        if ("insert".equals(service)) {
            handleInsertProduct(request, response);
        } else if ("update".equals(service)) {
            handleUpdateProduct(request, response);
        } else {
            session.setAttribute("errorMessage", "Invalid service: " + service);
            response.sendRedirect("seller?service=list");
        }
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

    private void handleInsertProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stock");
        String shelfLifeStr = request.getParameter("shelfLifeHours");
        String categoryName = request.getParameter("categoryName");
        boolean hasError = false;

        // Validate input fields
        if (name == null || name.trim().isEmpty()) {
            session.setAttribute("nameError", "Product name is required.");
            hasError = true;
        } else if (name.length() > 255) {
            session.setAttribute("nameError", "Product name cannot exceed 255 characters.");
            hasError = true;
        }

        if (description == null || description.trim().isEmpty()) {
            session.setAttribute("descriptionError", "Description is required.");
            hasError = true;
        }

        Double price = null;
        if (priceStr == null || priceStr.trim().isEmpty()) {
            session.setAttribute("priceError", "Price is required.");
            hasError = true;
        } else {
            try {
                price = Double.parseDouble(priceStr);
                if (price <= 0) {
                    session.setAttribute("priceError", "Price must be positive.");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("priceError", "Invalid price format.");
                hasError = true;
            }
        }

        Integer stock = null;
        if (stockStr == null || stockStr.trim().isEmpty()) {
            session.setAttribute("stockError", "Stock is required.");
            hasError = true;
        } else {
            try {
                stock = Integer.parseInt(stockStr);
                if (stock < 0) {
                    session.setAttribute("stockError", "Stock cannot be negative.");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("stockError", "Invalid stock format.");
                hasError = true;
            }
        }

        Double shelfLifeHours = null;
        if (shelfLifeStr == null || shelfLifeStr.trim().isEmpty()) {
            session.setAttribute("shelfLifeError", "Shelf life is required.");
            hasError = true;
        } else {
            try {
                shelfLifeHours = Double.parseDouble(shelfLifeStr);
                if (shelfLifeHours < 0) {
                    session.setAttribute("shelfLifeError", "Shelf life cannot be negativetell negative.");
                    hasError = true;
                }
            } catch (NumberFormatException e) {
                session.setAttribute("shelfLifeError", "Invalid shelf life format.");
                hasError = true;
            }
        }

        if (categoryName == null || categoryName.trim().isEmpty()) {
            session.setAttribute("categoryError", "Category name is required.");
            hasError = true;
        } else if (categoryName.length() > 100) {
            session.setAttribute("categoryError", "Category name cannot exceed 100 characters.");
            hasError = true;
        }

        String imageUrl = null;
        try {
            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                if (filePart.getSize() > 1024 * 1024 * 5) {
                    session.setAttribute("imageError", "Image file size exceeds 5MB limit.");
                    hasError = true;
                } else {
                    String contentType = filePart.getContentType();
                    if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                        session.setAttribute("imageError", "Only JPEG or PNG images are allowed.");
                        hasError = true;
                    } else {
                        String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                        File uploadDir = new File(uploadPath);
                        if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                            session.setAttribute("imageError", "Failed to create upload directory.");
                            hasError = true;
                        } else if (!uploadDir.canWrite()) {
                            session.setAttribute("imageError", "No write permission for upload directory.");
                            hasError = true;
                        } else {
                            String filePath = uploadPath + File.separator + fileName;
                            filePart.write(filePath);
                            imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
                            // Verify file was written
                            File uploadedFile = new File(filePath);
                            if (!uploadedFile.exists()) {
                                session.setAttribute("imageError", "Failed to save image file to server.");
                                hasError = true;
                            }
                        }
                    }
                }
            } else {
                session.setAttribute("imageError", "No image file uploaded.");
                hasError = true;
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "I/O error during image upload: {0}", e.getMessage());
            session.setAttribute("imageError", "Failed to upload image: I/O error.");
            hasError = true;
        } catch (ServletException e) {
            LOGGER.log(Level.SEVERE, "Servlet error during image upload: {0}", e.getMessage());
            session.setAttribute("imageError", "Failed to upload image: Server error.");
            hasError = true;
        }

        if (hasError) {
            session.setAttribute("errorMessage", "Please correct the errors below.");
            response.sendRedirect("seller?service=requestInsert");
            return;
        }

        try {
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setImgUrl(imageUrl);
            product.setShelfLifeHours(shelfLifeHours);
            product.setRate(0.0);

            Category category = new Category();
            category.setName(categoryName);
            product.setCategory(category);

            if (DAOSeller.insertProduct(product)) {
                session.setAttribute("message", "Product inserted successfully.");
            } else {
                session.setAttribute("errorMessage", "Failed to insert product into database.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Insert failed: {0}", e.getMessage());
            session.setAttribute("errorMessage", "Failed to insert product: " + e.getMessage());
        }
        response.sendRedirect("seller?service=list");
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stock");
        String shelfLifeStr = request.getParameter("shelfLifeHours");
        String categoryName = request.getParameter("categoryName");
        boolean hasError = false;
        int productId = 0;

        try {
            productId = Integer.parseInt(request.getParameter("productId"));
            Product existingProduct = DAOSeller.getProductById(productId);
            if (existingProduct == null) {
                session.setAttribute("errorMessage", "Product not found.");
                response.sendRedirect("seller?service=list");
                return;
            }

            // Validate input fields
            if (name == null || name.trim().isEmpty()) {
                session.setAttribute("nameError", "Product name is required.");
                hasError = true;
            } else if (name.length() > 255) {
                session.setAttribute("nameError", "Product name cannot exceed 255 characters.");
                hasError = true;
            }

            if (description == null || description.trim().isEmpty()) {
                session.setAttribute("descriptionError", "Description is required.");
                hasError = true;
            }

            Double price = null;
            if (priceStr == null || priceStr.trim().isEmpty()) {
                session.setAttribute("priceError", "Price is required.");
                hasError = true;
            } else {
                try {
                    price = Double.parseDouble(priceStr);
                    if (price <= 0) {
                        session.setAttribute("priceError", "Price must be positive.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("priceError", "Invalid price format.");
                    hasError = true;
                }
            }

            Integer stock = null;
            if (stockStr == null || stockStr.trim().isEmpty()) {
                session.setAttribute("stockError", "Stock is required.");
                hasError = true;
            } else {
                try {
                    stock = Integer.parseInt(stockStr);
                    if (stock < 0) {
                        session.setAttribute("stockError", "Stock cannot be negative.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("stockError", "Invalid stock format.");
                    hasError = true;
                }
            }

            Double shelfLifeHours = null;
            if (shelfLifeStr == null || shelfLifeStr.trim().isEmpty()) {
                session.setAttribute("shelfLifeError", "Shelf life is required.");
                hasError = true;
            } else {
                try {
                    shelfLifeHours = Double.parseDouble(shelfLifeStr);
                    if (shelfLifeHours < 0) {
                        session.setAttribute("shelfLifeError", "Shelf life cannot be negative.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("shelfLifeError", "Invalid shelf life format.");
                    hasError = true;
                }
            }

            if (categoryName == null || categoryName.trim().isEmpty()) {
                session.setAttribute("categoryError", "Category name is required.");
                hasError = true;
            } else if (categoryName.length() > 100) {
                session.setAttribute("categoryError", "Category name cannot exceed 100 characters.");
                hasError = true;
            }

            String imageUrl = existingProduct.getImgUrl();
            try {
                Part filePart = request.getPart("imageFile");
                if (filePart != null && filePart.getSize() > 0) {
                    if (filePart.getSize() > 1024 * 1024 * 5) {
                        session.setAttribute("imageError", "Image file size exceeds 5MB limit.");
                        hasError = true;
                    } else {
                        String contentType = filePart.getContentType();
                        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                            session.setAttribute("imageError", "Only JPEG or PNG images are allowed.");
                            hasError = true;
                        } else {
                            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                            File uploadDir = new File(uploadPath);
                            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                                session.setAttribute("imageError", "Failed to create upload directory.");
                                hasError = true;
                            } else if (!uploadDir.canWrite()) {
                                session.setAttribute("imageError", "No write permission for upload directory.");
                                hasError = true;
                            } else {
                                String filePath = uploadPath + File.separator + fileName;
                                filePart.write(filePath);
                                imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
                                // Verify file was written
                                File uploadedFile = new File(filePath);
                                if (!uploadedFile.exists()) {
                                    session.setAttribute("imageError", "Failed to save image file to server.");
                                    hasError = true;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "I/O error during image upload: {0}", e.getMessage());
                session.setAttribute("imageError", "Failed to upload image: I/O error.");
                hasError = true;
            } catch (ServletException e) {
                LOGGER.log(Level.SEVERE, "Servlet error during image upload: {0}", e.getMessage());
                session.setAttribute("imageError", "Failed to upload image: Server error.");
                hasError = true;
            }

            if (hasError) {
                session.setAttribute("errorMessage", "Please correct the errors below.");
                List<Category> categories = DAOCategory.getAllCategory();
                request.setAttribute("categories", categories);
                request.setAttribute("product", existingProduct);
                request.getRequestDispatcher("view/UpdateProduct.jsp").forward(request, response);
                return;
            }

            try {
                Product product = new Product();
                product.setId(productId);
                product.setName(name);
                product.setDescription(description);
                product.setPrice(price);
                product.setStock(stock);
                product.setImgUrl(imageUrl != null ? imageUrl : "/images/default.jpg");
                product.setShelfLifeHours(shelfLifeHours);
                product.setRate(existingProduct.getRate());

                Category category = new Category();
                category.setName(categoryName);
                product.setCategory(category);

                if (DAOSeller.updateProduct(product)) {
                    session.setAttribute("message", "Product updated successfully.");
                } else {
                    session.setAttribute("errorMessage", "Failed to update product in database.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Update failed: {0}", e.getMessage());
                session.setAttribute("errorMessage", "Failed to update product: " + e.getMessage());
            }
            response.sendRedirect("seller?service=list");
        } catch (NumberFormatException e) {
            session.setAttribute("errorMessage", "Invalid product ID.");
            response.sendRedirect("seller?service=list");
        }
    }
}