package Controller;

import DAO.DAO;
import Model.User;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import com.google.gson.Gson;

import com.google.api.client.json.gson.GsonFactory;
import java.io.BufferedReader;


/**
 *
 * @author mac
 */
public class LoginServlet extends HttpServlet {

    private static final String GOOGLE_CLIENT_ID = "463263011713-mrrbjqdmf75o6r3lofr88hougr4imc9a.apps.googleusercontent.com"; // Replace with your Google Client ID
    private final DAO dao = new DAO();
    private final GoogleIdTokenVerifier verifier;
    private final Gson gson = new Gson();

    public LoginServlet() {
      verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
        .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
        .build();

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String usernameOrEmail = request.getParameter("username");
        String password = request.getParameter("password");
        String googleIdToken = request.getParameter("googleIdToken");

        HttpSession session = request.getSession();
        Map<String, Object> jsonResponse = new HashMap<>();
        if (request.getContentType() != null && request.getContentType().contains("application/json")) {
    StringBuilder sb = new StringBuilder();
    try (BufferedReader reader = request.getReader()) {
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
    }
    String json = sb.toString();
    Map<String, String> jsonMap = gson.fromJson(json, Map.class);
    googleIdToken = jsonMap.get("googleIdToken");
}
        try {
            User user = null;
            if (googleIdToken != null) {
                String googleId = verifyGoogleToken(googleIdToken);
                if (googleId != null) {
                    user = dao.findByGoogleId(googleId);
                    if (user == null) {
                         
                        session.setAttribute("tempGoogleId", googleId);
                        session.setAttribute("tempEmail", extractEmailFromToken(googleIdToken));   
                       
                        
                          String defaultUsername = extractEmailFromToken(googleIdToken).split("@")[0]; // hoặc tuỳ bạn định danh
                           session.setAttribute("tempUsername", defaultUsername);   
                          
                        jsonResponse.put("success", true);
                        jsonResponse.put("redirect", request.getContextPath() + "/View/selectRole.jsp");
                    } else {
                       session.setAttribute("username", user.getUsername());
                        jsonResponse.put("success", true);
                        jsonResponse.put("redirect", request.getContextPath() + "/homePage");
                    }
                } else {
                    jsonResponse.put("success", false);
                    jsonResponse.put("message", "Invalid Google token");
                }
            } else {
                user = dao.authenticate(usernameOrEmail, password, null);
                if (user != null) {
                    session.setAttribute("username", user.getUsername());
                    response.sendRedirect("homePage");
                    return;
                } else {
                    request.setAttribute("error", "Invalid username/email or password");
                    request.getRequestDispatcher("/View/signIn.jsp").forward(request, response);
                    return;
                }
            }

            if (!jsonResponse.isEmpty()) {
                response.setContentType("application/json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(gson.toJson(jsonResponse));
            }
        } catch (SQLException e) {
            request.setAttribute("error", "Database error: " + e.getMessage());
            request.getRequestDispatcher("/View/signIn.jsp").forward(request, response);
        } catch (Exception e) {
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/View/signIn.jsp").forward(request, response);
        }
    }

    private String verifyGoogleToken(String idTokenString) throws Exception {
        GoogleIdToken idToken = verifier.verify(idTokenString);
        return (idToken != null) ? idToken.getPayload().getSubject() : null;
    }

    private String extractEmailFromToken(String idTokenString) throws Exception {
        GoogleIdToken idToken = verifier.verify(idTokenString);
        return (idToken != null) ? idToken.getPayload().getEmail() : null;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect("/View/signIn.jsp");
    }
}