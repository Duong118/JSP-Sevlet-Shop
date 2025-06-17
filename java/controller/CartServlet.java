package controller;

import dao.CartDAO;
import dao.ShoeDAO;
import dao.ShoeSizeDAO;
import model.CartItem;
import model.Shoe;
import model.User;
import util.AppLogger;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class CartServlet extends BaseServlet {

    private CartDAO cartDAO = new CartDAO();
    private ShoeDAO shoeDAO = new ShoeDAO();
    private ShoeSizeDAO shoeSizeDAO = new ShoeSizeDAO();    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        AppLogger.logRequest("CartServlet", request.getMethod(), request.getRequestURI(), request.getHeader("User-Agent"));
        
        // Handle both parameter-based and path-based routing
        String action = request.getParameter("action");
        String pathInfo = request.getPathInfo();
        
        // If path-based routing is used (e.g., /cart/view)
        if (action == null && pathInfo != null) {
            if (pathInfo.equals("/view")) {
                action = "view";
            } else if (pathInfo.equals("/count")) {
                action = "count";
            }
        }
        
        if (action == null) {
            action = "view";
        }
        
        AppLogger.info("CartServlet", "doGet", "Processing action: " + action);
        
        switch (action) {
            case "view":
                viewCart(request, response);
                break;
            case "count":
                getCartCount(request, response);
                break;
            default:
                AppLogger.warning("CartServlet", "doGet", "Unknown action: " + action + ", defaulting to view");
                viewCart(request, response);
                break;
        }
    }    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        requireLogin(request, response);
        AppLogger.logRequest("CartServlet", request.getMethod(), request.getRequestURI(), request.getHeader("User-Agent"));
        
        // Handle both parameter-based and path-based routing
        String action = request.getParameter("action");
        String pathInfo = request.getPathInfo();
        
        // If path-based routing is used (e.g., /cart/add)
        if (action == null && pathInfo != null) {
            if (pathInfo.equals("/add")) {
                action = "add";
            } else if (pathInfo.equals("/update")) {
                action = "update";
            } else if (pathInfo.equals("/remove")) {
                action = "remove";
            } else if (pathInfo.equals("/clear")) {
                action = "clear";
            }
        }
        
        if (action == null) {
            AppLogger.error("CartServlet", "doPost", "Action is required", null);
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Action is required");
            return;
        }
        
        AppLogger.info("CartServlet", "doPost", "Processing cart action: " + action);
        
        switch (action) {
            case "add":
                addToCart(request, response);
                break;
            case "update":
                updateCartItem(request, response);
                break;
            case "remove":
                removeFromCart(request, response);
                break;
            case "clear":
                clearCart(request, response);
                break;
            default:
                AppLogger.error("CartServlet", "doPost", "Invalid action: " + action, null);
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid action");
                break;
        }
    }    private void viewCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            AppLogger.info("CartServlet", "viewCart", "User not logged in, redirecting to login");
            requireLogin(request, response);
            return;
        }
        
        User user = getCurrentUser(request);
        AppLogger.info("CartServlet", "viewCart", "Viewing cart for user: " + user.getUsername());
        
        List<CartItem> cartItems = cartDAO.getCartItems(user.getUserID());
        double total = cartDAO.getCartTotal(user.getUserID());        
        
        AppLogger.info("CartServlet", "viewCart", "Cart retrieved for user " + user.getUsername() + 
                      " with " + cartItems.size() + " items, total: $" + total);
        
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", total);
        request.getRequestDispatcher("/cart-view.jsp").forward(request, response);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        String shoeIdStr = request.getParameter("shoeId");
        String sizeIdStr = request.getParameter("sizeId");
        String quantityStr = request.getParameter("quantity");
          
        AppLogger.info("CartServlet", "addToCart", "Attempting to add item for user " + user.getUsername() + 
                      " - ShoeID: " + shoeIdStr + ", SizeID: " + sizeIdStr + ", Quantity: " + quantityStr);
        
        if (shoeIdStr == null || sizeIdStr == null || quantityStr == null) {
            AppLogger.warning("CartServlet", "addToCart", "Missing required parameters for user " + user.getUsername());
            // For missing parameters, we can't determine shoeId, so redirect to shoes page
            response.sendRedirect(request.getContextPath() + "/shoes?error=" + 
                                java.net.URLEncoder.encode("Missing required parameters", "UTF-8"));
            return;
        }
        
        try {
            int shoeId = Integer.parseInt(shoeIdStr);
            int sizeId = Integer.parseInt(sizeIdStr);
            int quantity = Integer.parseInt(quantityStr);
              if (quantity <= 0) {
                AppLogger.warning("CartServlet", "addToCart", "Invalid quantity " + quantity + " for user " + user.getUsername());
                response.sendRedirect(request.getContextPath() + "/shoes/detail?shoeId=" + shoeId + 
                                    "&error=" + java.net.URLEncoder.encode("Invalid quantity", "UTF-8"));
                return;
            }
            
            // Check if shoe exists
            Shoe shoe = shoeDAO.getShoeById(shoeId);
            if (shoe == null) {
                AppLogger.warning("CartServlet", "addToCart", "Shoe not found: " + shoeId + " for user " + user.getUsername());
                response.sendRedirect(request.getContextPath() + "/shoes?error=" + 
                                    java.net.URLEncoder.encode("Shoe not found", "UTF-8"));
                return;
            }
            
            // Check stock availability
            int availableStock = shoeSizeDAO.getStockQuantity(shoeId, sizeId);
            if (availableStock < quantity) {
                AppLogger.warning("CartServlet", "addToCart", "Insufficient stock for shoe " + shoeId + 
                                " (available: " + availableStock + ", requested: " + quantity + ") for user " + user.getUsername());
                response.sendRedirect(request.getContextPath() + "/shoes/detail?shoeId=" + shoeId + 
                                    "&error=" + java.net.URLEncoder.encode("Insufficient stock", "UTF-8"));
                return;
            }
              
            // Check if item already in cart
            if (cartDAO.isItemInCart(user.getUserID(), shoeId, sizeId)) {
                CartItem existingItem = cartDAO.getCartItem(user.getUserID(), shoeId, sizeId);
                int newQuantity = existingItem.getQuantity() + quantity;
                
                if (availableStock < newQuantity) {
                    AppLogger.warning("CartServlet", "addToCart", "Insufficient stock for update - shoe " + shoeId + 
                                    " (available: " + availableStock + ", requested total: " + newQuantity + ") for user " + user.getUsername());
                    response.sendRedirect(request.getContextPath() + "/shoes/detail?shoeId=" + shoeId + 
                                        "&error=" + java.net.URLEncoder.encode("Insufficient stock", "UTF-8"));
                    return;
                }
                
                if (cartDAO.updateCartItemQuantity(user.getUserID(), shoeId, sizeId, newQuantity)) {
                    AppLogger.logCart("updateExisting", user.getUsername(), String.valueOf(shoeId), newQuantity, "Updated existing cart item");
                    response.sendRedirect(request.getContextPath() + "/shoes/detail?shoeId=" + shoeId + 
                                        "&success=" + java.net.URLEncoder.encode("Cart updated successfully", "UTF-8"));
                } else {
                    AppLogger.error("CartServlet", "addToCart", "Failed to update existing cart item for user " + user.getUsername(), null);
                    response.sendRedirect(request.getContextPath() + "/shoes/detail?shoeId=" + shoeId + 
                                        "&error=" + java.net.URLEncoder.encode("Failed to update cart", "UTF-8"));
                }
            } else {
                CartItem cartItem = new CartItem(user.getUserID(), shoeId, sizeId, quantity);
                if (cartDAO.addToCart(cartItem)) {
                    AppLogger.logCart("addNew", user.getUsername(), String.valueOf(shoeId), quantity, "Added new item to cart");
                    response.sendRedirect(request.getContextPath() + "/shoes/detail?shoeId=" + shoeId + 
                                        "&success=" + java.net.URLEncoder.encode("Item added to cart successfully", "UTF-8"));
                } else {
                    AppLogger.error("CartServlet", "addToCart", "Failed to add new item to cart for user " + user.getUsername(), null);
                    response.sendRedirect(request.getContextPath() + "/shoes/detail?shoeId=" + shoeId + 
                                        "&error=" + java.net.URLEncoder.encode("Failed to add item to cart", "UTF-8"));                }
            }
              } catch (NumberFormatException e) {
            AppLogger.error("CartServlet", "addToCart", "Invalid parameter format for user " + user.getUsername(), e);
            response.sendRedirect(request.getContextPath() + "/shoes?error=" + 
                                java.net.URLEncoder.encode("Invalid parameter format", "UTF-8"));
        } catch (Exception e) {
            AppLogger.error("CartServlet", "addToCart", "Unexpected error while adding to cart for user " + user.getUsername(), e);
            response.sendRedirect(request.getContextPath() + "/shoes?error=" + 
                                java.net.URLEncoder.encode("An error occurred while adding to cart", "UTF-8"));
        }
    }

    private void updateCartItem(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        String shoeIdStr = request.getParameter("shoeId");
        String sizeIdStr = request.getParameter("sizeId");
        String quantityStr = request.getParameter("quantity");
        
        AppLogger.info("CartServlet", "updateCartItem", "Updating cart item for user " + user.getUsername() + 
                      " - ShoeID: " + shoeIdStr + ", SizeID: " + sizeIdStr + ", Quantity: " + quantityStr);
        
        if (shoeIdStr == null || sizeIdStr == null || quantityStr == null) {
            AppLogger.warning("CartServlet", "updateCartItem", "Missing required parameters for user " + user.getUsername());
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }
        
        try {
            int shoeId = Integer.parseInt(shoeIdStr);
            int sizeId = Integer.parseInt(sizeIdStr);
            int quantity = Integer.parseInt(quantityStr);
              if (quantity < 0) {
                AppLogger.warning("CartServlet", "updateCartItem", "Invalid negative quantity " + quantity + " for user " + user.getUsername());
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid quantity");
                return;
            }
            
            if (quantity == 0) {
                // Remove item if quantity is 0
                AppLogger.info("CartServlet", "updateCartItem", "Removing cart item (quantity=0) for user " + user.getUsername());
                if (cartDAO.removeFromCart(user.getUserID(), shoeId, sizeId)) {
                    AppLogger.logCart("removeViaUpdate", user.getUsername(), String.valueOf(shoeId), 0, "Item removed via quantity update");
                    sendSuccess(response, "Item removed from cart");
                } else {
                    AppLogger.error("CartServlet", "updateCartItem", "Failed to remove item via update for user " + user.getUsername(), null);
                    sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to remove item");
                }
                return;
            }
            
            // Check stock availability
            int availableStock = shoeSizeDAO.getStockQuantity(shoeId, sizeId);
            if (availableStock < quantity) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Insufficient stock");
                return;
            }
            
            if (cartDAO.updateCartItemQuantity(user.getUserID(), shoeId, sizeId, quantity)) {
                sendSuccess(response, "Cart updated successfully");
            } else {
                sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to update cart");
            }
            
        } catch (NumberFormatException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter format");
        }
    }

    private void removeFromCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        String shoeIdStr = request.getParameter("shoeId");
        String sizeIdStr = request.getParameter("sizeId");
        
        if (shoeIdStr == null || sizeIdStr == null) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Missing required parameters");
            return;
        }
        
        try {
            int shoeId = Integer.parseInt(shoeIdStr);
            int sizeId = Integer.parseInt(sizeIdStr);
            
            if (cartDAO.removeFromCart(user.getUserID(), shoeId, sizeId)) {
                sendSuccess(response, "Item removed from cart successfully");
            } else {
                sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to remove item from cart");
            }
            
        } catch (NumberFormatException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid parameter format");
        }
    }

    private void clearCart(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        User user = getCurrentUser(request);
        
        if (cartDAO.clearCart(user.getUserID())) {
            sendSuccess(response, "Cart cleared successfully");
        } else {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to clear cart");
        }
    }

    private void getCartCount(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!isLoggedIn(request)) {
            sendJsonResponse(response, "{\"count\": 0}");
            return;
        }
        
        User user = getCurrentUser(request);
        int count = cartDAO.getCartItemCount(user.getUserID());
        sendJsonResponse(response, "{\"count\": " + count + "}");
    }
}
