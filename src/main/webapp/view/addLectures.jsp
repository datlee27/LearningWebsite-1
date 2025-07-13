<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="DAO.DAO" %>
<!DOCTYPE html>
<html lang="en" data-theme="light">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Lectures and Assignments</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css"/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/homePage">
                <div class="book-icon-container">
                    <i class="fas fa-book-open book-icon"></i>
                    <div class="play-button" onclick="alert('Quay về trang chủ!')">
                        <svg viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
                    </div>
                </div>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mx-auto">
                    <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/homePage">Home</a></li>
                    <% String role = (String) session.getAttribute("role"); %>
                    <% if ("teacher".equals(role)) { %>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/addCourses.jsp">Add Course</a></li>
                        <li class="nav-item"><a class="nav-link active" href="#">Add Lectures</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/gradeAssignments.jsp">Grade Assignments</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/studentList.jsp">Student List</a></li>
                    <% } else { %>
                        <li class="nav-item"><a class="nav-link" href="#">Courses</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Roadmap</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Practice</a></li>
                    <% } %>
                </ul>
                <div class="d-flex align-items-center">
                    <% if (session.getAttribute("username") == null) { %>
                        <a href="${pageContext.request.contextPath}/view/signIn.jsp" class="btn btn-outline-primary me-2">Sign In</a>
                        <a href="${pageContext.request.contextPath}/view/signUp.jsp" class="btn btn-outline-success me-2">Sign Up</a>
                    <% } else { %>
                        <div class="user-icon position-relative">
                            <button class="btn btn-outline-primary me-2" style="border: none; background: none;">
                                <i class="bi bi-person-circle"></i>
                            </button>
                            <div class="dropdown-menu">
                                <a href="${pageContext.request.contextPath}/view/profile.jsp"><i class="bi bi-person-fill me-2"></i>Profile</a>
                                <a href="${pageContext.request.contextPath}/view/settings.jsp"><i class="bi bi-gear-fill me-2"></i>Settings</a>
                                <a href="${pageContext.request.contextPath}/view/logOut.jsp"><i class="bi bi-box-arrow-right me-2"></i>Logout</a>
                            </div>
                        </div>
                    <% } %>
                    <button id="theme-toggle" class="btn btn-outline-secondary" title="Toggle Theme">
                        <i class="bi bi-sun-fill"></i>
                    </button>
                </div>
            </div>
        </div>
    </nav>

    <div class="container mt-4">
        <h2>Add Lectures and Assignments</h2>
        <% 
            String courseIdParam = request.getParameter("courseId");
            String success = request.getParameter("success");
            if (success != null && success.equals("true")) {
                out.println("<div class='alert alert-success'>Operation successful!</div>");
            }
            String error = (String) request.getAttribute("error");
            if (error != null) {
                out.println("<div class='alert alert-danger'>" + error + "</div>");
            }
            if (courseIdParam != null && !"".equals(courseIdParam.trim()) && "teacher".equals(session.getAttribute("role"))) {
                int courseId = Integer.parseInt(courseIdParam);
                DAO dao = new DAO();
        %>
        <h3>For Course ID: <%= courseId %></h3>
        <div class="row">
            <!-- Add Lecture Form -->
            <div class="col-md-6">
                <h4>Add Lecture</h4>
                <form action="${pageContext.request.contextPath}/addLecturesServlet" method="post">
                    <input type="hidden" name="courseId" value="<%= courseId %>">
                    <div class="mb-3">
                        <label for="lectureTitle" class="form-label">Lecture Title</label>
                        <input type="text" class="form-control" id="lectureTitle" name="title" required>
                    </div>
                    <div class="mb-3">
                        <label for="lectureContent" class="form-label">Content</label>
                        <textarea class="form-control" id="lectureContent" name="content" rows="3" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="videoUrl" class="form-label">Video URL (Optional)</label>
                        <input type="text" class="form-control" id="videoUrl" name="videoUrl">
                    </div>
                    <button type="submit" class="btn btn-primary">Add Lecture</button>
                </form>
            </div>
            <!-- Add Assignment Form -->
            <div class="col-md-6">
                <h4>Add Assignment</h4>
                <form action="${pageContext.request.contextPath}/addAssignmentServlet" method="post">
                    <input type="hidden" name="courseId" value="<%= courseId %>">
                    <div class="mb-3">
                        <label for="assignmentTitle" class="form-label">Assignment Title</label>
                        <input type="text" class="form-control" id="assignmentTitle" name="title" required>
                    </div>
                    <div class="mb-3">
                        <label for="assignmentDescription" class="form-label">Description</label>
                        <textarea class="form-control" id="assignmentDescription" name="description" rows="3" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="dueDate" class="form-label">Due Date</label>
                        <input type="datetime-local" class="form-control" id="dueDate" name="dueDate" required>
                    </div>
                    <button type="submit" class="btn btn-primary">Add Assignment</button>
                </form>
            </div>
        </div>
        <% 
            } else {
                out.println("<p class='text-danger'>Invalid course or access denied. Please ensure you are a teacher and a valid course ID is provided.</p>");
            }
        %>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.addEventListener('DOMContentLoaded', () => {
            const toggleButton = document.getElementById('theme-toggle');
            const htmlElement = document.documentElement;
            const currentTheme = localStorage.getItem('theme') || 'light';

            htmlElement.setAttribute('data-theme', currentTheme);
            updateToggleIcon(currentTheme);

            toggleButton.addEventListener('click', () => {
                const newTheme = htmlElement.getAttribute('data-theme') === 'light' ? 'dark' : 'light';
                htmlElement.setAttribute('data-theme', newTheme);
                localStorage.setItem('theme', newTheme);
                updateToggleIcon(newTheme);
            });

            function updateToggleIcon(theme) {
                const icon = toggleButton.querySelector('i');
                icon.className = theme === 'light' ? 'bi bi-sun-fill' : 'bi bi-moon-fill';
            }
        });
    </script>
</body>
</html>