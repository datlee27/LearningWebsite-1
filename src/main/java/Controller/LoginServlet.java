package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.gson.Gson;

import dao.ActivityDAO;
import dao.UserDAO;
import jakarta.servlet.ServletException; // <-- ADD THIS IMPORT
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import model.User;

@WebServlet(name = "LoginServlet", urlPatterns = {"/login"})
public class LoginServlet extends HttpServlet {

    private static final String GOOGLE_CLIENT_ID = "463263011713-mrrbjqdmf75o6r3lofr88hougr4imc9a.apps.googleusercontent.com";
    private final UserDAO userDAO = new UserDAO();
    private final ActivityDAO activityDAO = new ActivityDAO();
    private final GoogleIdTokenVerifier verifier;
    private final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(LoginServlet.class.getName());

    public LoginServlet() {
        verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String googleIdTokenString = null;
        if ("application/json".equalsIgnoreCase(request.getContentType())) {
            StringBuilder sb = new StringBuilder();
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
            }
            Map<String, String> jsonPayload = gson.fromJson(sb.toString(), Map.class);
            googleIdTokenString = jsonPayload.get("googleIdToken");
        }

        if (googleIdTokenString != null) {
            handleGoogleLogin(googleIdTokenString, request, response);
        } else {
            handleStandardLogin(request, response);
        }
    }

    private void handleStandardLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usernameOrEmail = request.getParameter("username");
        String password = request.getParameter("password");

        Optional<User> userOpt = userDAO.authenticate(usernameOrEmail, password);

        if (userOpt.isPresent()) {
            setupSession(request.getSession(), userOpt.get());
            response.sendRedirect(request.getContextPath() + "/homePage");
        } else {
            request.setAttribute("error", "Invalid username/email or password.");
            request.getRequestDispatcher("/view/signIn.jsp").forward(request, response);
        }
    }

    private void handleGoogleLogin(String idTokenString, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        Map<String, Object> jsonResponse = new HashMap<>();
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new GeneralSecurityException("Invalid Google ID token.");
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String googleId = payload.getSubject();
            String email = payload.getEmail();

            // This is the corrected logic using the new DAO
            Optional<User> userOpt = userDAO.findByGoogleId(googleId);

            if (userOpt.isPresent()) {
                setupSession(request.getSession(), userOpt.get());
                jsonResponse.put("success", true);
                jsonResponse.put("redirect", request.getContextPath() + "/homePage");
            } else {
                HttpSession session = request.getSession();
                session.setAttribute("tempGoogleId", googleId);
                session.setAttribute("tempEmail", email);
                session.setAttribute("tempUsername", email.split("@")[0]);
                jsonResponse.put("success", true);
                jsonResponse.put("redirect", request.getContextPath() + "/view/selectRole.jsp");
            }

        } catch (GeneralSecurityException | IOException e) {
            logger.warning("Google login failed: " + e.getMessage());
            jsonResponse.put("success", false);
            jsonResponse.put("message", "Google authentication failed.");
        }
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(jsonResponse));
    }

    private void setupSession(HttpSession session, User user) {
        session.setAttribute("user", user);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("role", user.getRole());
        
        try {
            // Assuming ActivityDAO is also refactored for JPA
            activityDAO.logLogin(user);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Failed to log login activity for user: " + user.getUsername(), e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/view/signIn.jsp");
    }
}
