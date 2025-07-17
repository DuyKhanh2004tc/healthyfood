package model;

import java.sql.Timestamp;
import java.util.List;

/**
 * @author ASUS
 */
public class Order {
    private int id;
    private User user;
    private Timestamp orderDate;
    private double totalAmount;
    private String paymentMethod;
    private OrderStatus status; 
    private User shipper;
    private String receiverName;
    private String receiverPhone;
    private String receiverEmail;
    private String shippingAddress;
    private String deliveryMessage;
    private List<OrderDetail> orderDetails;
    private List<OrderStatus> validStatuses; 

    public Order() {
    }
    
     public Order(int id) {
        this.id = id;
    }

    public Order(int id, User user, Timestamp orderDate, double totalAmount, String paymentMethod, OrderStatus status, User shipper, String receiverName, String receiverPhone, String receiverEmail, String shippingAddress, String deliveryMessage, List<OrderDetail> orderDetails, List<OrderStatus> validStatuses) {
        this.id = id;
        this.user = user;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.shipper = shipper;
        this.receiverName = receiverName;
        this.receiverPhone = receiverPhone;
        this.receiverEmail = receiverEmail;
        this.shippingAddress = shippingAddress;
        this.deliveryMessage = deliveryMessage;
        this.orderDetails = orderDetails;
        this.validStatuses = validStatuses;
    }

    public String getDeliveryMessage() {
        return deliveryMessage;
    }

    public void setDeliveryMessage(String deliveryMessage) {
        this.deliveryMessage = deliveryMessage;
    }

    

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Timestamp getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Timestamp orderDate) {
        this.orderDate = orderDate;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public User getShipper() {
        return shipper;
    }

    public void setShipper(User shipper) {
        this.shipper = shipper;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderStatus> getValidStatuses() {
        return validStatuses;
    }

    public void setValidStatuses(List<OrderStatus> validStatuses) {
        this.validStatuses = validStatuses;
    }
}