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
public class DAOTag {

    public static DAOBlog INSTANCE = new DAOBlog();
    private Connection con;
    private String status = "OK";

    public DAOTag() {
        con = new DBContext().connect;
    }

    public List<Tag> listAllTag() {
    List<Tag> list = new ArrayList<>();
    String sql = "SELECT * FROM Tag";
    try {
        PreparedStatement ps = con.prepareStatement(sql);
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
        status = "Error at listAllTag: " + e.getMessage();
    }
    return list;
}
public void deleteBlogTag(int blogId) {
    String sql = "DELETE FROM BlogTag WHERE blog_id = ?";
    try {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, blogId);
        ps.executeUpdate();
        ps.close();
    } catch (SQLException e) {
        status = "Error at deleteBlogTag: " + e.getMessage();
    }
}
public void insertBlogTag(int blogId, int tagId) {
    String sql = "INSERT INTO BlogTag (blog_id, tag_id) VALUES (?, ?)";
    try {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setInt(1, blogId);
        ps.setInt(2, tagId);
        ps.executeUpdate();
        ps.close();
    } catch (SQLException e) {
        status = "Error at insertBlogTag: " + e.getMessage();
    }
}

public List<Tag> listAllGoalTags() {
    List<Tag> list = new ArrayList<>();
    String sql = "SELECT * FROM Tag WHERE slug NOT IN ('weight-loss', 'weight-gain', 'maintain-weight')";
    try {
        PreparedStatement ps = con.prepareStatement(sql);
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
        status = "Error at listAllGoalTags: " + e.getMessage();
    }
    return list;
}

public Tag getTagBySlug(String slug) {
    String sql = "SELECT * FROM Tag WHERE slug = ?";
    try {
        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, slug);
        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            Tag tag = new Tag(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getString("slug"),
                rs.getString("description")
            );
            rs.close();
            ps.close();
            return tag;
        }
        rs.close();
        ps.close();
    } catch (SQLException e) {
        status = "Error at getTagBySlug: " + e.getMessage();
    }
    return null;
}

public boolean insertTag(Tag tag) {
    String sql = "INSERT INTO Tag (name, slug, description) VALUES (?, ?, ?)";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, tag.getName());
        ps.setString(2, tag.getSlug());
        ps.setString(3, tag.getDescription());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        status = "Error at insertTag: " + e.getMessage();
        return false;
    }
}

public boolean updateTag(Tag tag) {
    String sql = "UPDATE Tag SET name = ?, slug = ?, description = ? WHERE id = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setString(1, tag.getName());
        ps.setString(2, tag.getSlug());
        ps.setString(3, tag.getDescription());
        ps.setInt(4, tag.getId());
        int rowsAffected = ps.executeUpdate();
        return rowsAffected > 0;
    } catch (SQLException e) {
        status = "Error at updateTag: " + e.getMessage();
        return false;
    }
}

public boolean deleteTag(int tagId) {
    String sqlDeleteBlogTag = "DELETE FROM BlogTag WHERE tag_id = ?";
    String sqlDeleteTag = "DELETE FROM Tag WHERE id = ?";
    try {
        // Xóa BlogTag
        try (PreparedStatement ps1 = con.prepareStatement(sqlDeleteBlogTag)) {
            ps1.setInt(1, tagId);
            ps1.executeUpdate();
        }
        // Xóa Tag
        try (PreparedStatement ps2 = con.prepareStatement(sqlDeleteTag)) {
            ps2.setInt(1, tagId);
            int rowsAffected = ps2.executeUpdate();
            return rowsAffected > 0;
        }
    } catch (SQLException e) {
        status = "Error at deleteTag: " + e.getMessage();
        return false;
    }
}

public Tag getTagById(int tagId) {
    String sql = "SELECT * FROM Tag WHERE id = ?";
    try (PreparedStatement ps = con.prepareStatement(sql)) {
        ps.setInt(1, tagId);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return new Tag(
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("slug"),
                    rs.getString("description")
                );
            }
        }
    } catch (SQLException e) {
        status = "Error at getTagById: " + e.getMessage();
    }
    return null;
}

    public static void main(String[] args) {
       
    }
}
