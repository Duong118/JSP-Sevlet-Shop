package controller;

import dao.UserDAO;
import dao.OrderDAO;
import model.User;
import model.Order;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.PasswordUtil;
import util.AppLogger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AuthServlet extends BaseServlet {
    private UserDAO userDAO;
    private OrderDAO orderDAO;
    
    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
        orderDAO = new OrderDAO();
    }
      @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        // Log incoming request
        AppLogger.logRequest("AuthServlet", "GET", request.getRequestURI(), 
                           request.getHeader("User-Agent"));
        
        try {
            switch (pathInfo) {
                case "/login":
                    showLogin(request, response);
                    break;
                case "/register":
                    showRegister(request, response);
                    break;
                case "/profile":
                    showProfile(request, response);
                    break;
                case "/logout":
                    handleLogout(request, response);
                    break;
                default:
                    AppLogger.warning("AuthServlet", "doGet", "Unknown path: " + pathInfo);
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            handleError(response, "Database error occurred", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        try {
            switch (pathInfo) {
                case "/login":
                    handleLogin(request, response);
                    break;
                case "/register":
                    handleRegister(request, response);
                    break;
                case "/updateProfile":
                    handleUpdateProfile(request, response);
                    break;
                case "/changePassword":
                    handleChangePassword(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            handleError(response, "Database error occurred", e);
        }
    }
    
    private void showLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // If user is already logged in, redirect to home
        if (isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/");        return;
        }
        request.getRequestDispatcher("/login.jsp").forward(request, response);
    }
    
    private void showRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // If user is already logged in, redirect to home
        if (isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/");
            return;        }
        request.getRequestDispatcher("/register.jsp").forward(request, response);
    }
    
    private void showProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        // Check if user is logged in
        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        User user = getCurrentUser(request);
        // Get user's order history for the profile page
        List<Order> orders = orderDAO.getOrdersByUserId(user.getUserID());
        request.setAttribute("orders", orders);        request.setAttribute("user", user);
        
        request.getRequestDispatcher("/profile.jsp").forward(request, response);
    }
    
    private void handleLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {            request.setAttribute("error", "Username and password are required");
            request.getRequestDispatcher("/login.jsp").forward(request, response);
            return;
        }
        
        User user = userDAO.loginUser(username, password);
          if (user != null) {
            // Login successful
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            // Check for returnUrl parameter first, then session attribute for backward compatibility
            String returnUrl = request.getParameter("returnUrl");
            if (returnUrl == null || returnUrl.trim().isEmpty()) {
                returnUrl = (String) session.getAttribute("redirectAfterLogin");
            }
            
            if (returnUrl != null && !returnUrl.trim().isEmpty()) {
                // Clean up session attribute if it was used
                session.removeAttribute("redirectAfterLogin");
                response.sendRedirect(returnUrl);
            } else {
                response.sendRedirect(request.getContextPath() + "/");
            }
        } else {// Login failed
            request.setAttribute("error", "Invalid username or password");
            request.setAttribute("username", username);
            request.getRequestDispatcher("/login.jsp").forward(request, response);
        }
    }
    
    private void handleRegister(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirmPassword");
        String fullName = request.getParameter("fullName");
        String address = request.getParameter("address");
        String phone = request.getParameter("phone");
        
        // Validation
        if (username == null || email == null || password == null || confirmPassword == null || 
            fullName == null || username.trim().isEmpty() || email.trim().isEmpty() || 
            password.trim().isEmpty() || fullName.trim().isEmpty()) {            request.setAttribute("error", "All required fields must be filled");
            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        if (!password.equals(confirmPassword)) {
            request.setAttribute("error", "Passwords do not match");
            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Check if username already exists
        if (userDAO.isUsernameExists(username)) {
            request.setAttribute("error", "Username already exists");            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Check if email already exists
        if (userDAO.isEmailExists(email)) {
            request.setAttribute("error", "Email already exists");
            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }
        
        // Create new user
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setAddress(address != null ? address : "");
        user.setPhone(phone != null ? phone : "");
        user.setRole("User");
        user.setStatus("Active");
        
        boolean success = userDAO.registerUser(user);
        
        if (success) {
            // Registration successful, redirect to login with success message
            response.sendRedirect(request.getContextPath() + "/auth/login?success=Registration successful. Please login.");
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
    }
      private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {
        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
          User currentUser = getCurrentUser(request);
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String phoneNumber = request.getParameter("phoneNumber");
        String address = request.getParameter("address");
        
        AppLogger.debug("AuthServlet", "handleUpdateProfile", 
                       "Update Profile - Current User ID: " + currentUser.getUserID());
        AppLogger.debug("AuthServlet", "handleUpdateProfile", 
                       "Update Profile - Current Email: " + currentUser.getEmail());
        AppLogger.debug("AuthServlet", "handleUpdateProfile", 
                       "Update Profile - New Email: " + email);
        
        // Debug: List all users when debugging email issues
        if ("huynhphuoc2468@gmail.com".equals(email)) {
            userDAO.debugListAllUsers();
        }
        
        // Validation
        if (fullName == null || email == null || fullName.trim().isEmpty() || email.trim().isEmpty()) {
            AppLogger.warning("AuthServlet", "handleUpdateProfile", 
                             "Validation failed: Full name and email are required");
            request.setAttribute("error", "Full name and email are required");
            showProfile(request, response);
            return;
        }
        
        // Check if email is changed and if new email already exists for another user
        if (!email.equals(currentUser.getEmail())) {
            AppLogger.debug("AuthServlet", "handleUpdateProfile", 
                           "Email changed, checking if new email exists...");
            // Use the new method to check email exists for other users
            if (userDAO.isEmailExistsForOtherUser(email, currentUser.getUserID())) {
                AppLogger.warning("AuthServlet", "handleUpdateProfile", 
                                 "Email already exists for another user: " + email);
                request.setAttribute("error", "Email '" + email + "' is already in use by another account");
                showProfile(request, response);
                return;
            }
        }
        
        // Update user information
        currentUser.setFullName(fullName);
        currentUser.setEmail(email);
        currentUser.setPhone(phoneNumber != null ? phoneNumber : "");
        currentUser.setAddress(address != null ? address : "");
        
        boolean success = userDAO.updateUser(currentUser);
        
        if (success) {
            // Update session user
            HttpSession session = request.getSession();
            session.setAttribute("user", currentUser);
            request.setAttribute("success", "Profile updated successfully");
            AppLogger.info("AuthServlet", "handleUpdateProfile", 
                          "Profile update successful for user: " + currentUser.getEmail());
        } else {
            request.setAttribute("error", "Failed to update profile");
            AppLogger.error("AuthServlet", "handleUpdateProfile", 
                           "Profile update failed for user: " + currentUser.getEmail());
        }
        
        showProfile(request, response);
    }
    
    private void handleChangePassword(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        if (!isLoggedIn(request)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        User currentUser = getCurrentUser(request);
        String currentPassword = request.getParameter("currentPassword");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");
        
        // Validation
        if (currentPassword == null || newPassword == null || confirmPassword == null ||
            currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
            request.setAttribute("error", "All password fields are required");
            showProfile(request, response);
            return;
        }
        
        if (!newPassword.equals(confirmPassword)) {
            request.setAttribute("error", "New passwords do not match");
            showProfile(request, response);
            return;
        }
        
        // Verify current password
        User verifyUser = userDAO.loginUser(currentUser.getUsername(), currentPassword);
        if (verifyUser == null) {
            request.setAttribute("error", "Current password is incorrect");
            showProfile(request, response);
            return;
        }
        
        // Update password
        boolean success = userDAO.updatePassword(currentUser.getUserID(), newPassword);
        
        if (success) {
            request.setAttribute("success", "Password changed successfully");
        } else {
            request.setAttribute("error", "Failed to change password");
        }
        
        showProfile(request, response);
    }
    
    private void handleLogout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/");
    }
    
    private void preserveFormData(HttpServletRequest request) {
        request.setAttribute("username", request.getParameter("username"));
        request.setAttribute("email", request.getParameter("email"));
        request.setAttribute("fullName", request.getParameter("fullName"));
        request.setAttribute("address", request.getParameter("address"));
        request.setAttribute("phone", request.getParameter("phone"));
    }
    
    @SuppressWarnings("unused")
    private void handleError(HttpServletResponse response, String message, Exception e) throws IOException {
        e.printStackTrace();
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}