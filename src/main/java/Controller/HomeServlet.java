package controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import dao.CourseDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.User;

@WebServlet(name = "HomeServlet", urlPatterns = {"/home"})
public class HomeServlet extends HttpServlet {
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.getRequestDispatcher("homePage.jsp").forward(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        CourseDAO courseDAO = new CourseDAO();

        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            List<Course> courses = courseDAO.getRandomCourses(3); // Implement this method in your DAO
            request.setAttribute("courses", courses);
            request.getRequestDispatcher("/entryPage.jsp").forward(request, response);
        }
        int userId = user.getId();

        List<Course> courses = courseDAO.getCourses();
        Map<Integer, Integer> totalLessons = courseDAO.getLessonCounts();
        Map<Integer, Integer> completedLessons = courseDAO.getCompletedLectureCounts(userId);

        request.setAttribute("courses", courses);
        request.setAttribute("totalLessons", totalLessons);
        request.setAttribute("completedLessons", completedLessons);

        request.getRequestDispatcher("/homePage.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}
