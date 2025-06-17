package dao;

import model.User;
import util.DatabaseConnection;
import util.PasswordUtil;
import util.AppLogger;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    public boolean registerUser(User user) {
        String sql = "INSERT INTO [User] (Username, Password, Email, FullName, Address, Phone, Role, Status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, user.getUsername());
            stmt.setString(2, PasswordUtil.hashPassword(user.getPassword()));
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFullName());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.getRole() != null ? user.getRole() : "User");
            stmt.setString(8, user.getStatus() != null ? user.getStatus() : "Active");
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User loginUser(String username, String password) {
        String sql = "SELECT * FROM [User] WHERE Username = ? AND Status = 'Active'";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String hashedPassword = rs.getString("Password");
                if (PasswordUtil.verifyPassword(password, hashedPassword)) {
                    return mapResultSetToUser(rs);
                }
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public User getUserById(int userID) {
        String sql = "SELECT * FROM [User] WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public User getUserByEmail(String email) {
        String sql = "SELECT * FROM [User] WHERE Email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }    public boolean updateUser(User user) {
        String sql = "UPDATE [User] SET FullName = ?, Email = ?, Address = ?, Phone = ? WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setString(1, user.getFullName());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getAddress());
            stmt.setString(4, user.getPhone());
            stmt.setInt(5, user.getUserID());
            
            AppLogger.debug("UserDAO", "updateUser", "Updating user - ID: " + user.getUserID() + 
                             ", Email: " + user.getEmail() + 
                             ", FullName: " + user.getFullName());
            
            int rowsAffected = stmt.executeUpdate();
            AppLogger.debug("UserDAO", "updateUser", "Rows affected: " + rowsAffected);
            
            return rowsAffected > 0;
            
        } catch (SQLException e) {
            AppLogger.error("UserDAO", "updateUser", "Error updating user: " + user.getUserID(), e);
            return false;
        }
    }

    public boolean updatePassword(int userID, String newPassword) {
        String sql = "UPDATE [User] SET Password = ? WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, PasswordUtil.hashPassword(newPassword));
            stmt.setInt(2, userID);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM [User] ORDER BY UserID";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    public List<User> getUsersByRole(String role) {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM [User] WHERE Role = ? ORDER BY UserID";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return users;
    }

    public boolean updateUserStatus(int userID, String status) {
        String sql = "UPDATE [User] SET Status = ? WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            stmt.setInt(2, userID);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateUserRole(int userID, String role) {
        String sql = "UPDATE [User] SET Role = ? WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, role);
            stmt.setInt(2, userID);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteUser(int userID) {
        String sql = "DELETE FROM [User] WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isUsernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM [User] WHERE Username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }    public boolean isEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM [User] WHERE Email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
              if (rs.next()) {
                int count = rs.getInt(1);
                AppLogger.debug("UserDAO", "isEmailExists", "Email '" + email + "' exists count: " + count);
                return count > 0;
            }
            
        } catch (SQLException e) {
            AppLogger.error("UserDAO", "isEmailExists", "Error checking email exists for: " + email, e);
        }
        
        return false;
    }

    // Check if email exists for a different user (excluding current user)
    public boolean isEmailExistsForOtherUser(String email, int currentUserId) {
        String sql = "SELECT COUNT(*) FROM [User] WHERE Email = ? AND UserID != ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setInt(2, currentUserId);
            ResultSet rs = stmt.executeQuery();
              if (rs.next()) {
                int count = rs.getInt(1);
                AppLogger.debug("UserDAO", "isEmailExistsForOtherUser", "Email '" + email + "' exists for other users (excluding ID " + currentUserId + "): " + count);
                return count > 0;
            }
            
        } catch (SQLException e) {
            AppLogger.error("UserDAO", "isEmailExistsForOtherUser", "Error checking email exists for other users: " + email, e);
        }
        
        return false;
    }

    public int getTotalUsers() {
        String sql = "SELECT COUNT(*) FROM [User]";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM [User] WHERE Username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(User user) {
        return registerUser(user);
    }

    public List<User> searchUsers(String search) {
        String sql = "SELECT * FROM [User] WHERE Username LIKE ? OR Email LIKE ? OR FullName LIKE ?";
        List<User> users = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            String searchPattern = "%" + search + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                users.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }    // Debug method to list all users with their emails
    public void debugListAllUsers() {
        String sql = "SELECT UserID, Username, Email FROM [User] ORDER BY UserID";
        AppLogger.debug("UserDAO", "debugListAllUsers", "=== ALL USERS IN DATABASE ===");
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                AppLogger.debug("UserDAO", "debugListAllUsers", "ID: " + rs.getInt("UserID") + 
                                 ", Username: " + rs.getString("Username") + 
                                 ", Email: " + rs.getString("Email"));
            }
            
        } catch (SQLException e) {
            AppLogger.error("UserDAO", "debugListAllUsers", "Error listing users", e);
        }
        
        AppLogger.debug("UserDAO", "debugListAllUsers", "=== END USER LIST ===");
    }

    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserID(rs.getInt("UserID"));
        user.setUsername(rs.getString("Username"));
        user.setPassword(rs.getString("Password"));
        user.setEmail(rs.getString("Email"));
        user.setFullName(rs.getString("FullName"));
        user.setAddress(rs.getString("Address"));
        user.setPhone(rs.getString("Phone"));
        user.setRole(rs.getString("Role"));
        user.setStatus(rs.getString("Status"));
        return user;
    }
}
