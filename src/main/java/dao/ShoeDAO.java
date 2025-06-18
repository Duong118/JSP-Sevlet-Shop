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
            stmt.setString(4, shoe.getImage());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            AppLogger.error("ShoeDAO", "addShoe", "Error adding shoe: " + shoe.getName(), e);
            return false;
        }
    }

