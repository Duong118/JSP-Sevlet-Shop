package dao;

import model.ShoeSize;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoeSizeDAO {

    public boolean addShoeSize(ShoeSize shoeSize) {
        String sql = "INSERT INTO ShoeSize (ShoeID, SizeID, StockQuantity) VALUES (?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeSize.getShoeID());
            stmt.setInt(2, shoeSize.getSizeID());
            stmt.setInt(3, shoeSize.getStockQuantity());
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "addShoeSize", "Error adding shoe size for shoe ID: " + shoeSize.getShoeID() + ", size ID: " + shoeSize.getSizeID(), e);
            return false;
        }
    }

    public ShoeSize getShoeSize(int shoeID, int sizeID) {
        String sql = "SELECT ss.*, sz.SizeNumber, s.Name as ShoeName FROM ShoeSize ss " +
                    "LEFT JOIN Size sz ON ss.SizeID = sz.SizeID " +
                    "LEFT JOIN Shoe s ON ss.ShoeID = s.ShoeID " +
                    "WHERE ss.ShoeID = ? AND ss.SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            stmt.setInt(2, sizeID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToShoeSize(rs);
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "getShoeSize", "Error retrieving shoe size for shoe ID: " + shoeID + ", size ID: " + sizeID, e);
        }
        
        return null;
    }

    public List<ShoeSize> getSizesByShoe(int shoeID) {
        List<ShoeSize> shoeSizes = new ArrayList<>();
        String sql = "SELECT ss.*, sz.SizeNumber, s.Name as ShoeName FROM ShoeSize ss " +
                    "LEFT JOIN Size sz ON ss.SizeID = sz.SizeID " +
                    "LEFT JOIN Shoe s ON ss.ShoeID = s.ShoeID " +
                    "WHERE ss.ShoeID = ? ORDER BY sz.SizeNumber";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                shoeSizes.add(mapResultSetToShoeSize(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "getSizesByShoe", "Error retrieving sizes for shoe ID: " + shoeID, e);
        }
        
        return shoeSizes;
    }

    public List<ShoeSize> getShoesBySize(int sizeID) {
        List<ShoeSize> shoeSizes = new ArrayList<>();
        String sql = "SELECT ss.*, sz.SizeNumber, s.Name as ShoeName FROM ShoeSize ss " +
                    "LEFT JOIN Size sz ON ss.SizeID = sz.SizeID " +
                    "LEFT JOIN Shoe s ON ss.ShoeID = s.ShoeID " +
                    "WHERE ss.SizeID = ? ORDER BY s.Name";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sizeID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                shoeSizes.add(mapResultSetToShoeSize(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "getShoesBySize", "Error retrieving shoes for size ID: " + sizeID, e);
        }
        
        return shoeSizes;
    }

    public List<ShoeSize> getAllShoeSizes() {
        List<ShoeSize> shoeSizes = new ArrayList<>();
        String sql = "SELECT ss.*, sz.SizeNumber, s.Name as ShoeName FROM ShoeSize ss " +
                    "LEFT JOIN Size sz ON ss.SizeID = sz.SizeID " +
                    "LEFT JOIN Shoe s ON ss.ShoeID = s.ShoeID " +
                    "ORDER BY s.Name, sz.SizeNumber";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                shoeSizes.add(mapResultSetToShoeSize(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "getAllShoeSizes", "Error retrieving all shoe sizes", e);
        }
        
        return shoeSizes;
    }

    public boolean updateStock(int shoeID, int sizeID, int newStock) {
        String sql = "UPDATE ShoeSize SET StockQuantity = ? WHERE ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, newStock);
            stmt.setInt(2, shoeID);
            stmt.setInt(3, sizeID);
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "updateStock", "Error updating stock for shoe ID: " + shoeID + ", size ID: " + sizeID + " to " + newStock, e);
            return false;
        }
    }

    public boolean updateShoeSize(ShoeSize shoeSize) {
        String sql = "UPDATE ShoeSize SET StockQuantity = ? WHERE ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeSize.getStockQuantity());
            stmt.setInt(2, shoeSize.getShoeID());
            stmt.setInt(3, shoeSize.getSizeID());
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "updateShoeSize", "Error updating shoe size for shoe ID: " + shoeSize.getShoeID() + ", size ID: " + shoeSize.getSizeID(), e);
            return false;
        }
    }

    public boolean deleteShoeSize(int shoeID, int sizeID) {
        String sql = "DELETE FROM ShoeSize WHERE ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            stmt.setInt(2, sizeID);
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "deleteShoeSize", "Error deleting shoe size for shoe ID: " + shoeID + ", size ID: " + sizeID, e);
            return false;
        }
    }

    public boolean deleteSizesByShoe(int shoeID) {
        String sql = "DELETE FROM ShoeSize WHERE ShoeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            return stmt.executeUpdate() >= 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "deleteSizesByShoe", "Error deleting all sizes for shoe ID: " + shoeID, e);
            return false;
        }
    }

    public boolean deleteSizesBySize(int sizeID) {
        String sql = "DELETE FROM ShoeSize WHERE SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sizeID);
            return stmt.executeUpdate() >= 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "deleteSizesBySize", "Error deleting all shoes for size ID: " + sizeID, e);
            return false;
        }
    }

    public boolean isShoeSizeExists(int shoeID, int sizeID) {
        String sql = "SELECT COUNT(*) FROM ShoeSize WHERE ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            stmt.setInt(2, sizeID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "isShoeSizeExists", "Error checking if shoe size exists for shoe ID: " + shoeID + ", size ID: " + sizeID, e);
        }
        
        return false;
    }

    public int getStockQuantity(int shoeID, int sizeID) {
        String sql = "SELECT StockQuantity FROM ShoeSize WHERE ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, shoeID);
            stmt.setInt(2, sizeID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt("StockQuantity");
            }
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "getStockQuantity", "Error retrieving stock quantity for shoe ID: " + shoeID + ", size ID: " + sizeID, e);
        }
        
        return 0;
    }

    public boolean decreaseStock(int shoeID, int sizeID, int quantity) {
        String sql = "UPDATE ShoeSize SET StockQuantity = StockQuantity - ? WHERE ShoeID = ? AND SizeID = ? AND StockQuantity >= ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, shoeID);
            stmt.setInt(3, sizeID);
            stmt.setInt(4, quantity);
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "decreaseStock", "Error decreasing stock for shoe ID: " + shoeID + ", size ID: " + sizeID + " by " + quantity, e);
            return false;
        }
    }

    public boolean increaseStock(int shoeID, int sizeID, int quantity) {
        String sql = "UPDATE ShoeSize SET StockQuantity = StockQuantity + ? WHERE ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, shoeID);
            stmt.setInt(3, sizeID);
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("ShoeSizeDAO", "increaseStock", "Error increasing stock for shoe ID: " + shoeID + ", size ID: " + sizeID + " by " + quantity, e);
            return false;
        }
    }

    private ShoeSize mapResultSetToShoeSize(ResultSet rs) throws SQLException {
        ShoeSize shoeSize = new ShoeSize();
        shoeSize.setShoeID(rs.getInt("ShoeID"));
        shoeSize.setSizeID(rs.getInt("SizeID"));
        shoeSize.setStockQuantity(rs.getInt("StockQuantity"));
        shoeSize.setSizeNumber(rs.getInt("SizeNumber"));
        shoeSize.setShoeName(rs.getString("ShoeName"));
        return shoeSize;
    }
}
