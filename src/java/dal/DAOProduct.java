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

    public static void main(String[] args) {
        // Gọi getAllProduct từ INSTANCE
        List<Product> products = DAOProduct.INSTANCE.getAllProduct();

        // In ra thông tin từng sản phẩm
        for (Product p : products) {
            System.out.println("ID: " + p.getId());
            System.out.println("Name: " + p.getName());
            System.out.println("Description: " + p.getDescription());
            System.out.println("Price: " + p.getPrice());
            System.out.println("Stock: " + p.getStock());
            System.out.println("Image URL: " + p.getImgUrl());
            System.out.println("Shelf Life Hours: " + p.getShelfLifeHours());
            System.out.println("rate : " + p.getRate());
            System.out.println("Category: " + p.getCategory().getName());
            System.out.println("------------------------------------------");
        }
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

    public List<CartItem> getCartItemsByUserId(int userId) {
        List<CartItem> itemList = new ArrayList<>();
        String sql = "SELECT ci.id AS cart_item_id, ci.quantity, "
                + "p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, "
                + "p.image_url, p.shelf_life_hours, p.rate AS average_rate, "
                + "c.id AS cart_id, c.user_id, c.created_at, "
                + "ca.id AS category_id, ca.name AS category_name "
                + "FROM CartItem ci "
                + "INNER JOIN Product p ON ci.product_id = p.id "
                + "INNER JOIN Cart c ON ci.cart_id = c.id "
                + "INNER JOIN Category ca ON p.category_id = ca.id "
                + "WHERE c.user_id = ?";

        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, userId);
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

                Cart cart = new Cart();
                cart.setId(rs.getInt("cart_id"));

                User user = new User();
                user.setId(rs.getInt("user_id"));
                cart.setUser(user);

                CartItem item = new CartItem();
                item.setId(rs.getInt("cart_item_id"));
                item.setQuantity(rs.getInt("quantity"));
                item.setProduct(p);
                item.setCart(cart);

                itemList.add(item);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return itemList;
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

    public String getStatus() {
        try {
            if (con == null || con.isClosed()) {
                status = "Database connection is null or closed";
                LOGGER.log(Level.WARNING, "Invalid connection detected in getStatus()");
            }
        } catch (SQLException e) {
            status = "SQL Error checking connection: " + e.getMessage();
            LOGGER.log(Level.SEVERE, "SQL Error in getStatus: {0}", e.getMessage());
        }
        return status;
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

    public void insertProduct(Product product) {
        String sql = "INSERT INTO Product (name, description, price, stock, image_url, shelf_life_hours, category_id) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, product.getName());
            st.setString(2, product.getDescription());
            st.setDouble(3, product.getPrice());
            st.setInt(4, product.getStock());
            st.setString(5, product.getImgUrl());
            st.setDouble(6, product.getShelfLifeHours());

            st.setInt(8, product.getCategory().getId());
            st.executeUpdate();
            LOGGER.log(Level.INFO, "Inserted product: {0}", product.getName());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in insertProduct: {0}", e.getMessage());
        }
    }

    public boolean updateProduct(Product product) {
        String sql = "UPDATE Product SET name = ?, description = ?, price = ?, stock = ?, image_url = ?, shelf_life_hours = ?, category_id = ? "
                + "WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, product.getName());
            st.setString(2, product.getDescription());
            st.setDouble(3, product.getPrice());
            st.setInt(4, product.getStock());
            st.setString(5, product.getImgUrl());
            st.setDouble(6, product.getShelfLifeHours());
            st.setInt(7, product.getCategory().getId());
            st.setInt(8, product.getId());
            int rowsAffected = st.executeUpdate();
            if (rowsAffected == 0) {
                LOGGER.log(Level.WARNING, "No product found with ID: {0}", product.getId());
                return false;
            }
            LOGGER.log(Level.INFO, "Updated product with ID: {0}", product.getId());
            return true;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in updateProduct for ID {0}: {1}", new Object[]{product.getId(), e.getMessage()});
            return false;
        }
    }

    public void deleteProductById(int productId) {
        String sql = "DELETE FROM Product WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, productId);
            st.executeUpdate();
            LOGGER.log(Level.INFO, "Deleted product with ID: {0}", productId);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL Error in deleteProductById: {0}", e.getMessage());
        }
    }

    public List<Product> searchProductsByName(String keywords) {
        List<Product> productList = new ArrayList<>();
        String sql = "SELECT p.id AS product_id, p.name AS product_name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, "
                + "AVG(COALESCE(f.rate, p.rate)) AS average_rate, c.id AS category_id, c.name AS category_name "
                + "FROM Product p "
                + "INNER JOIN Category c ON p.category_id = c.id "
                + "LEFT JOIN Feedback f ON p.id = f.product_id "
                + "WHERE p.name LIKE ? "
                + "GROUP BY p.id, p.name, p.description, p.price, p.stock, p.image_url, p.shelf_life_hours, c.id, c.name";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, "%" + (keywords != null ? keywords.trim() : "") + "%");
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
            LOGGER.log(Level.SEVERE, "SQL Error in searchProductsByName: {0}", e.getMessage());
        }
        return productList;
    }

}
