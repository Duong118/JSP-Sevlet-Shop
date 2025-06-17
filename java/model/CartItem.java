package model;

import java.io.Serializable;

public class CartItem implements Serializable {
    private int cartItemID;
    private int userID;
    private int shoeID;
    private int sizeID;
    private int quantity;
    
    // Additional fields for display
    private String shoeName;
    private String shoeImage;
    private double shoePrice;
    private int sizeNumber;
    private int stockQuantity;
    
    // Nested objects for JSP compatibility
    private Shoe shoe;
    private Size size;

    public CartItem() {}

    public CartItem(int userID, int shoeID, int sizeID, int quantity) {
        this.userID = userID;
        this.shoeID = shoeID;
        this.sizeID = sizeID;
        this.quantity = quantity;
    }

    // Getters and Setters
    public int getCartItemID() { return cartItemID; }
    public void setCartItemID(int cartItemID) { this.cartItemID = cartItemID; }

    public int getUserID() { return userID; }
    public void setUserID(int userID) { this.userID = userID; }

    public int getShoeID() { return shoeID; }
    public void setShoeID(int shoeID) { this.shoeID = shoeID; }

    public int getSizeID() { return sizeID; }
    public void setSizeID(int sizeID) { this.sizeID = sizeID; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getShoeName() { return shoeName; }
    public void setShoeName(String shoeName) { this.shoeName = shoeName; }

    public String getShoeImage() { return shoeImage; }
    public void setShoeImage(String shoeImage) { this.shoeImage = shoeImage; }

    public double getShoePrice() { return shoePrice; }
    public void setShoePrice(double shoePrice) { this.shoePrice = shoePrice; }

    public int getSizeNumber() { return sizeNumber; }
    public void setSizeNumber(int sizeNumber) { this.sizeNumber = sizeNumber; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    // Alternative method names for compatibility
    public int getShoeId() { return shoeID; }
    public void setShoeId(int shoeId) { this.shoeID = shoeId; }
    
    public int getSizeId() { return sizeID; }
    public void setSizeId(int sizeId) { this.sizeID = sizeId; }
      public double getPrice() { return shoePrice; }
    public void setPrice(double price) { this.shoePrice = price; }
    
    public double getTotalPrice() {
        return shoePrice * quantity;
    }

    // Getters and setters for nested objects
    public Shoe getShoe() { 
        if (shoe == null) {
            shoe = new Shoe();
            shoe.setShoeId(shoeID);
            shoe.setShoeName(shoeName);
            shoe.setImageUrl(shoeImage);
            shoe.setPrice(shoePrice);
        }
        return shoe; 
    }
    public void setShoe(Shoe shoe) { this.shoe = shoe; }

    public Size getSize() { 
        if (size == null) {
            size = new Size();
            size.setSizeId(sizeID);
            size.setSizeNumber(sizeNumber);
        }
        return size; 
    }
    public void setSize(Size size) { this.size = size; }

    @Override
    public String toString() {
        return "CartItem{" +
                "cartItemID=" + cartItemID +
                ", userID=" + userID +
                ", shoeID=" + shoeID +
                ", sizeID=" + sizeID +
                ", quantity=" + quantity +
                '}';
    }
}
