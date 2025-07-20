package controller;

import dal.DAOCategory;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Category;


public class ManagerCategoriesServlet extends HttpServlet {
    private DAOCategory categoryDAO;

    @Override
    public void init() {
        categoryDAO = DAOCategory.INSTANCE;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        List<Category> categories = categoryDAO.getAllCategory();
        System.out.println("Retrieved " + categories.size() + " categories at " + new java.util.Date());
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("view/ManagerCategories.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if ("add".equals(action)) {
            // add
            String name = request.getParameter("name");
            Category category = new Category();
            category.setName(name);
            categoryDAO.addCategory(category);
            System.out.println("Added category: " + name + " at " + new java.util.Date());
        } else if ("update".equals(action)) {
            // update
            int id = Integer.parseInt(request.getParameter("id"));
            String name = request.getParameter("name");
            Category category = new Category();
            category.setId(id);
            category.setName(name);
            categoryDAO.updateCategory(category);
            System.out.println("Updated category ID: " + id + " at " + new java.util.Date());
        } else if ("delete".equals(action)) {
            // delete
            int id = Integer.parseInt(request.getParameter("id"));
            categoryDAO.deleteCategory(id);
            System.out.println("Deleted category ID: " + id + " at " + new java.util.Date());
        }
        // reset
        response.sendRedirect("ManagerCategoriesServlet");
    }
}