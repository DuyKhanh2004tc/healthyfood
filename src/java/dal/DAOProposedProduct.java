/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import model.ProposedProduct;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.Category;
import model.User;

public class DAOProposedProduct {

    public static DAOProposedProduct INSTANCE = new DAOProposedProduct();
    private Connection con;
    private String status = "OK";

    public DAOProposedProduct() {
        con = new DBContext().connect;
    }

    public List<ProposedProduct> listProductByNutritionistId(int nutritionistId) {
        List<ProposedProduct> list = new ArrayList<>();
        String sql = "SELECT * FROM ProposedProduct WHERE nutritionist_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, nutritionistId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ProposedProduct p = new ProposedProduct();
                p.setId(rs.getInt("id"));

                User u = new User();
                u.setId(rs.getInt("nutritionist_id"));
                p.setNutritionist(u);
                p.setName(rs.getString("name"));
                p.setImage(rs.getString("image"));
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
               p.setCategory(c);
                p.setDescription(rs.getString("description"));
                p.setReason(rs.getString("reason"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setStatus(rs.getString("status"));
                p.setShelfLife(rs.getInt("shelf_life"));
                list.add(p);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            status = "Error at listProductByNutritionistId: " + e.getMessage();
        }
        return list;
    }

    public void deleteProposedProductById(int id) {
        String sql = "DELETE FROM ProposedProduct WHERE id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            status = "Error at deleteProposedProductById: " + e.getMessage();
        }
    }

    public void insertProposedProduct(ProposedProduct p) {
        String sql = "INSERT INTO ProposedProduct (nutritionist_id, image, name, category_id, description, reason,shelf_life, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?,?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, p.getNutritionist().getId());
            ps.setString(2, p.getImage());
            ps.setString(3, p.getName());
            ps.setInt(4, p.getCategory().getId());
            ps.setString(5, p.getDescription());
            ps.setString(6, p.getReason());
            ps.setInt(7, p.getShelfLife());
            ps.setTimestamp(8, p.getCreatedAt());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            status = "Error at insertProposedProduct: " + e.getMessage();
        }
    }

    public List<ProposedProduct> getAllProposedProduct() {
        List<ProposedProduct> list = new ArrayList<>();
        String sql = "SELECT p.*, u.name AS nutritionist_name FROM ProposedProduct p "
                + "JOIN Users u ON p.nutritionist_id = u.id";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                ProposedProduct p = new ProposedProduct();
                p.setId(rs.getInt("id"));

                User u = new User();
                u.setId(rs.getInt("nutritionist_id"));
                u.setName(rs.getString("nutritionist_name")); 
                p.setNutritionist(u);

                p.setName(rs.getString("name"));
                p.setImage(rs.getString("image"));
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
               p.setCategory(c);
                p.setDescription(rs.getString("description"));
                p.setReason(rs.getString("reason"));
                p.setShelfLife(rs.getInt("shelf_life"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setStatus(rs.getString("status"));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    } 
    
    public List<ProposedProduct> getAllProposedProductByCategory(String categoryId) {
        List<ProposedProduct> list = new ArrayList<>();
        String sql = "SELECT p.*, u.name AS nutritionist_name FROM ProposedProduct p "
                + "JOIN Users u ON p.nutritionist_id = u.id "
                + "WHERE category_id = ?";
        try (PreparedStatement st = con.prepareStatement(sql);) {
             st.setString(1, categoryId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ProposedProduct p = new ProposedProduct();
                p.setId(rs.getInt("id"));

                User u = new User();
                u.setId(rs.getInt("nutritionist_id"));
                u.setName(rs.getString("nutritionist_name")); 
                p.setNutritionist(u);

                p.setName(rs.getString("name"));
                p.setImage(rs.getString("image"));
                p.setCategoryName(rs.getString("category_name"));
                p.setDescription(rs.getString("description"));
                p.setReason(rs.getString("reason"));
                p.setShelfLife(rs.getInt("shelf_life"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setStatus(rs.getString("status"));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<ProposedProduct> getAllProposedProductOrderByDESC() {
        List<ProposedProduct> list = new ArrayList<>();
        String sql = "SELECT p.*, u.name AS nutritionist_name FROM ProposedProduct p "
                + "JOIN Users u ON p.nutritionist_id = u.id "
                + "ORDER BY created_at DESC";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                ProposedProduct p = new ProposedProduct();
                p.setId(rs.getInt("id"));

                User u = new User();
                u.setId(rs.getInt("nutritionist_id"));
                u.setName(rs.getString("nutritionist_name")); 
                p.setNutritionist(u);

                p.setName(rs.getString("name"));
                p.setImage(rs.getString("image"));
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
               p.setCategory(c);
                p.setDescription(rs.getString("description"));
                p.setReason(rs.getString("reason"));
                p.setShelfLife(rs.getInt("shelf_life"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setStatus(rs.getString("status"));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<ProposedProduct> getProposedProductsByCategoryDESC(int categoryId) {
        List<ProposedProduct> list = new ArrayList<>();
        String sql = "SELECT p.*, u.name AS nutritionist_name FROM ProposedProduct p "
                + "JOIN Users u ON p.nutritionist_id = u.id "
                + "WHERE category_id = ? "
                + "ORDER BY created_at DESC";
        try (PreparedStatement st = con.prepareStatement(sql);) {
            st.setInt(1, categoryId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                ProposedProduct p = new ProposedProduct();
                p.setId(rs.getInt("id"));

                User u = new User();
                u.setId(rs.getInt("nutritionist_id"));
                u.setName(rs.getString("nutritionist_name")); 
                p.setNutritionist(u);

                p.setName(rs.getString("name"));
                p.setImage(rs.getString("image"));
                p.setCategoryName(rs.getString("category_name"));
                p.setDescription(rs.getString("description"));
                p.setReason(rs.getString("reason"));
                p.setShelfLife(rs.getInt("shelf_life"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setStatus(rs.getString("status"));

                list.add(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void updateProposedProductStatusById(int proposedId, String status) {
        String sql = "UPDATE ProposedProduct SET status = ? WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, status);
            st.setInt(2, proposedId);
            st.executeUpdate();
        } catch (SQLException e) {
            status = "Error at updateProposedProductStatusById: " + e.getMessage();
        }
    }

    public void updateProposedProduct(ProposedProduct p) {
        String sql = "UPDATE ProposedProduct SET image = ?, name = ?, category_id = ?, description = ?, reason = ?, shelf_life = ?, created_at = ?, status = ? WHERE id = ? AND nutritionist_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getImage());
            ps.setString(2, p.getName());
            ps.setInt(3, p.getCategory().getId());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getReason());
            ps.setInt(6, p.getShelfLife());
            ps.setTimestamp(7, p.getCreatedAt());
            ps.setString(8, p.getStatus());
            ps.setInt(9, p.getId());
            ps.setInt(10, p.getNutritionist().getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            status = "Error at updateProposedProduct: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        List<ProposedProduct> pro = DAOProposedProduct.INSTANCE.getAllProposedProduct();
        System.out.println(pro);
    }
}
