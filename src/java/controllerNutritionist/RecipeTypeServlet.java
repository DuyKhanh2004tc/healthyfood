package controllerNutritionist;

import dal.DAORecipe;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.RecipeType;
import model.User;

public class RecipeTypeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        DAORecipe dao = new DAORecipe();
        List<RecipeType> typeList = dao.listAllRecipeType();
        request.setAttribute("typeList", typeList);
        request.getRequestDispatcher("/view/recipeType.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        DAORecipe dao = new DAORecipe();
        String errorMessage = null;
        User user = (User) request.getSession().getAttribute("user");

            if ("add".equals(action)) {
                String name = request.getParameter("name");
               
                    dao.addRecipeType(name.trim());
                 response.sendRedirect(request.getContextPath() + "/recipeType");
            } else if ("edit".equals(action)) {
                String typeIdStr = request.getParameter("typeId");
                String name = request.getParameter("name");              
                    try {
                        int id = Integer.parseInt(typeIdStr);
                        dao.updateRecipeTypeById(id, name.trim());
                         response.sendRedirect(request.getContextPath() + "/recipeType");
                    } catch (NumberFormatException e) {
                        errorMessage = "Invalid type ID.";
                    
                }
            } else if ("delete".equals(action)) {
                String typeIdStr = request.getParameter("typeId");
             
                    try {
                        int id = Integer.parseInt(typeIdStr);
                        dao.deleteRecipeTypeById(id);
                         response.sendRedirect(request.getContextPath() + "/recipeType");
                    } catch (NumberFormatException e) {
                        errorMessage = "Invalid type ID.";
                    
                }      
            }
    }

    @Override
    public String getServletInfo() {
        return "Handles recipe type management";
    }
}
