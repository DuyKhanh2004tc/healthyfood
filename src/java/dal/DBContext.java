/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dal;
import java.sql.*;
/**
 *
 * @author ASUS
 */


public class DBContext {
    protected Connection connect;
    
public DBContext() {
    try {
        String url = "jdbc:sqlserver://" + serverName + ":" + portNumber + ";databaseName=" + dbName;
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connect = DriverManager.getConnection(url, userID, password);
        if (connect != null) {
            System.out.println("Database connection established successfully");
        } else {
            System.err.println("Failed to establish database connection");
        }
    } catch (Exception ex) {
        System.err.println("Error in DBContext: " + ex.getMessage());
        ex.printStackTrace();
    }
}

    private final String serverName = "localhost";
    private final String dbName = "HealthyFood";
    private final String portNumber = "1433";
    private final String userID = "sa";
    private final String password = "123";
}
