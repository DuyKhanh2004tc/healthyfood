/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.CookingRecipe;
import model.RecipeType;
import model.User;

public class DAORecipe {

    public static DAORecipe INSTANCE = new DAORecipe();
    private Connection con;
    private String status = "OK";

    public DAORecipe() {
        con = new DBContext().connect;
    }

    public List<CookingRecipe> listAllCookingRecipe() {
        List<CookingRecipe> list = new ArrayList<>();
        String sql = "SELECT r.id, r.name, r.image, r.description, r.created_at, r.nutritionist_id, t.id AS type_id, t.name AS type_name "
                + "FROM Cooking_Recipe r "
                + "JOIN Recipe_Type t ON r.type_id = t.id";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CookingRecipe recipe = new CookingRecipe();
                recipe.setId(rs.getInt("id"));
                recipe.setName(rs.getString("name"));
                recipe.setImage(rs.getString("image"));
                recipe.setDescription(rs.getString("description"));
                recipe.setCreatedAt(rs.getTimestamp("created_at"));
                User nutritionist = new User();
                nutritionist.setId(rs.getInt("nutritionist_id"));
                recipe.setNutritionist(nutritionist);
                RecipeType type = new RecipeType();
                type.setId(rs.getInt("type_id"));
                type.setName(rs.getString("type_name"));
                recipe.setType(type);
                list.add(recipe);
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
        }
        return list;
    }

    public List<RecipeType> listAllRecipeType() {
        List<RecipeType> list = new ArrayList<>();
        String sql = "SELECT id, name FROM Recipe_Type";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                RecipeType type = new RecipeType();
                type.setId(rs.getInt("id"));
                type.setName(rs.getString("name"));
                list.add(type);
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
        }
        return list;
    }

   public List<CookingRecipe> listAllCookingRecipeByProductIds(List<Integer> productIds) {
        List<CookingRecipe> recipes = new ArrayList<>();
        if (productIds == null || productIds.isEmpty()) {
            return recipes;
        }

        String sql = "SELECT DISTINCT c.id, c.name, c.description, c.created_at, c.nutritionist_id, c.type_id "
                + "FROM Cooking_Recipe c "
                + "INNER JOIN Cooking_Recipe_Product r ON r.cooking_recipe_id = c.id "
                + "INNER JOIN Product p ON p.id = r.product_id "
                + "WHERE p.id IN (?" + ",?".repeat(productIds.size() - 1) + ")";

        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            
            for (int i = 0; i < productIds.size(); i++) {
                stmt.setInt(i + 1, productIds.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    CookingRecipe recipe = new CookingRecipe();
                    recipe.setId(rs.getInt("id"));
                    recipe.setName(rs.getString("name"));
                    recipe.setDescription(rs.getString("description"));
                    recipe.setCreatedAt(rs.getTimestamp("created_at"));
                    RecipeType t = new RecipeType();
                    t.setId(rs.getInt("type_id"));
                    recipe.setType(t);

                    User nutritionist = new User();
                    nutritionist.setId(rs.getInt("nutritionist_id"));
                    recipe.setNutritionist(nutritionist);

                    recipes.add(recipe);
                }
            }
        } catch (SQLException e) {
          
        }

        return recipes;
    }

    public List<CookingRecipe> listAllCookingRecipeByTypeId(int typeId) {
        List<CookingRecipe> list = new ArrayList<>();
        String sql = "SELECT r.id, r.name, r.image, r.description, r.created_at, r.nutritionist_id, t.id AS type_id, t.name AS type_name "
                + "FROM Cooking_Recipe r "
                + "JOIN Recipe_Type t ON r.type_id = t.id "
                + "WHERE r.type_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, typeId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CookingRecipe recipe = new CookingRecipe();
                recipe.setId(rs.getInt("id"));
                recipe.setName(rs.getString("name"));
                recipe.setImage(rs.getString("image"));
                recipe.setDescription(rs.getString("description"));
                recipe.setCreatedAt(rs.getTimestamp("created_at"));
                User nutritionist = new User();
                nutritionist.setId(rs.getInt("nutritionist_id"));
                recipe.setNutritionist(nutritionist);
                RecipeType type = new RecipeType();
                type.setId(rs.getInt("type_id"));
                type.setName(rs.getString("type_name"));
                recipe.setType(type);
                list.add(recipe);
            }
        } catch (SQLException e) {
            status = "Error: " + e.getMessage();
        }
        return list;
    }

    public static void main(String[] args) {

    }
}
