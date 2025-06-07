/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Product;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Category;

/**
 *
 * @author ASUS
 */
public class DAOProduct {

    public static DAOProduct INSTANCE = new DAOProduct();
    private static Connection con;
    private String status = "OK";

    public DAOProduct() {
        if (INSTANCE == null) {
            con = new DBContext().connect;
        } else {
            INSTANCE = this;
        }
    }

    public List<Product> getAllProduct() {
    List<Product> productList = new ArrayList<>();
    String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, " +
                 "AVG(COALESCE(f.rate, p.rate)) AS average_rate, c.id AS category_id, c.name AS category_name " +
                 "FROM Product p " +
                 "INNER JOIN Category c ON p.category_id = c.id " +
                 "LEFT JOIN Feedback f ON p.id = f.product_id " +
                 "GROUP BY p.id, p.name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, c.id, c.name";
    try {
        PreparedStatement st = con.prepareStatement(sql);
        ResultSet rs = st.executeQuery();
        while (rs.next()) {
            Product p = new Product();
            p.setId(rs.getInt("product_id"));
            p.setName(rs.getString("product_name"));
            p.setDescription(rs.getString("description"));
            p.setPrice(rs.getDouble("price"));
            p.setStock(rs.getInt("stock"));
            p.setImgUrl(rs.getString("image_url"));
            p.setShelfLifeHours(rs.getDouble("shelf_life_hours"));
            p.setRate(rs.getDouble("average_rate"));

            Category c = new Category();
            c.setId(rs.getInt("category_id"));
            c.setName(rs.getString("category_name"));

            p.setCategory(c);
            productList.add(p);
        }
    } catch (SQLException e) {
        e.printStackTrace(); 
    }
    return productList;
}

    public List<Product> searchProduct(String namesearch) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id as product_id, p.name as product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours,p.rate, "
                + "c.id as category_id, c.name as category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.name LIKE ?";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setString(1, "%" + namesearch + "%");
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setImgUrl(rs.getString("image_url"));
                p.setShelfLifeHours(rs.getDouble("shelf_life_hours"));
                p.setRate(rs.getDouble("rate"));
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
                c.setName(rs.getString("category_name"));

                p.setCategory(c);
                productList.add(p);
            }

        } catch (SQLException e) {
        }
        return productList;
    }

    public List<Product> getPriceSorted(String orderBy) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id as product_id, p.name as product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours,p.rate, "
                + "c.id as category_id, c.name as category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.price " + orderBy;
        try {
            PreparedStatement st = con.prepareStatement(sql);

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setImgUrl(rs.getString("image_url"));
                p.setShelfLifeHours(rs.getDouble("shelf_life_hours"));
                p.setRate(rs.getDouble("rate"));
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
                c.setName(rs.getString("category_name"));

                p.setCategory(c);
                productList.add(p);
            }

        } catch (SQLException e) {
        }
        return productList;
    }

    public List<Product> getPriceInRange(double min, double max) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id as product_id, p.name as product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours,p.rate, "
                + "c.id as category_id, c.name as category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.price >= ? and p.price <= ? ORDER BY p.price ";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            st.setDouble(1, min);
            st.setDouble(2, max);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("product_name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setImgUrl(rs.getString("image_url"));
                p.setShelfLifeHours(rs.getDouble("shelf_life_hours"));
                p.setRate(rs.getDouble("rate"));
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
                c.setName(rs.getString("category_name"));

                p.setCategory(c);
                productList.add(p);
            }

        } catch (SQLException e) {
        }
        return productList;
    }

    public Product getProductById(int productId) {
        Product product = null;
        try {
            String sql = "SELECT p.id, p.name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours,p.rate, c.id AS category_id, c.name AS category_name "
                    + "FROM Product p JOIN Category c ON p.category_id = c.id WHERE p.id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                product = new Product();
                product.setId(rs.getInt("id"));
                product.setName(rs.getString("name"));
                product.setDescription(rs.getString("description"));
                product.setPrice(rs.getDouble("price"));
                product.setStock(rs.getInt("stock"));
                product.setImgUrl(rs.getString("image_url"));
                product.setShelfLifeHours(0);
                product.setRate(rs.getDouble("rate"));
                Category c = new Category();
                c.setId(rs.getInt("category_id"));
                c.setName(rs.getString("category_name"));

                product.setCategory(c);

            }

            rs.close();
            statement.close();
        } catch (SQLException ex) {

        }
        return product;
    }
}
