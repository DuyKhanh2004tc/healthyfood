package controller;

import dal.DAOSeller;
import dal.DAOCategory;
import dal.DAOProposedProduct;
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

@WebServlet(name = "InsertProductServlet", urlPatterns = {"/insertProduct"})
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 10)
public class InsertProductServlet extends HttpServlet {

    private DAOSeller DAOSeller;
    private DAOCategory DAOCategory;
    private static final Logger LOGGER = Logger.getLogger(InsertProductServlet.class.getName());
    private static final String UPLOAD_DIR = "images/uploads";

    @Override
    public void init() throws ServletException {
        DAOSeller = DAOSeller.getInstance();
        DAOCategory = DAOCategory.INSTANCE;
        LOGGER.log(Level.INFO, "InsertProductServlet initialized, DAO status: {0}", DAOSeller.getStatus());
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");

        if ("requestInsert".equals(service)) {
            List<Category> categories = DAOCategory.getAllCategory();
            if (categories == null || categories.isEmpty()) {
                session.setAttribute("errorMessage", "Không có danh mục nào. Vui lòng liên hệ quản trị viên.");
                response.sendRedirect("updateProduct?service=list");
                return;
            }
            String proposedId_raw = request.getParameter("proposedId");
            if (proposedId_raw != null && !proposedId_raw.isEmpty()) {
                try {
                    int proposedId = Integer.parseInt(proposedId_raw);
                    DAOProposedProduct daoPropose = new DAOProposedProduct();
                    model.ProposedProduct proposed = daoPropose.getProposedProductById(proposedId); // ⚠️ Bạn cần viết hàm này nếu chưa có

                    if (proposed != null) {

                        Product product = new Product();
                        product.setName(proposed.getName());
                        product.setImgUrl(proposed.getImage());
                        product.setDescription(proposed.getDescription());
                        
                        product.setShelfLifeHours(proposed.getShelfLife());
                        Category category = DAOCategory.getCategoryById(proposed.getCategory().getId());
                        product.setCategory(category);
                        request.setAttribute("product", product);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("view/SellerInsertProduct.jsp").forward(request, response);
        } else {
            session.setAttribute("errorMessage", "Yêu cầu không hợp lệ.");
            response.sendRedirect("updateProduct?service=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String service = request.getParameter("service");
        if (service == null || !"insert".equals(service)) {
            LOGGER.log(Level.WARNING, "Service parameter is null or invalid. URL: {0}, Params: {1}",
                    new Object[]{request.getRequestURL(), request.getParameterMap()});
            session.setAttribute("errorMessage", "Yêu cầu không hợp lệ: Thiếu tham số service.");
            response.sendRedirect("updateProduct?service=list");
            return;
        }

        handleInsertProduct(request, response);
    }

    private void handleInsertProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        String priceStr = request.getParameter("price");
        String stockStr = request.getParameter("stock");
        String shelfLifeStr = request.getParameter("shelfLifeHours");
        String categoryIdStr = request.getParameter("categoryId");
        Integer categoryId = null;
        Category category = null;
        boolean hasError = false;

        String validPattern = "^[a-zA-Z0-9\\s.,!?'\\-()/]*$";

        if (name == null || name.trim().isEmpty()) {
            session.setAttribute("nameError", "Tên sản phẩm là bắt buộc.");
            hasError = true;
        } else if (name.length() > 255) {
            session.setAttribute("nameError", "Tên sản phẩm không được vượt quá 255 ký tự.");
            hasError = true;
        } else if (!name.matches(validPattern)) {
            session.setAttribute("nameError", "Tên sản phẩm chứa ký tự đặc biệt không hợp lệ.");
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

        String imageUrl = null;
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
            } else {
                session.setAttribute("imageError", "Chưa tải lên tệp hình ảnh.");
                hasError = true;
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
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPrice(price != null ? price : 0.0);
            product.setStock(stock != null ? stock : 0);
            product.setShelfLifeHours(shelfLifeHours != null ? shelfLifeHours : 0.0);
            request.setAttribute("product", product);
            request.getRequestDispatcher("view/SellerInsertProduct.jsp").forward(request, response);
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
            product.setCategory(category);

            if (DAOSeller.insertProduct(product)) {
                session.setAttribute("message", "Thêm sản phẩm thành công.");
            } else {
                session.setAttribute("errorMessage", "Không thể thêm sản phẩm vào cơ sở dữ liệu.");
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Thêm sản phẩm thất bại: {0}", e.getMessage());
            session.setAttribute("errorMessage", "Không thể thêm sản phẩm: " + e.getMessage());
        }
        response.sendRedirect("updateProduct?service=list");
    }
}
