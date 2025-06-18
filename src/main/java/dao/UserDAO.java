public boolean updatePassword(int userID, String newPassword) {
    String sql = "UPDATE [User] SET Password = ? WHERE UserID = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, PasswordUtil.hashPassword(newPassword));
        stmt.setInt(2, userID);

        return stmt.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}