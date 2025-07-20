/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Category;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ASUS
 */
public class DAOCategory {
    private static final Logger LOGGER = Logger.getLogger(DAOCategory.class.getName());
    public static DAOCategory INSTANCE = new DAOCategory();
    private Connection con;
    private String status = "OK";

    public DAOCategory() {
        con = new DBContext().connect;
    }

    public List<Category> getAllCategory() {
        List<Category> categoryList = new ArrayList<>();
        String sql = "SELECT * FROM Category ";
        try (PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();){
            while(rs.next()){
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                categoryList.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categoryList;
    }
    
    public Category getCategoryById(int id) {
        String sql = "SELECT * FROM Category WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Category c = new Category();
                    c.setId(rs.getInt("id"));
                    c.setName(rs.getString("name"));
                    return c;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error fetching category by id {0}: {1}", new Object[]{id, e.getMessage()});
            e.printStackTrace();
        }
        return null;
    }

    // Add a new category
    public void addCategory(Category category) {
        String sql = "INSERT INTO [dbo].[Category] (name) VALUES (?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, category.getName());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Update a category
    public void updateCategory(Category category) {
        String sql = "UPDATE [dbo].[Category] SET name = ? WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, category.getName());
            st.setInt(2, category.getId());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Delete a category
    public void deleteCategory(int id) {
        String sql = "DELETE FROM [dbo].[Category] WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    

