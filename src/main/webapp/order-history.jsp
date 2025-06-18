<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">    <style>        .order-card {
            border: 2px solid #e3e6f0;
            border-radius: 20px;
            transition: all 0.4s ease;
            background: white;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            margin-bottom: 3rem;
            position: relative;
            overflow: hidden;
        }
        .order-card::before {
            content: '';
            position: absolute;
            top: 0;
            left: 0;
            right: 0;
            height: 5px;
            background: linear-gradient(90deg, #667eea 0%, #764ba2 50%, #667eea 100%);
            background-size: 200% 100%;
            animation: gradient-shift 3s ease-in-out infinite;
        }
        @keyframes gradient-shift {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }
        .order-card:hover {
            box-shadow: 0 12px 35px rgba(0,0,0,0.2);
            transform: translateY(-5px);
            border-color: #007bff;
        }        .order-card:nth-child(odd), 
        .order-card:nth-child(even) {
            border-color: #e3f2fd;
            background: linear-gradient(145deg, #ffffff 0%, #f8fdff 100%);
            box-shadow: 0 4px 15px rgba(33, 150, 243, 0.15);
        }
        .order-card:nth-child(odd)::before,
        .order-card:nth-child(even)::before {
            background: linear-gradient(90deg, #667eea 0%, #764ba2 50%, #667eea 100%);
            background-size: 200% 100%;
        }
          /* Enhanced order header styling for better distinction */
        .order-card:nth-child(odd) .order-header,
        .order-card:nth-child(even) .order-header {
            background: linear-gradient(135deg, #e8f4fd 0%, #f0f8ff 100%);
            border-bottom: 3px solid #667eea;
        }
        
        /* Different info-item styling for different orders */
        .order-card:nth-child(odd) .info-item:not(.highlight),
        .order-card:nth-child(even) .info-item:not(.highlight) {
            background: linear-gradient(135deg, #e3f2fd 0%, #bbdefb 100%);
            border-left: 4px solid #667eea;
        }        .order-header {
            background: linear-gradient(135deg, #e8f4fd 0%, #f0f8ff 100%);
            border-radius: 20px 20px 0 0;
            padding: 2rem;
            border-bottom: 3px solid #667eea;
            position: relative;
        }.status-badge {
            font-size: 0.9rem;
            padding: 0.6rem 1.2rem;
            border-radius: 25px;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            position: relative;
            overflow: hidden;
        }
        .status-badge::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.4), transparent);
            transition: left 0.8s;
        }
        .status-badge:hover::before {
            left: 100%;
        }
        .order-item-image {
            width: 80px;
            height: 80px;
            object-fit: cover;
            border-radius: 10px;
            border: 2px solid #f8f9fa;
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
        }
        .page-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 3rem 0;
            margin-bottom: 3rem;
            border-radius: 0 0 30px 30px;
        }
        .empty-state {
            text-align: center;
            padding: 5rem 2rem;
            background: #f8f9fa;
            border-radius: 20px;
            margin: 2rem 0;
        }
        .empty-state i {
            font-size: 5rem;
            color: #6c757d;
            margin-bottom: 1.5rem;
        }        .info-item {
            display: flex;
            align-items: center;
            margin-bottom: 0.8rem;
            padding: 0.6rem 0.8rem;
            background: linear-gradient(135deg, #e8f4fd 0%, #f0f8ff 100%);
            border-radius: 10px;
            border-left: 4px solid #667eea;
            box-shadow: 0 2px 4px rgba(0,0,0,0.05);
            min-height: 55px;
        }
        .info-item.highlight {
            background: linear-gradient(135deg, #fff3cd 0%, #fef9e7 100%);
            border-left: 4px solid #ffc107;
            box-shadow: 0 3px 6px rgba(255, 193, 7, 0.15);
            border: 2px solid rgba(255, 193, 7, 0.3);
            position: relative;
        }
        .info-item.highlight::before {
            content: '';
            position: absolute;
            top: -2px;
            left: -2px;
            right: -2px;
            bottom: -2px;
            background: linear-gradient(45deg, #ffc107, #fd7e14, #ffc107, #fd7e14);
            background-size: 400% 400%;
            border-radius: 12px;
            z-index: -1;
            animation: highlight-glow 3s ease-in-out infinite;
        }
        @keyframes highlight-glow {
            0%, 100% { background-position: 0% 50%; }
            50% { background-position: 100% 50%; }
        }        .info-label {
            font-weight: 700;
            color: #2c3e50;
            min-width: 70px;
            margin-right: 0.3rem;
            text-shadow: 0.5px 0.5px 1px rgba(0,0,0,0.1);
            flex-shrink: 0;
        }        .info-value {
            color: #212529;
            flex: 1;
            font-weight: 500;
            word-break: break-word;
            line-height: 1.3;
        }
        
        /* Special styling for date display */
        .info-value small {
            display: block;
            margin-top: 2px;
            font-size: 0.8rem;
        }.order-summary {
            background: linear-gradient(135deg, #e3f2fd 0%, #f0f8ff 100%);
            border-radius: 12px;
            padding: 1.5rem;
            border: 2px solid rgba(102, 126, 234, 0.3);
            box-shadow: 0 4px 12px rgba(0,0,0,0.1);
        }
        .items-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
            gap: 1rem;
            margin-top: 1rem;
        }        .item-card {
            background: white;
            border: 1px solid #e9ecef;
            border-radius: 10px;
            padding: 1rem;
            display: flex;
            align-items: center;
            transition: all 0.3s ease;
            box-shadow: 0 2px 6px rgba(0,0,0,0.08);
            border-left: 3px solid #667eea;
        }
        .item-card:hover {
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            transform: translateY(-2px);
            border-color: #667eea;
        }
        .item-details {
            margin-left: 1rem;
            flex: 1;
        }
        .item-name {
            font-weight: 600;
            color: #2c3e50;
            margin-bottom: 0.3rem;
        }
        .item-specs {
            color: #6c757d;
            font-size: 0.9rem;
        }
        .action-buttons {
            display: flex;
            gap: 0.5rem;
            flex-wrap: wrap;
        }        .btn-custom {
            border-radius: 20px;
            padding: 0.5rem 1rem;
            font-weight: 500;
            transition: all 0.3s ease;
            position: relative;
            overflow: hidden;
        }
        .btn-custom::before {
            content: '';
            position: absolute;
            top: 0;
            left: -100%;
            width: 100%;
            height: 100%;
            background: linear-gradient(90deg, transparent, rgba(255,255,255,0.3), transparent);
            transition: left 0.6s;
        }
        .btn-custom:hover::before {
            left: 100%;
        }
        .btn-custom:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.2);
        }
          /* Special styling for primary buttons */
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            border: none;
        }        .total-amount {
            font-size: 1.4rem;
            font-weight: 700;
            color: #28a745;
            text-shadow: 1px 1px 2px rgba(0,0,0,0.1);
        }
          /* Responsive adjustments */
        @media (max-width: 768px) {
            .info-label {
                min-width: 60px;
                font-size: 0.9rem;
                margin-right: 0.25rem;
            }
            .info-item {
                padding: 0.5rem 0.6rem;
                min-height: 45px;
            }
            .order-header {
                padding: 1.5rem;
            }
        }
        
        @media (max-width: 576px) {
            .info-label {
                min-width: 50px;
                font-size: 0.85rem;
                margin-right: 0.2rem;
            }
            .total-amount {
                font-size: 1.2rem;
            }
            .status-badge {
                font-size: 0.8rem;
                padding: 0.4rem 0.8rem;
            }
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>    <div class="page-header">
        <div class="container">
            <div class="row align-items-center">
                <div class="col-md-8">
                    <h1 class="mb-0 display-5">
                        <i class="fas fa-history me-3"></i>Order History
                    </h1>
                    <p class="mb-0 mt-3 fs-5 opacity-90">Track your orders and view your complete purchase history</p>
                </div>
                <div class="col-md-4 text-md-end mt-3 mt-md-0">
                    <a href="${pageContext.request.contextPath}/shoes" class="btn btn-light btn-lg btn-custom">
                        <i class="fas fa-shopping-bag me-2"></i>Continue Shopping
                    </a>
                </div>
            </div>
        </div>
    </div>

    <div class="container">
        <c:choose>
            <c:when test="${not empty lstOrder}">
                <div class="row">
                    <div class="col-md-12">                        <c:forEach var="order" items="${lstOrder}">
                            <div class="order-card">                                <div class="order-header">
                                    <div class="row g-2">
                                        <div class="col-xl-3 col-lg-6 col-md-6 mb-2">
                                            <div class="info-item">
                                                <span class="info-label">
                                                    <i class="fas fa-receipt me-1"></i>Order:
                                                </span>
                                                <span class="info-value">
                                                    <strong>#${order.orderId}</strong>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-xl-3 col-lg-6 col-md-6 mb-2">
                                            <div class="info-item">
                                                <span class="info-label">
                                                    <i class="fas fa-calendar me-1"></i>Date:
                                                </span>
                                                <span class="info-value">
                                                    <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, yyyy"/>
                                                    <br><small class="text-muted">
                                                        <fmt:formatDate value="${order.orderDate}" pattern="hh:mm a"/>
                                                    </small>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-xl-3 col-lg-6 col-md-6 mb-2">
                                            <div class="info-item">
                                                <span class="info-label">
                                                    <i class="fas fa-info-circle me-1"></i>Status:
                                                </span>
                                                <span class="info-value">
                                                    <span class="badge status-badge bg-${order.status == 'Pending' ? 'warning' : 
                                                                              order.status == 'Processing' ? 'info' : 
                                                                              order.status == 'Shipped' ? 'primary' : 
                                                                              order.status == 'Delivered' ? 'success' : 
                                                                              order.status == 'Cancelled' ? 'danger' : 'secondary'}">
                                                        ${order.status}
                                                    </span>
                                                </span>
                                            </div>
                                        </div>
                                        <div class="col-xl-3 col-lg-6 col-md-6 mb-2">
                                            <div class="info-item">
                                                <span class="info-label">
                                                    <i class="fas fa-dollar-sign me-1"></i>Total:
                                                </span>
                                                <span class="info-value">
                                                    <span class="total-amount">$<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></span>
                                                </span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                
                                <div class="card-body p-4">
                                    <div class="row">                                        <div class="col-lg-8">
                                            <!-- Shipping Address -->
                                            <div class="info-item highlight mb-3">
                                                <span class="info-label">
                                                    <i class="fas fa-map-marker-alt me-2"></i>Shipping Address:
                                                </span>
                                                <span class="info-value">${order.shippingAddress}</span>
                                            </div>
                                            
                                            <!-- Order Items -->
                                            <c:if test="${not empty order.orderDetails}">
                                                <div class="mb-3">
                                                    <div class="info-item highlight mb-2">
                                                        <span class="info-label">
                                                            <i class="fas fa-box me-2"></i>Items:
                                                        </span>
                                                        <span class="info-value">
                                                            <strong>${order.orderDetails.size()} item(s)</strong>
                                                        </span>
                                                    </div>
                                                    
                                                    <div class="items-grid">
                                                        <c:forEach var="detail" items="${order.orderDetails}" varStatus="status">
                                                            <c:if test="${status.index < 4}">
                                                                <div class="item-card">
                                                                    <img src="${detail.shoeImage != null ? detail.shoeImage : '/images/default-shoe.jpg'}" 
                                                                         alt="${detail.shoeName}" class="order-item-image">
                                                                    <div class="item-details">
                                                                        <div class="item-name">${detail.shoeName}</div>
                                                                        <div class="item-specs">
                                                                            <i class="fas fa-arrow-right me-1"></i>Quantity: <strong>${detail.quantity}</strong><br>
                                                                            <i class="fas fa-arrow-right me-1"></i>Size: <strong>${detail.sizeNumber}</strong><br>
                                                                            <i class="fas fa-arrow-right me-1"></i>Price: <strong>$<fmt:formatNumber value="${detail.price}" type="number" maxFractionDigits="2"/></strong>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </c:if>
                                                        </c:forEach>
                                                        
                                                        <c:if test="${order.orderDetails.size() > 4}">
                                                            <div class="item-card" style="justify-content: center; background: #f8f9fa;">
                                                                <div class="text-center">
                                                                    <i class="fas fa-plus-circle fa-2x text-muted mb-2"></i>
                                                                    <div class="text-muted">
                                                                        <strong>+${order.orderDetails.size() - 4}</strong><br>
                                                                        more items
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </div>
                                        
                                        <div class="col-lg-4">
                                            <div class="order-summary">
                                                <h6 class="mb-3">
                                                    <i class="fas fa-cogs me-2"></i>Order Actions
                                                </h6>
                                                
                                                <div class="action-buttons mb-3">
                                                    <a href="${pageContext.request.contextPath}/orders/details?orderId=${order.orderId}" 
                                                       class="btn btn-primary btn-custom">
                                                        <i class="fas fa-eye me-1"></i>View Details
                                                    </a>
                                                    
                                                    <c:if test="${order.status == 'Pending' || order.status == 'Processing'}">
                                                        <button type="button" class="btn btn-outline-danger btn-custom" 
                                                                onclick="cancelOrder('${order.orderId}')">
                                                            <i class="fas fa-times me-1"></i>Cancel
                                                        </button>
                                                    </c:if>
                                                </div>
                                                
                                                <c:if test="${order.status == 'Delivered'}">
                                                    <div class="action-buttons">
                                                        <a href="${pageContext.request.contextPath}/orders/details?orderId=${order.orderId}" 
                                                           class="btn btn-outline-success btn-custom">
                                                            <i class="fas fa-star me-1"></i>Rate & Review
                                                        </a>
                                                        <a href="${pageContext.request.contextPath}/shoes" 
                                                           class="btn btn-outline-info btn-custom">
                                                            <i class="fas fa-redo me-1"></i>Order Again
                                                        </a>
                                                    </div>
                                                </c:if>
                                                
                                                <c:if test="${order.status == 'Shipped'}">
                                                    <div class="mt-3 p-3 bg-info bg-opacity-10 rounded">
                                                        <h6 class="text-info">
                                                            <i class="fas fa-truck me-2"></i>Tracking Info
                                                        </h6>
                                                        <p class="mb-0 small">Your order is on its way! Expected delivery in 2-3 business days.</p>
                                                    </div>
                                                </c:if>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </div>
                
                <!-- Pagination could go here if needed -->
                <c:if test="${lstOrder.size() > 10}">
                    <div class="row mt-4">
                        <div class="col-12 text-center">
                            <nav aria-label="Order history pagination">
                                <!-- Pagination controls would go here -->
                            </nav>
                        </div>
                    </div>
                </c:if>
                
            </c:when>            <c:otherwise>
                <div class="empty-state">
                    <i class="fas fa-shopping-bag"></i>
                    <h2 class="mb-3">No Orders Yet</h2>
                    <p class="text-muted mb-4 lead">You haven't placed any orders yet. Start shopping to see your order history here!</p>
                    <a href="${pageContext.request.contextPath}/shoes" class="btn btn-primary btn-lg btn-custom">
                        <i class="fas fa-shopping-bag me-2"></i>Start Shopping Now
                    </a>
                </div>
            </c:otherwise>
        </c:choose>    </div>

    <script>
        function cancelOrder(orderId) {
            if (confirm('Are you sure you want to cancel this order?')) {
                fetch('${pageContext.request.contextPath}/orders/cancel', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/x-www-form-urlencoded',
                    },
                    body: 'orderId=' + orderId
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        alert('Order cancelled successfully');
                        location.reload();
                    } else {
                        alert('Error: ' + data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert('An error occurred while cancelling the order');
                });
            }        }
    </script>

<%@ include file="footer.jsp" %>
