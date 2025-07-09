package dal;

import model.Product;
import model.Category;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Order;
import model.OrderDetail;

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
    
    public List<Product> getAll() {
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

    public Product getProductById(int productId) {
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, p.rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p JOIN Category c ON p.category_id = c.id WHERE p.id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
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

    public void deleteProductById(int productId) {
        String sql = "DELETE FROM Product WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
            int rowsAffected = st.executeUpdate();
            LOGGER.log(Level.INFO, "Deleted product with ID: {0}, Rows affected: {1}", new Object[]{productId, rowsAffected});
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in deleteProductById: {0}", e.getMessage());
            throw new RuntimeException("Failed to delete product", e);
        }
    }

    public boolean insertProduct(Product product) {
        try {
            con.setAutoCommit(false);
            int categoryId = getOrInsertCategory(product.getCategory().getName());

            String sql = "UPDATE Product SET name = ?, description = ?, price = ?, stock = ?, image_url = ?, "
                    + "shelf_life_hours = ?, rate = ?, category_id = ? WHERE id = ?";
            try (PreparedStatement st = con.prepareStatement(sql)) {
                st.setString(1, product.getName());
                st.setString(2, product.getDescription());
                st.setDouble(3, product.getPrice());
                st.setInt(4, product.getStock());
                st.setString(5, product.getImgUrl());
                st.setDouble(6, product.getShelfLifeHours());
                st.setDouble(7, product.getRate());
                st.setInt(8, categoryId);
                int rowsAffected = st.executeUpdate();
                con.commit();
                LOGGER.log(Level.INFO, "Inserted product: {0}, Rows affected: {1}", new Object[]{product.getName(), rowsAffected});
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

    public boolean updateProduct(Product product) {
        try {
            con.setAutoCommit(false);
            int categoryId = getOrInsertCategory(product.getCategory().getName());

            String sql = "UPDATE Product SET name = ?, description = ?, price = ?, stock = ?, image_url = ?, "
                    + "shelf_life_hours = ?, rate = ?, category_id = ? WHERE id = ?";
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
                int rowsAffected = st.executeUpdate();
                con.commit();
                LOGGER.log(Level.INFO, "Updated product ID {0}: {1} rows affected", new Object[]{product.getId(), rowsAffected});
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

    public List<Product> getProductPagination(int page, int productsPerPage) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "ORDER BY p.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, (page - 1) * productsPerPage);
            st.setInt(2, productsPerPage);
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
            LOGGER.log(Level.SEVERE, "Error in getProductPagination: {0}", e.getMessage());
        }
        return productList;
    }

    public int getTotalProductCount() {
        String sql = "SELECT COUNT(*) FROM Product";
        try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in getTotalProductCount: {0}", e.getMessage());
        }
        return 0;
    }

    public List<Product> searchProductsByNamePaginated(String keywords, int page, int productsPerPage) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "WHERE p.name LIKE ? "
                + "ORDER BY p.id OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, "%" + (keywords != null ? keywords.trim() : "") + "%");
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

                    Category c = new Category();
                    c.setId(rs.getInt("category_id"));
                    c.setName(rs.getString("category_name"));
                    p.setCategory(c);
                    productList.add(p);
                }
                LOGGER.log(Level.INFO, "Retrieved {0} products for search query: {1}", new Object[]{productList.size(), keywords});
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Error in searchProductsByNamePaginated: {0}", e.getMessage());
        }
        return productList;
    }

    public int getSearchProductsByNameCount(String keywords) {
        String sql = "SELECT COUNT(*) FROM Product WHERE name LIKE ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, "%" + (keywords != null ? keywords.trim() : "") + "%");
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
    


public int getTotalOrderCount() {
    String sql = "SELECT COUNT(*) FROM Orders";
    try (PreparedStatement st = con.prepareStatement(sql); ResultSet rs = st.executeQuery()) {
        if (rs.next()) {
            return rs.getInt(1);
        }
    } catch (SQLException e) {
        LOGGER.log(Level.SEVERE, "Error in getTotalOrderCount: {0}", e.getMessage());
    }
    return 0;
}

    

}