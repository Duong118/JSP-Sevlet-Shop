<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ include file="header.jsp" %>

<div class="container mt-4">
    <div class="row">
        <!-- Filter Sidebar -->
        <div class="col-md-3">
            <div class="card">
                <div class="card-header">
                    <h5>Filters</h5>
                </div>
                <div class="card-body">                    <form action="${pageContext.request.contextPath}/shoes" method="get">
                        <input type="hidden" name="action" value="filter">
                        <div class="mb-3">
                            <label for="keyword" class="form-label">Search</label>
                            <input type="text" class="form-control" id="keyword" name="keyword" 
                                   value="${keyword}" placeholder="Search shoes...">
                        </div>
                        
                        <div class="mb-3">
                            <label for="category" class="form-label">Category</label>                            <select class="form-select" id="category" name="categoryId">
                                <option value="">All Categories</option>
                                <c:forEach items="${categories}" var="category">
                                    <option value="${category.categoryID}" 
                                            ${selectedCategoryId == category.categoryID ? 'selected' : ''}>
                                        ${category.categoryName}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                          <div class="mb-3">
                            <label for="minPrice" class="form-label">Min Price</label>
                            <input type="number" class="form-control" id="minPrice" name="minPrice" 
                                   value="${minPrice}" min="0" step="0.01">
                        </div>
                        
                        <div class="mb-3">
                            <label for="maxPrice" class="form-label">Max Price</label>
                            <input type="number" class="form-control" id="maxPrice" name="maxPrice" 
                                   value="${maxPrice}" min="0" step="0.01">
                        </div>
                          <div class="mb-3">
                            <label for="sort" class="form-label">Sort By</label>
                            <select class="form-select" id="sort" name="sort">
                                <option value="name" ${sort == 'name' ? 'selected' : ''}>Name</option>
                                <option value="price_asc" ${sort == 'price_asc' ? 'selected' : ''}>Price: Low to High</option>
                                <option value="price_desc" ${sort == 'price_desc' ? 'selected' : ''}>Price: High to Low</option>
                            </select>
                        </div>
                        
                        <button type="submit" class="btn btn-primary w-100">Apply Filters</button>
                        <a href="${pageContext.request.contextPath}/shoes" class="btn btn-outline-secondary w-100 mt-2">Clear Filters</a>
                    </form>
                </div>
            </div>
        </div>
        
        <!-- Shoes Grid -->
        <div class="col-md-9">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <h2>Shoes</h2>
                <span class="text-muted">${shoes.size()} shoes found</span>
            </div>
            
            <c:if test="${empty shoes}">
                <div class="alert alert-info" role="alert">
                    No shoes found matching your criteria.
                </div>
            </c:if>
            
            <div class="row">
                <c:forEach items="${shoes}" var="shoe">
                    <div class="col-md-4 mb-4">
                        <div class="card h-100">
                            <c:choose>
                                <c:when test="${not empty shoe.image}">
                                    <img src="${shoe.image != null ? shoe.image : '/images/default-shoe.jpg'}"
                                         class="card-img-top" alt="${shoe.name}" style="height: 200px; object-fit: cover;">
                                </c:when>
                                <c:otherwise>
                                    <div class="card-img-top bg-light d-flex align-items-center justify-content-center" 
                                         style="height: 200px;">
                                        <span class="text-muted">No Image</span>
                                    </div>
                                </c:otherwise>
                            </c:choose>
                            
                            <div class="card-body d-flex flex-column">
                                <h5 class="card-title">${shoe.name}</h5>
                                <p class="card-text text-muted">${shoe.categoryName}</p>
                                <p class="card-text flex-grow-1">${shoe.description}</p>
                                <div class="mt-auto">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <h6 class="text-primary mb-0">
                                            <fmt:formatNumber value="${shoe.price}" type="currency" currencySymbol="$" />
                                        </h6>
                                        <a href="${pageContext.request.contextPath}/shoes/${shoe.shoeID}" 
                                           class="btn btn-primary btn-sm">View Details</a>
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
                                <a class="page-link" href="?page=${currentPage - 1}&keyword=${keyword}&categoryId=${selectedCategoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sort}">Previous</a>
                            </li>
                        </c:if>
                        
                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="?page=${i}&keyword=${keyword}&categoryId=${selectedCategoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sort}">${i}</a>
                            </li>
                        </c:forEach>
                        
                        <c:if test="${currentPage < totalPages}">
                            <li class="page-item">
                                <a class="page-link" href="?page=${currentPage + 1}&keyword=${keyword}&categoryId=${selectedCategoryId}&minPrice=${minPrice}&maxPrice=${maxPrice}&sort=${sort}">Next</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>
</div>

<%@ include file="footer.jsp" %>
