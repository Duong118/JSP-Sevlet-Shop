package model;

import java.io.Serializable;

public class Category implements Serializable {
    private int categoryID;
    private String categoryName;

    public Category() {}

    public Category(String categoryName) {
        this.categoryName = categoryName;
    }

    public Category(int categoryID, String categoryName) {
        this.categoryID = categoryID;
        this.categoryName = categoryName;
    }

    // Getters and Setters
    public int getCategoryID() { return categoryID; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
    
    // Alternative method names for compatibility
    public int getCategoryId() { return categoryID; }
    public void setCategoryId(int categoryId) { this.categoryID = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    // Add description for compatibility (not in DB but used in servlet)
    public String getDescription() { return categoryName; }
    public void setDescription(String description) { this.categoryName = description; }

    @Override
    public String toString() {
        return "Category{" +
                "categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
