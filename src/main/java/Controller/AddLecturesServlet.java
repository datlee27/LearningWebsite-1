package Controller;

import DAO.DAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

public class AddLecturesServlet extends HttpServlet {
    private static final Logger logger = Logger.getLogger(AddLecturesServlet.class.getName());
    private final DAO dao = new DAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"teacher".equals(session.getAttribute("role"))) {
            response.sendRedirect(request.getContextPath() + "/view/signIn.jsp");
            return;
        }

        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String title = request.getParameter("title");
        String content = request.getParameter("content");
        String videoUrl = request.getParameter("videoUrl");

        if (title == null || title.trim().isEmpty() || content == null || content.trim().isEmpty()) {
            request.setAttribute("error", "Lecture title and content are required.");
            request.getRequestDispatcher("/view/addLectures.jsp?courseId=" + courseId).forward(request, response);
            return;
        }

        try {
            dao.saveLecture(courseId, title, content, videoUrl);
            logger.info("Lecture added successfully for course ID: " + courseId);
            response.sendRedirect(request.getContextPath() + "/view/addLectures.jsp?courseId=" + courseId + "&success=true");
        } catch (SQLException e) {
            logger.severe("Database error while adding lecture: " + e.getMessage());
            request.setAttribute("error", "Failed to add lecture: " + e.getMessage());
            request.getRequestDispatcher("/view/addLectures.jsp?courseId=" + courseId).forward(request, response);
        } catch (Exception e) {
            logger.severe("Unexpected error while adding lecture: " + e.getMessage());
            request.setAttribute("error", "An unexpected error occurred: " + e.getMessage());
            request.getRequestDispatcher("/view/addLectures.jsp?courseId=" + courseId).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/view/addLectures.jsp");
    }
}