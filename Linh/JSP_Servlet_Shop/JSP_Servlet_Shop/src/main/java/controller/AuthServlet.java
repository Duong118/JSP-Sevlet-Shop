package controller;

import dao.UserDAO;
import model.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import util.AppLogger;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class AuthServlet extends BaseServlet {
    private UserDAO userDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        userDAO = new UserDAO();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";

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

        if (userDAO.isUsernameExists(username)) {
            request.setAttribute("error", "Username already exists");            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

        if (userDAO.isEmailExists(email)) {
            request.setAttribute("error", "Email already exists");
            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
            return;
        }

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
            response.sendRedirect(request.getContextPath() + "/auth/login?success=Registration successful. Please login.");
        } else {
            request.setAttribute("error", "Registration failed. Please try again.");            preserveFormData(request);
            request.getRequestDispatcher("/register.jsp").forward(request, response);
        }
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