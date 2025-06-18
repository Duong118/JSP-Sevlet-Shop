<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .order-header {
            background: linear-gradient(135deg, #007bff, #0056b3);
            color: white;
            border-radius: 10px;
            padding: 2rem;
            margin-bottom: 2rem;
        }
        .status-timeline {
            position: relative;
            padding-left: 2rem;
        }
        .status-step {
            position: relative;
            padding-bottom: 2rem;
        }
        .status-step::before {
            content: '';
            position: absolute;
            left: -2rem;
            top: 0.5rem;
            width: 12px;
            height: 12px;
            border-radius: 50%;
            background-color: #dee2e6;
        }
        .status-step.completed::before {
            background-color: #28a745;
        }
        .status-step.current::before {
            background-color: #007bff;
            box-shadow: 0 0 0 4px rgba(0, 123, 255, 0.3);
        }
        .status-step::after {
            content: '';
            position: absolute;
            left: -1.75rem;
            top: 1rem;
            width: 2px;
            height: calc(100% - 1rem);
            background-color: #dee2e6;
        }
        .status-step.completed::after {
            background-color: #28a745;
        }
        .status-step:last-child::after {
            display: none;
        }
        .order-item {
            border: 1px solid #dee2e6;
            border-radius: 10px;
            padding: 1rem;
            margin-bottom: 1rem;
        }
        .product-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 8px;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container mt-4">
        <!-- Order Header -->
        <div class="order-header">
            <div class="row align-items-center">
                <div class="col-md-6">
                    <h1 class="mb-2">Order #${order.orderId}</h1>
                    <p class="mb-1">
                        <i class="fas fa-calendar"></i> 
                        Placed on <fmt:formatDate value="${order.orderDate}" pattern="MMMM dd, yyyy 'at' HH:mm"/>
                    </p>
                    <p class="mb-0">
                        <i class="fas fa-credit-card"></i> 
                        Payment: ${order.paymentMethod}
                    </p>
                </div>
                <div class="col-md-6 text-md-end">
                    <div class="mb-2">
                        <span class="badge bg-${order.status == 'Pending' ? 'warning' : 
                                          order.status == 'Processing' ? 'info' : 
                                          order.status == 'Shipped' ? 'primary' : 
                                          order.status == 'Delivered' ? 'success' : 'secondary'} fs-6">
                            ${order.status}
                        </span>
                    </div>
                    <h3 class="mb-0">Total: $<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></h3>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-8">
                <!-- Order Items -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-shopping-bag"></i> Order Items
                        </h5>
                    </div>
                    <div class="card-body">                        <c:forEach var="detail" items="${orderDetails}">
                            <div class="order-item">
                                <div class="row align-items-center">
                                    <div class="col-md-2">
                                        <img src="${detail.shoeImage != null ? detail.shoeImage : '/images/default-shoe.jpg'}" 
                                             alt="${detail.shoeName}" class="product-img">
                                    </div>
                                    <div class="col-md-5">
                                        <h6 class="mb-1">${detail.shoeName}</h6>
                                        <small class="text-muted">
                                            Size: ${detail.sizeNumber}
                                        </small>
                                    </div>
                                    <div class="col-md-2 text-center">
                                        <strong>×${detail.quantity}</strong>
                                    </div>
                                    <div class="col-md-2 text-center">
                                        $<fmt:formatNumber value="${detail.price}" type="number" maxFractionDigits="2"/>
                                    </div>
                                    <div class="col-md-1 text-end">
                                        <strong>$<fmt:formatNumber value="${detail.price * detail.quantity}" type="number" maxFractionDigits="2"/></strong>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                        
                        <div class="row mt-3">
                            <div class="col-md-8"></div>
                            <div class="col-md-4">
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Subtotal:</span>
                                    <span>$<fmt:formatNumber value="${order.totalAmount / 1.08}" type="number" maxFractionDigits="2"/></span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Tax (8%):</span>
                                    <span>$<fmt:formatNumber value="${order.totalAmount * 0.08 / 1.08}" type="number" maxFractionDigits="2"/></span>
                                </div>
                                <div class="d-flex justify-content-between mb-2">
                                    <span>Shipping:</span>
                                    <span class="text-success">FREE</span>
                                </div>
                                <hr>
                                <div class="d-flex justify-content-between">
                                    <strong>Total:</strong>
                                    <strong>$<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></strong>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Shipping Information -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-truck"></i> Shipping Information
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="row">                            <div class="col-md-6">
                                <h6>Shipping Address:</h6>
                                <p class="mb-1"><strong>${order.userName}</strong></p>
                                <p class="mb-1">${order.shippingAddress}</p>
                                <p class="mb-1">
                                    <i class="fas fa-phone"></i> ${order.phoneNumber}
                                </p>
                                <p class="mb-0">
                                    <i class="fas fa-envelope"></i> ${order.userEmail}
                                </p>
                            </div>
                            <div class="col-md-6">
                                <c:if test="${not empty order.notes}">
                                    <h6>Order Notes:</h6>
                                    <p class="text-muted">${order.notes}</p>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <!-- Order Status Timeline -->
                <div class="card mb-4">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-route"></i> Order Status
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="status-timeline">
                            <div class="status-step ${order.status != 'Cancelled' ? 'completed' : ''}">
                                <h6>Order Placed</h6>
                                <small class="text-muted">
                                    <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy HH:mm"/>
                                </small>
                            </div>
                            
                            <div class="status-step ${order.status == 'Processing' || order.status == 'Shipped' || order.status == 'Delivered' ? 'completed' : 
                                                  order.status == 'Processing' ? 'current' : ''}">
                                <h6>Processing</h6>
                                <small class="text-muted">
                                    <c:choose>
                                        <c:when test="${order.status == 'Processing' || order.status == 'Shipped' || order.status == 'Delivered'}">
                                            Order is being prepared
                                        </c:when>
                                        <c:otherwise>
                                            Waiting to be processed
                                        </c:otherwise>
                                    </c:choose>
                                </small>
                            </div>
                            
                            <div class="status-step ${order.status == 'Shipped' || order.status == 'Delivered' ? 'completed' : 
                                                  order.status == 'Shipped' ? 'current' : ''}">
                                <h6>Shipped</h6>
                                <small class="text-muted">
                                    <c:choose>
                                        <c:when test="${order.status == 'Shipped' || order.status == 'Delivered'}">
                                            Order is on the way
                                        </c:when>
                                        <c:otherwise>
                                            Not yet shipped
                                        </c:otherwise>
                                    </c:choose>
                                </small>
                            </div>
                            
                            <div class="status-step ${order.status == 'Delivered' ? 'completed current' : ''}">
                                <h6>Delivered</h6>
                                <small class="text-muted">
                                    <c:choose>
                                        <c:when test="${order.status == 'Delivered'}">
                                            Order delivered successfully
                                        </c:when>
                                        <c:otherwise>
                                            Not yet delivered
                                        </c:otherwise>
                                    </c:choose>
                                </small>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- Admin/Staff Actions -->
                <c:if test="${sessionScope.user.role == 'Admin' || sessionScope.user.role == 'Staff'}">
                    <div class="card">
                        <div class="card-header">
                            <h5 class="mb-0">
                                <i class="fas fa-cogs"></i> Actions
                            </h5>
                        </div>
                        <div class="card-body">
                            <div class="d-grid gap-2">
                                <c:if test="${order.status == 'Pending'}">
                                    <form action="${pageContext.request.contextPath}/${sessionScope.user.role.toLowerCase()}" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="updateOrderStatus">
                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                        <input type="hidden" name="status" value="Processing">
                                        <button type="submit" class="btn btn-primary w-100">
                                            <i class="fas fa-play"></i> Start Processing
                                        </button>
                                    </form>
                                </c:if>
                                
                                <c:if test="${order.status == 'Processing'}">
                                    <form action="${pageContext.request.contextPath}/${sessionScope.user.role.toLowerCase()}" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="updateOrderStatus">
                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                        <input type="hidden" name="status" value="Shipped">
                                        <button type="submit" class="btn btn-success w-100">
                                            <i class="fas fa-shipping-fast"></i> Mark as Shipped
                                        </button>
                                    </form>
                                </c:if>
                                
                                <c:if test="${order.status == 'Shipped'}">
                                    <form action="${pageContext.request.contextPath}/${sessionScope.user.role.toLowerCase()}" method="post" style="display: inline;">
                                        <input type="hidden" name="action" value="updateOrderStatus">
                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                        <input type="hidden" name="status" value="Delivered">
                                        <button type="submit" class="btn btn-success w-100">
                                            <i class="fas fa-check"></i> Mark as Delivered
                                        </button>
                                    </form>
                                </c:if>
                                
                                <c:if test="${order.status != 'Delivered' && order.status != 'Cancelled'}">
                                    <form action="${pageContext.request.contextPath}/${sessionScope.user.role.toLowerCase()}" method="post" 
                                          style="display: inline;" onsubmit="return confirm('Are you sure you want to cancel this order?')">
                                        <input type="hidden" name="action" value="updateOrderStatus">
                                        <input type="hidden" name="orderId" value="${order.orderId}">
                                        <input type="hidden" name="status" value="Cancelled">
                                        <button type="submit" class="btn btn-outline-danger w-100">
                                            <i class="fas fa-times"></i> Cancel Order
                                        </button>
                                    </form>
                                </c:if>
                            </div>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="mt-4">
            <a href="javascript:history.back()" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left"></i> Back
            </a>            <c:if test="${sessionScope.user.userId == order.userId}">
                <a href="${pageContext.request.contextPath}/auth/profile" class="btn btn-outline-primary">
                    <i class="fas fa-user"></i> View Profile
                </a>
            </c:if>
        </div>    </div>

<%@ include file="footer.jsp" %>
