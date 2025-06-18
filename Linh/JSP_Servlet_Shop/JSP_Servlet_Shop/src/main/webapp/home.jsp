<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Shoe Store - Home</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .hero-section {
            background: linear-gradient(rgba(0,0,0,0.5), rgba(0,0,0,0.5)), url('${pageContext.request.contextPath}/images/hero-bg.jpg');
            background-size: cover;
            background-position: center;
            height: 500px;
            display: flex;
            align-items: center;
            color: white;
            text-align: center;
        }
        
        .product-card {
            transition: transform 0.3s;
            height: 100%;
        }
        
        .product-card:hover {
            transform: translateY(-5px);
        }
        
        .product-image {
            height: 200px;
            object-fit: cover;
        }
        
        .category-card {
            text-decoration: none;
            color: inherit;
        }
        
        .category-card:hover {
            text-decoration: none;
            color: inherit;
        }
    </style>
</head>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/home">
                <i class="fas fa-shoe-prints"></i> Shoe Store
            </a>
            
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav me-auto">
                    <li class="nav-item">
                        <a class="nav-link active" href="${pageContext.request.contextPath}/home">Home</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="${pageContext.request.contextPath}/shoes/all">Shoes</a>
                    </li>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                            Categories
                        </a>
                        <ul class="dropdown-menu">
                            <c:forEach items="${categories}" var="category">
                                <li><a class="dropdown-item" href="${pageContext.request.contextPath}/shoes/category?categoryId=${category.categoryId}">${category.categoryName}</a></li>
                            </c:forEach>
                        </ul>
                    </li>
                </ul>
                
                <!-- Search Form -->                <form class="d-flex me-3" action="${pageContext.request.contextPath}/shoes/search" method="get">
                    <input class="form-control me-2" type="search" name="keyword" placeholder="Search shoes...">
                    <button class="btn btn-outline-light" type="submit">
                        <i class="fas fa-search"></i>
                    </button>
                </form>
                
                <!-- User Menu -->
                <ul class="navbar-nav">
                    <c:choose>
                        <c:when test="${sessionScope.user != null}">
                            <!-- Cart -->
                            <li class="nav-item">
                                <a class="nav-link position-relative" href="${pageContext.request.contextPath}/carts/view">
                                    <i class="fas fa-shopping-cart"></i>
                                    <c:if test="${sessionScope.cartCount > 0}">
                                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                                            ${sessionScope.cartCount}
                                        </span>
                                    </c:if>
                                </a>
                            </li>
                            
                            <!-- User Dropdown -->
                            <li class="nav-item dropdown">
                                <a class="nav-link dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown">
                                    <i class="fas fa-user"></i> ${sessionScope.user.fullName}
                                </a>
                                <ul class="dropdown-menu">
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth/profile">Profile</a></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/orders/history">Order History</a></li>
                                    <c:if test="${sessionScope.user.role == 'admin'}">
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/dashboard">Admin Panel</a></li>
                                    </c:if>
                                    <c:if test="${sessionScope.user.role == 'staff' || sessionScope.user.role == 'admin'}">
                                        <li><hr class="dropdown-divider"></li>
                                        <li><a class="dropdown-item" href="${pageContext.request.contextPath}/staff/dashboard">Staff Panel</a></li>
                                    </c:if>
                                    <li><hr class="dropdown-divider"></li>
                                    <li><a class="dropdown-item" href="${pageContext.request.contextPath}/auth/logout">Logout</a></li>
                                </ul>
                            </li>
                        </c:when>
                        <c:otherwise>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/auth/login">Login</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="${pageContext.request.contextPath}/auth/register">Register</a>
                            </li>
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
        </div>
    </nav>
    
    <!-- Alert Messages -->
    <c:if test="${param.error != null}">
        <div class="alert alert-danger alert-dismissible fade show m-3" role="alert">
            <c:out value="${param.error}" />
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    
    <c:if test="${param.success != null}">
        <div class="alert alert-success alert-dismissible fade show m-3" role="alert">
            <c:out value="${param.success}" />
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    </c:if>
    
    <!-- Hero Section -->
    <section class="hero-section">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-lg-8">
                    <h1 class="display-4 fw-bold mb-4">Find Your Perfect Shoes</h1>
                    <p class="lead mb-4">Discover our wide collection of high-quality footwear for every occasion</p>
                    <a href="${pageContext.request.contextPath}/shoes/all" class="btn btn-primary btn-lg">Shop Now</a>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Categories Section -->
    <section class="py-5">
        <div class="container">
            <h2 class="text-center mb-5">Shop by Category</h2>
            <div class="row g-4">
                <c:forEach items="${categories}" var="category" varStatus="status">
                    <div class="col-md-4">
                        <a href="${pageContext.request.contextPath}/shoes/category?categoryId=${category.categoryId}" class="category-card">
                            <div class="card h-100 shadow-sm">
                                <div class="card-body text-center">
                                    <div class="mb-3">
                                        <i class="fas fa-shoe-prints fa-3x text-primary"></i>
                                    </div>
                                    <h5 class="card-title">${category.categoryName}</h5>
                                    <p class="card-text text-muted">${category.description}</p>
                                </div>
                            </div>
                        </a>
                    </div>
                </c:forEach>
            </div>
        </div>
    </section>
    
    <!-- Featured Products -->
    <section class="py-5 bg-light">
        <div class="container">
            <h2 class="text-center mb-5">Featured Products</h2>
            <div class="row g-4">
                <c:forEach items="${featuredShoes}" var="shoe" varStatus="status">
                    <div class="col-lg-3 col-md-6">
                        <div class="card product-card shadow-sm">
                            <c:choose>
                                <c:when test="${shoe.imageUrl != null}">
                                    <img src="${pageContext.request.contextPath}/${shoe.imageUrl}" class="card-img-top product-image" alt="${shoe.shoeName}">
                                </c:when>
                                <c:otherwise>
                                    <div class="card-img-top product-image bg-light d-flex align-items-center justify-content-center">
                                        <i class="fas fa-shoe-prints fa-3x text-muted"></i>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            <div class="card-body">
                                <h5 class="card-title">${shoe.shoeName}</h5>
                                <p class="card-text text-muted small">${shoe.brand}</p>
                                <p class="card-text">
                                    <fmt:formatNumber value="${shoe.price}" type="currency" />
                                </p>
                                <div class="d-flex justify-content-between align-items-center">                                    <a href="${pageContext.request.contextPath}/shoes/${shoe.shoeID}" class="btn btn-primary btn-sm">View Details</a>
                                    <c:if test="${sessionScope.user != null}">
                                        <button class="btn btn-outline-primary btn-sm add-to-cart" 
                                                data-shoe-id="${shoe.shoeID}" 
                                                data-shoe-name="${shoe.name}">
                                            <i class="fas fa-cart-plus"></i>
                                        </button>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>
            
            <div class="text-center mt-4">
                <a href="${pageContext.request.contextPath}/shoes/all" class="btn btn-outline-primary">View All Products</a>
            </div>
        </div>
    </section>
    
    <!-- Why Choose Us -->
    <section class="py-5">
        <div class="container">
            <h2 class="text-center mb-5">Why Choose Us</h2>
            <div class="row g-4">
                <div class="col-md-4 text-center">
                    <div class="mb-3">
                        <i class="fas fa-shipping-fast fa-3x text-primary"></i>
                    </div>
                    <h5>Fast Shipping</h5>
                    <p class="text-muted">Free shipping on orders over $100</p>
                </div>
                <div class="col-md-4 text-center">
                    <div class="mb-3">
                        <i class="fas fa-undo fa-3x text-primary"></i>
                    </div>
                    <h5>Easy Returns</h5>
                    <p class="text-muted">30-day return policy</p>
                </div>
                <div class="col-md-4 text-center">
                    <div class="mb-3">
                        <i class="fas fa-headset fa-3x text-primary"></i>
                    </div>
                    <h5>24/7 Support</h5>
                    <p class="text-muted">Customer support available anytime</p>
                </div>
            </div>
        </div>
    </section>
    
    <!-- Footer -->
    <footer class="bg-dark text-light">
        <div class="container py-4">
            <div class="row">
                <div class="col-md-6">
                    <h5>Shoe Store</h5>
                    <p>Your trusted partner for quality footwear.</p>
                </div>
                <div class="col-md-3">
                    <h5>Quick Links</h5>
                    <ul class="list-unstyled">
                        <li><a href="${pageContext.request.contextPath}/home" class="text-light">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/shoes/all" class="text-light">All Shoes</a></li>
                        <li><a href="${pageContext.request.contextPath}/auth/register" class="text-light">Register</a></li>
                    </ul>
                </div>
                <div class="col-md-3">
                    <h5>Contact</h5>
                    <p><i class="fas fa-envelope"></i> info@shoestore.com</p>
                    <p><i class="fas fa-phone"></i> +1 234 567 8900</p>
                </div>
            </div>
            <hr>
            <div class="text-center">
                <p>&copy; 2024 Shoe Store. All rights reserved.</p>
            </div>
        </div>
    </footer>
    
    <!-- Size Selection Modal -->
    <div class="modal fade" id="sizeModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Select Size</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Select a size for <span id="selectedShoeName"></span>:</p>
                    <div id="sizeOptions" class="d-flex flex-wrap gap-2">
                        <!-- Sizes will be loaded here -->
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                    <button type="button" class="btn btn-primary" id="confirmAddToCart">Add to Cart</button>
                </div>
            </div>
        </div>
    </div>
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Add to cart functionality
        document.addEventListener('DOMContentLoaded', function() {
            const addToCartButtons = document.querySelectorAll('.add-to-cart');
            const sizeModal = new bootstrap.Modal(document.getElementById('sizeModal'));
            let selectedShoeId = null;
            let selectedSizeId = null;
            
            addToCartButtons.forEach(button => {
                button.addEventListener('click', function() {
                    selectedShoeId = this.dataset.shoeId;
                    const shoeName = this.dataset.shoeName;
                    
                    document.getElementById('selectedShoeName').textContent = shoeName;
                    
                    // Load available sizes
                    fetch('${pageContext.request.contextPath}/shoes/sizes?shoeId=' + selectedShoeId)
                        .then(response => response.json())
                        .then(data => {
                            const sizeOptions = document.getElementById('sizeOptions');
                            sizeOptions.innerHTML = '';
                            
                            data.forEach(size => {
                                const sizeButton = document.createElement('button');
                                sizeButton.className = 'btn btn-outline-primary size-option';
                                sizeButton.textContent = size.sizeValue;
                                sizeButton.dataset.sizeId = size.sizeId;
                                sizeButton.addEventListener('click', function() {
                                    document.querySelectorAll('.size-option').forEach(btn => btn.classList.remove('active'));
                                    this.classList.add('active');
                                    selectedSizeId = this.dataset.sizeId;
                                });
                                sizeOptions.appendChild(sizeButton);
                            });
                        });
                    
                    sizeModal.show();
                });
            });            document.getElementById('confirmAddToCart').addEventListener('click', function() {
                if (!selectedSizeId) {
                    alert('Please select a size');
                    return;
                }
                
                // Add to cart
                const formData = new FormData();
                formData.append('shoeId', selectedShoeId);
                formData.append('sizeId', selectedSizeId);
                formData.append('quantity', '1');
                
                fetch('${pageContext.request.contextPath}/carts/add', {
                    method: 'POST',
                    body: formData
                })
                .then(response => response.json())
                .then(data => {
                    if (data.success) {
                        sizeModal.hide();
                        // Update cart count if needed
                        location.reload();
                    } else {
                        alert(data.message);
                    }
                });
            });        });    </script>
    
    <!-- Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

    <%@ include file="footer.jsp" %>
</body>
</html>
