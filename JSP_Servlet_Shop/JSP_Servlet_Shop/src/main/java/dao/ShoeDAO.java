package dao;

import model.Shoe;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoeDAO {

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
