package controllerNutritionist;

import dal.DAORecipe;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
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
        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");

        HttpSession session = request.getSession();
        String action = request.getParameter("action");
        DAORecipe dao = new DAORecipe();
        User user = (User) session.getAttribute("user");

        if (user == null) {
            session.setAttribute("error", "Please log in to perform this action.");
            response.sendRedirect(request.getContextPath() + "/recipeType");
            return;
        }

        if ("add".equals(action)) {
            String name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                session.setAttribute("error", "Recipe type name is required.");
                response.sendRedirect(request.getContextPath() + "/recipeType");
                return;
            }
            try {
                dao.addRecipeType(name.trim());
                session.setAttribute("message", "Recipe type added successfully.");
            } catch (Exception e) {
                session.setAttribute("error", "Failed to add recipe type: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/recipeType");
        } else if ("edit".equals(action)) {
            String typeIdStr = request.getParameter("typeId");
            String name = request.getParameter("name");
            if (name == null || name.trim().isEmpty()) {
                session.setAttribute("error", "Recipe type name is required.");
                response.sendRedirect(request.getContextPath() + "/recipeType");
                return;
            }
            try {
                int id = Integer.parseInt(typeIdStr);
                dao.updateRecipeTypeById(id, name.trim());
                session.setAttribute("message", "Recipe type updated successfully.");
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid recipe type ID.");
            } catch (Exception e) {
                session.setAttribute("error", "Failed to update recipe type: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/recipeType");
        } else if ("delete".equals(action)) {
            String typeIdStr = request.getParameter("typeId");
            try {
                int id = Integer.parseInt(typeIdStr);
                dao.deleteRecipeTypeById(id);
                session.setAttribute("message", "Recipe type deleted successfully.");
            } catch (NumberFormatException e) {
                session.setAttribute("error", "Invalid recipe type ID.");
            } catch (Exception e) {
                session.setAttribute("error", "Failed to delete recipe type: " + e.getMessage());
            }
            response.sendRedirect(request.getContextPath() + "/recipeType");
        } else {
            session.setAttribute("error", "Invalid action.");
            response.sendRedirect(request.getContextPath() + "/recipeType");
        }
    }

    @Override
    public String getServletInfo() {
        return "Handles recipe type management";
    }
}
