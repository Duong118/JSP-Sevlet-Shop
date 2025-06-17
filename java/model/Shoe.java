package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Shoe implements Serializable {
    private int shoeID;
    private String name;
    private String description;
    private BigDecimal price;
    private String image;
    private int categoryID;
    private String categoryName; // For displaying purpose
    
    // Additional fields for compatibility (not in DB but used by servlet)
    private String brand;
    private String color;
    private String gender;

    public Shoe() {}

    public Shoe(String name, String description, BigDecimal price, String image, int categoryID) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.categoryID = categoryID;
    }

    // Getters and Setters
    public int getShoeID() { return shoeID; }
    public void setShoeID(int shoeID) { this.shoeID = shoeID; }
    
    // Alternative method names for compatibility
    public int getShoeId() { return shoeID; }
    public void setShoeId(int shoeId) { this.shoeID = shoeId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    // Alternative method names for shoe name
    public String getShoeName() { return name; }
    public void setShoeName(String shoeName) { this.name = shoeName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    
    // Overload for double price
    public void setPrice(double price) { this.price = BigDecimal.valueOf(price); }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }
    
    // Alternative method names for image
    public String getImageUrl() { return image; }
    public void setImageUrl(String imageUrl) { this.image = imageUrl; }

    public int getCategoryID() { return categoryID; }
    public void setCategoryID(int categoryID) { this.categoryID = categoryID; }
    
    // Alternative method names for categoryId
    public int getCategoryId() { return categoryID; }
    public void setCategoryId(int categoryId) { this.categoryID = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }
    
    // Additional fields for compatibility
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }
    
    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }
    
    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    @Override
    public String toString() {
        return "Shoe{" +
                "shoeID=" + shoeID +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", image='" + image + '\'' +
                ", categoryID=" + categoryID +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
