package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

import java.io.IOException;
import java.sql.SQLException;

public abstract class BaseServlet extends HttpServlet {
    
    protected User getCurrentUser(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (User) session.getAttribute("user");
    }
    
    protected boolean isLoggedIn(HttpServletRequest request) {
        return getCurrentUser(request) != null;
    }
    
    protected boolean isAdmin(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null && "Admin".equals(user.getRole());
    }
    
    protected boolean isAdmin(User user) {
        return user != null && "Admin".equals(user.getRole());
    }
    
    protected boolean isStaff(HttpServletRequest request) {
        User user = getCurrentUser(request);
        return user != null && ("Staff".equals(user.getRole()) || "Admin".equals(user.getRole()));
    }
    
    protected boolean isAdminOrStaff(HttpServletRequest request) {
        return isStaff(request);
    }
    
    protected void sendError(HttpServletResponse response, int statusCode, String message) throws IOException {
        response.setStatus(statusCode);
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
    
    protected void sendSuccess(HttpServletResponse response, String message) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write("{\"success\": true, \"message\": \"" + message + "\"}");
    }
    
    protected void sendJsonResponse(HttpServletResponse response, String json) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }    protected void requireLogin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            if (request.getHeader("X-Requested-With") != null && 
                request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
                sendError(response, HttpServletResponse.SC_UNAUTHORIZED, "Please login first");
            } else {
                // Capture the current URL and append as returnUrl parameter
                String currentUrl = getFullRequestURL(request);
                String encodedReturnUrl = java.net.URLEncoder.encode(currentUrl, "UTF-8");
                response.sendRedirect(request.getContextPath() + "/auth/login?returnUrl=" + encodedReturnUrl);
            }
            return;
        }
    }
    
    protected void requireAdmin(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        requireLogin(request, response);
        if (!isAdmin(request)) {
            if (request.getHeader("X-Requested-With") != null && 
                request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, "Admin access required");
            } else {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            }
            return;
        }
    }
    
    protected void requireStaff(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        requireLogin(request, response);
        if (!isStaff(request)) {
            if (request.getHeader("X-Requested-With") != null && 
                request.getHeader("X-Requested-With").equals("XMLHttpRequest")) {
                sendError(response, HttpServletResponse.SC_FORBIDDEN, "Staff access required");
            } else {
                response.sendRedirect(request.getContextPath() + "/access-denied.jsp");
            }
            return;
        }
    }
    
    protected String getFullRequestURL(HttpServletRequest request) {
        StringBuffer requestURL = request.getRequestURL();
        String queryString = request.getQueryString();
        
        if (queryString == null) {
            return requestURL.toString();
        } else {
            return requestURL.append('?').append(queryString).toString();
        }
    }
    
    protected void handleError(HttpServletResponse response, String message, SQLException e) throws IOException {
        e.printStackTrace();
        sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }
}
