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
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import model.*;

/**
 *
 * @author HP
 */
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
        if (typeIdstr != null&& !typeIdstr.trim().isEmpty()) {
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
        processRequest(request, response);
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
