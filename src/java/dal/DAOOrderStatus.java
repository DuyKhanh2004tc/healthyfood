package dal;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.OrderStatus;



/**
 * @author Cuong
 */
public class DAOOrderStatus {
    public static DAOOrderStatus INSTANCE = new DAOOrderStatus();
    private static Connection con;
    private String status = "OK";

    public DAOOrderStatus() {
        if (INSTANCE == null) {
            con = new DBContext().connect;
        } else {
            INSTANCE = this;
        }
    }
    
    public List<OrderStatus> getAllOrderStatuses() {
        List<OrderStatus> statuses = new ArrayList<>();
        String query = "SELECT id, status_name, description FROM OrderStatus";
        try (PreparedStatement ps = con.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                OrderStatus status = new OrderStatus();
                status.setId(rs.getInt("id"));
                status.setStatusName(rs.getString("status_name"));
                status.setDescription(rs.getString("description"));
                statuses.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            status = "Error: Unable to retrieve order statuses - " + e.getMessage();
        }
        return statuses;
    }

    public boolean isValidTransition(int currentStatusId, int newStatusId) {
        boolean isValid = false;
        switch (currentStatusId) {
            case 1: // Pending Confirmation
                isValid = (newStatusId == 2 || newStatusId == 7 || newStatusId == 9);
                break;
            case 2: // Confirmed
                isValid = (newStatusId == 3 || newStatusId == 7);
                break;
            case 3: // Processing
                isValid = (newStatusId == 4 || newStatusId == 7);
                break;
            case 4: // Waiting for Delivery
                isValid = (newStatusId == 5);
                break;
            case 5: // Delivering
                isValid = (newStatusId == 6);
                break;
            case 6: // Delivered
                isValid = (newStatusId == 8);
                break;
            case 7: // Cancelled
            case 8: // Returned
                isValid = (newStatusId == currentStatusId);
                break;
            default:
                isValid = false;
        }
        return isValid;
    }

    public List<OrderStatus> getValidStatusesFor(int currentStatusId) {
        List<OrderStatus> validStatuses = new ArrayList<>();
        List<OrderStatus> allStatuses = getAllOrderStatuses();
        if (allStatuses != null) {
            for (OrderStatus status : allStatuses) {
                if (isValidTransition(currentStatusId, status.getId())) {
                    validStatuses.add(status);
                }
            }
            // Thêm trạng thái hiện tại vào đầu danh sách để giữ nguyên được chọn mặc định
            for (OrderStatus status : allStatuses) {
                if (status.getId() == currentStatusId) {
                    validStatuses.add(0, status);
                    break;
                }
            }
        }
        return validStatuses;
    }

    public void closeConnection() {
        try {
            if (con != null && !con.isClosed()) {
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            status = "Error: Unable to close connection - " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        DAOOrderStatus dao = DAOOrderStatus.INSTANCE;
        List<OrderStatus> statuses = dao.getAllOrderStatuses();
        for (OrderStatus status : statuses) {
            System.out.println(status.getId() + " - " + status.getStatusName() + " - " + status.getDescription());
        }
        dao.closeConnection();
    }

    
}