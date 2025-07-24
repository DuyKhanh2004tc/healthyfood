/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.Product;
import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Cart;
import model.CartItem;
import model.User;

/**
 *
 * @author ASUS
 */
public class DAOProduct {

    public static DAOProduct INSTANCE = new DAOProduct();
    private static Connection con;
    private String status = "OK";
    private static final Logger LOGGER = Logger.getLogger(DAOProduct.class.getName());

    public DAOProduct() {
        if (INSTANCE == null) {
            con = new DBContext().connect;
        } else {
            INSTANCE = this;
        }
    }

    // Placeholder for connection retrieval
    public Connection getConnection() throws SQLException {
        // Implement connection logic, e.g., using DataSource or DriverManager
        return con; // Replace with actual connection logic
    }

    public List<Product> getAllProduct() {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id";

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
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

    public int getTotalProduct() {
        String sql = " SELECT COUNT(*) FROM PRODUCT ";
        try (PreparedStatement st = con.prepareStatement(sql)) {

            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int n = rs.getInt(1);
                return n;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public int getTotalProductByCid(int categoryId) {
        String sql = " SELECT COUNT(*) FROM PRODUCT WHERE category_id = ? ";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int n = rs.getInt(1);
                return n;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return 0;
    }

    public Product getProductById(int productId) {
        Product product = null;
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p JOIN Category c ON p.category_id = c.id WHERE p.id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setImgUrl(rs.getString("image_url"));
                    product.setShelfLifeHours(rs.getDouble("shelf_life_hours"));
                    product.setRate(rs.getDouble("rate"));

                    Category c = new Category();
                    c.setId(rs.getInt("category_id"));
                    c.setName(rs.getString("category_name"));
                    product.setCategory(c);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getProductById: {0}", e.getMessage());
        }
        return product;
    }

    public List<Product> getProductPagination(int index, int row) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, (index - 1) * row);
            st.setInt(2, row);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Product> getProductPaginationByCid(int categoryId, int index, int row) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE c.id = ? "
                + "ORDER BY p.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            st.setInt(2, (index - 1) * row);
            st.setInt(3, row);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public Product getNewestProduct() {
        String sql = "SELECT TOP 1 p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.id DESC";

        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
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
                return p;

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void addToCart(int userId, int productId, int quantity) {
        try {
            String checkCartExistSQL = "SELECT id FROM Cart WHERE user_id = ? ";
            int cartId = -1;
            try (PreparedStatement st = con.prepareStatement(checkCartExistSQL)) {
                st.setInt(1, userId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    cartId = rs.getInt("id");
                } else {
                    String createCartSQL = "INSERT INTO Cart (user_id) VALUES (?) ";
                    try (PreparedStatement st2 = con.prepareStatement(createCartSQL, Statement.RETURN_GENERATED_KEYS)) {
                        st2.setInt(1, userId);
                        st2.executeUpdate();
                        ResultSet rs2 = st2.getGeneratedKeys();
                        if (rs2.next()) {
                            cartId = rs.getInt(1);
                        }
                    }
                }
            }

            String checkItemExistSQL = "SELECT id, quantity FROM CartItem WHERE cart_id =? AND product_id = ?";
            try (PreparedStatement st = con.prepareStatement(checkItemExistSQL)) {
                st.setInt(1, cartId);
                st.setInt(2, productId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    int existedQuantity = rs.getInt("quantity");
                    int newQuantity = existedQuantity + quantity;
                    int itemId = rs.getInt("id");
                    String updateSQL = "UPDATE CartItem SET quantity = ? WHERE id = ? ";
                    try (PreparedStatement st3 = con.prepareStatement(updateSQL)) {
                        st3.setInt(1, newQuantity);
                        st3.setInt(2, itemId);
                        st3.executeUpdate();
                    }
                } else {
                    String insertItemSQL = "INSERT INTO CartItem (cart_id, product_id,quantity) VALUES (?,?,?) ";
                    try (PreparedStatement st4 = con.prepareStatement(insertItemSQL)) {
                        st4.setInt(1, cartId);
                        st4.setInt(2, productId);
                        st4.setInt(3, quantity);
                        st4.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void reduceStock(int productId, int quantity) {
        String sql = "UPDATE Product SET stock = stock - ? WHERE id = ? ";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, quantity);
            st.setInt(2, productId);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getProductStock(int productId) {
        int stock = 0;
        String sql = "SELECT stock FROM Product WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                stock = rs.getInt("stock");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }

    public List<Product> getProductByCategory(int categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, "
                + "AVG(COALESCE(f.rate, p.rate)) AS average_rate, c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "LEFT JOIN Feedback f ON p.id = f.product_id "
                + "WHERE p.category_id = ? "
                + "GROUP BY p.id, p.name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, c.id, c.name";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getProductByCategory: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> searchProduct(String namesearch) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.name LIKE ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, "%" + namesearch + "%");
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in searchProduct: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> sortSearchedProduct(String namesearch, String orderBy) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.name LIKE ? "
                + "ORDER BY p.price " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, "%" + namesearch + "%");
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in searchProduct: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getPriceSortedByCategoryId(String orderBy, int categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.category_id = ? "
                + "ORDER BY p.price " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getPriceSorted(String orderBy) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.price " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getNameSorted(String orderBy) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.name " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getNameSortedByCategoryId(String orderBy, int categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.category_id = ? "
                + "ORDER BY p.name " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getRatingSorted(String orderBy) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.rate " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getRatingSortedByCategoryId(String orderBy, int categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.category_id = ? "
                + "ORDER BY p.rate " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getTimeSorted(String orderBy) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.id " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getTimeSortedByCategoryId(String orderBy, int categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.category_id = ? "
                + "ORDER BY p.id " + (orderBy.equalsIgnoreCase("DESC") ? "DESC" : "ASC");
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceSorted: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getPriceInRange(double min, double max) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.price >= ? AND p.price <= ? ORDER BY p.price";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setDouble(1, min);
            st.setDouble(2, max);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceInRange: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Product> getPriceInRangeByCategoryId(double min, double max, int categoryId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.price >= ? AND p.price <= ? AND p.category_id = ? ORDER BY p.price";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setDouble(1, min);
            st.setDouble(2, max);
            st.setInt(3, categoryId);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getPriceInRange: {0}", e.getMessage());
        }
        return productList;
    }

    public List<Category> getAllCategories() {
        List<Category> categoryList = new ArrayList<>();
        String sql = "SELECT id, name FROM Category";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            while (rs.next()) {
                Category c = new Category();
                c.setId(rs.getInt("id"));
                c.setName(rs.getString("name"));
                categoryList.add(c);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in getAllCategories: {0}", e.getMessage());
        }
        return categoryList;
    }

    public List<Product> getProductSortedPaginationByCid(int categoryId, int index, int row, String orderBy) {
        List<Product> productList = new ArrayList<>();

        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE c.id = ? "
                + "ORDER BY p.price " + orderBy + " "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, categoryId);
            st.setInt(2, (index - 1) * row);
            st.setInt(3, row);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Product> getProductSortedPagination(int index, int row, String orderBy) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.price " + orderBy + " "
                + "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, (index - 1) * row);
            st.setInt(2, row);
            try (ResultSet rs = st.executeQuery()) {
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
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productList;
    }

    public List<Product> getNewProduct(int number) {
        List<Product> list = new ArrayList<>();
        String sql = "SELECT TOP (?) p.id AS product_id, p.name AS product_name, p.description, p.price, "
                + "p.stock, p.image_url, p.shelf_life_hours, p.rate, c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.id DESC";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, number);
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

                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
