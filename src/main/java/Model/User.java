package Model;

/**
 *
 * @author mac
 */
public class User {
    private int id;
    private String username;
    private String password;
    private String email;
    private String role;
    private String googleId; // Added for Google Sign-In

    public User() {
    }

    public User(String username, String password, String email, String role, String googleId) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.googleId = googleId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        if (role != null && (role.equals("student") || role.equals("teacher") || role.equals("admin"))) {
            this.role = role;
        } else {
    
        }
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username=" + username + ", password=" + password + 
               ", email=" + email + ", role=" + role + ", googleId=" + googleId + '}';
    }
}