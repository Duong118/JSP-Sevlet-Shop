<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Shoes - Admin Panel</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .shoe-card {
            transition: transform 0.2s;
        }
        .shoe-card:hover {
            transform: translateY(-5px);
        }
        .shoe-img {
            width: 100%;
            height: 200px;
            object-fit: cover;
        }
        .search-controls {
            background-color: #f8f9fa;
            border-radius: 10px;
            padding: 1.5rem;
            margin-bottom: 2rem;
        }
        .btn-action {
            width: 35px;
            height: 35px;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 0 2px;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container-fluid mt-4">
        <div class="row">
            <div class="col-12">
                <div class="d-flex justify-content-between align-items-center mb-4">
                    <h1><i class="fas fa-shoe-prints"></i> Manage Shoes</h1>
                    <button type="button" class="btn btn-success" data-bs-toggle="modal" data-bs-target="#addShoeModal">
                        <i class="fas fa-plus"></i> Add New Shoe
                    </button>
                </div>
            </div>
        </div>

        <!-- Search and Filter Controls -->
        <div class="search-controls">
            <form action="${pageContext.request.contextPath}/admin" method="get" class="row g-3">
                <input type="hidden" name="action" value="shoes">
                <div class="col-md-3">
                    <input type="text" class="form-control" name="search" value="${param.search}" 
                           placeholder="Search shoes...">
                </div>
                <div class="col-md-2">
                    <select class="form-select" name="category">
                        <option value="">All Categories</option>
                        <c:forEach var="category" items="${categories}">
                            <option value="${category.categoryId}" 
                                    ${param.category == category.categoryId ? 'selected' : ''}>
                                ${category.categoryName}
                            </option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <select class="form-select" name="brand">
                        <option value="">All Brands</option>
                        <c:forEach var="brand" items="${brands}">
                            <option value="${brand}" ${param.brand == brand ? 'selected' : ''}>${brand}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="col-md-2">
                    <select class="form-select" name="gender">
                        <option value="">All Genders</option>
                        <option value="Men" ${param.gender == 'Men' ? 'selected' : ''}>Men</option>
                        <option value="Women" ${param.gender == 'Women' ? 'selected' : ''}>Women</option>
                        <option value="Kids" ${param.gender == 'Kids' ? 'selected' : ''}>Kids</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <select class="form-select" name="sortBy">
                        <option value="shoeName" ${param.sortBy == 'shoeName' ? 'selected' : ''}>Name</option>
                        <option value="price" ${param.sortBy == 'price' ? 'selected' : ''}>Price</option>
                        <option value="createdAt" ${param.sortBy == 'createdAt' ? 'selected' : ''}>Date Added</option>
                    </select>
                </div>
                <div class="col-md-1">
                    <button type="submit" class="btn btn-primary w-100">
                        <i class="fas fa-search"></i>
                    </button>
                </div>
            </form>
        </div>

        <!-- Shoes Grid -->
        <div class="row">
            <c:forEach var="shoe" items="${shoes}">
                <div class="col-lg-3 col-md-4 col-sm-6 mb-4">
                    <div class="card shoe-card h-100">
                        <img src="${shoe.imageUrl != null ? shoe.imageUrl : '/images/default-shoe.jpg'}" 
                             class="card-img-top shoe-img" alt="${shoe.shoeName}">
                        <div class="card-body d-flex flex-column">
                            <h6 class="card-title">${shoe.shoeName}</h6>
                            <p class="card-text text-muted small">${shoe.brand}</p>
                            <p class="card-text">
                                <span class="badge bg-secondary">${shoe.category.categoryName}</span>
                                <span class="badge bg-info">${shoe.gender}</span>
                            </p>
                            <p class="card-text">
                                <strong class="text-success">$<fmt:formatNumber value="${shoe.price}" type="number" maxFractionDigits="2"/></strong>
                            </p>
                            <div class="mt-auto">
                                <div class="d-flex justify-content-between align-items-center">
                                    <small class="text-muted">${shoe.totalStock} in stock</small>
                                    <div class="btn-group">
                                        <button type="button" class="btn btn-outline-primary btn-action" 
                                                onclick="viewShoe(${shoe.shoeId})" title="View Details">
                                            <i class="fas fa-eye"></i>
                                        </button>
                                        <button type="button" class="btn btn-outline-warning btn-action" 
                                                onclick="editShoe(${shoe.shoeId})" title="Edit">
                                            <i class="fas fa-edit"></i>
                                        </button>
                                        <button type="button" class="btn btn-outline-danger btn-action" 
                                                onclick="deleteShoe(${shoe.shoeId}, '${shoe.shoeName}')" title="Delete">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Shoes pagination">
                <ul class="pagination justify-content-center">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="?action=shoes&page=${currentPage - 1}&search=${param.search}&category=${param.category}&brand=${param.brand}&gender=${param.gender}&sortBy=${param.sortBy}">
                                Previous
                            </a>
                        </li>
                    </c:if>
                    
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="?action=shoes&page=${i}&search=${param.search}&category=${param.category}&brand=${param.brand}&gender=${param.gender}&sortBy=${param.sortBy}">
                                ${i}
                            </a>
                        </li>
                    </c:forEach>
                    
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="?action=shoes&page=${currentPage + 1}&search=${param.search}&category=${param.category}&brand=${param.brand}&gender=${param.gender}&sortBy=${param.sortBy}">
                                Next
                            </a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </c:if>
    </div>

    <!-- Add Shoe Modal -->
    <div class="modal fade" id="addShoeModal" tabindex="-1">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add New Shoe</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <form action="${pageContext.request.contextPath}/admin" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="action" value="createShoe">
                    <div class="modal-body">
                        <div class="row">
                            <div class="col-md-6 mb-3">
                                <label for="shoeName" class="form-label">Shoe Name *</label>
                                <input type="text" class="form-control" id="shoeName" name="shoeName" required>
                            </div>
                            <div class="col-md-6 mb-3">
                                <label for="brand" class="form-label">Brand *</label>
                                <input type="text" class="form-control" id="brand" name="brand" required>
                            </div>
                        </div>
                        
                        <div class="row">
                            <div class="col-md-4 mb-3">
                                <label for="price" class="form-label">Price *</label>
                                <input type="number" class="form-control" id="price" name="price" 
                                       step="0.01" min="0" required>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="categoryId" class="form-label">Category *</label>
                                <select class="form-select" id="categoryId" name="categoryId" required>
                                    <option value="">Select Category</option>
                                    <c:forEach var="category" items="${categories}">
                                        <option value="${category.categoryId}">${category.categoryName}</option>
                                    </c:forEach>
                                </select>
                            </div>
                            <div class="col-md-4 mb-3">
                                <label for="gender" class="form-label">Gender *</label>
                                <select class="form-select" id="gender" name="gender" required>
                                    <option value="">Select Gender</option>
                                    <option value="Men">Men</option>
                                    <option value="Women">Women</option>
                                    <option value="Kids">Kids</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="mb-3">
                            <label for="color" class="form-label">Color *</label>
                            <input type="text" class="form-control" id="color" name="color" required>
                        </div>
                        
                        <div class="mb-3">
                            <label for="imageFile" class="form-label">Product Image</label>
                            <input type="file" class="form-control" id="imageFile" name="imageFile" 
                                   accept="image/*">
                            <div class="form-text">Upload a product image (JPG, PNG, GIF)</div>
                        </div>
                        
                        <div class="mb-3">
                            <label class="form-label">Available Sizes & Stock</label>
                            <div class="row">
                                <c:forEach var="size" items="${sizes}">
                                    <div class="col-md-4 mb-2">
                                        <div class="input-group">
                                            <span class="input-group-text">
                                                <input type="checkbox" name="sizeIds" value="${size.sizeId}" 
                                                       onchange="toggleStockInput(this, ${size.sizeId})">
                                                ${size.sizeValue}
                                            </span>
                                            <input type="number" class="form-control" name="stock_${size.sizeId}" 
                                                   placeholder="Stock" min="0" disabled>
                                        </div>
                                    </div>
                                </c:forEach>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                        <button type="submit" class="btn btn-success">Add Shoe</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function viewShoe(shoeId) {
            window.open('${pageContext.request.contextPath}/shoes?action=detail&shoeId=' + shoeId, '_blank');
        }
        
        function editShoe(shoeId) {
            window.location.href = '${pageContext.request.contextPath}/admin?action=editShoe&shoeId=' + shoeId;
        }
          function deleteShoe(shoeId, shoeName) {
            confirmDelete(shoeName, function() {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = '${pageContext.request.contextPath}/admin';
                
                const actionInput = document.createElement('input');
                actionInput.type = 'hidden';
                actionInput.name = 'action';
                actionInput.value = 'deleteShoe';
                
                const shoeIdInput = document.createElement('input');
                shoeIdInput.type = 'hidden';
                shoeIdInput.name = 'shoeId';
                shoeIdInput.value = shoeId;
                
                form.appendChild(actionInput);
                form.appendChild(shoeIdInput);
                document.body.appendChild(form);
                form.submit();
            }
        }
        
        function toggleStockInput(checkbox, sizeId) {
            const stockInput = document.querySelector('input[name="stock_' + sizeId + '"]');
            stockInput.disabled = !checkbox.checked;
            if (!checkbox.checked) {
                stockInput.value = '';
            }
        }
    </script>

    <%@ include file="footer.jsp" %>
</body>
</html>
