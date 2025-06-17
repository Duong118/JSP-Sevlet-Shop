package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class OrderDetail implements Serializable {
    private int orderDetailID;
    private int orderID;
    private int shoeID;
    private int sizeID;
    private int quantity;
    private BigDecimal price;
    
    // Additional fields for display
    private String shoeName;
    private String shoeImage;
    private int sizeNumber;

    public OrderDetail() {}

    public OrderDetail(int orderID, int shoeID, int sizeID, int quantity, BigDecimal price) {
        this.orderID = orderID;
        this.shoeID = shoeID;
        this.sizeID = sizeID;
        this.quantity = quantity;
        this.price = price;
    }

    // Getters and Setters
    public int getOrderDetailID() { return orderDetailID; }
    public void setOrderDetailID(int orderDetailID) { this.orderDetailID = orderDetailID; }

    public int getOrderID() { return orderID; }
    public void setOrderID(int orderID) { this.orderID = orderID; }

    public int getShoeID() { return shoeID; }
    public void setShoeID(int shoeID) { this.shoeID = shoeID; }

    public int getSizeID() { return sizeID; }
    public void setSizeID(int sizeID) { this.sizeID = sizeID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getShoeName() { return shoeName; }
    public void setShoeName(String shoeName) { this.shoeName = shoeName; }

    public String getShoeImage() { return shoeImage; }
    public void setShoeImage(String shoeImage) { this.shoeImage = shoeImage; }

    public int getSizeNumber() { return sizeNumber; }
    public void setSizeNumber(int sizeNumber) { this.sizeNumber = sizeNumber; }

    // Alternative method names for compatibility
    public int getOrderId() { return orderID; }
    public void setOrderId(int orderId) { this.orderID = orderId; }
    
    public int getShoeId() { return shoeID; }
    public void setShoeId(int shoeId) { this.shoeID = shoeId; }
    
    public int getSizeId() { return sizeID; }
    public void setSizeId(int sizeId) { this.sizeID = sizeId; }

    public BigDecimal getTotalPrice() {
        return price.multiply(new BigDecimal(quantity));
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "orderDetailID=" + orderDetailID +
                ", orderID=" + orderID +
                ", shoeID=" + shoeID +
                ", sizeID=" + sizeID +
                ", quantity=" + quantity +
                ", price=" + price +
                '}';
    }
}
