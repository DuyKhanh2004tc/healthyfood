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

    public boolean updateBlog(Blog blog) {
        String sql = "UPDATE Blog SET title = ?, image = ?, description = ?, user_id = ? WHERE id = ?";
        try (PreparedStatement st = con.prepareStatement(sql)) {
            st.setString(1, blog.getTitle());
            st.setString(2, blog.getImage());
            st.setString(3, blog.getDescription());
            st.setInt(4, blog.getUser().getId());
            st.setInt(5, blog.getId());

            int rowsUpdated = st.executeUpdate();
            return rowsUpdated > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
public boolean deleteBlogById(int blogId) {
    String sqlDeleteBlogTag = "DELETE FROM BlogTag WHERE blog_id = ?";
    String sql = "DELETE FROM Blog WHERE id = ?";
    try {
        
        PreparedStatement ps1 = con.prepareStatement(sqlDeleteBlogTag);
        ps1.setInt(1, blogId);
        ps1.executeUpdate();
        ps1.close();

        
        PreparedStatement ps2 = con.prepareStatement(sql);
        ps2.setInt(1, blogId);
        int rowsAffected = ps2.executeUpdate();
        ps2.close();
        return rowsAffected > 0;
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
public List<Tag> getTagByBlogId(int blogId) {
    List<Tag> list = new ArrayList<>();
    String sql = "SELECT t.id, t.name, t.slug, t.description "
               + "FROM BlogTag bt "
               + "JOIN Tag t ON bt.tag_id = t.id "
               + "WHERE bt.blog_id = ?";
    try {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, blogId);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            Tag tag = new Tag(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("slug"),
                rs.getString("description")
            );
            list.add(tag);
        }
        rs.close();
        ps.close();
    } catch (SQLException e) {
        status = "Error at getTagByBlogId: " + e.getMessage();
    }
    return list;
}

public List<Blog> getBlogsByTagSlug(String slug) {
    List<Blog> list = new ArrayList<>();
    String sql = """
        SELECT b.id AS blog_id, b.user_id, b.title, b.image, b.description, b.created_at,
               u.id AS user_id, u.name AS user_name, u.email, u.password, u.phone,
               u.dob, u.address, u.gender, u.created_at AS user_created_at
        FROM Blog b
        JOIN BlogTag bt ON b.id = bt.blog_id
        JOIN Tag t ON bt.tag_id = t.id
        JOIN Users u ON b.user_id = u.id
        WHERE t.slug = ?
        ORDER BY b.created_at DESC
        """;
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, slug);
        try (ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("phone"),
                        rs.getDate("dob"),
                        rs.getString("address"),
                        rs.getBoolean("gender"),
                        null, // Role nếu cần lấy thì join thêm
                        rs.getTimestamp("user_created_at")
                );
                Blog blog = new Blog(
                        rs.getInt("blog_id"),
                        rs.getString("title"),
                        rs.getString("image"),
                        rs.getString("description"),
                        rs.getTimestamp("created_at"),
                        user
                );
                list.add(blog);
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

    public static void main(String[] args) {
        Blog blog = DAOBlog.INSTANCE.getBlogById(1);
        System.out.println(blog.getImage());

    }
}
