package dal;

import model.Product;
import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DAOSeller {
    private static final Logger LOGGER = Logger.getLogger(DAOSeller.class.getName());
    private final Connection con;
    public static volatile DAOSeller INSTANCE;

    private DAOSeller() {
        try {
            this.con = new DBContext().connect;
            if (this.con == null) {
                throw new SQLException("Failed to initialize database connection");
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize DAOSeller: {0}", e.getMessage());
            throw new RuntimeException("Database connection initialization failed", e);
        }
    }

    public static DAOSeller getInstance() {
        if (INSTANCE == null) {
            synchronized (DAOSeller.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DAOSeller();
                }
            }
        }
        return INSTANCE;
    }
    
    public String getStatus() {
        try {
            if (con == null || con.isClosed()) {
                return "Database connection is null or closed";
            }
            return "OK";
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error checking connection status: {0}", e.getMessage());
            return "SQL Error: " + e.getMessage();
        }
    }
    
    public List<Product> getAll(int sellerId) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, p.seller_id, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.seller_id = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, sellerId); // Thêm tham số sellerId
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
                    p.setSellerId(rs.getInt("seller_id")); // Sửa: Lấy seller_id từ ResultSet

                    Category c = new Category();
                    c.setId(rs.getInt("category_id"));
                    c.setName(rs.getString("category_name"));
                    p.setCategory(c);
                    productList.add(p);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getAll: {0}", e.getMessage());
            e.printStackTrace();
        }
        return productList;
    }

    public Product getProductById(int productId, int sellerId) {
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, p.seller_id, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p JOIN Category c ON p.category_id = c.id WHERE p.id = ? AND p.seller_id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
            st.setInt(2, sellerId); // Thêm tham số sellerId
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setId(rs.getInt("product_id"));
                    product.setName(rs.getString("product_name"));
                    product.setDescription(rs.getString("description"));
                    product.setPrice(rs.getDouble("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setImgUrl(rs.getString("image_url"));
                    product.setShelfLifeHours(rs.getDouble("shelf_life_hours"));
                    product.setRate(rs.getDouble("rate"));
                    product.setSellerId(rs.getInt("seller_id")); // Sửa: Lấy seller_id từ ResultSet

                    Category c = new Category();
                    c.setId(rs.getInt("category_id"));
                    c.setName(rs.getString("category_name"));
                    product.setCategory(c);
                    return product;
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getProductById: {0}", e.getMessage());
        }
        return null;
    }

    public void deleteProductById(int productId, int sellerId) {
        String sql = "DELETE FROM Product WHERE id = ? AND seller_id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
            st.setInt(2, sellerId); // Thêm tham số sellerId
            int rowsAffected = st.executeUpdate();
            LOGGER.log(Level.INFO, "Deleted product with ID: {0}, Seller ID: {1}, Rows affected: {2}", 
                      new Object[]{productId, sellerId, rowsAffected});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in deleteProductById: {0}", e.getMessage());
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    public boolean insertProduct(Product product, int sellerId) {
        try {
            con.setAutoCommit(false);
            int categoryId = getOrInsertCategory(product.getCategory().getName());

            String sql = "INSERT INTO Product (name, description, price, stock, image_url, shelf_life_hours, rate, category_id, seller_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement st = con.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
                st.setString(1, product.getName());
                st.setString(2, product.getDescription());
                st.setDouble(3, product.getPrice());
                st.setInt(4, product.getStock());
                st.setString(5, product.getImgUrl());
                st.setDouble(6, product.getShelfLifeHours());
                st.setDouble(7, product.getRate());
                st.setInt(8, categoryId);
                st.setInt(9, sellerId); // Thêm seller_id
                int rowsAffected = st.executeUpdate();
                if (rowsAffected > 0) {
                    try (ResultSet generatedKeys = st.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            product.setId(generatedKeys.getInt(1));
                        }
                    }
                }
                con.commit();
                LOGGER.log(Level.INFO, "Inserted product: {0}, Seller ID: {1}, Rows affected: {2}", 
                          new Object[]{product.getName(), sellerId, rowsAffected});
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Rollback failed: {0}", rollbackEx.getMessage());
            }
            LOGGER.log(Level.SEVERE, "Error in insertProduct: {0}", e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit: {0}", e.getMessage());
            }
        }
    }

    public boolean updateProduct(Product product, int sellerId) {
        try {
            con.setAutoCommit(false);
            int categoryId = getOrInsertCategory(product.getCategory().getName());

            String sql = "UPDATE Product SET name = ?, description = ?, price = ?, stock = ?, image_url = ?, " +
                         "shelf_life_hours = ?, rate = ?, category_id = ? WHERE id = ? AND seller_id = ?";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setString(1, product.getName());
                st.setString(2, product.getDescription());
                st.setDouble(3, product.getPrice());
                st.setInt(4, product.getStock());
                st.setString(5, product.getImgUrl());
                st.setDouble(6, product.getShelfLifeHours());
                st.setDouble(7, product.getRate());
                st.setInt(8, categoryId);
                st.setInt(9, product.getId());
                st.setInt(10, sellerId); // Thêm tham số sellerId
                int rowsAffected = st.executeUpdate();
                con.commit();
                LOGGER.log(Level.INFO, "Updated product ID {0}, Seller ID: {1}: {2} rows affected", 
                          new Object[]{product.getId(), sellerId, rowsAffected});
                return rowsAffected > 0;
            }
        } catch (SQLException e) {
            try {
                con.rollback();
            } catch (SQLException rollbackEx) {
                LOGGER.log(Level.SEVERE, "Rollback failed: {0}", rollbackEx.getMessage());
            }
            LOGGER.log(Level.SEVERE, "Error in updateProduct: {0}", e.getMessage());
            return false;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                LOGGER.log(Level.SEVERE, "Failed to reset auto-commit: {0}", e.getMessage());
            }
        }
    }

    public List<Product> getProductPagination(int sellerId, int page, int productsPerPage) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, p.seller_id, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.seller_id = ? "
                + "ORDER BY p.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, sellerId); // Thêm tham số sellerId
            st.setInt(2, (page - 1) * productsPerPage);
            st.setInt(3, productsPerPage);
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
                    p.setSellerId(rs.getInt("seller_id")); // Sửa: Lấy seller_id từ ResultSet

                    Category c = new Category();
                    c.setId(rs.getInt("category_id"));
                    c.setName(rs.getString("category_name"));
                    p.setCategory(c);
                    productList.add(p);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getProductPagination: {0}", e.getMessage());
        }
        return productList;
    }

    public int getTotalProductCount(int sellerId) {
        String sql = "SELECT COUNT(*) FROM Product WHERE seller_id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, sellerId); // Thêm tham số sellerId
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getTotalProductCount: {0}", e.getMessage());
        }
        return 0;
    }

    public List<Product> searchProductsByNamePaginated(int sellerId, String keywords, int page, int productsPerPage) {
        List<Product> productList = new ArrayList<>();
        // Sửa: Thêm điều kiện WHERE seller_id = ? vào truy vấn
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, p.seller_id, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.seller_id = ? AND p.name LIKE ? "
                + "ORDER BY p.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, sellerId); 
            st.setString(2, "%" + (keywords != null ? keywords.trim() : "") + "%");
            st.setInt(3, (page - 1) * productsPerPage);
            st.setInt(4, productsPerPage);
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
                    p.setSellerId(rs.getInt("seller_id")); 
                    Category c = new Category();
                    c.setId(rs.getInt("category_id"));
                    c.setName(rs.getString("category_name"));
                    p.setCategory(c);
                    productList.add(p);
                }
                LOGGER.log(Level.INFO, "Retrieved {0} products for search query: {1}, Seller ID: {2}", 
                          new Object[]{productList.size(), keywords, sellerId});
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchProductsByNamePaginated: {0}", e.getMessage());
        }
        return productList;
    }

    public int getSearchProductsByNameCount(int sellerId, String keywords) {
        String sql = "SELECT COUNT(*) FROM Product WHERE seller_id = ? AND name LIKE ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, sellerId); // Thêm tham số sellerId
            st.setString(2, "%" + (keywords != null ? keywords.trim() : "") + "%");
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getSearchProductsByNameCount: {0}", e.getMessage());
        }
        return 0;
    }

    private int getOrInsertCategory(String categoryName) throws SQLException {
        String selectSql = "SELECT id FROM Category WHERE name = ?";
        try (PreparedStatement st = con.prepareStatement(selectSql)) {
            st.setString(1, categoryName);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        String insertSql = "INSERT INTO Category (name) VALUES (?)";
        try (PreparedStatement st = con.prepareStatement(insertSql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            st.setString(1, categoryName);
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("Failed to insert or retrieve category ID");
    }
}