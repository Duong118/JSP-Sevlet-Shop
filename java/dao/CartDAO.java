package dao;

import model.CartItem;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CartDAO {

    public boolean addToCart(CartItem cartItem) {
        String sql = "INSERT INTO CartItem (UserID, ShoeID, SizeID, Quantity) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartItem.getUserID());
            stmt.setInt(2, cartItem.getShoeID());
            stmt.setInt(3, cartItem.getSizeID());            stmt.setInt(4, cartItem.getQuantity());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "addToCart", "Error adding item to cart", e);
            return false;
        }
    }

    public boolean updateCartItem(CartItem cartItem) {
        String sql = "UPDATE CartItem SET Quantity = ? WHERE UserID = ? AND ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, cartItem.getQuantity());
            stmt.setInt(2, cartItem.getUserID());
            stmt.setInt(3, cartItem.getShoeID());            stmt.setInt(4, cartItem.getSizeID());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "updateCartItem", "Error updating cart item", e);
            return false;
        }
    }

    public boolean updateCartItemQuantity(int userID, int shoeID, int sizeID, int quantity) {
        if (quantity <= 0) {
            return removeFromCart(userID, shoeID, sizeID);
        }
        
        String sql = "UPDATE CartItem SET Quantity = ? WHERE UserID = ? AND ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, quantity);
            stmt.setInt(2, userID);
            stmt.setInt(3, shoeID);            stmt.setInt(4, sizeID);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "updateCartItemQuantity", "Error updating cart item quantity", e);
            return false;
        }
    }

    public boolean removeFromCart(int userID, int shoeID, int sizeID) {
        String sql = "DELETE FROM CartItem WHERE UserID = ? AND ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            stmt.setInt(2, shoeID);            stmt.setInt(3, sizeID);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "removeFromCart", "Error removing item from cart", e);
            return false;
        }
    }

    public boolean clearCart(int userID) {
        String sql = "DELETE FROM CartItem WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setInt(1, userID);
            return stmt.executeUpdate() >= 0;
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "clearCart", "Error clearing cart for user: " + userID, e);
            return false;
        }
    }

    public List<CartItem> getCartItems(int userID) {
        List<CartItem> cartItems = new ArrayList<>();
        String sql = "SELECT ci.*, s.Name as ShoeName, s.Price as ShoePrice, s.Image as ShoeImage, " +
                    "sz.SizeNumber, ss.StockQuantity " +
                    "FROM CartItem ci " +
                    "LEFT JOIN Shoe s ON ci.ShoeID = s.ShoeID " +
                    "LEFT JOIN Size sz ON ci.SizeID = sz.SizeID " +
                    "LEFT JOIN ShoeSize ss ON ci.ShoeID = ss.ShoeID AND ci.SizeID = ss.SizeID " +
                    "WHERE ci.UserID = ? ORDER BY ci.CartItemID";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
              while (rs.next()) {
                cartItems.add(mapResultSetToCartItem(rs));
            }
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "getCartItems", "Error retrieving cart items for user: " + userID, e);
        }
        
        return cartItems;
    }    public List<CartItem> getCartByUserId(int userId) {
        return getCartItems(userId);
    }

    public CartItem getCartItem(int userID, int shoeID, int sizeID) {
        String sql = "SELECT ci.*, s.Name as ShoeName, s.Price as ShoePrice, s.Image as ShoeImage, " +
                    "sz.SizeNumber, ss.StockQuantity " +
                    "FROM CartItem ci " +
                    "LEFT JOIN Shoe s ON ci.ShoeID = s.ShoeID " +
                    "LEFT JOIN Size sz ON ci.SizeID = sz.SizeID " +
                    "LEFT JOIN ShoeSize ss ON ci.ShoeID = ss.ShoeID AND ci.SizeID = ss.SizeID " +
                    "WHERE ci.UserID = ? AND ci.ShoeID = ? AND ci.SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            stmt.setInt(2, shoeID);
            stmt.setInt(3, sizeID);
            ResultSet rs = stmt.executeQuery();
              if (rs.next()) {
                return mapResultSetToCartItem(rs);
            }
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "getCartItem", "Error retrieving cart item", e);
        }
        
        return null;
    }

    public boolean isItemInCart(int userID, int shoeID, int sizeID) {
        String sql = "SELECT COUNT(*) FROM CartItem WHERE UserID = ? AND ShoeID = ? AND SizeID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            stmt.setInt(2, shoeID);
            stmt.setInt(3, sizeID);
            ResultSet rs = stmt.executeQuery();
              if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "isItemInCart", "Error checking if item is in cart", e);
        }
        
        return false;
    }

    public int getCartItemCount(int userID) {
        String sql = "SELECT COUNT(*) FROM CartItem WHERE UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
              if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "getCartItemCount", "Error getting cart item count for user: " + userID, e);
        }
        
        return 0;
    }

    public double getCartTotal(int userID) {
        String sql = "SELECT SUM(ci.Quantity * s.Price) as Total " +
                    "FROM CartItem ci " +
                    "LEFT JOIN Shoe s ON ci.ShoeID = s.ShoeID " +
                    "WHERE ci.UserID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
              if (rs.next()) {
                return rs.getDouble("Total");
            }
            
        } catch (SQLException e) {
            AppLogger.error("CartDAO", "getCartTotal", "Error calculating cart total for user: " + userID, e);
        }
        
        return 0.0;
    }

    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setCartItemID(rs.getInt("CartItemID"));
        cartItem.setUserID(rs.getInt("UserID"));
        cartItem.setShoeID(rs.getInt("ShoeID"));
        cartItem.setSizeID(rs.getInt("SizeID"));
        cartItem.setQuantity(rs.getInt("Quantity"));
        cartItem.setShoeName(rs.getString("ShoeName"));
        cartItem.setShoePrice(rs.getDouble("ShoePrice"));
        cartItem.setShoeImage(rs.getString("ShoeImage"));
        cartItem.setSizeNumber(rs.getInt("SizeNumber"));
        cartItem.setStockQuantity(rs.getInt("StockQuantity"));
        return cartItem;
    }
}
