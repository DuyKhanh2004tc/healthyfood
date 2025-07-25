/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import dal.DAOCategory;
import dal.DAOProposedProduct;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.Category;
import model.ProposedProduct;
import model.User;
import utils.Pagination;

/**
 *
 * @author HP
 */
@MultipartConfig
public class ProposeProductServlet extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    private final String SAVE_DIR = "images";

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet ProposeProductServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet ProposeProductServlet at " + request.getContextPath() + "</h1>");
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
    User user = (User) session.getAttribute("user");
    DAOProposedProduct dao = new DAOProposedProduct();
    DAOCategory dao2 = new DAOCategory();
    List<Category> categoryList = dao2.getAllCategory();
    request.setAttribute("categoryList", categoryList);

    String categoryId_raw = request.getParameter("categoryId");
    String status = request.getParameter("status");
    String productName = request.getParameter("productName");
    int page = 1;
    int pageSize = 5;

    if (request.getParameter("page") != null) {
        try {
            page = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException e) {
            page = 1;
        }
    }

    List<ProposedProduct> proposedProductList = dao.listProductByNutritionistId(user.getId());
    if (categoryId_raw != null && !categoryId_raw.equals("0")) {
        int categoryId = Integer.parseInt(categoryId_raw);
        session.setAttribute("categoryId", categoryId);
        proposedProductList = dao.getProposedProductByNutritionistIdAndCategoryId(user.getId(), categoryId);
    }
    if (status != null && !status.equals("all")) {
        session.setAttribute("status", status);
        proposedProductList = dao.getProposedProductByNutritionistIdAndStatus(user.getId(), status);
    }
    if (productName != null && !productName.isEmpty()) {
        List<ProposedProduct> filteredList = new ArrayList<>();
        for (ProposedProduct p : proposedProductList) {
            if (p.getName().toLowerCase().contains(productName.toLowerCase())) {
                filteredList.add(p);
            }
        }
        proposedProductList = filteredList;
    }

   
    List<ProposedProduct> pagedList = Pagination.paginate(proposedProductList, page, pageSize);
    int totalPages = (int) Math.ceil((double) proposedProductList.size() / pageSize);

    // Gán dữ liệu vào request
    request.setAttribute("proposedList", pagedList);
    request.setAttribute("currentPage", page);
    request.setAttribute("totalPages", totalPages);
    
    request.getRequestDispatcher("view/proposedProduct.jsp").forward(request, response);
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
    request.setCharacterEncoding("UTF-8");
    response.setContentType("text/html;charset=UTF-8");

    HttpSession session = request.getSession();
    User user = (User) session.getAttribute("user");
    String action = request.getParameter("action");
    String proposeIdStr = request.getParameter("proposedId");
    String name = request.getParameter("name");
    String categoryStr = request.getParameter("category");
    String description = request.getParameter("description");
    String reason = request.getParameter("reason");
    String shelfLifeStr = request.getParameter("shelfLife");
    String image = request.getParameter("image");
    DAOProposedProduct dao = new DAOProposedProduct();

    if ("edit".equals(action) || "add".equals(action)) {
        // Validate required fields
        if (name == null || name.trim().isEmpty()) {
            request.setAttribute("error", "Name is required.");
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
            return;
        }
        if (categoryStr == null || categoryStr.trim().isEmpty()) {
            request.setAttribute("error", "Category is required.");
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
            return;
        }
        if (description == null || description.trim().isEmpty()) {
            request.setAttribute("error", "Description is required.");
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
            return;
        }
        if (reason == null || reason.trim().isEmpty()) {
            request.setAttribute("error", "Reason is required.");
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
            return;
        }
        if (shelfLifeStr == null || shelfLifeStr.trim().isEmpty()) {
            request.setAttribute("error", "Shelf life is required.");
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
            return;
        }

        int shelfLife = 0;
        try {
            shelfLife = Integer.parseInt(shelfLifeStr.trim());
            if (shelfLife <= 0) {
                request.setAttribute("error", "Shelf life must be a positive number.");
                response.sendRedirect(request.getContextPath() + "/proposeProduct");
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Shelf life must be a valid number.");
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
            return;
        }

        Part filePart = request.getPart("file");
        String fileName = getFileName(filePart);
        if (filePart != null && filePart.getSize() > 0 && (fileName == null || fileName.isEmpty())) {
            request.setAttribute("error", "Invalid file name.");
            response.sendRedirect(request.getContextPath() + "/proposeProduct");
            return;
        }

        String appPath = request.getServletContext().getRealPath("");
        File projectRoot = new File(appPath).getParentFile().getParentFile();
        String savePath = projectRoot.getAbsolutePath() + File.separator + "build" + File.separator + "web" + File.separator + SAVE_DIR;

        if (filePart != null && filePart.getSize() > 0 && fileName != null && !fileName.isEmpty()) {
            File fileSaveDir = new File(savePath);
            if (!fileSaveDir.exists()) {
                fileSaveDir.mkdirs();
            }

            File saveFile = new File(savePath, fileName);
            File parentDir = saveFile.getParentFile();
            if (!parentDir.exists()) {
                parentDir.mkdirs();
            }

            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, saveFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                image = fileName;
            } catch (IOException e) {
                request.setAttribute("error", "Failed to upload image.");
                response.sendRedirect(request.getContextPath() + "/proposeProduct");
                return;
            }
        }

        ProposedProduct p = new ProposedProduct();
        if ("edit".equals(action)) {
            try {
                p.setId(Integer.parseInt(proposeIdStr));
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid product ID.");
                response.sendRedirect(request.getContextPath() + "/proposeProduct");
                return;
            }
            dao.updateProposedProduct(p);
            request.setAttribute("message", "Product updated successfully.");
        } else {
            p.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            p.setStatus("pending");
            dao.insertProposedProduct(p);
            request.setAttribute("message", "Product added successfully.");
        }

        p.setName(name);
        Category c = new Category();
        c.setId(Integer.parseInt(categoryStr.trim()));
        p.setCategory(c);
        p.setDescription(description);
        p.setImage(image);
        p.setNutritionist(user);
        p.setReason(reason);
        p.setShelfLife(shelfLife);

        response.sendRedirect(request.getContextPath() + "/proposeProduct");
        return;
    }

    if ("delete".equals(action)) {
        try {
            dao.deleteProposedProductById(Integer.parseInt(proposeIdStr));
            request.setAttribute("message", "Product deleted successfully.");
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid product ID.");
        } catch (Exception e) {
            request.setAttribute("error", "Failed to delete product.");
        }
        response.sendRedirect(request.getContextPath() + "/proposeProduct");
        return;
    }
}
    private String getFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        for (String token : contentDisp.split(";")) {
            if (token.trim().startsWith("filename")) {
                return token.substring(token.indexOf("=") + 2, token.length() - 1);
            }
        }
        return "";
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
