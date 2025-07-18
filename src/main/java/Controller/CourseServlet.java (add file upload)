package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.AssignmentDAO;
import dao.CourseDAO;
import dao.EnrollmentDAO;
import dao.LectureDAO;
import dao.UserProgressDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Course;
import model.Lecture;
import model.User;

@WebServlet(name = "CourseServlet", urlPatterns = {"/courses"})
public class CourseServlet extends HttpServlet {
    private final CourseDAO courseDAO = new CourseDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String role = (String) request.getSession().getAttribute("role");
        int userId = (int) request.getSession().getAttribute("id");
        List<Course> courses;
        if ("teacher".equals(role)) {
            courses = courseDAO.getCoursesByTeacher(userId);
        } else if ("student".equals(role)) {
            courses = courseDAO.getCoursesByStudent(userId);
        } else {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        UserProgressDAO progressDAO = new UserProgressDAO();
        LectureDAO lectureDAO = new LectureDAO();
        Map<Integer, Integer> lastLectureMap = new HashMap<>();
        for (Course course : courses) {
            Integer lastLectureId = progressDAO.getLastStudiedLectureId(userId, course.getIdCourse());
            if (lastLectureId == null) {
                // fallback to first lecture
                List<Lecture> lectures = lectureDAO.getLecturesByCourseForStudent(course.getIdCourse(), userId);
                if (!lectures.isEmpty()) {
                    lastLectureId = lectures.get(0).getIdLecture();
                }
            }
            lastLectureMap.put(course.getIdCourse(), lastLectureId);
        }
        request.setAttribute("courses", courses);
        request.setAttribute("lastLectureMap", lastLectureMap);
        request.setAttribute("role", role);
        request.getRequestDispatcher("WEB-INF/jsp/courseList.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) {
            action = "create"; // Default action
        }

        try {
            switch (action) {
                case "update":
                    updateCourse(request);
                    break;
                case "delete":
                    deleteCourse(request);
                    break;
                default:
                    createCourse(request);
                    break;
            }
        } catch (Exception e) {
            throw new ServletException("Error processing course action: " + action, e);
        }
        response.sendRedirect(request.getContextPath() + "/courses");
    }

    private void createCourse(HttpServletRequest request) {
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        User user = (User) request.getSession().getAttribute("user");
        int teacherId = user.getId();

        String thumbnail = request.getParameter("thumbnail");
        Course course = new Course(name, description, teacherId, thumbnail);
        courseDAO.saveCourse(course);
    }

    private void updateCourse(HttpServletRequest request) {
        int courseId = Integer.parseInt(request.getParameter("courseId"));
        String name = request.getParameter("name");
        String description = request.getParameter("description");
        
        Course course = courseDAO.findCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID:" + courseId));
        
        course.setName(name);
        course.setDescription(description);
        courseDAO.updateCourse(course);
    }

    private void deleteCourse(HttpServletRequest request) {
        int courseId = Integer.parseInt(request.getParameter("courseId"));

        // Delete related enrollments
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
        enrollmentDAO.deleteByCourseId(courseId);

        // Delete related assignments (if you have AssignmentDAO)
        AssignmentDAO assignmentDAO = new AssignmentDAO();
        assignmentDAO.deleteByCourseId(courseId);

        // Delete the course itself
        courseDAO.deleteCourse(courseId);
    }
}
