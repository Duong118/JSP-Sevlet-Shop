package model;

import java.io.Serializable;

public class Size implements Serializable {
    private int sizeID;
    private int sizeNumber;

    public Size() {}

    public Size(int sizeNumber) {
        this.sizeNumber = sizeNumber;
    }

    public Size(int sizeID, int sizeNumber) {
        this.sizeID = sizeID;
        this.sizeNumber = sizeNumber;
    }    // Getters and Setters
    public int getSizeID() { return sizeID; }
    public void setSizeID(int sizeID) { this.sizeID = sizeID; }
    
    // Alternative method names for compatibility
    public int getSizeId() { return sizeID; }
    public void setSizeId(int sizeId) { this.sizeID = sizeId; }

    public int getSizeNumber() { return sizeNumber; }
    public void setSizeNumber(int sizeNumber) { this.sizeNumber = sizeNumber; }
    
    // Add sizeValue for compatibility (treat as string representation)
    public String getSizeValue() { return String.valueOf(sizeNumber); }
    public void setSizeValue(String sizeValue) { 
        try {
            this.sizeNumber = Integer.parseInt(sizeValue);
        } catch (NumberFormatException e) {
            this.sizeNumber = 0;
        }
    }

    @Override
    public String toString() {
        return "Size{" +
                "sizeID=" + sizeID +
                ", sizeNumber=" + sizeNumber +
                '}';
    }
}
