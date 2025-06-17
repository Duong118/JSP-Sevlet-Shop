package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dao.OrderDAO;
import dao.OrderDetailDAO;
import dao.CartDAO;
import dao.ShoeDAO;
import model.User;
import model.Order;
import model.OrderDetail;
import model.CartItem;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.AppLogger;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

// Remove @WebServlet annotation since we're using web.xml mapping for /order/*
public class OrderServlet extends BaseServlet {
    private OrderDAO orderDAO;
    private OrderDetailDAO orderDetailDAO;
    private CartDAO cartDAO;
    private ShoeDAO shoeDAO;
    private ObjectMapper objectMapper;    @Override
    public void init() throws ServletException {
        super.init();
        try {
            orderDAO = new OrderDAO();
            orderDetailDAO = new OrderDetailDAO();
            cartDAO = new CartDAO();
            shoeDAO = new ShoeDAO();
            objectMapper = new ObjectMapper();
        } catch (Exception e) {
            throw new ServletException("OrderServlet initialization failed", e);
        }
    }    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        try {
            switch (pathInfo) {
                case "/history":
                    showOrderHistory(request, response);
                    break;                case "/checkout":
                    showCheckout(request, response);
                    break;
                case "/details":
                    showOrderDetails(request, response);
                    break;
                case "/confirm":
                    showOrderConfirmation(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            handleError(response, "Database error occurred", e);
        } catch (Exception e) {
            throw new ServletException("Error in OrderServlet", e);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String pathInfo = request.getPathInfo();
        if (pathInfo == null) pathInfo = "/";
        
        try {
            switch (pathInfo) {
                case "/place":
                    placeOrder(request, response);
                    break;
                case "/update-status":
                    updateOrderStatus(request, response);
                    break;
                case "/cancel":
                    cancelOrder(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (SQLException e) {
            handleError(response, "Database error occurred", e);
        }
    }

    private void placeOrder(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {        User user = getCurrentUser(request);
        if (user == null) {
            handleValidationError(request, response, "Please login to place order");
            return;
        }

        List<CartItem> cartItems = cartDAO.getCartByUserId(user.getUserId());
        if (cartItems.isEmpty()) {
            handleValidationError(request, response, "Cart is empty");
            return;
        }

        // Get order details from request
        String shippingAddress = request.getParameter("shippingAddress");
        String phoneNumber = request.getParameter("phoneNumber");
        String paymentMethod = request.getParameter("paymentMethod");
        String notes = request.getParameter("notes");

        // Validate required fields
        if (shippingAddress == null || shippingAddress.trim().isEmpty()) {
            handleValidationError(request, response, "Shipping address is required");
            return;
        }

        if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
            handleValidationError(request, response, "Phone number is required");
            return;
        }

        if (paymentMethod == null || paymentMethod.trim().isEmpty()) {
            handleValidationError(request, response, "Payment method is required");
            return;
        }

        // Calculate total using BigDecimal for precision
        BigDecimal orderTotalBigDecimal = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            if (item.getPrice() > 0 && item.getQuantity() > 0) {
                try {
                    BigDecimal itemPrice = BigDecimal.valueOf(item.getPrice());
                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
                    orderTotalBigDecimal = orderTotalBigDecimal.add(itemPrice.multiply(quantity));                } catch (Exception e) {
                    // Log error or handle, for now, skip problematic item
                    AppLogger.error("OrderServlet", "placeOrder", "Error calculating total for item " + item.getShoeName(), e);
                }
            }
        }
        
        // Ensure total is not negative
        if (orderTotalBigDecimal.compareTo(BigDecimal.ZERO) < 0) {
            orderTotalBigDecimal = BigDecimal.ZERO;
        }        // Check stock availability
        for (CartItem item : cartItems) {
            int availableStock = shoeDAO.getAvailableStock(item.getShoeId(), item.getSizeId());
            if (availableStock < item.getQuantity()) {
                handleValidationError(request, response, "Insufficient stock for " + item.getShoeName());
                return;
            }
        }

        try {
            // Create order
            Order order = new Order();
            order.setUserId(user.getUserId());
            order.setOrderDate(new Date());
            order.setTotalAmount(orderTotalBigDecimal.doubleValue()); // Convert BigDecimal to double for Order model
            order.setStatus("pending");
            order.setShippingAddress(shippingAddress);
            order.setPhoneNumber(phoneNumber);
            order.setPaymentMethod(paymentMethod);
            order.setNotes(notes);

            int orderId = orderDAO.createOrder(order);
            order.setOrderId(orderId);

            // Create order details and update stock
            for (CartItem item : cartItems) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setShoeId(item.getShoeId());
                orderDetail.setSizeId(item.getSizeId());
                orderDetail.setQuantity(item.getQuantity());
                // Assuming CartItem.getPrice() returns a double that can be accurately represented by BigDecimal
                orderDetail.setPrice(BigDecimal.valueOf(item.getPrice())); 

                orderDetailDAO.createOrderDetail(orderDetail);

                // Update stock
                shoeDAO.updateStock(item.getShoeId(), item.getSizeId(), -item.getQuantity());
            }            // Clear cart
            cartDAO.clearCart(user.getUserId());

            // Check if this is an AJAX request
            String requestedWith = request.getHeader("X-Requested-With");
            boolean isAjax = "XMLHttpRequest".equals(requestedWith);
            
            if (isAjax) {
                // Send JSON response for AJAX requests
                Map<String, Object> responseData = new HashMap<>();
                responseData.put("success", true);
                responseData.put("message", "Order placed successfully");
                responseData.put("orderId", orderId);

                if ("vnpay".equalsIgnoreCase(paymentMethod)) {
                    String vnPayUrl = request.getContextPath() + "/payment/vnpay?orderId=" + orderId + "&amount=" + orderTotalBigDecimal.toPlainString();
                    responseData.put("paymentUrl", vnPayUrl);
                }

                response.setContentType("application/json");
                response.getWriter().write(objectMapper.writeValueAsString(responseData));
            } else {
                // Redirect to confirmation page for form submissions
                response.sendRedirect(request.getContextPath() + "/orders/confirm?orderId=" + orderId + 
                                    "&success=" + java.net.URLEncoder.encode("Order placed successfully!", "UTF-8"));
            }        } catch (Exception e) {
            // Log the full error for server-side debugging
            AppLogger.error("OrderServlet", "placeOrder", "Error placing order for user " + user.getUserId(), e);
            
            // Check if this is an AJAX request
            String requestedWith = request.getHeader("X-Requested-With");
            boolean isAjax = "XMLHttpRequest".equals(requestedWith);
            
            if (isAjax) {
                sendJsonResponse(response, false, "Error placing order. Please try again or contact support.");
            } else {
                response.sendRedirect(request.getContextPath() + "/orders/checkout?error=" + 
                                    java.net.URLEncoder.encode("Error placing order. Please try again.", "UTF-8"));
            }
        }
    }    private void showOrderHistory(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        User user = getCurrentUser(request);
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        List<Order> orders = orderDAO.getOrdersByUserId(user.getUserId());

        // Load order details for each order to display items
        for (Order order : orders) {
            List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(order.getOrderId());
            order.setOrderDetails(orderDetails);
        }
        
        request.setAttribute("lstOrder", orders);
        request.getRequestDispatcher("/order-history.jsp").forward(request, response);
    }
    
    private void showOrderDetails(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
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
            
            // Check if user owns this order (unless admin/staff)
            if (!isAdminOrStaff(request) && order.getUserId() != user.getUserId()) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Access denied");
                return;
            }
              List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);
            
            request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            request.getRequestDispatcher("/order-detail.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid order ID");
        }
    }
    
    private void showOrderConfirmation(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null) {
            response.sendRedirect(request.getContextPath() + "/orders/history");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                response.sendRedirect(request.getContextPath() + "/orders/history?error=Order not found");
                return;
            }
            
            List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);
              request.setAttribute("order", order);
            request.setAttribute("orderDetails", orderDetails);
            request.getRequestDispatcher("/confirmation.jsp").forward(request, response);
            
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/orders/history?error=Invalid order ID");
        }
    }
    
    private void updateOrderStatus(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        // Only admin and staff can update order status        User user = getCurrentUser(request);
        if (!isAdminOrStaff(request)) {
            sendJsonResponse(response, false, "Access denied");
            return;
        }
        
        String orderIdStr = request.getParameter("orderId");
        String status = request.getParameter("status");
        
        if (orderIdStr == null || status == null) {
            sendJsonResponse(response, false, "Order ID and status are required");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            
            // Validate status
            if (!isValidOrderStatus(status)) {
                sendJsonResponse(response, false, "Invalid order status");
                return;
            }
            
            orderDAO.updateOrderStatus(orderId, status);
            sendJsonResponse(response, true, "Order status updated successfully");
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid order ID");
        }
    }
    
    private void cancelOrder(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        User user = getCurrentUser(request);
        if (user == null) {
            sendJsonResponse(response, false, "Please login to cancel order");
            return;
        }
        
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null) {
            sendJsonResponse(response, false, "Order ID is required");
            return;
        }
        
        try {
            int orderId = Integer.parseInt(orderIdStr);
            Order order = orderDAO.getOrderById(orderId);
            
            if (order == null) {
                sendJsonResponse(response, false, "Order not found");
                return;
            }
            
            // Check if user owns this order (unless admin/staff)
            if (!isAdminOrStaff(request) && order.getUserId() != user.getUserId()) {
                sendJsonResponse(response, false, "Access denied");
                return;
            }
            
            // Check if order can be cancelled
            if ("cancelled".equals(order.getStatus()) || "delivered".equals(order.getStatus())) {
                sendJsonResponse(response, false, "Order cannot be cancelled");
                return;
            }
            
            // Update order status to cancelled
            orderDAO.updateOrderStatus(orderId, "cancelled");
            
            // Restore stock if order was confirmed
            if ("confirmed".equals(order.getStatus()) || "processing".equals(order.getStatus())) {
                List<OrderDetail> orderDetails = orderDetailDAO.getOrderDetailsByOrderId(orderId);
                for (OrderDetail detail : orderDetails) {
                    shoeDAO.updateStock(detail.getShoeId(), detail.getSizeId(), detail.getQuantity());
                }
            }
            
            sendJsonResponse(response, true, "Order cancelled successfully");
            
        } catch (NumberFormatException e) {
            sendJsonResponse(response, false, "Invalid order ID");
        }
    }
    
    private boolean isValidOrderStatus(String status) {
        return "pending".equals(status) || "confirmed".equals(status) || 
               "processing".equals(status) || "shipped".equals(status) || 
               "delivered".equals(status) || "cancelled".equals(status);
    }
    
    private void sendJsonResponse(HttpServletResponse response, boolean success, String message) 
            throws IOException {
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("success", success);
        responseMap.put("message", message);
        
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(responseMap));
    }
      private void handleError(HttpServletResponse response, String message, Exception e) 
            throws IOException {
        AppLogger.error("OrderServlet", "handleError", message, e);
        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
    }

    private void showCheckout(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException, SQLException {
        
        User user = getCurrentUser(request);
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }
        
        // Get cart items for the user
        List<CartItem> cartItems = cartDAO.getCartByUserId(user.getUserId());
        
        if (cartItems.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/carts/view?error=" + 
                                java.net.URLEncoder.encode("Your cart is empty", "UTF-8"));
            return;
        }
        
        // Calculate cart total
        double cartTotal = cartDAO.getCartTotal(user.getUserId());
        
        // Set attributes for the checkout page
        request.setAttribute("cartItems", cartItems);
        request.setAttribute("cartTotal", cartTotal);
        request.setAttribute("cartItemCount", cartItems.size());
        
        // Forward to checkout page
        request.getRequestDispatcher("/checkout.jsp").forward(request, response);
    }

    private void handleValidationError(HttpServletRequest request, HttpServletResponse response, 
                                     String message) throws IOException {
        String requestedWith = request.getHeader("X-Requested-With");
        boolean isAjax = "XMLHttpRequest".equals(requestedWith);
        
        if (isAjax) {
            sendJsonResponse(response, false, message);
        } else {
            response.sendRedirect(request.getContextPath() + "/orders/checkout?error=" + 
                                java.net.URLEncoder.encode(message, "UTF-8"));
        }
    }
}
