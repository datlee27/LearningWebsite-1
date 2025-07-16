package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import org.mindrot.jbcrypt.BCrypt;

import model.User;

public class UserDAO {
    private final DBConnection DBConnection = new DBConnection();
    private static final Logger logger = Logger.getLogger(UserDAO.class.getName());

     public void save(User user) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
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
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close ResultSet: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public User authenticate(String identifier, String password, String googleIdToken) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
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
                    String logPassword = storedPassword != null && !storedPassword.isEmpty() ? storedPassword.substring(0, Math.min(10, storedPassword.length())) + "..." : "null";
                    logger.info("Found user: " + rs.getString("username") + ", stored password hash: " + logPassword);
                    if (storedPassword != null) {
                        if (BCrypt.checkpw(password, storedPassword)) {
                            logger.info("Authentication successful with BCrypt for user: " + rs.getString("username"));
                            return mapToUser(rs);
                        } else if (password != null && password.equals(storedPassword)) {
                            logger.warning("Authentication successful with plain text match (temporary) for user: " + rs.getString("username"));
                            return mapToUser(rs);
                        } else {
                            logger.warning("Password mismatch for identifier: " + identifier + ". Input: " + (password != null ? password.substring(0, Math.min(5, password.length())) + "..." : "null"));
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
            if (conn != null) DBConnection.closeConnection(conn);
        }
        return null;
    }


    public User findByUsername(String username) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {

            conn = DBConnection.getConnection();
            String sql = "SELECT id, username, password, email, role, google_id FROM learning_management.Users WHERE username = ?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close ResultSet: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
    }

    public User findByGoogleId(String googleId) throws SQLException, Exception {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "SELECT id, username, password, email, role, google_id FROM learning_management.Users WHERE google_id = ?;";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, googleId);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                return mapToUser(rs);
            }
            return null;
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close ResultSet: " + e.getMessage());
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                    logger.warning("Failed to close PreparedStatement: " + e.getMessage());
                }
            }
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        }
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
}