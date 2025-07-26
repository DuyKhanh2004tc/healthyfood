/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controllerNutritionist;

import dal.DAOProduct;
import dal.DAORecipe;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.*;
import utils.Pagination;

/**
 *
 * @author HP
 */
@MultipartConfig
public class AllRecipeServlet extends HttpServlet {

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
            out.println("<title>Servlet AllRecipeServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet AllRecipeServlet at " + request.getContextPath() + "</h1>");
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
        String productName = request.getParameter("productName");
        String typeIdstr = request.getParameter("typeId");

        DAORecipe dao = new DAORecipe();
        DAOProduct daoP = new DAOProduct();
        List<Product> productList = daoP.getAllProduct();
        List<Integer> productIdList = new ArrayList<>();
        for (Product p : productList) {
            if (productName == null || p.getName().toLowerCase().contains(productName.toLowerCase())) {
                productIdList.add(p.getId());
            }
        }
        List<CookingRecipe> recipeByProductId = dao.listAllCookingRecipeByProductIds(productIdList);
        if (typeIdstr != null && !typeIdstr.trim().isEmpty()) {
            List<CookingRecipe> filteredRecipe = new ArrayList<>();
            int typeId = Integer.parseInt(typeIdstr);
            for (CookingRecipe c : recipeByProductId) {
                if (c.getType().getId() == typeId) {
                    filteredRecipe.add(c);
                }
            }
            recipeByProductId = filteredRecipe;
        }

        List<RecipeType> typeList = dao.listAllRecipeType();
         int page = 1;
        int pageSize = 6;
        if (request.getParameter("page") != null) {
            try {
                page = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException e) {
                page = 1;
            }
        }
        List<CookingRecipe> pagedList = Pagination.paginate(recipeByProductId, page, pageSize);
        int totalPages = (int) Math.ceil((double) recipeByProductId.size() / pageSize);

        request.setAttribute("recipeList", pagedList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", totalPages);
        request.setAttribute("productList", productList);
        request.setAttribute("typeList", typeList);
        request.setAttribute("cookingRecipeList", recipeByProductId);
        request.getRequestDispatcher("/view/allRecipe.jsp").forward(request, response);
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

    User user = (User) request.getSession().getAttribute("user");
    String image = request.getParameter("image");
    String name = request.getParameter("name");
    String[] productIdstr = request.getParameterValues("chooseProduct");
    String typeIdStr = request.getParameter("typeId");
    String description = request.getParameter("description");
    Part filePart = request.getPart("file");
    DAORecipe dao = new DAORecipe();

    if (name == null || name.trim().isEmpty()) {
        request.getSession().setAttribute("error", "Name is required.");
        response.sendRedirect(request.getContextPath() + "/allRecipe");
        return;
    }
    if (typeIdStr == null || typeIdStr.trim().isEmpty()) {
        request.getSession().setAttribute("error", "Type is required.");
        response.sendRedirect(request.getContextPath() + "/allRecipe");
        return;
    }
    if (description == null || description.trim().isEmpty()) {
        request.getSession().setAttribute("error", "Description is required.");
        response.sendRedirect(request.getContextPath() + "/allRecipe");
        return;
    }
    if (productIdstr == null || productIdstr.length == 0) {
        request.getSession().setAttribute("error", "At least one product must be selected.");
        response.sendRedirect(request.getContextPath() + "/allRecipe");
        return;
    }

    int typeId = 0;
    try {
        typeId = Integer.parseInt(typeIdStr.trim());
        if (typeId <= 0) {
            request.getSession().setAttribute("error", "Invalid type ID.");
            response.sendRedirect(request.getContextPath() + "/allRecipe");
            return;
        }
    } catch (NumberFormatException e) {
        request.getSession().setAttribute("error", "Type ID must be a valid number.");
        response.sendRedirect(request.getContextPath() + "/allRecipe");
        return;
    }

    List<Integer> productId = new ArrayList<>();
    for (String id : productIdstr) {
        try {
            int pid = Integer.parseInt(id.trim());
            if (pid <= 0) {
                request.getSession().setAttribute("error", "Invalid product ID.");
                response.sendRedirect(request.getContextPath() + "/allRecipe");
                return;
            }
            productId.add(pid);
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "Invalid product ID format.");
            response.sendRedirect(request.getContextPath() + "/allRecipe");
            return;
        }
    }

    String fileName = getFileName(filePart);
    if (filePart != null && filePart.getSize() > 0 && (fileName == null || fileName.isEmpty())) {
        request.getSession().setAttribute("error", "Invalid file name.");
        response.sendRedirect(request.getContextPath() + "/allRecipe");
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
            request.getSession().setAttribute("error", "Failed to upload image.");
            response.sendRedirect(request.getContextPath() + "/allRecipe");
            return;
        }
    }

   
    CookingRecipe cook = new CookingRecipe();
    cook.setImage(image);
    cook.setName(name);
    cook.setDescription(description);
    cook.setNutritionist(user);
    cook.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
    RecipeType type = new RecipeType();
    type.setId(typeId);
    cook.setType(type);

    try {
        dao.insertCookingRecipe(cook);
        dao.insertCookingRecipeProduct(cook.getId(), productId);
        request.getSession().setAttribute("message", "Recipe added successfully.");
    } catch (Exception e) {
        request.getSession().setAttribute("error", "Failed to add recipe.");
    }

    response.sendRedirect(request.getContextPath() + "/allRecipe");
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
