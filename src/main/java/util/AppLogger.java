package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;
import java.util.logging.SimpleFormatter;

/**
 * Utility class for application-wide logging
 * Provides standardized logging methods for the JSP Servlet Shop application
 */
public class AppLogger {
    
    private static final Logger logger = Logger.getLogger("JSP_Servlet_Shop");
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    static {
        // Configure the logger
        logger.setLevel(Level.ALL);
        
        // Remove default handlers to avoid duplicate logs
        Logger rootLogger = Logger.getLogger("");
        for (java.util.logging.Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }
        
        // Add console handler with custom formatting
        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.ALL);
        consoleHandler.setFormatter(new SimpleFormatter());
        logger.addHandler(consoleHandler);
        logger.setUseParentHandlers(false);
    }
    
    /**
     * Log info message
     */
    public static void info(String className, String methodName, String message) {
        String logMessage = formatMessage("INFO", className, methodName, message);
        logger.info(logMessage);
    }
    
    /**
     * Log error message
     */
    public static void error(String className, String methodName, String message) {
        String logMessage = formatMessage("ERROR", className, methodName, message);
        logger.severe(logMessage);
    }
    
    /**
     * Log error message with exception
     */
    public static void error(String className, String methodName, String message, Throwable throwable) {
        String logMessage = formatMessage("ERROR", className, methodName, message + " - Exception: " + throwable.getMessage());
        logger.log(Level.SEVERE, logMessage, throwable);
    }
    
    /**
     * Log warning message
     */
    public static void warning(String className, String methodName, String message) {
        String logMessage = formatMessage("WARNING", className, methodName, message);
        logger.warning(logMessage);
    }
    
    /**
     * Log debug message
     */
    public static void debug(String className, String methodName, String message) {
        String logMessage = formatMessage("DEBUG", className, methodName, message);
        logger.fine(logMessage);
    }
    
    /**
     * Log servlet request details
     */
    public static void logRequest(String servletName, String method, String requestURI, String userAgent) {
        String message = String.format("Request: %s %s | User-Agent: %s", method, requestURI, userAgent);
        info(servletName, "doGet/doPost", message);
    }
    
    /**
     * Log servlet response details
     */
    public static void logResponse(String servletName, String method, int statusCode, String message) {
        String logMessage = String.format("Response: %s | Status: %d | %s", method, statusCode, message);
        info(servletName, "response", logMessage);
    }
    
    /**
     * Log database operations
     */
    public static void logDatabase(String daoClass, String operation, String table, String details) {
        String message = String.format("DB Operation: %s on %s | %s", operation, table, details);
        info(daoClass, operation, message);
    }
    
    /**
     * Log user authentication events
     */
    public static void logAuth(String event, String username, String result) {
        String message = String.format("Auth Event: %s | User: %s | Result: %s", event, username, result);
        info("AuthServlet", event, message);
    }
    
    /**
     * Log user session events
     */
    public static void logSession(String event, String sessionId, String username, String details) {
        String message = String.format("Session Event: %s | Session: %s | User: %s | %s", 
                                       event, sessionId, username, details);
        info("SessionManager", event, message);
    }
    
    /**
     * Log cart operations
     */
    public static void logCart(String operation, String username, String shoeId, int quantity, String details) {
        String message = String.format("Cart Operation: %s | User: %s | Shoe: %s | Qty: %d | %s", 
                                       operation, username, shoeId, quantity, details);
        info("CartServlet", operation, message);
    }
    
    /**
     * Log order operations
     */
    
    /**
     * Format log message with timestamp and context
     */
    private static String formatMessage(String level, String className, String methodName, String message) {
        return String.format("[%s] [%s] [%s.%s] %s", 
                           LocalDateTime.now().format(formatter), 
                           level, 
                           className, 
                           methodName, 
                           message);
    }
    
    /**
     * Enable debug logging
     */
    public static void enableDebugLogging() {
        logger.setLevel(Level.FINE);
        info("AppLogger", "enableDebugLogging", "Debug logging enabled");
    }
    
    /**
     * Disable debug logging
     */
    public static void disableDebugLogging() {
        logger.setLevel(Level.INFO);
        info("AppLogger", "disableDebugLogging", "Debug logging disabled");
    }
    
    /**
     * Log application startup
     */
    public static void logStartup(String applicationName, String version) {
        String border = "=".repeat(60);
        info("Application", "startup", border);
        info("Application", "startup", String.format("%s v%s starting up...", applicationName, version));
        info("Application", "startup", "Timestamp: " + LocalDateTime.now().format(formatter));
        info("Application", "startup", border);
    }
    
    /**
     * Log application shutdown
     */
    public static void logShutdown(String applicationName) {
        String border = "=".repeat(60);
        info("Application", "shutdown", border);
        info("Application", "shutdown", String.format("%s shutting down...", applicationName));
        info("Application", "shutdown", "Timestamp: " + LocalDateTime.now().format(formatter));
        info("Application", "shutdown", border);
    }
}
