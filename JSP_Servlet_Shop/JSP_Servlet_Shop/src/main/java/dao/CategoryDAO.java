package dao;

import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {


    public Category getCategoryById(int categoryID) {
        String sql = "SELECT * FROM Category WHERE CategoryID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryID);
            ResultSet rs = stmt.executeQuery();
              if (rs.next()) {
                return mapResultSetToCategory(rs);
            }
            
        } catch (SQLException e) {
            AppLogger.error("CategoryDAO", "getCategoryById", "Error retrieving category with ID: " + categoryID, e);
        }
        
        return null;
    }

    public List<Category> getAllCategories() {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM Category ORDER BY CategoryName";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
              while (rs.next()) {
                categories.add(mapResultSetToCategory(rs));
            }
            
        } catch (SQLException e) {
            AppLogger.error("CategoryDAO", "getAllCategories", "Error retrieving all categories", e);
        }
        
        return categories;
    }





    private Category mapResultSetToCategory(ResultSet rs) throws SQLException {
        Category category = new Category();
        category.setCategoryID(rs.getInt("CategoryID"));
        category.setCategoryName(rs.getString("CategoryName"));
        return category;
    }

    public boolean createCategory(Category category) {
        return addCategory(category);
    }
}
