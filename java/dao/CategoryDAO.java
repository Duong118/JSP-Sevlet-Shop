package dao;

import model.Category;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    public boolean addCategory(Category category) {
        String sql = "INSERT INTO Category (CategoryName) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setString(1, category.getCategoryName());
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("CategoryDAO", "addCategory", "Error adding category: " + category.getCategoryName(), e);
            return false;
        }
    }

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

    public boolean updateCategory(Category category) {
        String sql = "UPDATE Category SET CategoryName = ? WHERE CategoryID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, category.getCategoryName());            stmt.setInt(2, category.getCategoryID());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("CategoryDAO", "updateCategory", "Error updating category: " + category.getCategoryName(), e);
            return false;
        }
    }

    public boolean deleteCategory(int categoryID) {
        String sql = "DELETE FROM Category WHERE CategoryID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setInt(1, categoryID);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("CategoryDAO", "deleteCategory", "Error deleting category with ID: " + categoryID, e);
            return false;
        }
    }

    public boolean isCategoryNameExists(String categoryName) {
        String sql = "SELECT COUNT(*) FROM Category WHERE CategoryName = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setString(1, categoryName);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            AppLogger.error("CategoryDAO", "isCategoryNameExists", "Error checking category name existence: " + categoryName, e);
        }
        
        return false;
    }

    public boolean isCategoryNameExists(String categoryName, int excludeCategoryID) {
        String sql = "SELECT COUNT(*) FROM Category WHERE CategoryName = ? AND CategoryID != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, categoryName);            stmt.setInt(2, excludeCategoryID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            AppLogger.error("CategoryDAO", "isCategoryNameExists", "Error checking category name existence with exclusion: " + categoryName, e);
        }
        
        return false;
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
