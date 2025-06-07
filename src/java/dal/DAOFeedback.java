package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Feedback;
import model.Product;
import model.User;

/**
 *
 * @author Cuong
 */
public class DAOFeedback {

    public static DAOFeedback INSTANCE = new DAOFeedback();
    private static Connection con;
    private String status = "OK";

    public DAOFeedback() {
        if (INSTANCE == null) {
            con = new DBContext().connect;
        } else {
            INSTANCE = this;
        }
    }


    public ArrayList getFeedbackByProductId(int productId) {
        ArrayList<Feedback> feedbackList = new ArrayList<>();
        try {
            String sql = "SELECT f.id,f.content,f.created_at,f.rate,p.id AS product_id,p.name,p.price,p.shelf_life_hours,p.stock,p.image_url,p.description,p.rate AS rate_product,u.id AS user_id,u.name AS user_name,u.email,u.password,u.phone,u.dob,u.address,u.gender,u.created_at AS user_created_at "
                    + "FROM Feedback f "
                    + "JOIN Product p on p.id = f.product_id "
                    + "JOIN Users u on u.id = f.user_id WHERE p.id = ?";
            PreparedStatement statement = con.prepareStatement(sql);
            statement.setInt(1, productId);
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                Feedback feedback = new Feedback();

                feedback.setId(rs.getInt("id"));
                feedback.setContent(rs.getString("content"));
                feedback.setCreatedAt(rs.getTimestamp("created_at"));
                feedback.setRate(rs.getDouble("rate"));

                Product p = new Product();
                p.setId(rs.getInt("product_id"));
                p.setName(rs.getString("name"));
                p.setDescription(rs.getString("description"));
                p.setPrice(rs.getDouble("price"));
                p.setStock(rs.getInt("stock"));
                p.setImgUrl(rs.getString("image_url"));
                p.setShelfLifeHours(0);
                p.setRate(rs.getDouble("rate"));
                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setName(rs.getString("user_name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setPhone(rs.getString("phone"));
                u.setDob(rs.getDate("dob"));
                u.setAddress(rs.getString("address"));
                u.setGender(rs.getBoolean("gender"));
                u.setCreatedAt(rs.getTimestamp("user_created_at"));

                feedback.setProduct(p);
                feedback.setUser(u);
                feedbackList.add(feedback);
            }

            rs.close();
            statement.close();
        } catch (SQLException ex) {

        }
        return feedbackList;
    }
    

    public void insertFeedback(Feedback feedback) {
        String sql = "INSERT INTO Feedback(user_id, product_id, content, created_at, rate) VALUES (?, ?, ?, ?,? )";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, feedback.getUser().getId());
            ps.setInt(2, feedback.getProduct().getId());
            ps.setString(3, feedback.getContent());
            ps.setTimestamp(4, feedback.getCreatedAt());
             ps.setDouble(5, feedback.getRate());
            ps.executeUpdate();
        } catch (SQLException e) {
            Logger.getLogger(DAO.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}
