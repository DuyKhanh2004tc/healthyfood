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
import model.Product;
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

        String sql = "SELECT DISTINCT c.id, c.name,c.image, c.description, c.created_at, c.nutritionist_id, c.type_id "
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
                    recipe.setImage(rs.getString("image"));
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
public void insertCookingRecipeProduct(int recipeId, List<Integer> productIds) {
    String sql = "INSERT INTO Cooking_Recipe_Product (cooking_recipe_id, product_id) VALUES (?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        for (int productId : productIds) {
            ps.setInt(1, recipeId);
            ps.setInt(2, productId);
            ps.addBatch();
        }
        ps.executeBatch();
    } catch (SQLException e) {
        status = "Error: " + e.getMessage();
    }
}

public void deleteCookingRecipeProduct(int recipeId) {
    String sql = "DELETE FROM Cooking_Recipe_Product WHERE cooking_recipe_id = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, recipeId);
        ps.executeUpdate();
    } catch (SQLException e) {
        status = "Error: " + e.getMessage();
    }
}
public void insertCookingRecipe(CookingRecipe recipe) {
    String sql = "INSERT INTO Cooking_Recipe (name, image, description, created_at, nutritionist_id, type_id) "
               + "VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
        ps.setString(1, recipe.getName());
        ps.setString(2, recipe.getImage());
        ps.setString(3, recipe.getDescription());
        ps.setTimestamp(4, recipe.getCreatedAt());
        ps.setInt(5, recipe.getNutritionist().getId());
        ps.setInt(6, recipe.getType().getId());

        ps.executeUpdate();

      
        try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                recipe.setId(generatedKeys.getInt(1));
            }
        }
    } catch (SQLException e) {
        status = "Error: " + e.getMessage();
    }
}
public void deleteCookingRecipe(int recipeId) {
    String sql = "DELETE FROM Cooking_Recipe WHERE id = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, recipeId);
        ps.executeUpdate();
    } catch (SQLException e) {
        status = "Error: " + e.getMessage();
    }
}
public CookingRecipe getRecipeById(int recipeId) {
    CookingRecipe recipe = null;
    String sql = "SELECT r.id, r.name, r.image, r.description, r.created_at, "
               + "r.nutritionist_id, t.id AS type_id, t.name AS type_name "
               + "FROM Cooking_Recipe r "
               + "JOIN Recipe_Type t ON r.type_id = t.id "
               + "WHERE r.id = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, recipeId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                recipe = new CookingRecipe();
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
            }
        }
    } catch (SQLException e) {
        status = "Error: " + e.getMessage();
    }
    return recipe;
}

public List<Product> getProductByRecipeId(int recipeId) {
    List<Product> products = new ArrayList<>();
    String sql = "SELECT p.id, p.name "
               + "FROM Product p "
               + "JOIN Cooking_Recipe_Product crp ON p.id = crp.product_id "
               + "WHERE crp.cooking_recipe_id = ?";

    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, recipeId);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                products.add(product);
            }
        }
    } catch (SQLException e) {
        status = "Error: " + e.getMessage();
    }

    return products;
}

public void updateCookingRecipe(CookingRecipe recipe) {
    String sql = "UPDATE Cooking_Recipe SET name = ?, image = ?, description = ?, "
               + "created_at = ?, nutritionist_id = ?, type_id = ? WHERE id = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, recipe.getName());
        ps.setString(2, recipe.getImage());
        ps.setString(3, recipe.getDescription());
        ps.setTimestamp(4, recipe.getCreatedAt());
        ps.setInt(5, recipe.getNutritionist().getId());
        ps.setInt(6, recipe.getType().getId());
        ps.setInt(7, recipe.getId());

        ps.executeUpdate();
    } catch (SQLException e) {
        status = "Error: " + e.getMessage();
    }
}

    public static void main(String[] args) {

    }
}
