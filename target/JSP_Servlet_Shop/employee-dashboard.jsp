<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Dashboard - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .dashboard-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            transition: transform 0.3s;
        }
        .dashboard-card:hover {
            transform: translateY(-5px);
        }
        .stat-icon {
            font-size: 2.5rem;
            opacity: 0.8;
        }
        .quick-action-btn {
            border-radius: 10px;
            padding: 1rem;
            text-decoration: none;
            transition: all 0.3s;
        }
        .quick-action-btn:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
        }
        .order-item {
            border-bottom: 1px solid #dee2e6;
            padding: 1rem 0;
        }
        .order-item:last-child {
            border-bottom: none;
        }
        .status-badge {
            font-size: 0.8rem;
            padding: 0.3rem 0.6rem;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-12">
                <h1 class="mb-4">
                    <i class="fas fa-user-tie"></i> Staff Dashboard
                </h1>
            </div>
        </div>

        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-3 mb-3">
                <div class="card dashboard-card bg-info text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="card-title">Pending Orders</h6>
                                <h3 class="mb-0">${pendingOrders}</h3>
                                <small>Need attention</small>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-clock"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-3 mb-3">
                <div class="card dashboard-card bg-warning text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="card-title">Processing Orders</h6>
                                <h3 class="mb-0">${processingOrders}</h3>
                                <small>In progress</small>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-cogs"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-3 mb-3">
                <div class="card dashboard-card bg-primary text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="card-title">Total Products</h6>
                                <h3 class="mb-0">${totalShoes}</h3>
                                <small>In inventory</small>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-shoe-prints"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-3 mb-3">
                <div class="card dashboard-card bg-success text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="card-title">Today's Revenue</h6>
                                <h3 class="mb-0">$<fmt:formatNumber value="${todayRevenue}" type="number" maxFractionDigits="2"/></h3>
                                <small>Daily earnings</small>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-dollar-sign"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Quick Actions and Recent Orders -->
        <div class="row mb-4">
            <div class="col-md-4">
                <div class="card dashboard-card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-bolt"></i> Quick Actions
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/staff?action=shoes" 
                               class="btn btn-outline-primary quick-action-btn">
                                <i class="fas fa-plus"></i> Add New Shoe
                            </a>
                            <a href="${pageContext.request.contextPath}/staff?action=inventory" 
                               class="btn btn-outline-secondary quick-action-btn">
                                <i class="fas fa-boxes"></i> Manage Inventory
                            </a>
                            <a href="${pageContext.request.contextPath}/staff?action=categories" 
                               class="btn btn-outline-info quick-action-btn">
                                <i class="fas fa-tags"></i> Manage Categories
                            </a>
                            <a href="${pageContext.request.contextPath}/staff?action=orders" 
                               class="btn btn-outline-warning quick-action-btn">
                                <i class="fas fa-list-alt"></i> Process Orders
                            </a>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-8">
                <div class="card dashboard-card">
                    <div class="card-header d-flex justify-content-between align-items-center">
                        <h5 class="mb-0">
                            <i class="fas fa-list"></i> Recent Orders Requiring Attention
                        </h5>
                        <a href="${pageContext.request.contextPath}/staff?action=orders" class="btn btn-sm btn-outline-primary">
                            View All
                        </a>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty recentOrders}">
                                <c:forEach var="order" items="${recentOrders}">
                                    <div class="order-item">
                                        <div class="row align-items-center">
                                            <div class="col-md-2">
                                                <strong>Order #${order.orderId}</strong>
                                            </div>
                                            <div class="col-md-3">
                                                <div>${order.user.firstName} ${order.user.lastName}</div>
                                                <small class="text-muted">${order.user.email}</small>
                                            </div>
                                            <div class="col-md-2">
                                                <fmt:formatDate value="${order.orderDate}" pattern="MMM dd, HH:mm"/>
                                            </div>
                                            <div class="col-md-2">
                                                <strong>$<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></strong>
                                            </div>
                                            <div class="col-md-2">
                                                <span class="badge status-badge bg-${order.status == 'Pending' ? 'warning' : 
                                                                              order.status == 'Processing' ? 'info' : 
                                                                              order.status == 'Shipped' ? 'primary' : 'secondary'}">
                                                    ${order.status}
                                                </span>
                                            </div>
                                            <div class="col-md-1">
                                                <div class="dropdown">
                                                    <button class="btn btn-sm btn-outline-secondary dropdown-toggle" 
                                                            type="button" data-bs-toggle="dropdown">
                                                        <i class="fas fa-cog"></i>
                                                    </button>
                                                    <ul class="dropdown-menu">
                                                        <li>
                                                            <a class="dropdown-item" 
                                                               href="${pageContext.request.contextPath}/orders?action=view&orderId=${order.orderId}">
                                                                <i class="fas fa-eye"></i> View Details
                                                            </a>
                                                        </li>
                                                        <c:if test="${order.status == 'Pending'}">
                                                            <li>
                                                                <a class="dropdown-item" 
                                                                   href="${pageContext.request.contextPath}/staff?action=updateOrder&orderId=${order.orderId}&status=Processing">
                                                                    <i class="fas fa-play"></i> Start Processing
                                                                </a>
                                                            </li>
                                                        </c:if>
                                                        <c:if test="${order.status == 'Processing'}">
                                                            <li>
                                                                <a class="dropdown-item" 
                                                                   href="${pageContext.request.contextPath}/staff?action=updateOrder&orderId=${order.orderId}&status=Shipped">
                                                                    <i class="fas fa-shipping-fast"></i> Mark as Shipped
                                                                </a>
                                                            </li>
                                                        </c:if>
                                                    </ul>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-4">
                                    <i class="fas fa-check-circle fa-3x text-success mb-3"></i>
                                    <h5>All caught up!</h5>
                                    <p class="text-muted">No orders requiring immediate attention.</p>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <!-- Low Stock Alert -->
        <div class="row">
            <div class="col-12">
                <div class="card dashboard-card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-exclamation-triangle text-warning"></i> Low Stock Alert
                        </h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${not empty lowStockShoes}">
                                <div class="row">
                                    <c:forEach var="shoe" items="${lowStockShoes}">
                                        <div class="col-md-6 col-lg-4 mb-3">
                                            <div class="card border-warning">
                                                <div class="card-body">
                                                    <div class="d-flex align-items-center">
                                                        <img src="${shoe.imageUrl != null ? shoe.imageUrl : '/images/default-shoe.jpg'}" 
                                                             alt="${shoe.shoeName}" 
                                                             style="width: 50px; height: 50px; object-fit: cover; border-radius: 5px;" 
                                                             class="me-3">
                                                        <div class="flex-grow-1">
                                                            <h6 class="mb-1">${shoe.shoeName}</h6>
                                                            <small class="text-muted">${shoe.brand}</small>
                                                            <div class="text-warning fw-bold">
                                                                <i class="fas fa-exclamation-circle"></i> 
                                                                ${shoe.totalStock} left
                                                            </div>
                                                        </div>
                                                        <a href="${pageContext.request.contextPath}/staff?action=editShoe&shoeId=${shoe.shoeId}" 
                                                           class="btn btn-sm btn-outline-primary">
                                                            <i class="fas fa-edit"></i> Update
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <div class="text-center py-3">
                                    <i class="fas fa-check-circle fa-2x text-success mb-2"></i>
                                    <h6>All products are well stocked!</h6>
                                    <small class="text-muted">No items require immediate restocking.</small>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>    </div>

<%@ include file="footer.jsp" %>
