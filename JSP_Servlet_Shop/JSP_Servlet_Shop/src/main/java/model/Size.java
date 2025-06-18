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


    public int getSizeNumber() { return sizeNumber; }
    public void setSizeNumber(int sizeNumber) { this.sizeNumber = sizeNumber; }

    @Override
    public String toString() {
        return "Size{" +
                "sizeID=" + sizeID +
                ", sizeNumber=" + sizeNumber +
                '}';
    }
}
