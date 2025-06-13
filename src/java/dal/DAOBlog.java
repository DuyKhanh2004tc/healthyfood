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
                b.setTitle(rs.getString("title"));
                b.setImage(rs.getString("image"));
                b.setDescription(rs.getString("description"));
                b.setCreated_at(rs.getTimestamp("created_at"));

                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setPhone(rs.getString("phone"));
                u.setDob(rs.getDate("dob"));
                u.setAddress(rs.getString("address"));
                u.setGender(rs.getBoolean("gender"));
                u.setCreatedAt(rs.getTimestamp("created_at"));

                Role r = new Role();
                r.setId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));

                u.setRole(r);
                b.setUser(u);
                blogList.add(b);
            }
        } catch (SQLException e) {
        }
        return blogList;

    }

    public Blog getBlogById(int blogId) {
        Blog blog = null;
        String sql = "Select b.id,b.title,b.image,b.description,b.created_at,u.id AS user_id,u.name,u.email,u.password,u.phone,u.dob,u.address,u.gender,u.created_at AS userCreate,r.id AS role_id,r.role_name "
                + "From Blog b "
                + "join Users u on u.id = b.user_id "
                + "join Role R on r.id = u.role_id WHERE b.id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setInt(1, blogId);
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                blog = new Blog();
                blog.setId(rs.getInt("id"));
                blog.setTitle(rs.getString("title"));
                blog.setImage(rs.getString("image"));
                blog.setDescription(rs.getString("description"));
                blog.setCreated_at(rs.getTimestamp("created_at"));

                User u = new User();
                u.setId(rs.getInt("user_id"));
                u.setName(rs.getString("name"));
                u.setEmail(rs.getString("email"));
                u.setPassword(rs.getString("password"));
                u.setPhone(rs.getString("phone"));
                u.setDob(rs.getDate("dob"));
                u.setAddress(rs.getString("address"));
                u.setGender(rs.getBoolean("gender"));
                u.setCreatedAt(rs.getTimestamp("userCreate"));

                Role r = new Role();
                r.setId(rs.getInt("role_id"));
                r.setRoleName(rs.getString("role_name"));

                u.setRole(r);
                blog.setUser(u);
            
                }
            }
        } catch (SQLException e) {

        }
        return blog;
    }
    public static void main(String[] args) {
            Blog blog = DAOBlog.INSTANCE.getBlogById(1);
            System.out.println(blog.getImage());
            
        }
}
