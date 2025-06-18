<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>User Profile - Shoe Store</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        .profile-card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }        .profile-header {
            background: linear-gradient(135deg, #667eea, #764ba2);
            color: white;
            border-radius: 15px 15px 0 0;
            padding: 2rem;
        }
        .profile-avatar {
            width: 100px;
            height: 100px;
            background-color: rgba(255, 255, 255, 0.2);
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 3rem;
            margin-bottom: 1rem;
        }
        .nav-pills .nav-link {
            border-radius: 25px;
            margin-right: 0.5rem;
        }
        .nav-pills .nav-link.active {
            background-color: #007bff;
        }        .form-floating > label {
            color: #6c757d;
        }
    </style>
</head>
<body>
    <%@ include file="header.jsp" %>

    <div class="container mt-4">
        <div class="row">
            <div class="col-lg-4">
                <div class="card profile-card">
                    <div class="profile-header text-center">                        <div class="profile-avatar mx-auto">
                            <i class="fas fa-user"></i>
                        </div>
                        <h4>${user.fullName}</h4>
                        <p class="mb-0">${user.email}</p>
                        <small>Member since joining the store</small>
                    </div>
                    <div class="card-body">
                        <ul class="nav nav-pills flex-column" id="profileTabs" role="tablist">                            <li class="nav-item" role="presentation">
                                <button class="nav-link active w-100 text-start mb-2" id="profile-tab" 
                                        data-bs-toggle="pill" data-bs-target="#profile" type="button" role="tab">
                                    <i class="fas fa-user me-2"></i> Profile Information
                                </button>
                            </li>
                            <li class="nav-item" role="presentation">
                                <button class="nav-link w-100 text-start mb-2" id="password-tab" 
                                        data-bs-toggle="pill" data-bs-target="#password" type="button" role="tab">
                                    <i class="fas fa-lock me-2"></i> Change Password
                                </button>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
            
            <div class="col-lg-8">
                <div class="tab-content" id="profileTabsContent">
                    <!-- Profile Information Tab -->
                    <div class="tab-pane fade show active" id="profile" role="tabpanel">
                        <div class="card profile-card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-user-edit"></i> Profile Information
                                </h5>
                            </div>                            <div class="card-body">
                                <!-- Alert Messages -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                
                                <c:if test="${not empty success}">
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        <i class="fas fa-check-circle me-2"></i>${success}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                
                                <form action="${pageContext.request.contextPath}/auth/updateProfile" method="post" id="profileForm">
                                    <input type="hidden" name="action" value="update">
                                      <div class="mb-3">
                                        <div class="form-floating">
                                            <input type="text" class="form-control" id="fullName" 
                                                   name="fullName" value="${user.fullName}" required>
                                            <label for="fullName">Full Name</label>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <div class="form-floating">
                                            <input type="email" class="form-control" id="email" 
                                                   name="email" value="${user.email}" required>
                                            <label for="email">Email Address</label>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <div class="form-floating">
                                            <input type="tel" class="form-control" id="phoneNumber" 
                                                   name="phoneNumber" value="${user.phoneNumber}">
                                            <label for="phoneNumber">Phone Number</label>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <div class="form-floating">
                                            <textarea class="form-control" id="address" name="address" 
                                                      style="height: 100px">${user.address}</textarea>
                                            <label for="address">Address</label>
                                        </div>
                                    </div>
                                    
                                    <div class="d-grid">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-save"></i> Update Profile
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>                    </div>
                    
                    <!-- Change Password Tab -->
                    <div class="tab-pane fade" id="password" role="tabpanel">
                        <div class="card profile-card">
                            <div class="card-header">
                                <h5 class="mb-0">
                                    <i class="fas fa-key"></i> Change Password
                                </h5>
                            </div>                            <div class="card-body">
                                <!-- Alert Messages for Password Change -->
                                <c:if test="${not empty error}">
                                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                        <i class="fas fa-exclamation-triangle me-2"></i>${error}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                
                                <c:if test="${not empty success}">
                                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                                        <i class="fas fa-check-circle me-2"></i>${success}
                                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                                    </div>
                                </c:if>
                                
                                <form action="${pageContext.request.contextPath}/auth/changePassword" method="post" id="passwordForm">
                                    <input type="hidden" name="action" value="changePassword">
                                    
                                    <div class="mb-3">
                                        <div class="form-floating">
                                            <input type="password" class="form-control" id="currentPassword" 
                                                   name="currentPassword" required>
                                            <label for="currentPassword">Current Password</label>
                                        </div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <div class="form-floating">
                                            <input type="password" class="form-control" id="newPassword" 
                                                   name="newPassword" required minlength="6">
                                            <label for="newPassword">New Password</label>
                                        </div>
                                        <div class="form-text">Password must be at least 6 characters long.</div>
                                    </div>
                                    
                                    <div class="mb-3">
                                        <div class="form-floating">
                                            <input type="password" class="form-control" id="confirmPassword" 
                                                   name="confirmPassword" required>
                                            <label for="confirmPassword">Confirm New Password</label>
                                        </div>
                                    </div>
                                    
                                    <div class="d-grid">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="fas fa-key"></i> Change Password
                                        </button>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>        </div>
    </div>

    <!-- Success/Error Modal -->
    <div class="modal fade" id="messageModal" tabindex="-1" aria-labelledby="messageModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header" id="modalHeader">
                    <h5 class="modal-title" id="messageModalLabel">
                        <i id="modalIcon" class="me-2"></i>
                        <span id="modalTitle">Notification</span>
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <p id="modalMessage" class="mb-0"></p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal">OK</button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>    <script>
        // Check for error messages and show modal (success messages only shown as alerts)
        <c:if test="${not empty error}">
            showModal('error', 'Error!', '${error}');
        </c:if>

        function showModal(type, title, message) {
            const modal = new bootstrap.Modal(document.getElementById('messageModal'));
            const modalHeader = document.getElementById('modalHeader');
            const modalIcon = document.getElementById('modalIcon');
            const modalTitle = document.getElementById('modalTitle');
            const modalMessage = document.getElementById('modalMessage');

            if (type === 'success') {
                modalHeader.className = 'modal-header bg-success text-white';
                modalIcon.className = 'fas fa-check-circle me-2';
            } else {
                modalHeader.className = 'modal-header bg-danger text-white';
                modalIcon.className = 'fas fa-exclamation-triangle me-2';
            }

            modalTitle.textContent = title;
            modalMessage.textContent = message;
            modal.show();
        }

        // Password confirmation validation
        document.getElementById('passwordForm').addEventListener('submit', function(e) {
            const newPassword = document.getElementById('newPassword').value;
            const confirmPassword = document.getElementById('confirmPassword').value;
            
            if (newPassword !== confirmPassword) {
                e.preventDefault();
                showModal('error', 'Validation Error', 'New password and confirmation password do not match.');
                return;
            }
            
            if (newPassword.length < 6) {
                e.preventDefault();
                showModal('error', 'Validation Error', 'Password must be at least 6 characters long.');
                return;
            }
        });

        // Profile form submission with loading state
        document.getElementById('profileForm').addEventListener('submit', function(e) {
            const submitBtn = this.querySelector('button[type="submit"]');
            const originalText = submitBtn.innerHTML;
            submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Updating...';
            submitBtn.disabled = true;
            
            // Re-enable button after 3 seconds if form doesn't submit
            setTimeout(() => {
                submitBtn.innerHTML = originalText;
                submitBtn.disabled = false;
            }, 3000);
        });

        // Phone number formatting
        document.getElementById('phoneNumber').addEventListener('input', function(e) {
            let value = e.target.value.replace(/\D/g, '');
            if (value.length >= 6) {
                value = value.replace(/(\d{3})(\d{3})(\d{4})/, '($1) $2-$3');
            } else if (value.length >= 3) {
                value = value.replace(/(\d{3})(\d{3})/, '($1) $2');
            }
            e.target.value = value;
        });    </script>

<%@ include file="footer.jsp" %>
