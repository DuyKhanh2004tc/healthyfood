
package dal;
import java.sql.*;



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
