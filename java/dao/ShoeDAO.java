package dao;

import model.Shoe;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoeDAO {

    public boolean addShoe(Shoe shoe) {
        String sql = "INSERT INTO Shoe (Name, Description, Price, Image, CategoryID) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, shoe.getName());
            stmt.setString(2, shoe.getDescription());
            stmt.setBigDecimal(3, shoe.getPrice());
            stmt.setString(4, shoe.getImage());            stmt.setInt(5, shoe.getCategoryID());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "addShoe", "Error adding shoe: " + shoe.getName(), e);
            return false;
        }
    }

    public Shoe getShoeById(int shoeID) {
        String sql = "SELECT s.*, c.CategoryName FROM Shoe s " +
                    "LEFT JOIN Category c ON s.CategoryID = c.CategoryID " +
                    "WHERE s.ShoeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToShoe(rs);
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "getShoeById", "Error retrieving shoe by ID: " + shoeID, e);
        }
        
        return null;
    }

    public List<Shoe> getAllShoes() {
        List<Shoe> shoes = new ArrayList<>();
        String sql = "SELECT s.*, c.CategoryName FROM Shoe s " +
                    "LEFT JOIN Category c ON s.CategoryID = c.CategoryID " +
                    "ORDER BY s.ShoeID DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                shoes.add(mapResultSetToShoe(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "getAllShoes", "Error retrieving all shoes", e);
        }
        
        return shoes;
    }

    public List<Shoe> getShoesByCategory(int categoryID) {
        List<Shoe> shoes = new ArrayList<>();
        String sql = "SELECT s.*, c.CategoryName FROM Shoe s " +
                    "LEFT JOIN Category c ON s.CategoryID = c.CategoryID " +
                    "WHERE s.CategoryID = ? ORDER BY s.ShoeID DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, categoryID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                shoes.add(mapResultSetToShoe(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "getShoesByCategory", "Error retrieving shoes by category ID: " + categoryID, e);
        }
        
        return shoes;
    }

    public List<Shoe> searchShoes(String keyword) {
        List<Shoe> shoes = new ArrayList<>();
        String sql = "SELECT s.*, c.CategoryName FROM Shoe s " +
                    "LEFT JOIN Category c ON s.CategoryID = c.CategoryID " +
                    "WHERE s.Name LIKE ? OR s.Description LIKE ? OR c.CategoryName LIKE ? " +
                    "ORDER BY s.ShoeID DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + keyword + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                shoes.add(mapResultSetToShoe(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "searchShoes", "Error searching shoes with keyword: " + keyword, e);
        }
        
        return shoes;
    }

    public List<Shoe> getShoesByPriceRange(double minPrice, double maxPrice) {
        List<Shoe> shoes = new ArrayList<>();
        String sql = "SELECT s.*, c.CategoryName FROM Shoe s " +
                    "LEFT JOIN Category c ON s.CategoryID = c.CategoryID " +
                    "WHERE s.Price BETWEEN ? AND ? ORDER BY s.ShoeID DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setDouble(1, minPrice);
            stmt.setDouble(2, maxPrice);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                shoes.add(mapResultSetToShoe(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "getShoesByPriceRange", "Error retrieving shoes by price range: " + minPrice + "-" + maxPrice, e);
        }
        
        return shoes;
    }    public List<Shoe> filterShoes(String keyword, Integer categoryID, Double minPrice, Double maxPrice) {
        return filterShoes(keyword, categoryID, minPrice, maxPrice, null);
    }
    
    public List<Shoe> filterShoes(String keyword, Integer categoryID, Double minPrice, Double maxPrice, String sort) {
        List<Shoe> shoes = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT s.*, c.CategoryName FROM Shoe s ");
        sql.append("LEFT JOIN Category c ON s.CategoryID = c.CategoryID WHERE 1=1 ");
        
        List<Object> params = new ArrayList<>();
        
        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append("AND (s.Name LIKE ? OR s.Description LIKE ? OR c.CategoryName LIKE ?) ");
            String searchPattern = "%" + keyword.trim() + "%";
            params.add(searchPattern);
            params.add(searchPattern);
            params.add(searchPattern);
        }
        
        if (categoryID != null && categoryID > 0) {
            sql.append("AND s.CategoryID = ? ");
            params.add(categoryID);
        }
        
        if (minPrice != null) {
            sql.append("AND s.Price >= ? ");
            params.add(minPrice);
        }
        
        if (maxPrice != null) {
            sql.append("AND s.Price <= ? ");
            params.add(maxPrice);
        }
        
        // Add sorting logic
        if (sort != null && !sort.trim().isEmpty()) {
            switch (sort) {
                case "name":
                    sql.append("ORDER BY s.Name ASC");
                    break;
                case "price_asc":
                    sql.append("ORDER BY s.Price ASC");
                    break;
                case "price_desc":
                    sql.append("ORDER BY s.Price DESC");
                    break;
                default:
                    sql.append("ORDER BY s.ShoeID DESC");
                    break;
            }
        } else {
            sql.append("ORDER BY s.ShoeID DESC");
        }
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                shoes.add(mapResultSetToShoe(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "filterShoes", "Error filtering shoes with criteria", e);
        }
        
        return shoes;
    }

    public boolean updateShoe(Shoe shoe) {
        String sql = "UPDATE Shoe SET Name = ?, Description = ?, Price = ?, Image = ?, CategoryID = ? WHERE ShoeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, shoe.getName());
            stmt.setString(2, shoe.getDescription());
            stmt.setBigDecimal(3, shoe.getPrice());
            stmt.setString(4, shoe.getImage());
            stmt.setInt(5, shoe.getCategoryID());
            stmt.setInt(6, shoe.getShoeID());
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "updateShoe", "Error updating shoe ID: " + shoe.getShoeID(), e);
            return false;
        }
    }

    public boolean deleteShoe(int shoeID) {
        String sql = "DELETE FROM Shoe WHERE ShoeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "deleteShoe", "Error deleting shoe ID: " + shoeID, e);
            return false;
        }
    }

    public int getShoeCount() {
        String sql = "SELECT COUNT(*) FROM Shoe";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "getShoeCount", "Error retrieving shoe count", e);
        }
        
        return 0;
    }

    public int getTotalShoes() {
        String sql = "SELECT COUNT(*) FROM Shoe";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }        } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "getTotalShoes", "Error retrieving total shoes count", e);
        }
        return 0;
    }

    public boolean createShoe(Shoe shoe) {
        return addShoe(shoe);
    }

    public boolean updateStock(int shoeId, int sizeId, int quantity) {
        String sql = "UPDATE ShoeSize SET StockQuantity = StockQuantity - ? WHERE ShoeID = ? AND SizeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, quantity);
            stmt.setInt(2, shoeId);
            stmt.setInt(3, sizeId);
            return stmt.executeUpdate() > 0;        } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "updateStock", "Error updating stock for shoe ID: " + shoeId + ", size ID: " + sizeId, e);
            return false;
        }
    }

    public int getAvailableStock(int shoeId, int sizeId) {
        String sql = "SELECT StockQuantity FROM ShoeSize WHERE ShoeID = ? AND SizeID = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, shoeId);
            stmt.setInt(2, sizeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("StockQuantity");
            }        } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "getAvailableStock", "Error retrieving stock for shoe ID: " + shoeId + ", size ID: " + sizeId, e);
        }
        return 0;
    }

    private Shoe mapResultSetToShoe(ResultSet rs) throws SQLException {
        Shoe shoe = new Shoe();
        shoe.setShoeID(rs.getInt("ShoeID"));
        shoe.setName(rs.getString("Name"));
        shoe.setDescription(rs.getString("Description"));
        shoe.setPrice(rs.getBigDecimal("Price"));
        shoe.setImage(rs.getString("Image"));
        shoe.setCategoryID(rs.getInt("CategoryID"));
        shoe.setCategoryName(rs.getString("CategoryName"));
        return shoe;
    }
}
