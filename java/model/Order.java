package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

public class Order implements Serializable {
    private int orderID;
    private int userID;
    private Timestamp orderDate;
    private String status;
    private BigDecimal totalAmount;
    private String shippingAddress;
    
    // Additional fields for display
    private String userName;
    private String userEmail;
    private List<OrderDetail> orderDetails;

    // Additional fields for compatibility (not in DB but used by servlet)
    private String phoneNumber;
    private String paymentMethod;
    private String notes;

    public Order() {}

    public Order(int userID, String status, BigDecimal totalAmount, String shippingAddress) {
        this.userID = userID;
        this.status = status;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
    }

    // Getters and Setters
    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public Timestamp getOrderDate() { return orderDate; }
    public void setOrderDate(Timestamp orderDate) { this.orderDate = orderDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String shippingAddress) { this.shippingAddress = shippingAddress; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public List<OrderDetail> getOrderDetails() { return orderDetails; }
    public void setOrderDetails(List<OrderDetail> orderDetails) { this.orderDetails = orderDetails; }

    // Alternative method names for compatibility
    public int getOrderId() { return orderID; }
    public void setOrderId(int orderId) { this.orderID = orderId; }
    
    public int getUserId() { return userID; }
    public void setUserId(int userId) { this.userID = userId; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
    
    // Overload for double totalAmount
    public void setTotalAmount(double totalAmount) { 
        this.totalAmount = BigDecimal.valueOf(totalAmount); 
    }
    
    // Overload for Date orderDate
    public void setOrderDate(java.util.Date orderDate) { 
        this.orderDate = new Timestamp(orderDate.getTime()); 
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderID=" + orderID +
                ", userID=" + userID +
                ", orderDate=" + orderDate +
                ", status='" + status + '\'' +
                ", totalAmount=" + totalAmount +
                '}';
    }
}
