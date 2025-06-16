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
        try {
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while(rs.next()){
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                categoryList.add(c);
            }
        } catch (SQLException e) {
        }
        return categoryList;
    }

    public Category getOrCreateCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            LOGGER.log(Level.WARNING, "Invalid category name provided: {0}", name);
            return null;
        }
        name = name.trim();
        Category category = null;
        String selectSql = "SELECT id, name FROM Category WHERE name = ?";
        try (PreparedStatement st = con.prepareStatement(selectSql)) {
            st.setString(1, name);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    category = new Category();
                    category.setId(rs.getInt("id"));
                    category.setName(rs.getString("name"));
                    LOGGER.log(Level.INFO, "Found category: {0}", name);
                    return category;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getOrCreateCategory (select): {0}", e.getMessage());
        }
        String insertSql = "INSERT INTO Category (name) VALUES (?)";
        try (PreparedStatement st = con.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, name);
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    category = new Category();
                    category.setId(rs.getInt(1));
                    category.setName(name);
                    LOGGER.log(Level.INFO, "Created new category: {0}", name);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getOrCreateCategory (insert): {0}", e.getMessage());
        }
        return category;
    }
}