<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Confirmation - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .confirmation-container {
            max-width: 800px;
            margin: 2rem auto;
            padding: 2rem;
        }
        .success-icon {
            font-size: 4rem;
            color: #28a745;
        }
        .order-details {
            background-color: #f8f9fa;
            border-radius: 8px;
            padding: 1.5rem;
            margin: 1rem 0;
        }
        .order-item {
            border-bottom: 1px solid #dee2e6;
            padding: 1rem 0;
        }
        .order-item:last-child {
            border-bottom: none;
        }
        .order-summary {
            background-color: #e9ecef;
            border-radius: 5px;
            padding: 1rem;
        }
        .btn-continue {
            background-color: #007bff;
            border-color: #007bff;
            padding: 0.75rem 2rem;
            font-weight: 500;
        }
        .btn-continue:hover {
            background-color: #0056b3;
            border-color: #0056b3;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <div class="container">
        <div class="confirmation-container">
            <!-- Success Message -->
            <div class="text-center mb-4">
                <i class="fas fa-check-circle success-icon"></i>
                <h1 class="mt-3 mb-2">Order Confirmed!</h1>
                <p class="lead text-muted">Thank you for your purchase. Your order has been successfully placed.</p>
            </div>
            
            <!-- Order Information -->
            <div class="order-details">
                <h3 class="mb-3">Order Details</h3>
                
                <div class="row mb-3">
                    <div class="col-md-6">
                        <strong>Order Number:</strong>
                        <span class="text-primary">#${order.id != null ? order.id : '12345'}</span>
                    </div>
                    <div class="col-md-6">
                        <strong>Order Date:</strong>
                        <fmt:formatDate value="${order.createdDate != null ? order.createdDate : now}" pattern="MMM dd, yyyy" />
                    </div>
                </div>
                
                <div class="row mb-3">
                    <div class="col-md-6">
                        <strong>Customer:</strong>
                        ${sessionScope.user.name != null ? sessionScope.user.name : 'Guest Customer'}
                    </div>
                    <div class="col-md-6">
                        <strong>Email:</strong>
                        ${sessionScope.user.email != null ? sessionScope.user.email : 'guest@example.com'}
                    </div>
                </div>
                
                <!-- Shipping Information -->
                <c:if test="${not empty param.fullName}">
                    <h4 class="mt-4 mb-3">Shipping Information</h4>
                    <div class="row">
                        <div class="col-md-6">
                            <strong>Name:</strong> ${param.fullName}<br>
                            <strong>Phone:</strong> ${param.phone}<br>
                            <strong>Email:</strong> ${param.email}
                        </div>
                        <div class="col-md-6">
                            <strong>Address:</strong><br>
                            ${param.address}<br>
                            ${param.city}, ${param.state} ${param.zipCode}<br>
                            ${param.country}
                        </div>
                    </div>
                </c:if>
                
                <!-- Payment Information -->
                <h4 class="mt-4 mb-3">Payment Information</h4>
                <div class="row">
                    <div class="col-md-6">
                        <strong>Payment Method:</strong>
                        <c:choose>
                            <c:when test="${param.paymentMethod == 'cod'}">Cash on Delivery</c:when>
                            <c:when test="${param.paymentMethod == 'credit'}">Credit Card</c:when>
                            <c:when test="${param.paymentMethod == 'bank'}">Bank Transfer</c:when>
                            <c:when test="${param.paymentMethod == 'digital'}">Digital Wallet</c:when>
                            <c:otherwise>Cash on Delivery</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="col-md-6">
                        <strong>Status:</strong>
                        <span class="badge bg-warning">Processing</span>
                    </div>
                </div>
            </div>
            
            <!-- Order Items -->
            <div class="order-details">
                <h3 class="mb-3">Order Items</h3>
                
                <c:choose>
                    <c:when test="${not empty orderItems}">
                        <c:forEach var="item" items="${orderItems}">
                            <div class="order-item">
                                <div class="row align-items-center">
                                    <div class="col-md-2">
                                        <img src="${item.shoe.imageUrl}" alt="${item.shoe.name}" 
                                             class="img-fluid rounded" style="max-height: 80px;">
                                    </div>
                                    <div class="col-md-6">
                                        <h5 class="mb-1">${item.shoe.name}</h5>
                                        <p class="text-muted mb-1">Brand: ${item.shoe.brand}</p>
                                        <p class="text-muted mb-0">Size: ${item.size}</p>
                                    </div>
                                    <div class="col-md-2 text-center">
                                        <strong>Qty: ${item.quantity}</strong>
                                    </div>
                                    <div class="col-md-2 text-end">
                                        <strong>$<fmt:formatNumber value="${item.shoe.price * item.quantity}" pattern="#,##0.00"/></strong>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <!-- Fallback for when orderItems is not available -->
                        <div class="order-item">
                            <div class="row align-items-center">
                                <div class="col-md-6">
                                    <p class="mb-0">Your ordered items will be processed shortly.</p>
                                </div>
                            </div>
                        </div>
                    </c:otherwise>
                </c:choose>
                
                <!-- Order Summary -->
                <div class="order-summary mt-4">
                    <div class="row">
                        <div class="col-md-8">
                            <h5>Order Summary</h5>
                        </div>
                        <div class="col-md-4 text-end">
                            <c:choose>
                                <c:when test="${not empty order.total}">
                                    <h5>Total: $<fmt:formatNumber value="${order.total}" pattern="#,##0.00"/></h5>
                                </c:when>
                                <c:when test="${not empty sessionScope.cartTotal}">
                                    <h5>Total: $<fmt:formatNumber value="${sessionScope.cartTotal}" pattern="#,##0.00"/></h5>
                                </c:when>
                                <c:otherwise>
                                    <h5>Total: $0.00</h5>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- Next Steps -->
            <div class="alert alert-info" role="alert">
                <h5 class="alert-heading">What's Next?</h5>
                <ul class="mb-0">
                    <li>You will receive an email confirmation shortly</li>
                    <li>Your order will be processed within 1-2 business days</li>
                    <li>You can track your order status in your account</li>
                    <li>Estimated delivery: 3-5 business days</li>
                </ul>
            </div>
            
            <!-- Action Buttons -->
            <div class="text-center mt-4">
                <a href="orders" class="btn btn-outline-primary me-3">
                    <i class="fas fa-list me-2"></i>View Order History
                </a>
                <a href="shoes" class="btn btn-continue">
                    <i class="fas fa-shopping-bag me-2"></i>Continue Shopping
                </a>
            </div>
        </div>
    </div>
    
    <%@ include file="footer.jsp" %>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <!-- Clear cart after successful order -->
    <script>
        // If the user reached this page, the order was successful
        // We can clear any client-side cart data here if needed
        localStorage.removeItem('cart');
        
        // Auto-redirect to home after 30 seconds (optional)
        // setTimeout(function() {
        //     window.location.href = 'shoes';
        // }, 30000);
    </script>
</body>
</html>