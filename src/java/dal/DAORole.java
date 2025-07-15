/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import model.Role;

/**
 *
 * @author Cuong
 */
public class DAORole {

    public static DAORole INSTANCE = new DAORole();
    private Connection con;
    private String status = "OK";

    public DAORole() {
        if (INSTANCE == null) {
            con = new DBContext().connect;
        } else {
            INSTANCE = this;
        }
    }

    public ArrayList<Role> getAllRoles() {
        ArrayList<Role> roles = new ArrayList<>();
        String query = "SELECT id, role_name FROM Role";
        try (PreparedStatement ps = con.prepareStatement(query); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Role role = new Role();
                role.setId(rs.getInt("id"));
                role.setRoleName(rs.getString("role_name"));
                roles.add(role);
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = "Error: Unable to retrieve roles - " + e.getMessage();
        }
        return roles;
    }
   public Role getRoleById(int id) {
        String query = "SELECT id, role_name FROM Role WHERE id = ?";
        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Role role = new Role();
                    role.setId(rs.getInt("id"));
                    role.setRoleName(rs.getString("role_name"));
                    return role;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = "Error: Unable to retrieve role by ID - " + e.getMessage();
        }
        return null; 
    }
    

    public static void main(String[] args) {
        ArrayList<Role> n = DAORole.INSTANCE.getAllRoles();
        for (int i = 0; i < n.size(); i++) {
            System.out.println(n.get(i).getId() + n.get(i).getRoleName());
        }
    }

}
