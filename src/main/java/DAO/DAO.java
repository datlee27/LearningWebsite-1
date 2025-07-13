package DAO;

import Model.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import org.mindrot.jbcrypt.BCrypt;
import java.util.logging.Logger;

public class DAO {
    private DBcontext dbContext = new DBcontext();
    private static final Logger logger = Logger.getLogger(DAO.class.getName());

    public void save(User user) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            String sql = "INSERT INTO learning_management.Users (username, password, email, role, google_id) VALUES (?, ?, ?, ?, ?)";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword() != null ? BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()) : null);
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getRole());
            pstmt.setString(5, user.getGoogleId());
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
    }

    public User authenticate(String identifier, String password, String googleIdToken) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            if (googleIdToken != null) {
                return null;
            } else {
                String sql = "SELECT id, username, password, email, role, google_id FROM learning_management.Users WHERE (username = ? OR email = ?)";
                logger.info("Authenticating with identifier: " + identifier + ", password length: " + (password != null ? password.length() : 0));
                pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, identifier);
                pstmt.setString(2, identifier);
                rs = pstmt.executeQuery();
                if (rs.next()) {
                    String storedPassword = rs.getString("password");
                    logger.info("Found user: " + rs.getString("username") + ", stored password hash: " + (storedPassword != null ? storedPassword.substring(0, 10) + "..." : "null"));
                    if (storedPassword != null) {
                        if (BCrypt.checkpw(password, storedPassword)) {
                            logger.info("Authentication successful with BCrypt for user: " + rs.getString("username"));
                            return mapToUser(rs);
                        } else if (password != null && password.equals(storedPassword)) {
                            logger.warning("Authentication successful with plain text match (temporary) for user: " + rs.getString("username"));
                            return mapToUser(rs);
                        } else {
                            logger.warning("Password mismatch for identifier: " + identifier + ". Input: " + password.substring(0, Math.min(5, password.length())) + "...");
                        }
                    } else {
                        logger.warning("No password found for identifier: " + identifier);
                    }
                } else {
                    logger.warning("No user found for identifier: " + identifier);
                }
            }
        } catch (SQLException e) {
            logger.severe("Database error during authentication: " + e.getMessage());
            throw e;
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
        return null;
    }

    public User findByGoogleId(String googleId) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            String sql = "SELECT id, username, password, email, role, google_id FROM learning_management.Users WHERE google_id = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, googleId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
        return null;
    }

    public void logLogin(String username) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbContext.getConnection();
            String sql = "INSERT INTO user_activity (username, login_time) VALUES (?, NOW())";
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
    }

    public void logLogout(String username) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbContext.getConnection();
            // Cập nhật logout_time và duration_minutes cho session hiện tại
            String sql = "UPDATE user_activity SET logout_time = NOW(), duration_minutes = TIMESTAMPDIFF(MINUTE, login_time, NOW()) " +
                         "WHERE username = ? AND logout_time IS NULL";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated == 0) {
                logger.warning("No active session found to log out for username: " + username);
            }
        } finally {
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
    }

    public Map<String, Integer> getUserActivity(String username) throws SQLException, Exception {
        Map<String, Integer> onlineTimes = new HashMap<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = dbContext.getConnection();
            // Lấy dữ liệu trong 30 ngày gần nhất
            String sql = "SELECT DATE(login_time) as activity_date, COALESCE(SUM(duration_minutes), 0) as total_minutes " +
                         "FROM user_activity WHERE username = ? AND login_time >= DATE_SUB(NOW(), INTERVAL 30 DAY) " +
                         "GROUP BY DATE(login_time)";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                String date = rs.getString("activity_date");
                int minutes = rs.getInt("total_minutes");
                onlineTimes.put(date, minutes);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) dbContext.closeConnection();
        }
        return onlineTimes;
    }

    private User mapToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setRole(rs.getString("role"));
        user.setGoogleId(rs.getString("google_id"));
        return user;
    }
    public int getTotalActivityForDate(String username, String date) throws SQLException, Exception {
    Connection conn = null;
    PreparedStatement pstmt = null;
    ResultSet rs = null;
    int totalMinutes = 0;
    try {
        conn = dbContext.getConnection();
        String sql = "SELECT COALESCE(SUM(duration_minutes), 0) as total_minutes " +
                    "FROM user_activity WHERE username = ? AND DATE(login_time) = ?";
        pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, username);
        pstmt.setString(2, date);
        rs = pstmt.executeQuery();
        if (rs.next()) {
            totalMinutes = rs.getInt("total_minutes");
        }
    } finally {
        if (rs != null) rs.close();
        if (pstmt != null) pstmt.close();
        if (conn != null) dbContext.closeConnection();
    }
    return totalMinutes;
}
}