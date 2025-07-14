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
import model.User;

public class DAOProposedProduct {

    public static DAOTag INSTANCE = new DAOTag();
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
                p.setCategoryName(rs.getString("category_name"));
                p.setDescription(rs.getString("description"));
                p.setReason(rs.getString("reason"));
                p.setCreatedAt(rs.getTimestamp("created_at"));
                p.setStatus(rs.getString("status"));
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
        String sql = "INSERT INTO ProposedProduct (nutritionist_id, image, name, category_name, description, reason, created_at) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, p.getNutritionist().getId());
            ps.setString(2, p.getImage());
            ps.setString(3, p.getName());
            ps.setString(4, p.getCategoryName());
            ps.setString(5, p.getDescription());
            ps.setString(6, p.getReason());
            ps.setTimestamp(7, p.getCreatedAt());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            status = "Error at insertProposedProduct: " + e.getMessage();
        }
    }

    public void updateProposedProduct(ProposedProduct p) {
        String sql = "UPDATE ProposedProduct SET image = ?, name = ?, category_name = ?, description = ?, reason = ?, created_at = ?, status = ? WHERE id = ? AND nutritionist_id = ?";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, p.getImage());
            ps.setString(2, p.getName());
            ps.setString(3, p.getCategoryName());
            ps.setString(4, p.getDescription());
            ps.setString(5, p.getReason());
            ps.setTimestamp(6, p.getCreatedAt());
            ps.setString(7, p.getStatus());
            ps.setInt(8, p.getId());
            ps.setInt(9, p.getNutritionist().getId());

            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            status = "Error at updateProposedProduct: " + e.getMessage();
        }
    }
}
