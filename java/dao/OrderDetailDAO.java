package dao;

import model.OrderDetail;
import util.DatabaseConnection;
import util.AppLogger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAO {

    public boolean addOrderDetail(OrderDetail orderDetail) {
        String sql = "INSERT INTO OrderDetail (OrderID, ShoeID, SizeID, Quantity, Price) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderDetail.getOrderID());
            stmt.setInt(2, orderDetail.getShoeID());
            stmt.setInt(3, orderDetail.getSizeID());
            stmt.setInt(4, orderDetail.getQuantity());            stmt.setBigDecimal(5, orderDetail.getPrice());
            
            return stmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            AppLogger.error("OrderDetailDAO", "addOrderDetail", "Error adding order detail", e);
            return false;
        }
    }

    public List<OrderDetail> getOrderDetailsByOrderId(int orderID) {
        List<OrderDetail> orderDetails = new ArrayList<>();
        String sql = "SELECT od.*, s.Name as ShoeName, s.Image as ShoeImage, sz.SizeNumber " +
                    "FROM OrderDetail od " +
                    "LEFT JOIN Shoe s ON od.ShoeID = s.ShoeID " +
                    "LEFT JOIN Size sz ON od.SizeID = sz.SizeID " +
                    "WHERE od.OrderID = ? ORDER BY od.OrderDetailID";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderID);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                orderDetails.add(mapResultSetToOrderDetail(rs));
            }
              } catch (SQLException e) {
            AppLogger.error("OrderDetailDAO", "getOrderDetailsByOrderId", "Error retrieving order details for order ID: " + orderID, e);
        }
        
        return orderDetails;
    }

    public OrderDetail getOrderDetailById(int orderDetailID) {
        String sql = "SELECT od.*, s.Name as ShoeName, s.Image as ShoeImage, sz.SizeNumber " +
                    "FROM OrderDetail od " +
                    "LEFT JOIN Shoe s ON od.ShoeID = s.ShoeID " +
                    "LEFT JOIN Size sz ON od.SizeID = sz.SizeID " +
                    "WHERE od.OrderDetailID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderDetailID);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                return mapResultSetToOrderDetail(rs);
            }
              } catch (SQLException e) {
            AppLogger.error("OrderDetailDAO", "getOrderDetailById", "Error retrieving order detail by ID: " + orderDetailID, e);
        }
        
        return null;
    }

    public boolean updateOrderDetail(OrderDetail orderDetail) {
        String sql = "UPDATE OrderDetail SET Quantity = ?, Price = ? WHERE OrderDetailID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderDetail.getQuantity());
            stmt.setBigDecimal(2, orderDetail.getPrice());
            stmt.setInt(3, orderDetail.getOrderDetailID());
            
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("OrderDetailDAO", "updateOrderDetail", "Error updating order detail ID: " + orderDetail.getOrderDetailID(), e);
            return false;
        }
    }

    public boolean deleteOrderDetail(int orderDetailID) {
        String sql = "DELETE FROM OrderDetail WHERE OrderDetailID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderDetailID);
            return stmt.executeUpdate() > 0;
              } catch (SQLException e) {
            AppLogger.error("OrderDetailDAO", "deleteOrderDetail", "Error deleting order detail ID: " + orderDetailID, e);
            return false;
        }
    }

    public boolean deleteOrderDetailsByOrderId(int orderID) {
        String sql = "DELETE FROM OrderDetail WHERE OrderID = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, orderID);
            return stmt.executeUpdate() >= 0;
              } catch (SQLException e) {
            AppLogger.error("OrderDetailDAO", "deleteOrderDetailsByOrderId", "Error deleting order details for order ID: " + orderID, e);
            return false;
        }
    }

    public boolean createOrderDetail(OrderDetail orderDetail) {
        return addOrderDetail(orderDetail);
    }

    private OrderDetail mapResultSetToOrderDetail(ResultSet rs) throws SQLException {
        OrderDetail orderDetail = new OrderDetail();
        orderDetail.setOrderDetailID(rs.getInt("OrderDetailID"));
        orderDetail.setOrderID(rs.getInt("OrderID"));
        orderDetail.setShoeID(rs.getInt("ShoeID"));
        orderDetail.setSizeID(rs.getInt("SizeID"));
        orderDetail.setQuantity(rs.getInt("Quantity"));
        orderDetail.setPrice(rs.getBigDecimal("Price"));
        orderDetail.setShoeName(rs.getString("ShoeName"));
        orderDetail.setShoeImage(rs.getString("ShoeImage"));
        orderDetail.setSizeNumber(rs.getInt("SizeNumber"));
        return orderDetail;
    }
}
