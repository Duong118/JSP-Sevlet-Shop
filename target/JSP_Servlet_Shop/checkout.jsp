<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Checkout - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .checkout-section {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 1.5rem;
            margin-bottom: 1rem;
        }
        .order-item {
            border-bottom: 1px solid #dee2e6;
            padding: 0.75rem 0;
        }
        .order-item:last-child {
            border-bottom: none;
        }
        .product-img {
            width: 60px;
            height: 60px;
            object-fit: cover;
            border-radius: 8px;
        }
        .total-section {
            background-color: #e9ecef;
            border-radius: 10px;
            padding: 1.5rem;
        }
        .form-section {
            background-color: white;
            border-radius: 10px;
            padding: 1.5rem;
            margin-bottom: 1rem;
            box-shadow: 0 2px 4px rgba(0,0,0,0.1);
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container mt-4">
        <div class="row mb-4">
            <div class="col-12">
                <nav aria-label="breadcrumb">
                    <ol class="breadcrumb">
                        <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/carts/view">Cart</a></li>
                        <li class="breadcrumb-item active" aria-current="page">Checkout</li>
                    </ol>
                </nav>
                <h1 class="mb-0">
                    <i class="fas fa-credit-card"></i> Checkout
                </h1>
            </div>
        </div>

        <c:if test="${empty cartItems}">
            <div class="alert alert-warning text-center">
                <h4><i class="fas fa-exclamation-triangle"></i> Your cart is empty</h4>
                <p>Please add items to your cart before proceeding to checkout.</p>
                <a href="${pageContext.request.contextPath}/shoes" class="btn btn-primary">
                    <i class="fas fa-shoe-prints"></i> Browse Shoes
                </a>
            </div>
        </c:if>

        <c:if test="${not empty cartItems}">
            <form action="${pageContext.request.contextPath}/orders/place" method="post" id="checkoutForm">
                <div class="row">
                    <!-- Order Form -->
                    <div class="col-lg-8">
                        <!-- Shipping Information -->
                        <div class="form-section">
                            <h4 class="mb-3"><i class="fas fa-truck"></i> Shipping Information</h4>
                            
                            <div class="row">
                                <div class="col-md-6 mb-3">
                                    <label for="fullName" class="form-label">Full Name *</label>
                                    <input type="text" class="form-control" id="fullName" name="fullName" 
                                           value="${sessionScope.user.fullName}" required>
                                </div>
                                <div class="col-md-6 mb-3">
                                    <label for="phoneNumber" class="form-label">Phone Number *</label>
                                    <input type="tel" class="form-control" id="phoneNumber" name="phoneNumber" 
                                           value="${sessionScope.user.phoneNumber}" required>
                                </div>
                            </div>
                            
                            <div class="mb-3">
                                <label for="email" class="form-label">Email Address *</label>
                                <input type="email" class="form-control" id="email" name="email" 
                                       value="${sessionScope.user.email}" required readonly>
                            </div>
                            
                            <div class="mb-3">
                                <label for="shippingAddress" class="form-label">Shipping Address *</label>
                                <textarea class="form-control" id="shippingAddress" name="shippingAddress" 
                                          rows="3" required placeholder="Enter your complete shipping address"></textarea>
                            </div>
                            
                            <div class="mb-3">
                                <label for="notes" class="form-label">Order Notes (Optional)</label>
                                <textarea class="form-control" id="notes" name="notes" 
                                          rows="2" placeholder="Any special instructions for your order"></textarea>
                            </div>
                        </div>

                        <!-- Payment Method -->
                        <div class="form-section">
                            <h4 class="mb-3"><i class="fas fa-credit-card"></i> Payment Method</h4>
                            
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="radio" name="paymentMethod" 
                                               id="cod" value="Cash on Delivery" checked>
                                        <label class="form-check-label" for="cod">
                                            <i class="fas fa-money-bill-wave"></i> Cash on Delivery
                                        </label>
                                    </div>
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="radio" name="paymentMethod" 
                                               id="card" value="Credit/Debit Card">
                                        <label class="form-check-label" for="card">
                                            <i class="fas fa-credit-card"></i> Credit/Debit Card
                                        </label>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="radio" name="paymentMethod" 
                                               id="bank" value="Bank Transfer">
                                        <label class="form-check-label" for="bank">
                                            <i class="fas fa-university"></i> Bank Transfer
                                        </label>
                                    </div>
                                    <div class="form-check mb-2">
                                        <input class="form-check-input" type="radio" name="paymentMethod" 
                                               id="wallet" value="Digital Wallet">
                                        <label class="form-check-label" for="wallet">
                                            <i class="fas fa-wallet"></i> Digital Wallet
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Order Summary -->
                    <div class="col-lg-4">
                        <div class="checkout-section">
                            <h4 class="mb-3"><i class="fas fa-list-alt"></i> Order Summary</h4>
                            
                            <!-- Order Items -->
                            <div class="mb-3">
                                <c:forEach var="item" items="${cartItems}">
                                    <div class="order-item">
                                        <div class="row align-items-center">
                                            <div class="col-3">
                                                <img src="${item.shoeImage != null ? item.shoeImage : '/images/default-shoe.jpg'}" 
                                                     alt="${item.shoeName}" class="product-img">
                                            </div>
                                            <div class="col-6">
                                                <h6 class="mb-1 small">${item.shoeName}</h6>
                                                <small class="text-muted">Size: ${item.sizeNumber}</small><br>
                                                <small class="text-muted">Qty: ${item.quantity}</small>
                                            </div>
                                            <div class="col-3 text-end">
                                                <strong>$<fmt:formatNumber value="${item.price * item.quantity}" type="number" maxFractionDigits="2"/></strong>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                            
                            <!-- Totals -->
                            <div class="total-section">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Subtotal:</span>
                                    <span>$<fmt:formatNumber value="${cartTotal}" type="number" maxFractionDigits="2"/></span>
                                </div>
                                
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Shipping:</span>
                                    <span class="text-success">FREE</span>
                                </div>
                                
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Tax (8%):</span>
                                    <span>$<fmt:formatNumber value="${cartTotal * 0.08}" type="number" maxFractionDigits="2"/></span>
                                </div>
                                
                                <hr>
                                
                                <div class="d-flex justify-content-between mb-3">
                                    <strong>Total:</strong>
                                    <strong>$<fmt:formatNumber value="${cartTotal * 1.08}" type="number" maxFractionDigits="2"/></strong>
                                </div>
                                
                                <!-- Place Order Button -->
                                <div class="d-grid gap-2">
                                    <button type="submit" class="btn btn-success btn-lg">
                                        <i class="fas fa-check-circle"></i> Place Order
                                    </button>
                                    <a href="${pageContext.request.contextPath}/carts/view" class="btn btn-outline-secondary">
                                        <i class="fas fa-arrow-left"></i> Back to Cart
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>
        // Form validation
        document.getElementById('checkoutForm').addEventListener('submit', function(e) {
            const requiredFields = ['fullName', 'phoneNumber', 'email', 'shippingAddress'];
            let isValid = true;
            
            requiredFields.forEach(function(fieldId) {
                const field = document.getElementById(fieldId);
                if (!field.value.trim()) {
                    isValid = false;
                    field.classList.add('is-invalid');
                } else {
                    field.classList.remove('is-invalid');
                }
            });
            
            if (!isValid) {
                e.preventDefault();
                alert('Please fill in all required fields.');
                return false;
            }
            
            // Confirm order placement
            if (!confirm('Are you sure you want to place this order?')) {
                e.preventDefault();
                return false;
            }
        });

        // Phone number validation
        document.getElementById('phoneNumber').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length > 10) {
                value = value.slice(0, 10);
            }
            e.target.value = value;
        });
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
