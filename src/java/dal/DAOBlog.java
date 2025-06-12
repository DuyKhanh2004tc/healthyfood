/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ASUS
 */
public class DAOBlog {

    public static DAOBlog INSTANCE = new DAOBlog();
    private Connection con;
    private String status = "OK";

    public DAOBlog() {
        con = new DBContext().connect;
    }

    public List<Blog> getAllBlog() {
        List<Blog> blogList = new ArrayList<>();
        String sql = "Select b.id,b.title,b.image,b.description,b.created_at,u.id AS user_id,u.name,u.email,u.password,u.phone,u.dob,u.address,u.gender,u.created_at,r.id AS role_id,r.role_name "
                + "From Blog b "
                + "join Users u on u.id = b.user_id "
                + "join Role R on r.id = u.role_id";
        try {
            PreparedStatement st = con.prepareStatement(sql);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
               Blog b = new Blog();
               b.setId(rs.getInt("id"));
            }
        } catch (SQLException e) {
        }
        return blogList;

    }
}
