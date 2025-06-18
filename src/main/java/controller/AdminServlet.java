package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.*;
import model.*;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import util.PasswordUtil;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.io.File;
import java.nio.file.Paths;

// Remove @WebServlet annotation since we're using web.xml mapping for /admin/*
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2,  // 2MB
    maxFileSize = 1024 * 1024 * 10,       // 10MB
    maxRequestSize = 1024 * 1024 * 50     // 50MB
)
public class AdminServlet extends BaseServlet {
    private UserDAO userDAO;

    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;
    private ObjectMapper objectMapper;
    
    private static final String UPLOAD_DIR = "uploads/shoes";
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
        orderDetailDAO = new OrderDetailDAO();
        objectMapper = new ObjectMapper();
    }


    private void showDashboard(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        // Get dashboard statistics
        int totalUsers = userDAO.getTotalUsers();
        int totalOrders = orderDAO.getTotalOrders();
        double totalRevenue = orderDAO.getTotalRevenue().doubleValue();
        
        // Get recent orders
        List<Order> recentOrders = orderDAO.getRecentOrders(10);
        
        request.setAttribute("totalUsers", totalUsers);
    request.setAttribute("totalOrders", totalOrders);
        request.setAttribute("totalRevenue", totalRevenue);
        request.setAttribute("recentOrders", recentOrders);
        
        request.getRequestDispatcher("/admin-dashboard.jsp").forward(request, response);
    }
    
    private void showUsers(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String search = request.getParameter("search");
        String role = request.getParameter("role");
        
        List<User> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userDAO.searchUsers(search);
        } else if (role != null && !role.trim().isEmpty()) {
            users = userDAO.getUsersByRole(role);
        } else {
            users = userDAO.getAllUsers();
        }
          request.setAttribute("users", users);
        request.setAttribute("search", search);
        request.setAttribute("selectedRole", role);
        request.getRequestDispatcher("/admin-users.jsp").forward(request, response);
    }

    private void showOrders(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String status = request.getParameter("status");
        String userIdStr = request.getParameter("userId");
        
        List<Order> orders;
        if (status != null && !status.trim().isEmpty()) {
            orders = orderDAO.getOrdersByStatus(status);
        } else if (userIdStr != null && !userIdStr.trim().isEmpty()) {
            try {
                int userId = Integer.parseInt(userIdStr);
                orders = orderDAO.getOrdersByUserId(userId);
            } catch (NumberFormatException e) {
                orders = orderDAO.getAllOrders();
            }
        } else {
            orders = orderDAO.getAllOrders();
        }
        
        request.setAttribute("orders", orders);
        request.setAttribute("selectedStatus", status);        request.setAttribute("selectedUserId", userIdStr);
        request.getRequestDispatcher("/admin-orders.jsp").forward(request, response);
    }
    
    private void showRevenue(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
          // Get revenue data (this would be more complex in a real application)
        double totalRevenue = orderDAO.getTotalRevenue().doubleValue();
        java.util.Calendar cal = java.util.Calendar.getInstance();
        double monthlyRevenue = orderDAO.getMonthlyRevenue(cal.get(java.util.Calendar.YEAR), cal.get(java.util.Calendar.MONTH) + 1).doubleValue();
        double dailyRevenue = orderDAO.getDailyRevenue().doubleValue();
        
        // Get top selling shoes
        List<Map<String, Object>> topShoes = orderDAO.getTopSellingShoes(10);
        
        request.setAttribute("totalRevenue", totalRevenue);        request.setAttribute("monthlyRevenue", monthlyRevenue);
        request.setAttribute("dailyRevenue", dailyRevenue);
        request.setAttribute("topShoes", topShoes);
        
        request.getRequestDispatcher("/admin-revenue.jsp").forward(request, response);
    }
    
    private void createUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        String role = request.getParameter("role");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        
        // Validate required fields
        if (username == null || username.trim().isEmpty() ||
            email == null || email.trim().isEmpty() ||
            password == null || password.trim().isEmpty() ||
            fullName == null || fullName.trim().isEmpty() ||
            role == null || role.trim().isEmpty()) {
            sendJsonResponse(response, false, "All required fields must be filled");
            return;
        }
        
        // Validate role
        if (!"user".equals(role) && !"staff".equals(role) && !"admin".equals(role)) {
            sendJsonResponse(response, false, "Invalid role");
            return;
        }
        
        // Check if username or email already exists
        if (userDAO.getUserByUsername(username) != null) {
            sendJsonResponse(response, false, "Username already exists");
            return;
        }
        
        if (userDAO.getUserByEmail(email) != null) {
            sendJsonResponse(response, false, "Email already exists");
            return;
        }
        
        // Create user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPasswordHash(PasswordUtil.hashPassword(password));
        user.setFullName(fullName);
        user.setRole(role);
        user.setPhoneNumber(phoneNumber);
        user.setAddress(address);
        user.setActive(true);
        
        userDAO.createUser(user);
        sendJsonResponse(response, true, "User created successfully");
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null) {
            sendJsonResponse(response, false, "User ID is required");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                sendJsonResponse(response, false, "User not found");
                return;
            }
            
            // Update fields if provided
            String email = request.getParameter("email");
            String fullName = request.getParameter("fullName");
            String role = request.getParameter("role");
            String phoneNumber = request.getParameter("phoneNumber");
            String address = request.getParameter("address");
            String password = request.getParameter("password");
            
            if (email != null && !email.trim().isEmpty()) {
                user.setEmail(email);
            }
            if (fullName != null && !fullName.trim().isEmpty()) {
                user.setFullName(fullName);
            }
            if (role != null && !role.trim().isEmpty()) {
                if ("user".equals(role) || "staff".equals(role) || "admin".equals(role)) {
                    user.setRole(role);
                } else {
                    sendJsonResponse(response, false, "Invalid role");
                    return;
                }
            }
            if (phoneNumber != null) {
                user.setPhoneNumber(phoneNumber);
            }
            if (address != null) {
                user.setAddress(address);
            }
            if (password != null && !password.trim().isEmpty()) {
                user.setPasswordHash(PasswordUtil.hashPassword(password));
            }
            
            userDAO.updateUser(user);
            sendJsonResponse(response, true, "User updated successfully");
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid user ID");
        }
    }
    
    private void banUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null) {
            sendJsonResponse(response, false, "User ID is required");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                sendJsonResponse(response, false, "User not found");
                return;
            }
            
            user.setActive(false);
            userDAO.updateUser(user);
            sendJsonResponse(response, true, "User banned successfully");
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid user ID");
        }
    }
    
    private void unbanUser(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String userIdStr = request.getParameter("userId");
        if (userIdStr == null) {
            sendJsonResponse(response, false, "User ID is required");
            return;
        }
        
        try {
            int userId = Integer.parseInt(userIdStr);
            User user = userDAO.getUserById(userId);
            
            if (user == null) {
                sendJsonResponse(response, false, "User not found");
                return;
            }
            
            user.setActive(true);
            userDAO.updateUser(user);
            sendJsonResponse(response, true, "User unbanned successfully");
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid user ID");
        }
    }
    

    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) 
            throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", success);
        responseMap.put("message", message);
        
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseMap));
    }
}
