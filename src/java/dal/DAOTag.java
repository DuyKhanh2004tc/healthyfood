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

    public static void main(String[] args) {
       
    }
}
