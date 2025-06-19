package controller;

import dal.DAOProduct;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.util.List;
import model.Product;
import model.Category;

@WebServlet(name = "ProductManagementServlet", urlPatterns = {"/productmanagement"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class ProductManagementServlet extends HttpServlet {

    private DAOProduct productDAO;
    private static final String UPLOAD_DIR = "images/uploads";

    @Override
    public void init() throws ServletException {
        productDAO = new DAOProduct();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if ("requestInsert".equals(service)) {
            // Forward to insert form
            request.getRequestDispatcher("view/insertProduct.jsp").forward(request, response);
        } else if ("requestUpdate".equals(service)) {
            // Fetch product by ID for update
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                Product product = productDAO.getProductById(productId);
                if (product != null) {
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("view/UpdateProduct.jsp").forward(request, response);
                } else {
                    request.setAttribute("errorMessage", "Product not found.");
                    displayProductList(request, response);
                }
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid product ID.");
                displayProductList(request, response);
            }
        } else if ("requestDelete".equals(service)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                productDAO.deleteProductById(productId);
                request.setAttribute("message", "Product deleted successfully!");
            } catch (NumberFormatException e) {
                request.setAttribute("errorMessage", "Invalid product ID.");
            }
            displayProductList(request, response);
        } else if ("searchByKeywords".equals(service)) {
            // Handle search
            String keywords = request.getParameter("keywords");
            List<Product> productList = productDAO.searchProducts(keywords != null ? keywords.trim() : "");
            request.setAttribute("allProducts", productList);
            request.setAttribute("keywords", keywords); // Retain search input
            request.getRequestDispatcher("view/ProductManagement.jsp").forward(request, response);
        } else {
            // Default: list all products
            displayProductList(request, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String service = request.getParameter("service");

        if ("insert".equals(service)) {
            handleInsertProduct(request, response);
        } else if ("update".equals(service)) {
            handleUpdateProduct(request, response);
        } else {
            displayProductList(request, response);
        }
    }

    private void displayProductList(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<Product> productList = productDAO.getAllProduct();
        request.setAttribute("allProducts", productList);
        request.getRequestDispatcher("view/ProductManagement.jsp").forward(request, response);
    }

    private void handleInsertProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = null;
        String description = null;
        Double price = null;
        Integer stock = null;
        Double shelfLifeHours = null;
        String categoryName = null;
        String imageUrl = null;
        boolean hasError = false;

        try {
            name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("nameError", "Product name is required.");
                hasError = true;
            } else if (name.length() > 255) {
                request.setAttribute("nameError", "Product name cannot exceed 255 characters.");
                hasError = true;
            }

            description = request.getParameter("description");

            String priceStr = request.getParameter("price");
            if (priceStr == null || priceStr.trim().isEmpty()) {
                request.setAttribute("priceError", "Price is required.");
                hasError = true;
            } else {
                try {
                    price = Double.parseDouble(priceStr);
                    if (price <= 0) {
                        request.setAttribute("priceError", "Price must be positive.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("priceError", "Invalid price format.");
                    hasError = true;
                }
            }

            String stockStr = request.getParameter("stock");
            if (stockStr == null || stockStr.trim().isEmpty()) {
                request.setAttribute("stockError", "Stock is required.");
                hasError = true;
            } else {
                try {
                    stock = Integer.parseInt(stockStr);
                    if (stock < 0) {
                        request.setAttribute("stockError", "Stock cannot be negative.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("stockError", "Invalid stock format.");
                    hasError = true;
                }
            }

            String shelfLifeStr = request.getParameter("shelfLifeHours");
            if (shelfLifeStr == null || shelfLifeStr.trim().isEmpty()) {
                request.setAttribute("shelfLifeError", "Shelf life is required.");
                hasError = true;
            } else {
                try {
                    shelfLifeHours = Double.parseDouble(shelfLifeStr);
                    if (shelfLifeHours < 0) {
                        request.setAttribute("shelfLifeError", "Shelf life cannot be negative.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("shelfLifeError", "Invalid shelf life format.");
                    hasError = true;
                }
            }

            categoryName = request.getParameter("categoryName");
            if (categoryName == null || categoryName.trim().isEmpty()) {
                request.setAttribute("categoryError", "Category name is required.");
                hasError = true;
            } else if (categoryName.length() > 100) {
                request.setAttribute("categoryError", "Category name cannot exceed 100 characters.");
                hasError = true;
            }

            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String contentType = filePart.getContentType();
                if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                    request.setAttribute("imageError", "Only JPEG or PNG images are allowed.");
                    hasError = true;
                } else {
                    String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    String filePath = uploadPath + File.separator + fileName;
                    filePart.write(filePath);
                    imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
                }
            }

            if (hasError) {
                request.setAttribute("errorMessage", "Please correct the errors below.");
                request.getRequestDispatcher("view/insertProduct.jsp").forward(request, response);
                return;
            }

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

            productDAO.insertProduct(product);

            response.sendRedirect("productmanagement?service=list&message=Product inserted successfully");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Failed to insert product: " + e.getMessage());
            request.getRequestDispatcher("view/insertProduct.jsp").forward(request, response);
        }
    }

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = null;
        String description = null;
        Double price = null;
        Integer stock = null;
        Double shelfLifeHours = null;
        String categoryName = null;
        String imageUrl = null;
        boolean hasError = false;
        int productId = 0;

        try {
            productId = Integer.parseInt(request.getParameter("productId"));
            Product existingProduct = productDAO.getProductById(productId);
            if (existingProduct == null) {
                request.setAttribute("errorMessage", "Product not found.");
                displayProductList(request, response);
                return;
            }

            name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                request.setAttribute("nameError", "Product name is required.");
                hasError = true;
            } else if (name.length() > 255) {
                request.setAttribute("nameError", "Product name cannot exceed 255 characters.");
                hasError = true;
            }

            description = request.getParameter("description");

            String priceStr = request.getParameter("price");
            if (priceStr == null || priceStr.trim().isEmpty()) {
                request.setAttribute("priceError", "Price is required.");
                hasError = true;
            } else {
                try {
                    price = Double.parseDouble(priceStr);
                    if (price <= 0) {
                        request.setAttribute("priceError", "Price must be positive.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("priceError", "Invalid price format.");
                    hasError = true;
                }
            }

            String stockStr = request.getParameter("stock");
            if (stockStr == null || stockStr.trim().isEmpty()) {
                request.setAttribute("stockError", "Stock is required.");
                hasError = true;
            } else {
                try {
                    stock = Integer.parseInt(stockStr);
                    if (stock < 0) {
                        request.setAttribute("stockError", "Stock cannot be negative.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("stockError", "Invalid stock format.");
                    hasError = true;
                }
            }

            String shelfLifeStr = request.getParameter("shelfLifeHours");
            if (shelfLifeStr == null || shelfLifeStr.trim().isEmpty()) {
                request.setAttribute("shelfLifeError", "Shelf life is required.");
                hasError = true;
            } else {
                try {
                    shelfLifeHours = Double.parseDouble(shelfLifeStr);
                    if (shelfLifeHours < 0) {
                        request.setAttribute("shelfLifeError", "Shelf life cannot be negative.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    request.setAttribute("shelfLifeError", "Invalid shelf life format.");
                    hasError = true;
                }
            }

            categoryName = request.getParameter("categoryName");
            if (categoryName == null || categoryName.trim().isEmpty()) {
                request.setAttribute("categoryError", "Category name is required.");
                hasError = true;
            } else if (categoryName.length() > 100) {
                request.setAttribute("categoryError", "Category name cannot exceed 100 characters.");
                hasError = true;
            }

            Part filePart = request.getPart("imageFile");
            if (filePart != null && filePart.getSize() > 0) {
                String contentType = filePart.getContentType();
                if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                    request.setAttribute("imageError", "Only JPEG or PNG images are allowed.");
                    hasError = true;
                } else {
                    String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                    String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                    File uploadDir = new File(uploadPath);
                    if (!uploadDir.exists()) {
                        uploadDir.mkdirs();
                    }
                    String filePath = uploadPath + File.separator + fileName;
                    filePart.write(filePath);
                    imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
                }
            } else {
                imageUrl = existingProduct.getImgUrl();
            }

            if (hasError) {
                request.setAttribute("errorMessage", "Please correct the errors below.");
                request.setAttribute("product", existingProduct);
                request.getRequestDispatcher("view/UpdateProduct.jsp").forward(request, response);
                return;
            }

            Product product = new Product();
            product.setId(productId);
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price);
            product.setStock(stock);
            product.setImgUrl(imageUrl);
            product.setShelfLifeHours(shelfLifeHours);
            product.setRate(existingProduct.getRate());

            Category category = new Category();
            category.setName(categoryName);
            product.setCategory(category);

            productDAO.updateProduct(product);

            response.sendRedirect("productmanagement?service=list&message=Product updated successfully");
        } catch (Exception e) {
            request.setAttribute("errorMessage", "Failed to update product: " + e.getMessage());
            request.setAttribute("product", productDAO.getProductById(productId));
            request.getRequestDispatcher("view/UpdateProduct.jsp").forward(request, response);
        }
    }
}