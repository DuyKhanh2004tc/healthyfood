
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
        String sql = "INSERT INTO Orders (user_id, total_amount, payment_method, status_id, shipper_id, "
                + "receiver_name, receiver_phone, receiver_email, shipping_address) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try(PreparedStatement st = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            if(order.getUser()!= null){
                st.setInt(1,order.getUser().getId());
            } else {
                st.setNull(1,Types.INTEGER );
            }
            st.setDouble(2, order.getTotalAmount());
            st.setString(3, order.getPaymentMethod());
            st.setInt(4, order.getStatus().getId());
            if(order.getShipper()!= null){
                st.setInt(5,order.getShipper().getId());
            } else {
                st.setNull(5,Types.INTEGER );
            }
            st.setString(6, order.getReceiverName());
            st.setString(7, order.getReceiverPhone());
            st.setString(8, order.getReceiverEmail());
            st.setString(9, order.getShippingAddress());
            
            int insertedRows = st.executeUpdate();
            if(insertedRows > 0){
                try(ResultSet rs = st.getGeneratedKeys()){
                    if (rs.next()){
                        orderId=rs.getInt(1);
                    }
                }
            }
                   
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orderId;
    }
    
    
}
