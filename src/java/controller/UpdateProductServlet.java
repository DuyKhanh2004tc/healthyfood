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

@WebServlet(name = "UpdateProductServlet", urlPatterns = {"/updateProduct"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class UpdateProductServlet extends HttpServlet {
    private DAOSeller DAOSeller;
    private DAOCategory DAOCategory;
    private static final Logger LOGGER = Logger.getLogger(UpdateProductServlet.class.getName());
    private static final String UPLOAD_DIR = "images/uploads";
    private static final int PRODUCTS_PER_PAGE = 5;

    @Override
    public void init() throws ServletException {
        DAOSeller = DAOSeller.getInstance();
        DAOCategory = DAOCategory.INSTANCE;
        LOGGER.log(Level.INFO, "UpdateProductServlet initialized, DAO status: {0}", DAOSeller.getStatus());
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

        if ("requestUpdate".equals(service)) {
            try {
                int productId = Integer.parseInt(request.getParameter("productId"));
                Product product = DAOSeller.getProductById(productId);
                if (product != null) {
                    List<Category> categories = DAOCategory.getAllCategory();
                    request.setAttribute("categories", categories);
                    request.setAttribute("product", product);
                    request.getRequestDispatcher("view/UpdateProduct.jsp").forward(request, response);
                } else {
                    session.setAttribute("errorMessage", "Không tìm thấy sản phẩm.");
                    displayProductList(request, response, null, currentPage);
                }
            } catch (NumberFormatException e) {
                session.setAttribute("errorMessage", "ID sản phẩm không hợp lệ.");
                displayProductList(request, response, null, currentPage);
            }
        } else if ("requestDelete".equals(service)) {
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
        if (service == null || !"update".equals(service)) {
            LOGGER.log(Level.WARNING, "Service parameter is null or invalid. URL: {0}, Params: {1}",
                    new Object[]{request.getRequestURL(), request.getParameterMap()});
            session.setAttribute("errorMessage", "Yêu cầu không hợp lệ: Thiếu tham số service.");
            response.sendRedirect("updateProduct?service=list");
            return;
        }

        handleUpdateProduct(request, response);
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

    private void handleUpdateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stock");
        String shelfLifeStr = request.getParameter("shelfLifeHours");
        String categoryIdStr = request.getParameter("categoryId");
        boolean hasError = false;
        int productId = 0;

        try {
            productId = Integer.parseInt(request.getParameter("productId"));
            Product existingProduct = DAOSeller.getProductById(productId);
            if (existingProduct == null) {
                session.setAttribute("errorMessage", "Không tìm thấy sản phẩm.");
                response.sendRedirect("updateProduct?service=list");
                return;
            }

            if (name == null || name.trim().isEmpty()) {
                session.setAttribute("nameError", "Tên sản phẩm là bắt buộc.");
                hasError = true;
            } else if (name.length() > 255) {
                session.setAttribute("nameError", "Tên sản phẩm không được vượt quá 255 ký tự.");
                hasError = true;
            }

            if (description == null || description.trim().isEmpty()) {
                session.setAttribute("descriptionError", "Mô tả là bắt buộc.");
                hasError = true;
            }

            Double price = null;
            if (priceStr == null || priceStr.trim().isEmpty()) {
                session.setAttribute("priceError", "Giá là bắt buộc.");
                hasError = true;
            } else {
                try {
                    price = Double.parseDouble(priceStr);
                    if (price <= 0) {
                        session.setAttribute("priceError", "Giá phải lớn hơn 0.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("priceError", "Định dạng giá không hợp lệ.");
                    hasError = true;
                }
            }

            Integer stock = null;
            if (stockStr == null || stockStr.trim().isEmpty()) {
                session.setAttribute("stockError", "Số lượng tồn kho là bắt buộc.");
                hasError = true;
            } else {
                try {
                    stock = Integer.parseInt(stockStr);
                    if (stock < 0) {
                        session.setAttribute("stockError", "Số lượng tồn kho không được âm.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("stockError", "Định dạng số lượng tồn kho không hợp lệ.");
                    hasError = true;
                }
            }

            Double shelfLifeHours = null;
            if (shelfLifeStr == null || shelfLifeStr.trim().isEmpty()) {
                session.setAttribute("shelfLifeError", "Thời hạn sử dụng là bắt buộc.");
                hasError = true;
            } else {
                try {
                    shelfLifeHours = Double.parseDouble(shelfLifeStr);
                    if (shelfLifeHours < 0) {
                        session.setAttribute("shelfLifeError", "Thời hạn sử dụng không được âm.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("shelfLifeError", "Định dạng thời hạn sử dụng không hợp lệ.");
                    hasError = true;
                }
            }

            Integer categoryId = null;
            Category category = null;
            if (categoryIdStr == null || categoryIdStr.trim().isEmpty()) {
                session.setAttribute("categoryError", "Vui lòng chọn danh mục.");
                hasError = true;
            } else {
                try {
                    categoryId = Integer.parseInt(categoryIdStr);
                    category = DAOCategory.getCategoryById(categoryId);
                    if (category == null) {
                        session.setAttribute("categoryError", "Danh mục đã chọn không tồn tại.");
                        hasError = true;
                    }
                } catch (NumberFormatException e) {
                    session.setAttribute("categoryError", "Định dạng ID danh mục không hợp lệ.");
                    hasError = true;
                }
            }

            String imageUrl = existingProduct.getImgUrl();
            try {
                Part filePart = request.getPart("imageFile");
                if (filePart != null && filePart.getSize() > 0) {
                    if (filePart.getSize() > 1024 * 1024 * 5) {
                        session.setAttribute("imageError", "Kích thước tệp hình ảnh vượt quá giới hạn 5MB.");
                        hasError = true;
                    } else {
                        String contentType = filePart.getContentType();
                        if (!contentType.equals("image/jpeg") && !contentType.equals("image/png")) {
                            session.setAttribute("imageError", "Chỉ cho phép hình ảnh JPEG hoặc PNG.");
                            hasError = true;
                        } else {
                            String fileName = System.currentTimeMillis() + "_" + filePart.getSubmittedFileName();
                            String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;
                            File uploadDir = new File(uploadPath);
                            if (!uploadDir.exists() && !uploadDir.mkdirs()) {
                                session.setAttribute("imageError", "Không thể tạo thư mục tải lên.");
                                hasError = true;
                            } else if (!uploadDir.canWrite()) {
                                session.setAttribute("imageError", "Không có quyền ghi vào thư mục tải lên.");
                                hasError = true;
                            } else {
                                String filePath = uploadPath + File.separator + fileName;
                                filePart.write(filePath);
                                imageUrl = request.getContextPath() + "/" + UPLOAD_DIR + "/" + fileName;
                                File uploadedFile = new File(filePath);
                                if (!uploadedFile.exists()) {
                                    session.setAttribute("imageError", "Không thể lưu tệp hình ảnh lên máy chủ.");
                                    hasError = true;
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Lỗi I/O khi tải lên hình ảnh: {0}", e.getMessage());
                session.setAttribute("imageError", "Không thể tải lên hình ảnh: Lỗi I/O.");
                hasError = true;
            } catch (ServletException e) {
                LOGGER.log(Level.SEVERE, "Lỗi servlet khi tải lên hình ảnh: {0}", e.getMessage());
                session.setAttribute("imageError", "Không thể tải lên hình ảnh: Lỗi máy chủ.");
                hasError = true;
            }

            if (hasError) {
                session.setAttribute("errorMessage", "Vui lòng sửa các lỗi dưới đây.");
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
                product.setCategory(category);

                if (DAOSeller.updateProduct(product)) {
                    session.setAttribute("message", "Cập nhật sản phẩm thành công.");
                } else {
                    session.setAttribute("errorMessage", "Không thể cập nhật sản phẩm trong cơ sở dữ liệu.");
                }
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Cập nhật thất bại: {0}", e.getMessage());
                session.setAttribute("errorMessage", "Không thể cập nhật sản phẩm: " + e.getMessage());
            }
            response.sendRedirect("updateProduct?service=list");
        }
        catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Cập nhật thất bại: {0}", e.getMessage());
                session.setAttribute("errorMessage", "Không thể cập nhật sản phẩm: " + e.getMessage());
        }
    }
}