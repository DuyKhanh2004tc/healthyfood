/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

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
import model.CookingRecipe;
import model.Product;
import model.RecipeType;
import model.User;

/**
 *
 * @author HP
 */
@MultipartConfig
public class RecipeDetailServlet extends HttpServlet {
   private final String SAVE_DIR = "images";
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
            out.println("<title>Servlet RecipeDetailServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet RecipeDetailServlet at " + request.getContextPath() + "</h1>");
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
        String recipeIdstr = request.getParameter("recipeId");
        int recipeId = Integer.parseInt(recipeIdstr.trim());
        DAORecipe dao = new DAORecipe();
        DAOProduct dao2 =new DAOProduct();
        CookingRecipe cook = dao.getRecipeById(recipeId);
        List<CookingRecipe> c = dao.listAllCookingRecipe();
        int prevId = 0;
        int nextId = 0;
        for (int i = 0; i < c.size(); i++) {
            if (c.get(i).getId() == recipeId) {
                if (i > 0) {
                    prevId = c.get(i - 1).getId();
                }
                if (i < c.size() - 1) {
                    nextId = c.get(i + 1).getId();
                }
                break;
            }
        }
        List<RecipeType> typeList = dao.listAllRecipeType();
        List<Product> productByRecipeId = dao.getProductByRecipeId(recipeId);
         List<Product> productList = dao2.getAllProduct();
        request.setAttribute("recipeId", cook.getId());
        request.setAttribute("name", cook.getName());
        request.setAttribute("image", cook.getImage());
        request.setAttribute("createBy", cook.getNutritionist().getName());
        request.setAttribute("createdAt", cook.getCreatedAt());
        request.setAttribute("type", cook.getType().getName());
        request.setAttribute("typeList", typeList);
        request.setAttribute("recipe", cook);
          request.setAttribute("description", cook.getDescription());
        request.setAttribute("productByRecipeId", productByRecipeId);
         request.setAttribute("productList", productList);
        request.setAttribute("prevId", prevId != 0 ? prevId : c.get(c.size() - 1).getId());
        request.setAttribute("nextId", nextId != 0 ? nextId : c.get(0).getId());
        request.getRequestDispatcher("view/recipeDetail.jsp").forward(request, response);
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
       User user = (User) request.getSession().getAttribute("user");
       String recipeIdstr = request.getParameter("recipeId");
       int recipeId = Integer.parseInt(recipeIdstr.trim());
        String image = request.getParameter("image");
        String name = request.getParameter("name");
        String[] productIdstr = request.getParameterValues("chooseProduct");
        String typeIdStr = request.getParameter("typeId");
        String description = request.getParameter("description");
        Part filePart = request.getPart("file");
        DAORecipe dao = new DAORecipe();
       
        String action = request.getParameter("action");
        String fileName = getFileName(filePart);
        
        if(action.equals("editRecipe")){
        String appPath = request.getServletContext().getRealPath("");
        File projectRoot = new File(appPath).getParentFile().getParentFile();
        String savePath = projectRoot.getAbsolutePath() + File.separator + "build" + File.separator + "web" + File.separator + SAVE_DIR;

        if (fileName != null && !fileName.isEmpty()) {
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
            }

            image = fileName;
        }
       CookingRecipe cook = new CookingRecipe();
             cook.setId(recipeId);
            cook.setName(name);
            cook.setDescription(description);
            cook.setImage(image);
            cook. setNutritionist(user);
            cook.setCreatedAt(new java.sql.Timestamp(System.currentTimeMillis()));
            RecipeType type = new RecipeType();
            type.setId(Integer.parseInt(typeIdStr.trim()));
            cook.setType(type);
            dao.updateCookingRecipe(cook);
            dao.deleteCookingRecipeProduct(recipeId);
            List<Integer> productId = new ArrayList<Integer>();
            for(String p:productIdstr){
                productId.add(Integer.parseInt(p.trim()));
            }
            dao.insertCookingRecipeProduct(recipeId, productId);
        response.sendRedirect(request.getContextPath() + "/recipeDetail?recipeId="+recipeId);
        }else if(action.equals("deleteRecipe")){
            dao.deleteCookingRecipeProduct(recipeId);
            dao.deleteCookingRecipe(recipeId);
              response.sendRedirect(request.getContextPath() + "/allRecipe");
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
