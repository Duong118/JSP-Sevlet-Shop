/**
 * Main application JavaScript for Shoe Store
 * Handles dropdown functionality, navigation, and common UI interactions
 */

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', function() {
    initializeDropdowns();
    initializeTooltips();
    initializeNavigation();
});

/**
 * Initialize Bootstrap dropdowns with fallback for problematic pages
 */
function initializeDropdowns() {
    // Initialize all dropdowns using Bootstrap
    var dropdownElementList = [].slice.call(document.querySelectorAll('.dropdown-toggle'));
    var dropdownList = dropdownElementList.map(function (dropdownToggleEl) {
        return new bootstrap.Dropdown(dropdownToggleEl);
    });

    // Fallback: Manual dropdown handling for cases where Bootstrap fails
    dropdownElementList.forEach(function(dropdown) {
        dropdown.addEventListener('click', function(e) {
            e.preventDefault();
            e.stopPropagation();
            
            // Close all other dropdowns first
            document.querySelectorAll('.dropdown-menu.show').forEach(function(menu) {
                if (menu !== this.nextElementSibling) {
                    menu.classList.remove('show');
                }
            }.bind(this));
            
            // Toggle current dropdown
            var menu = this.nextElementSibling;
            if (menu && menu.classList.contains('dropdown-menu')) {
                menu.classList.toggle('show');
            }
        });
    });

    // Close dropdowns when clicking outside
    document.addEventListener('click', function(e) {
        if (!e.target.closest('.dropdown')) {
            document.querySelectorAll('.dropdown-menu.show').forEach(function(menu) {
                menu.classList.remove('show');
            });
        }
    });

    // Also support hover for desktop (but keep click for mobile)
    if (window.innerWidth > 768) {
        document.querySelectorAll('.dropdown').forEach(function(dropdown) {
            dropdown.addEventListener('mouseenter', function() {
                var menu = this.querySelector('.dropdown-menu');
                if (menu) {
                    menu.classList.add('show');
                }
            });
            
            dropdown.addEventListener('mouseleave', function() {
                var menu = this.querySelector('.dropdown-menu');
                if (menu) {
                    // Small delay to prevent flickering
                    setTimeout(function() {
                        menu.classList.remove('show');
                    }, 100);
                }
            });
        });
    }
}

/**
 * Initialize Bootstrap tooltips
 */
function initializeTooltips() {
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'));
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
        return new bootstrap.Tooltip(tooltipTriggerEl);
    });
}

/**
 * Initialize navigation-related functionality
 */
function initializeNavigation() {
    // Highlight current page in navigation
    highlightCurrentPage();
    
    // Mobile menu handling
    handleMobileMenu();
}

/**
 * Highlight the current page in navigation
 */
function highlightCurrentPage() {
    var currentPath = window.location.pathname;
    var navLinks = document.querySelectorAll('.navbar-nav .nav-link');
    
    navLinks.forEach(function(link) {
        var href = link.getAttribute('href');
        if (href && currentPath.includes(href)) {
            link.classList.add('active');
        } else {
            link.classList.remove('active');
        }
    });
}

/**
 * Handle mobile menu functionality
 */
function handleMobileMenu() {
    var navbarToggler = document.querySelector('.navbar-toggler');
    var navbarCollapse = document.querySelector('.navbar-collapse');
    
    if (navbarToggler && navbarCollapse) {
        navbarToggler.addEventListener('click', function() {
            navbarCollapse.classList.toggle('show');
        });
        
        // Close mobile menu when clicking on a link
        var navLinks = navbarCollapse.querySelectorAll('.nav-link');
        navLinks.forEach(function(link) {
            link.addEventListener('click', function() {
                if (window.innerWidth < 992) {
                    navbarCollapse.classList.remove('show');
                }
            });
        });
    }
}

/**
 * Debug function to check dropdown functionality
 */
function debugDropdowns() {
    console.log('=== Dropdown Debug Info ===');
    console.log('Bootstrap version:', typeof bootstrap !== 'undefined' ? 'Loaded' : 'Not loaded');
    console.log('Dropdown toggles found:', document.querySelectorAll('.dropdown-toggle').length);
    console.log('Dropdown menus found:', document.querySelectorAll('.dropdown-menu').length);
    
    document.querySelectorAll('.dropdown').forEach(function(dropdown, index) {
        var toggle = dropdown.querySelector('.dropdown-toggle');
        var menu = dropdown.querySelector('.dropdown-menu');
        console.log(`Dropdown ${index + 1}:`, {
            toggle: toggle ? 'Found' : 'Missing',
            menu: menu ? 'Found' : 'Missing',
            menuVisible: menu ? menu.classList.contains('show') : false
        });
    });
}

// Make debug function available globally
window.debugDropdowns = debugDropdowns;

// Re-initialize dropdowns when content is dynamically loaded
window.reinitializeDropdowns = initializeDropdowns;
