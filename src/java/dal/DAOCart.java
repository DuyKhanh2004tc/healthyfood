/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Cart;
import model.CartItem;
import model.Category;
import model.Product;
import model.User;

/**
 *
 * @author ASUS
 */
public class DAOCart {

    public static DAOCart INSTANCE = new DAOCart();
    private Connection con;
    private String status = "OK";

    public DAOCart() {
        con = new DBContext().connect;
    }

    public void removeCartItem(int userId, int productId) {
        try {
            String getCartIdSQL = " SELECT id FROM Cart WHERE user_id = ? ";
            int cartId = -1;
            try (PreparedStatement st = con.prepareStatement(getCartIdSQL)) {
                st.setInt(1, userId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    cartId = rs.getInt("id");
                }
            }

            String getItemSQL = "SELECT id  FROM CartItem WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement st = con.prepareStatement(getItemSQL)) {
                st.setInt(1, cartId);
                st.setInt(2, productId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    int itemId = rs.getInt("id");
                    String deleteItemSQL = "DELETE FROM CartItem WHERE id = ?";
                    try (PreparedStatement st2 = con.prepareStatement(deleteItemSQL)) {
                        st2.setInt(1, itemId);
                        st2.executeUpdate();

                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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

    public void updateCartItemQuantity(int userId, int productId, int n) {
        try {
            String getCartIdSQL = " SELECT id FROM Cart WHERE user_id = ? ";
            int cartId = -1;
            try (PreparedStatement st = con.prepareStatement(getCartIdSQL)) {
                st.setInt(1, userId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    cartId = rs.getInt("id");
                }
            }

            String getItemSQL = "SELECT id, quantity FROM CartItem WHERE cart_id = ? AND product_id = ?";
            try (PreparedStatement st = con.prepareStatement(getItemSQL)) {
                st.setInt(1, cartId);
                st.setInt(2, productId);
                ResultSet rs = st.executeQuery();
                if (rs.next()) {
                    int itemId = rs.getInt("id");
                    int quantity = rs.getInt("quantity") + n;
                    if (quantity <= 0) {
                        String deleteItemSQL = "DELETE FROM CartItem WHERE id = ?";
                        try (PreparedStatement st2 = con.prepareStatement(deleteItemSQL)) {
                            st2.setInt(1, itemId);
                            st2.executeUpdate();
                        }
                    } else {
                        String updateItemSQL = "UPDATE CartItem SET quantity = ? WHERE id = ? ";
                        try (PreparedStatement st3 = con.prepareStatement(updateItemSQL)) {
                            st3.setInt(1, quantity);
                            st3.setInt(2, itemId);
                            st3.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
