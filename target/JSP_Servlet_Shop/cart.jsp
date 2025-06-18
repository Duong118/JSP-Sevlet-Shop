<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shopping Cart - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .cart-item {
            border-bottom: 1px solid #dee2e6;
            padding: 1rem 0;
        }
        .cart-item:last-child {
            border-bottom: none;
        }
        .product-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
        }
        .quantity-controls {
            display: flex;
            align-items: center;
            gap: 10px;
        }
        .quantity-btn {
            width: 35px;
            height: 35px;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
        }
        .total-section {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 1.5rem;
        }
        .empty-cart {
            text-align: center;
            padding: 3rem;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container mt-4">
        <h1 class="mb-4">
            <i class="fas fa-shopping-cart"></i> Shopping Cart
        </h1>

        <c:choose>
            <c:when test="${empty cartItems}">
                <div class="empty-cart">
                    <i class="fas fa-shopping-cart fa-5x mb-3"></i>
                    <h3>Your cart is empty</h3>
                    <p>Add some shoes to get started!</p>
                    <a href="${pageContext.request.contextPath}/shoes" class="btn btn-primary">
                        <i class="fas fa-shoe-prints"></i> Browse Shoes
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="row">
                    <div class="col-lg-8">
                        <div class="card">
                            <div class="card-body">
                                <c:forEach var="item" items="${cartItems}">
                                    <div class="cart-item">
                                        <div class="row align-items-center">
                                            <div class="col-md-2">
                                                <img src="${item.shoeImage != null ? item.shoeImage : '/images/default-shoe.jpg'}" 
                                                     alt="${item.shoeName}" class="product-img">
                                            </div>
                                            <div class="col-md-4">
                                                <h6 class="mb-1">${item.shoeName}</h6>
                                                <small class="text-muted">
                                                    Size: ${item.sizeNumber}
                                                </small>
                                            </div>
                                            <div class="col-md-2">
                                                <strong>$<fmt:formatNumber value="${item.price}" type="number" maxFractionDigits="2"/></strong>
                                            </div>
                                            <div class="col-md-3">
                                                <div class="quantity-controls">
                                                    <form action="${pageContext.request.contextPath}/carts" method="post" style="display: inline;">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="shoeId" value="${item.shoeId}">
                                                        <input type="hidden" name="sizeId" value="${item.sizeId}">
                                                        <input type="hidden" name="quantity" value="${item.quantity - 1}">
                                                        <button type="submit" class="btn btn-outline-secondary quantity-btn" 
                                                                ${item.quantity <= 1 ? 'disabled' : ''}>-</button>
                                                    </form>
                                                    
                                                    <span class="fw-bold">${item.quantity}</span>
                                                    
                                                    <form action="${pageContext.request.contextPath}/carts" method="post" style="display: inline;">
                                                        <input type="hidden" name="action" value="update">
                                                        <input type="hidden" name="shoeId" value="${item.shoeId}">
                                                        <input type="hidden" name="sizeId" value="${item.sizeId}">
                                                        <input type="hidden" name="quantity" value="${item.quantity + 1}">
                                                        <button type="submit" class="btn btn-outline-secondary quantity-btn">+</button>
                                                    </form>
                                                </div>
                                            </div>
                                            <div class="col-md-1">
                                                <form action="${pageContext.request.contextPath}/carts" method="post" style="display: inline;">
                                                    <input type="hidden" name="action" value="remove">
                                                    <input type="hidden" name="shoeId" value="${item.shoeId}">
                                                    <input type="hidden" name="sizeId" value="${item.sizeId}">
                                                    <button type="submit" class="btn btn-outline-danger btn-sm" 
                                                            onclick="return confirm('Remove this item from cart?')">
                                                        <i class="fas fa-trash"></i>
                                                    </button>
                                                </form>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                        
                        <div class="mt-3">
                            <a href="${pageContext.request.contextPath}/shoes" class="btn btn-outline-primary">
                                <i class="fas fa-arrow-left"></i> Continue Shopping
                            </a>
                        </div>
                    </div>
                    
                    <div class="col-lg-4">
                        <div class="total-section">
                            <h5>Order Summary</h5>
                            
                            <div class="d-flex justify-content-between mb-2">
                                <span>Subtotal (${cartItemCount} items):</span>
                                <span>$<fmt:formatNumber value="${cartTotal}" type="number" maxFractionDigits="2"/></span>
                            </div>
                            
                            <div class="d-flex justify-content-between mb-2">
                                <span>Shipping:</span>
                                <span class="text-success">FREE</span>
                            </div>
                            
                            <div class="d-flex justify-content-between mb-2">
                                <span>Tax (estimated):</span>
                                <span>$<fmt:formatNumber value="${cartTotal * 0.08}" type="number" maxFractionDigits="2"/></span>
                            </div>
                            
                            <hr>
                            
                            <div class="d-flex justify-content-between mb-3">
                                <strong>Total:</strong>
                                <strong>$<fmt:formatNumber value="${cartTotal * 1.08}" type="number" maxFractionDigits="2"/></strong>
                            </div>
                            
                            <c:choose>
                                <c:when test="${sessionScope.user != null}">
                                    <div class="d-grid">
                                        <a href="${pageContext.request.contextPath}/orders/checkout" class="btn btn-success btn-lg">
                                            <i class="fas fa-credit-card"></i> Proceed to Checkout
                                        </a>
                                    </div>
                                </c:when>
                                <c:otherwise>
                                    <div class="alert alert-info">
                                        <i class="fas fa-info-circle"></i>                                        Please <a href="${pageContext.request.contextPath}/auth/login">login</a> 
                                        to proceed with checkout.
                                    </div>
                                </c:otherwise>
                            </c:choose>
                        </div>
                        
                        <div class="mt-3">
                            <form action="${pageContext.request.contextPath}/carts" method="post">
                                <input type="hidden" name="action" value="clear">
                                <button type="submit" class="btn btn-outline-danger w-100" 
                                        onclick="return confirm('Clear all items from cart?')">
                                    <i class="fas fa-trash-alt"></i> Clear Cart
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>

    <%@ include file="footer.jsp" %>
</body>
</html>
