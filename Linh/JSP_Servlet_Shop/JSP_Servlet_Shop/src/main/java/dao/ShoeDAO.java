package dao;

import model.Shoe;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ShoeDAO {
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

    