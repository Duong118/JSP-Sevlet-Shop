package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.nio.file.Paths;

// Remove @WebServlet annotation since we're using web.xml mapping for /staff/*
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class StaffServlet extends BaseServlet {
    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;
    private ObjectMapper objectMapper;
    
    private static final String UPLOAD_DIR = "uploads/shoes";
    
    @Override
    public void init() throws ServletException {
        super.init();
        orderDAO = new OrderDAO();
        orderDetailDAO = new OrderDetailDAO();
        objectMapper = new ObjectMapper();
    }
    

    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        

        int pendingOrders = orderDAO.getOrderCountByStatus("pending");
        int processingOrders = orderDAO.getOrderCountByStatus("processing");
        
        // Get recent orders that need attention
        List<Order> recentOrders = orderDAO.getRecentOrdersByStatus("pending", 10);
        
    request.setAttribute("pendingOrders", pendingOrders);
        request.setAttribute("processingOrders", processingOrders);
        request.setAttribute("recentOrders", recentOrders);
        
        request.getRequestDispatcher("/employee-dashboard.jsp").forward(request, response);
    }

    
    private void showOrders(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String status = request.getParameter("status");
        
        List<Order> orders;
        if (status != null && !status.trim().isEmpty()) {
            orders = orderDAO.getOrdersByStatus(status);
        } else {
            orders = orderDAO.getAllOrders();
        }
          request.setAttribute("orders", orders);
        request.setAttribute("selectedStatus", status);
        request.getRequestDispatcher("/employee-orders.jsp").forward(request, response);
    }
    
    private void showOrderDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Order ID is required");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Order not found");
                return;
            }
            
            List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);
            
            request.setAttribute("order", order);            request.setAttribute("orderDetails", orderDetails);
            request.getRequestDispatcher("/employee-order-details.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        }
    }}
    
