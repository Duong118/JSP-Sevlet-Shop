package dao;

import model.Order;
import model.OrderDetail;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class OrderDAO {

    public int createOrder(Order order) {
        String sql = "INSERT INTO [Order] (UserID, Status, TotalAmount, ShippingAddress) OUTPUT INSERTED.OrderID VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, order.getUserID());
            stmt.setString(2, order.getStatus());
            stmt.setBigDecimal(3, order.getTotalAmount());
            stmt.setString(4, order.getShippingAddress());
              ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
            
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "createOrder", "Error creating order for user: " + order.getUserID(), e);
        }
        
        return -1;
    }

    public Order getOrderById(int orderID) {
        String sql = "SELECT o.*, u.Username, u.Email as UserEmail, u.FullName as UserName " +
                    "FROM [Order] o " +
                    "LEFT JOIN [User] u ON o.UserID = u.UserID " +
                    "WHERE o.OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                // Get order details
                OrderDetailDAO orderDetailDAO = new OrderDetailDAO();                order.setOrderDetails(orderDetailDAO.getOrderDetailsByOrderId(orderID));
                return order;
            }
            
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getOrderById", "Error retrieving order with ID: " + orderID, e);
        }
        
        return null;
    }

    public List<Order> getOrdersByUser(int userID) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.Username, u.Email as UserEmail, u.FullName as UserName " +
                    "FROM [Order] o " +
                    "LEFT JOIN [User] u ON o.UserID = u.UserID " +
                    "WHERE o.UserID = ? ORDER BY o.OrderDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setInt(1, userID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getOrdersByUser", "Error retrieving orders for user: " + userID, e);
        }
        
        return orders;
    }

    public List<Order> getAllOrders() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.Username, u.Email as UserEmail, u.FullName as UserName " +
                    "FROM [Order] o " +
                    "LEFT JOIN [User] u ON o.UserID = u.UserID " +
                    "ORDER BY o.OrderDate DESC";
          try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getAllOrders", "Error retrieving all orders", e);
        }
        
        return orders;
    }

    public List<Order> getOrdersByStatus(String status) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT o.*, u.Username, u.Email as UserEmail, u.FullName as UserName " +
                    "FROM [Order] o " +
                    "LEFT JOIN [User] u ON o.UserID = u.UserID " +
                    "WHERE o.Status = ? ORDER BY o.OrderDate DESC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
              while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
            
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getOrdersByStatus", "Error retrieving orders with status: " + status, e);
        }
        
        return orders;
    }

    public boolean updateOrderStatus(int orderID, String status) {
        String sql = "UPDATE [Order] SET Status = ? WHERE OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setString(1, status);
            stmt.setInt(2, orderID);
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "updateOrderStatus", "Error updating order status for order: " + orderID, e);
            return false;
        }
    }

    public boolean deleteOrder(int orderID) {
        String sql = "DELETE FROM [Order] WHERE OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
              stmt.setInt(1, orderID);
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "deleteOrder", "Error deleting order with ID: " + orderID, e);
            return false;
        }
    }

    public int getOrderCount() {
        String sql = "SELECT COUNT(*) FROM [Order]";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
              } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getOrderCount", "Error retrieving total order count", e);
        }
        
        return 0;
    }

    public int getOrderCountByStatus(String status) {
        String sql = "SELECT COUNT(*) FROM [Order] WHERE Status = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return rs.getInt(1);
            }
              } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getOrderCountByStatus", "Error retrieving order count for status: " + status, e);
        }
        
        return 0;
    }

    public int getTotalOrders() {
        String sql = "SELECT COUNT(*) FROM [Order]";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getTotalOrders", "Error retrieving total orders count", e);
        }
        return 0;
    }

    public BigDecimal getTotalRevenue() {
        String sql = "SELECT ISNULL(SUM(TotalAmount), 0) FROM [Order] WHERE Status = 'Delivered'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getTotalRevenue", "Error calculating total revenue from delivered orders", e);
        }
        return BigDecimal.ZERO;
    }    public List<Order> getRecentOrders(int limit) {
        String sql = "SELECT TOP " + limit + " o.*, u.Username, u.Email as UserEmail, u.FullName as UserName " +
                    "FROM [Order] o " +
                    "LEFT JOIN [User] u ON o.UserID = u.UserID " +
                    "ORDER BY o.OrderDate DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getRecentOrders", "Error retrieving recent orders with limit: " + limit, e);
        }
        return orders;
    }public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT o.*, u.Username, u.Email as UserEmail, u.FullName as UserName " +
                    "FROM [Order] o " +
                    "LEFT JOIN [User] u ON o.UserID = u.UserID " +
                    "WHERE o.UserID = ? ORDER BY o.OrderDate DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getOrdersByUserId", "Error retrieving orders for user ID: " + userId, e);
        }
        return orders;
    }

    public BigDecimal getMonthlyRevenue(int year, int month) {
        String sql = "SELECT ISNULL(SUM(TotalAmount), 0) FROM [Order] " +
                    "WHERE YEAR(OrderDate) = ? AND MONTH(OrderDate) = ? AND Status = 'Delivered'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, year);
            stmt.setInt(2, month);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getMonthlyRevenue", "Error calculating monthly revenue for " + year + "/" + month, e);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getDailyRevenue() {
        String sql = "SELECT ISNULL(SUM(TotalAmount), 0) FROM [Order] " +
                    "WHERE CAST(OrderDate AS DATE) = CAST(GETDATE() AS DATE) AND Status = 'Delivered'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getDailyRevenue", "Error calculating daily revenue", e);
        }
        return BigDecimal.ZERO;
    }

    public List<Map<String, Object>> getTopSellingShoes(int limit) {
        String sql = "SELECT TOP " + limit + " s.Name, SUM(od.Quantity) as TotalSold " +
                    "FROM OrderDetail od " +
                    "JOIN Shoe s ON od.ShoeID = s.ShoeID " +
                    "JOIN [Order] o ON od.OrderID = o.OrderID " +
                    "WHERE o.Status = 'Delivered' " +
                    "GROUP BY s.ShoeID, s.Name " +
                    "ORDER BY TotalSold DESC";
        List<Map<String, Object>> topShoes = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Map<String, Object> shoe = new HashMap<>();
                shoe.put("name", rs.getString("Name"));
                shoe.put("totalSold", rs.getInt("TotalSold"));
                topShoes.add(shoe);
            }        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getTopSellingShoes", "Error retrieving top selling shoes with limit: " + limit, e);
        }
        return topShoes;
    }    public List<Order> getRecentOrdersByStatus(String status, int limit) {
        String sql = "SELECT TOP " + limit + " o.*, u.Username, u.Email as UserEmail, u.FullName as UserName " +
                    "FROM [Order] o " +
                    "LEFT JOIN [User] u ON o.UserID = u.UserID " +
                    "WHERE o.Status = ? ORDER BY o.OrderDate DESC";
        List<Order> orders = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, status);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            AppLogger.error("OrderDAO", "getRecentOrdersByStatus", "Error retrieving recent orders by status '" + status + "' with limit: " + limit, e);
        }
        return orders;
    }

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setOrderID(rs.getInt("OrderID"));
        order.setUserID(rs.getInt("UserID"));
        order.setOrderDate(rs.getTimestamp("OrderDate"));
        order.setStatus(rs.getString("Status"));
        order.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        order.setShippingAddress(rs.getString("ShippingAddress"));
        order.setUserName(rs.getString("Username"));
        order.setUserEmail(rs.getString("UserEmail"));
        return order;
    }
}
