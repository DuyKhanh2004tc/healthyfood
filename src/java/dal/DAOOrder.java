
package dal;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.*;
import model.Order;
/**
 *
 * @author ASUS
 */
public class DAOOrder {
    public static DAOOrder INSTANCE= new DAOOrder();
    private static Connection con;
    private String status="OK";

    public DAOOrder() {
        if (INSTANCE==null){
            con = new DBContext().connect;
        }else
            INSTANCE = this;
    }
    
    public int insertOrder(Order order){
        int orderId = -1;
        String sql = "INSERT INTO Orders (user_id, total_amount, payment_method, status, shipper_id, "
                + "receiver_name, receiver_phone, receiver_emaill, shipping_address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement st = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
//            st.setInt(1, order.getUser().getId() != null ? order.getUser().getId() : null);
            st.setDouble(2, order.getTotalAmount());
            st.setString(3, order.getPaymentMethod());
            st.setString(4, order.getStatus());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }
    
    
}
