package model;

import java.io.Serializable;

public class ShoeSize implements Serializable {
    private int shoeID;
    private int sizeID;
    private int stockQuantity;
    private int sizeNumber; // For displaying purpose
    private String shoeName; // For displaying purpose

    public ShoeSize() {}

    public ShoeSize(int shoeID, int sizeID, int stockQuantity) {
        this.shoeID = shoeID;
        this.sizeID = sizeID;
        this.stockQuantity = stockQuantity;
    }

    // Getters and Setters
    public int getShoeID() { return shoeID; }
    public void setShoeID(int shoeID) { this.shoeID = shoeID; }

    public int getSizeID() { return sizeID; }
    public void setSizeID(int sizeID) { this.sizeID = sizeID; }

    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }

    public int getSizeNumber() { return sizeNumber; }
    public void setSizeNumber(int sizeNumber) { this.sizeNumber = sizeNumber; }

    public String getShoeName() { return shoeName; }
    public void setShoeName(String shoeName) { this.shoeName = shoeName; }

    @Override
    public String toString() {
        return "ShoeSize{" +
                "shoeID=" + shoeID +
                ", sizeID=" + sizeID +
                ", stockQuantity=" + stockQuantity +
                '}';
    }
}
