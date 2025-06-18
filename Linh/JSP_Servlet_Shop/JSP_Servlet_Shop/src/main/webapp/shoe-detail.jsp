<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${shoe.shoeName} - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .product-img {
            width: 100%;
            height: 400px;
            object-fit: cover;
            border-radius: 10px;
        }
        .size-btn {
            width: 50px;
            height: 50px;
            border-radius: 50%;
            margin: 5px;
        }
        .size-btn.active {
            background-color: #007bff;
            color: white;
        }
        .price-display {
            font-size: 2rem;
            font-weight: bold;
            color: #e74c3c;
        }
        .stock-info {
            font-size: 0.9rem;
            color: #6c757d;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container mt-4">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/">Home</a></li>
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/shoes">Shoes</a></li>
                <li class="breadcrumb-item active">${shoe.shoeName}</li>
            </ol>
        </nav>

        <!-- Alert Messages -->
        <c:if test="${param.error != null}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-triangle me-2"></i>
                <c:out value="${param.error}" />
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>
        
        <c:if test="${param.success != null}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle me-2"></i>
                <c:out value="${param.success}" />
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <div class="row">
            <div class="col-md-6">
                <img src="${shoe.imageUrl != null ? shoe.imageUrl : '/images/default-shoe.jpg'}" 
                     alt="${shoe.shoeName}" class="product-img">
            </div>
            
            <div class="col-md-6">
                <h1 class="mb-3">${shoe.shoeName}</h1>
                  <div class="mb-3">
                    <span class="badge bg-secondary">${shoe.categoryName}</span>
                    <span class="badge bg-info">${shoe.gender}</span>
                </div>
                
                <div class="price-display mb-3">
                    $<fmt:formatNumber value="${shoe.price}" type="number" maxFractionDigits="2"/>
                </div>
                
                <div class="mb-3">
                    <strong>Brand:</strong> ${shoe.brand}<br>
                    <strong>Color:</strong> ${shoe.color}
                </div>                <div class="mb-4">
                    <h5>Available Sizes:</h5>
                    <div id="sizeSelection">
                        <c:choose>
                            <c:when test="${not empty shoeSizes}">
                                <c:forEach var="shoeSize" items="${shoeSizes}">
                                    <c:if test="${shoeSize.stockQuantity > 0}">
                                        <button type="button" 
                                                class="btn btn-outline-primary size-btn" 
                                                data-size-id="${shoeSize.sizeID}"
                                                data-stock="${shoeSize.stockQuantity}">
                                            ${shoeSize.sizeNumber}
                                        </button>
                                    </c:if>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted">No sizes available</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div id="stockInfo" class="stock-info mt-2"></div>
                </div>
                
                <form id="addToCartForm" action="${pageContext.request.contextPath}/carts" method="post">
                    <input type="hidden" name="action" value="add">
                    <input type="hidden" name="shoeId" value="${shoe.shoeID}">
                    <input type="hidden" name="sizeId" id="selectedSizeId">
                    
                    <div class="mb-3">
                        <label for="quantity" class="form-label">Quantity:</label>
                        <div class="input-group" style="width: 150px;">
                            <button type="button" class="btn btn-outline-secondary" onclick="decreaseQuantity()">-</button>
                            <input type="number" class="form-control text-center" id="quantity" name="quantity" 
                                   value="1" min="1" max="10">
                            <button type="button" class="btn btn-outline-secondary" onclick="increaseQuantity()">+</button>
                        </div>
                    </div>
                    
                    <div class="d-grid gap-2">
                        <button type="submit" class="btn btn-primary btn-lg" id="addToCartBtn" disabled>
                            <i class="fas fa-cart-plus"></i> Add to Cart
                        </button>
                        <a href="${pageContext.request.contextPath}/shoes" class="btn btn-outline-secondary">
                            <i class="fas fa-arrow-left"></i> Back to Shoes
                        </a>
                    </div>
                </form>
            </div>
        </div>
        
        <div class="row mt-5">
            <div class="col-12">
                <h3>Product Details</h3>
                <div class="card">
                    <div class="card-body">
                        <p>Experience premium comfort and style with the ${shoe.shoeName}. 
                        This ${shoe.gender.toLowerCase()} shoe from ${shoe.brand} features 
                        exceptional craftsmanship in a stunning ${shoe.color.toLowerCase()} color.</p>
                        
                        <h5>Features:</h5>
                        <ul>
                            <li>Premium materials for durability and comfort</li>
                            <li>Modern design suitable for various occasions</li>
                            <li>Available in multiple sizes</li>
                            <li>Easy care and maintenance</li>
                        </ul>
                        
                        <h5>Care Instructions:</h5>
                        <p>Clean with a soft cloth and appropriate cleaning products. 
                        Store in a cool, dry place when not in use.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        let selectedSize = null;
        let maxStock = 0;
        
        // Size selection
        document.querySelectorAll('.size-btn').forEach(btn => {
            btn.addEventListener('click', function() {
                // Remove active class from all buttons
                document.querySelectorAll('.size-btn').forEach(b => b.classList.remove('active'));
                
                // Add active class to clicked button
                this.classList.add('active');
                
                selectedSize = this.dataset.sizeId;
                maxStock = parseInt(this.dataset.stock);
                
                document.getElementById('selectedSizeId').value = selectedSize;
                document.getElementById('addToCartBtn').disabled = false;
                
                // Update stock info
                const stockInfo = document.getElementById('stockInfo');
                if (maxStock > 0) {
                    stockInfo.innerHTML = `<i class="fas fa-check-circle text-success"></i> ${maxStock} in stock`;
                    document.getElementById('quantity').max = Math.min(maxStock, 10);
                } else {
                    stockInfo.innerHTML = `<i class="fas fa-times-circle text-danger"></i> Out of stock`;
                    document.getElementById('addToCartBtn').disabled = true;
                }
            });
        });
        
        function increaseQuantity() {
            const quantityInput = document.getElementById('quantity');
            const currentValue = parseInt(quantityInput.value);
            const maxValue = parseInt(quantityInput.max);
            
            if (currentValue < maxValue) {
                quantityInput.value = currentValue + 1;
            }
        }
        
        function decreaseQuantity() {
            const quantityInput = document.getElementById('quantity');
            const currentValue = parseInt(quantityInput.value);
            
            if (currentValue > 1) {
                quantityInput.value = currentValue - 1;
            }
        }
          // Form submission with AJAX
        document.getElementById('addToCartForm').addEventListener('submit', function(e) {
            e.preventDefault(); // Prevent default form submission
            
            if (!selectedSize) {
                showError('Please select a size first.');
                return;
            }
            
            if (maxStock <= 0) {
                showError('Selected size is out of stock.');
                return;
            }
            
            // Show loading state
            const submitBtn = document.getElementById('addToCartBtn');
            const originalText = submitBtn.innerHTML;
            submitBtn.disabled = true;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin"></i> Adding...';
            
            // Prepare form data
            const formData = new FormData(this);
            
            // Send AJAX request
            fetch(this.action, {
                method: 'POST',
                headers: {
                    'X-Requested-With': 'XMLHttpRequest'
                },
                body: formData
            })
            .then(response => response.json())
            .then(data => {
                if (data.success) {
                    // Update cart count in navigation if available
                    const cartCountElement = document.querySelector('#cartCount');
                    if (cartCountElement && data.cartCount !== undefined) {
                        cartCountElement.textContent = data.cartCount;
                    }
                    
                    // Show success message and stay on page
                    showSuccess(data.message || 'Item added to cart successfully!');
                } else {
                    showError(data.message || 'Failed to add item to cart');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                showError('An error occurred while adding to cart');
            })
            .finally(() => {
                // Restore button state
                submitBtn.disabled = false;
                submitBtn.innerHTML = originalText;
            });
        });</script>

    <%@ include file="footer.jsp" %>
</body>
</html>
