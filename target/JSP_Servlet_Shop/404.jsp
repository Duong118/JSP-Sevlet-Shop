<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<c:set var="title" value="Page Not Found - Shoe Store" />
<jsp:include page="header.jsp" />

<style>
    .error-container {
        min-height: 60vh;
        display: flex;
        align-items: center;
        justify-content: center;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        color: white;
        text-align: center;
        position: relative;
        overflow: hidden;
    }
    
    .error-container::before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="shoe-pattern" x="0" y="0" width="20" height="20" patternUnits="userSpaceOnUse"><path d="M5 5h10v10H5z" fill="none" stroke="rgba(255,255,255,0.1)" stroke-width="0.5"/></pattern></defs><rect width="100" height="100" fill="url(%23shoe-pattern)"/></svg>');
        opacity: 0.1;
    }
    
    .error-content {
        position: relative;
        z-index: 2;
        max-width: 600px;
        padding: 2rem;
    }
    
    .error-404 {
        font-size: 8rem;
        font-weight: bold;
        line-height: 1;
        margin-bottom: 1rem;
        text-shadow: 0 4px 8px rgba(0,0,0,0.3);
        background: linear-gradient(45deg, #fff, #f8f9fa);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        background-clip: text;
    }
    
    .error-shoe {
        font-size: 6rem;
        margin: 1rem 0;
        animation: bounce 2s infinite;
        filter: drop-shadow(0 4px 8px rgba(0,0,0,0.3));
    }
    
    @keyframes bounce {
        0%, 20%, 50%, 80%, 100% {
            transform: translateY(0);
        }
        40% {
            transform: translateY(-20px);
        }
        60% {
            transform: translateY(-10px);
        }
    }
    
    .error-message {
        font-size: 1.5rem;
        margin-bottom: 2rem;
        opacity: 0.9;
    }
    
    .error-submessage {
        font-size: 1.1rem;
        margin-bottom: 2rem;
        opacity: 0.8;
    }
    
    .btn-home {
        background: linear-gradient(45deg, #28a745, #20c997);
        border: none;
        padding: 12px 30px;
        font-size: 1.1rem;
        border-radius: 50px;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
        margin: 0.5rem;
        box-shadow: 0 4px 15px rgba(0,0,0,0.2);
    }
    
    .btn-home:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(0,0,0,0.3);
        background: linear-gradient(45deg, #20c997, #28a745);
    }
    
    .btn-shop {
        background: linear-gradient(45deg, #007bff, #6610f2);
        border: none;
        padding: 12px 30px;
        font-size: 1.1rem;
        border-radius: 50px;
        transition: all 0.3s ease;
        text-decoration: none;
        display: inline-block;
        margin: 0.5rem;
        box-shadow: 0 4px 15px rgba(0,0,0,0.2);
    }
    
    .btn-shop:hover {
        transform: translateY(-2px);
        box-shadow: 0 6px 20px rgba(0,0,0,0.3);
        background: linear-gradient(45deg, #6610f2, #007bff);
    }
    
    .floating-shoes {
        position: absolute;
        font-size: 2rem;
        opacity: 0.1;
        animation: float 6s ease-in-out infinite;
    }
    
    .floating-shoes:nth-child(1) {
        top: 10%;
        left: 10%;
        animation-delay: 0s;
    }
    
    .floating-shoes:nth-child(2) {
        top: 20%;
        right: 15%;
        animation-delay: 2s;
    }
    
    .floating-shoes:nth-child(3) {
        bottom: 30%;
        left: 15%;
        animation-delay: 4s;
    }
    
    .floating-shoes:nth-child(4) {
        bottom: 20%;
        right: 10%;
        animation-delay: 1s;
    }
    
    @keyframes float {
        0%, 100% {
            transform: translateY(0px) rotate(0deg);
        }
        33% {
            transform: translateY(-20px) rotate(5deg);
        }
        66% {
            transform: translateY(10px) rotate(-5deg);
        }
    }
    
    .suggestions {
        background: rgba(255,255,255,0.1);
        backdrop-filter: blur(10px);
        border-radius: 15px;
        padding: 2rem;
        margin-top: 3rem;
        border: 1px solid rgba(255,255,255,0.2);
    }
    
    .suggestions h4 {
        margin-bottom: 1.5rem;
        font-weight: 600;
    }
    
    .suggestions ul {
        list-style: none;
        padding: 0;
    }
    
    .suggestions li {
        margin: 0.8rem 0;
        font-size: 1.1rem;
    }
    
    .suggestions a {
        color: #fff;
        text-decoration: none;
        transition: all 0.3s ease;
        display: inline-flex;
        align-items: center;
        gap: 0.5rem;
    }
    
    .suggestions a:hover {
        color: #ffd700;
        transform: translateX(5px);
    }
</style>

<div class="error-container">
    <!-- Floating decorative shoes -->
    <div class="floating-shoes"><i class="fas fa-shoe-prints"></i></div>
    <div class="floating-shoes"><i class="fas fa-running"></i></div>
    <div class="floating-shoes"><i class="fas fa-shoe-prints"></i></div>
    <div class="floating-shoes"><i class="fas fa-running"></i></div>
    
    <div class="error-content">
        <div class="error-404">404</div>
        <div class="error-shoe">
            <i class="fas fa-shoe-prints"></i>
        </div>
        <h2 class="error-message">Oops! You've Lost Your Way</h2>
        <p class="error-submessage">
            It looks like the page you're looking for has walked away! 
            Don't worry, even the best shoes sometimes take a wrong turn.
        </p>
        
        <div class="mt-4">
            <a href="${pageContext.request.contextPath}/home" class="btn btn-home text-white">
                <i class="fas fa-home me-2"></i>Back to Home
            </a>
            <a href="${pageContext.request.contextPath}/shoes" class="btn btn-shop text-white">
                <i class="fas fa-shopping-bag me-2"></i>Browse Shoes
            </a>
        </div>
        
        <div class="suggestions">
            <h4><i class="fas fa-lightbulb me-2"></i>Maybe you were looking for:</h4>
            <ul>
                <li>
                    <a href="${pageContext.request.contextPath}/shoes?category=men">
                        <i class="fas fa-male"></i>
                        Men's Shoes Collection
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/shoes?category=women">
                        <i class="fas fa-female"></i>
                        Women's Shoes Collection
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/shoes?category=kids">
                        <i class="fas fa-child"></i>
                        Kids' Shoes Collection
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/cart?action=view">
                        <i class="fas fa-shopping-cart"></i>
                        Your Shopping Cart
                    </a>
                </li>
                <li>
                    <a href="${pageContext.request.contextPath}/auth?action=login">
                        <i class="fas fa-user"></i>
                        Login to Your Account
                    </a>
                </li>
            </ul>
        </div>
    </div>
</div>

<!-- Additional helpful section -->
<div class="container my-5">
    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-search fa-3x text-primary mb-3"></i>
                    <h5 class="card-title">Search Our Store</h5>
                    <p class="card-text">Use our search feature to find the perfect pair of shoes for any occasion.</p>
                    <form action="${pageContext.request.contextPath}/shoes" method="get" class="mt-3">
                        <div class="input-group">
                            <input type="text" class="form-control" name="search" placeholder="Search for shoes...">
                            <button class="btn btn-primary" type="submit">
                                <i class="fas fa-search"></i>
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <div class="col-md-4 mb-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-star fa-3x text-warning mb-3"></i>
                    <h5 class="card-title">Featured Products</h5>
                    <p class="card-text">Check out our most popular and highly-rated shoes that customers love.</p>
                    <a href="${pageContext.request.contextPath}/shoes?featured=true" class="btn btn-warning text-white">
                        <i class="fas fa-arrow-right me-2"></i>View Featured
                    </a>
                </div>
            </div>
        </div>
        
        <div class="col-md-4 mb-4">
            <div class="card h-100 border-0 shadow-sm">
                <div class="card-body text-center">
                    <i class="fas fa-headset fa-3x text-success mb-3"></i>
                    <h5 class="card-title">Need Help?</h5>
                    <p class="card-text">Our customer service team is here to help you find exactly what you're looking for.</p>
                    <a href="#" class="btn btn-success">
                        <i class="fas fa-phone me-2"></i>Contact Support
                    </a>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="footer.jsp" />