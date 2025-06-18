package dao;

import model.Size;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SizeDAO {

    public boolean addSize(Size size) {
        String sql = "INSERT INTO Size (SizeNumber) VALUES (?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, size.getSizeNumber());
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("SizeDAO", "addSize", "Error adding size: " + size.getSizeNumber(), e);
            return false;
        }
    }

    public Size getSizeById(int sizeID) {
        String sql = "SELECT * FROM Size WHERE SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sizeID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToSize(rs);
            }
              } catch (SQLException e) {
            AppLogger.error("SizeDAO", "getSizeById", "Error retrieving size by ID: " + sizeID, e);
        }
        
        return null;
    }

    public List<Size> getAllSizes() {
        List<Size> sizes = new ArrayList<>();
        String sql = "SELECT * FROM Size ORDER BY SizeNumber";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                sizes.add(mapResultSetToSize(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("SizeDAO", "getAllSizes", "Error retrieving all sizes", e);
        }
        
        return sizes;
    }

    public boolean updateSize(Size size) {
        String sql = "UPDATE Size SET SizeNumber = ? WHERE SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, size.getSizeNumber());
            stmt.setInt(2, size.getSizeID());
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("SizeDAO", "updateSize", "Error updating size ID: " + size.getSizeID() + " to " + size.getSizeNumber(), e);
            return false;
        }
    }

    public boolean deleteSize(int sizeID) {
        String sql = "DELETE FROM Size WHERE SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sizeID);
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("SizeDAO", "deleteSize", "Error deleting size ID: " + sizeID, e);
            return false;
        }
    }

    public boolean isSizeNumberExists(int sizeNumber) {
        String sql = "SELECT COUNT(*) FROM Size WHERE SizeNumber = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sizeNumber);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
              } catch (SQLException e) {
            AppLogger.error("SizeDAO", "isSizeNumberExists", "Error checking if size number exists: " + sizeNumber, e);
        }
        
        return false;
    }

    public boolean isSizeNumberExists(int sizeNumber, int excludeSizeID) {
        String sql = "SELECT COUNT(*) FROM Size WHERE SizeNumber = ? AND SizeID != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, sizeNumber);
            stmt.setInt(2, excludeSizeID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
              } catch (SQLException e) {
            AppLogger.error("SizeDAO", "isSizeNumberExists", "Error checking if size number exists (excluding ID): " + sizeNumber + ", exclude ID: " + excludeSizeID, e);
        }
        
        return false;
    }

    private Size mapResultSetToSize(ResultSet rs) throws SQLException {
        Size size = new Size();
        size.setSizeID(rs.getInt("SizeID"));
        size.setSizeNumber(rs.getInt("SizeNumber"));
        return size;
    }

