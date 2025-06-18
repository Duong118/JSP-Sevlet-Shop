<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<footer class="bg-dark text-light mt-5">
    <div class="container">
        <div class="row py-4">
            <div class="col-md-3 mb-3">
                <h5>Shoe Store</h5>
                <p class="text-muted">Your premier destination for quality footwear. Step into style with our curated collection of shoes for every occasion.</p>
                <div class="d-flex gap-3">
                    <a href="#" class="text-light"><i class="fab fa-facebook"></i></a>
                    <a href="#" class="text-light"><i class="fab fa-twitter"></i></a>
                    <a href="#" class="text-light"><i class="fab fa-instagram"></i></a>
                    <a href="#" class="text-light"><i class="fab fa-youtube"></i></a>
                </div>
            </div>
            
            <div class="col-md-3 mb-3">
                <h6>Quick Links</h6>
                <ul class="list-unstyled">
                    <li><a href="${pageContext.request.contextPath}/" class="text-muted text-decoration-none">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/shoes" class="text-muted text-decoration-none">Shop</a></li>
                    <li><a href="${pageContext.request.contextPath}/shoes?category=men" class="text-muted text-decoration-none">Men's Shoes</a></li>
                    <li><a href="${pageContext.request.contextPath}/shoes?category=women" class="text-muted text-decoration-none">Women's Shoes</a></li>
                    <li><a href="${pageContext.request.contextPath}/shoes?category=kids" class="text-muted text-decoration-none">Kids' Shoes</a></li>
                </ul>
            </div>
            
            <div class="col-md-3 mb-3">
                <h6>Customer Service</h6>
                <ul class="list-unstyled">
                    <li><a href="#" class="text-muted text-decoration-none">Contact Us</a></li>
                    <li><a href="#" class="text-muted text-decoration-none">Size Guide</a></li>
                    <li><a href="#" class="text-muted text-decoration-none">Shipping Info</a></li>
                    <li><a href="#" class="text-muted text-decoration-none">Returns & Exchanges</a></li>
                    <li><a href="#" class="text-muted text-decoration-none">FAQ</a></li>
                </ul>
            </div>
            
            <div class="col-md-3 mb-3">
                <h6>Contact Info</h6>
                <ul class="list-unstyled text-muted">
                    <li><i class="fas fa-map-marker-alt me-2"></i>123 Fashion Street, Style City, SC 12345</li>
                    <li><i class="fas fa-phone me-2"></i>(555) 123-4567</li>
                    <li><i class="fas fa-envelope me-2"></i>info@shoestore.com</li>
                    <li><i class="fas fa-clock me-2"></i>Mon-Sat: 9AM-8PM, Sun: 10AM-6PM</li>
                </ul>
            </div>
        </div>
        
        <hr class="my-3">
        
        <div class="row align-items-center">
            <div class="col-md-6">
                <p class="text-muted mb-0">&copy; 2025 Shoe Store. All rights reserved.</p>
            </div>
            <div class="col-md-6 text-md-end">
                <ul class="list-inline mb-0">
                    <li class="list-inline-item">
                        <a href="#" class="text-muted text-decoration-none">Privacy Policy</a>
                    </li>
                    <li class="list-inline-item">
                        <a href="#" class="text-muted text-decoration-none">Terms of Service</a>
                    </li>
                    <li class="list-inline-item">
                        <a href="#" class="text-muted text-decoration-none">Cookie Policy</a>
                    </li>
                </ul>
            </div>
        </div>
    </div>
</footer>

<!-- Back to Top Button -->
<button id="backToTop" class="btn btn-primary position-fixed" 
        style="bottom: 20px; right: 20px; z-index: 1000; border-radius: 50%; width: 50px; height: 50px; display: none;">
    <i class="fas fa-arrow-up"></i>
</button>

<script>
    // Back to Top functionality
    window.addEventListener('scroll', function() {
        const backToTopBtn = document.getElementById('backToTop');
        if (window.pageYOffset > 300) {
            backToTopBtn.style.display = 'block';
        } else {
            backToTopBtn.style.display = 'none';
        }
    });
      document.getElementById('backToTop').addEventListener('click', function() {
        window.scrollTo({
            top: 0,
            behavior: 'smooth'
        });
    });
</script>

<!-- Bootstrap JavaScript - Load first -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

<!-- Custom Application JavaScript -->
<script src="${pageContext.request.contextPath}/js/app.js"></script>

<!-- Backup dropdown initialization for problematic pages -->
<script>
    // Backup initialization in case main script fails
    setTimeout(function() {
        if (typeof window.reinitializeDropdowns === 'function') {
            window.reinitializeDropdowns();
        }
    }, 100);
    
    // Additional fallback for specific problematic URLs
    if (window.location.pathname.includes('/shoes/') || window.location.pathname.includes('/carts/')) {
        setTimeout(function() {
            // Force re-initialize dropdowns for these pages
            var dropdowns = document.querySelectorAll('.dropdown-toggle');
            dropdowns.forEach(function(toggle) {
                if (typeof bootstrap !== 'undefined' && bootstrap.Dropdown) {
                    try {
                        var existingDropdown = bootstrap.Dropdown.getInstance(toggle);
                        if (!existingDropdown) {
                            new bootstrap.Dropdown(toggle);
                        }
                    } catch (e) {
                        console.warn('Dropdown initialization failed for:', toggle, e);
                    }
                }
            });
        }, 200);
    }
</script>
</body>
</html>
