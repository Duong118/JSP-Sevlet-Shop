private void showProfile(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
    // Check if user is logged in
    if (!isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }

    User user = getCurrentUser(request);
    // Get user's order history for the profile page
    request.setAttribute("orders", orders);        request.setAttribute("user", user);

    request.getRequestDispatcher("/profile.jsp").forward(request, response);
}

private void handleUpdateProfile(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
    if (!isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }
    User currentUser = getCurrentUser(request);
    String fullName = request.getParameter("fullName");
    String email = request.getParameter("email");
    String phoneNumber = request.getParameter("phoneNumber");
    String address = request.getParameter("address");

    AppLogger.debug("AuthServlet", "handleUpdateProfile",
            "Update Profile - Current User ID: " + currentUser.getUserID());
    AppLogger.debug("AuthServlet", "handleUpdateProfile",
            "Update Profile - Current Email: " + currentUser.getEmail());
    AppLogger.debug("AuthServlet", "handleUpdateProfile",
            "Update Profile - New Email: " + email);

    if ("nguyenchiduongp1@gmail.com".equals(email)) {
        userDAO.debugListAllUsers();
    }

    if (fullName == null || email == null || fullName.trim().isEmpty() || email.trim().isEmpty()) {
        AppLogger.warning("AuthServlet", "handleUpdateProfile",
                "Validation failed: Full name and email are required");
        request.setAttribute("error", "Full name and email are required");
        showProfile(request, response);
        return;
    }

    if (!email.equals(currentUser.getEmail())) {
        AppLogger.debug("AuthServlet", "handleUpdateProfile",
                "Email changed, checking if new email exists...");
        if (userDAO.isEmailExistsForOtherUser(email, currentUser.getUserID())) {
            AppLogger.warning("AuthServlet", "handleUpdateProfile",
                    "Email already exists for another user: " + email);
            request.setAttribute("error", "Email '" + email + "' is already in use by another account");
            showProfile(request, response);
            return;
        }
    }

    currentUser.setFullName(fullName);
    currentUser.setEmail(email);
    currentUser.setPhone(phoneNumber != null ? phoneNumber : "");
    currentUser.setAddress(address != null ? address : "");

    boolean success = userDAO.updateUser(currentUser);

    if (success) {
        HttpSession session = request.getSession();
        session.setAttribute("user", currentUser);
        request.setAttribute("success", "Profile updated successfully");
        AppLogger.info("AuthServlet", "handleUpdateProfile",
                "Profile update successful for user: " + currentUser.getEmail());
    } else {
        request.setAttribute("error", "Failed to update profile");
        AppLogger.error("AuthServlet", "handleUpdateProfile",
                "Profile update failed for user: " + currentUser.getEmail());
    }

    showProfile(request, response);
}

private void handleChangePassword(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException, SQLException {
    if (!isLoggedIn(request)) {
        response.sendRedirect(request.getContextPath() + "/auth/login");
        return;
    }

    User currentUser = getCurrentUser(request);
    String currentPassword = request.getParameter("currentPassword");
    String newPassword = request.getParameter("newPassword");
    String confirmPassword = request.getParameter("confirmPassword");

    if (currentPassword == null || newPassword == null || confirmPassword == null ||
            currentPassword.trim().isEmpty() || newPassword.trim().isEmpty() || confirmPassword.trim().isEmpty()) {
        request.setAttribute("error", "All password fields are required");
        showProfile(request, response);
        return;
    }

    if (!newPassword.equals(confirmPassword)) {
        request.setAttribute("error", "New passwords do not match");
        showProfile(request, response);
        return;
    }

    User verifyUser = userDAO.loginUser(currentUser.getUsername(), currentPassword);
    if (verifyUser == null) {
        request.setAttribute("error", "Current password is incorrect");
        showProfile(request, response);
        return;
    }

    boolean success = userDAO.updatePassword(currentUser.getUserID(), newPassword);

    if (success) {
        request.setAttribute("success", "Password changed successfully");
    } else {
        request.setAttribute("error", "Failed to change password");
    }

    showProfile(request, response);
}