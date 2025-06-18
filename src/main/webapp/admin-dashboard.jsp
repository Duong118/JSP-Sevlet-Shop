<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        .chart-container {
            position: relative;
            height: 300px;
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
        .recent-activity {
            max-height: 400px;
            overflow-y: auto;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-12">
                <h1 class="mb-4">
                    <i class="fas fa-tachometer-alt"></i> Admin Dashboard
                </h1>
            </div>
        </div>

        <!-- Statistics Cards -->
        <div class="row mb-4">
            <div class="col-md-3 mb-3">
                <div class="card dashboard-card bg-primary text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="card-title">Total Revenue</h6>
                                <h3 class="mb-0">$<fmt:formatNumber value="${totalRevenue}" type="number" maxFractionDigits="2"/></h3>
                                <small>All time</small>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-dollar-sign"></i>
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
                                <h6 class="card-title">Total Orders</h6>
                                <h3 class="mb-0">${totalOrders}</h3>
                                <small>All time</small>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-shopping-bag"></i>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-3 mb-3">
                <div class="card dashboard-card bg-info text-white">
                    <div class="card-body">
                        <div class="d-flex justify-content-between">
                            <div>
                                <h6 class="card-title">Total Users</h6>
                                <h3 class="mb-0">${totalUsers}</h3>
                                <small>Registered users</small>
                            </div>
                            <div class="stat-icon">
                                <i class="fas fa-users"></i>
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
        </div>

        <!-- Charts and Quick Actions -->
        <div class="row mb-4">
            <div class="col-md-8">
                <div class="card dashboard-card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-chart-line"></i> Monthly Revenue
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="chart-container">
                            <canvas id="revenueChart"></canvas>
                        </div>
                    </div>
                </div>
            </div>
            
            <div class="col-md-4">
                <div class="card dashboard-card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-bolt"></i> Quick Actions
                        </h5>
                    </div>
                    <div class="card-body">
                        <div class="d-grid gap-2">
                            <a href="${pageContext.request.contextPath}/admin?action=shoes" 
                               class="btn btn-outline-primary quick-action-btn">
                                <i class="fas fa-plus"></i> Add New Shoe
                            </a>
                            <a href="${pageContext.request.contextPath}/admin?action=categories" 
                               class="btn btn-outline-secondary quick-action-btn">
                                <i class="fas fa-tags"></i> Manage Categories
                            </a>
                            <a href="${pageContext.request.contextPath}/admin?action=users" 
                               class="btn btn-outline-info quick-action-btn">
                                <i class="fas fa-users-cog"></i> Manage Users
                            </a>
                            <a href="${pageContext.request.contextPath}/admin?action=orders" 
                               class="btn btn-outline-warning quick-action-btn">
                                <i class="fas fa-list-alt"></i> View Orders
                            </a>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Recent Activity and Top Products -->
        <div class="row">
            <div class="col-md-6">
                <div class="card dashboard-card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-clock"></i> Recent Orders
                        </h5>
                    </div>
                    <div class="card-body recent-activity">
                        <c:choose>
                            <c:when test="${not empty recentOrders}">
                                <c:forEach var="order" items="${recentOrders}">
                                    <div class="d-flex justify-content-between align-items-center mb-3 p-2 border-bottom">
                                        <div>
                                            <strong>Order #${order.orderId}</strong><br>
                                            <small class="text-muted">
                                                ${order.user.firstName} ${order.user.lastName}
                                            </small>
                                        </div>
                                        <div class="text-end">
                                            <div class="fw-bold">$<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></div>
                                            <span class="badge bg-${order.status == 'Pending' ? 'warning' : 
                                                              order.status == 'Processing' ? 'info' : 
                                                              order.status == 'Shipped' ? 'primary' : 
                                                              order.status == 'Delivered' ? 'success' : 'secondary'}">
                                                ${order.status}
                                            </span>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted text-center">No recent orders</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
            
            <div class="col-md-6">
                <div class="card dashboard-card">
                    <div class="card-header">
                        <h5 class="mb-0">
                            <i class="fas fa-star"></i> Top Selling Products
                        </h5>
                    </div>
                    <div class="card-body recent-activity">
                        <c:choose>
                            <c:when test="${not empty topSellingShoes}">
                                <c:forEach var="shoe" items="${topSellingShoes}">
                                    <div class="d-flex justify-content-between align-items-center mb-3 p-2 border-bottom">
                                        <div class="d-flex align-items-center">
                                            <img src="${shoe.imageUrl != null ? shoe.imageUrl : '/images/default-shoe.jpg'}" 
                                                 alt="${shoe.shoeName}" style="width: 40px; height: 40px; object-fit: cover; border-radius: 5px;" class="me-3">
                                            <div>
                                                <strong>${shoe.shoeName}</strong><br>
                                                <small class="text-muted">${shoe.brand}</small>
                                            </div>
                                        </div>
                                        <div class="text-end">
                                            <div class="fw-bold">$<fmt:formatNumber value="${shoe.price}" type="number" maxFractionDigits="2"/></div>
                                            <small class="text-muted">${shoe.soldCount} sold</small>
                                        </div>
                                    </div>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted text-center">No sales data available</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Revenue Chart
        const ctx = document.getElementById('revenueChart').getContext('2d');
        const revenueChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [
                    <c:forEach var="revenue" items="${monthlyRevenue}" varStatus="status">
                        '${revenue.month}'<c:if test="${!status.last}">,</c:if>
                    </c:forEach>
                ],
                datasets: [{
                    label: 'Revenue ($)',
                    data: [
                        <c:forEach var="revenue" items="${monthlyRevenue}" varStatus="status">
                            ${revenue.amount}<c:if test="${!status.last}">,</c:if>
                        </c:forEach>
                    ],
                    borderColor: 'rgb(75, 192, 192)',
                    backgroundColor: 'rgba(75, 192, 192, 0.2)',
                    tension: 0.1,
                    fill: true
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: {
                    legend: {
                        display: false
                    }
                },
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: {
                            callback: function(value) {
                                return '$' + value.toLocaleString();
                            }
                        }
                    }
                }
            }        });
    </script>

<%@ include file="footer.jsp" %>
