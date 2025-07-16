<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="your.package.Course" %>
<%
    String role = (String) session.getAttribute("role");
    String username = (String) session.getAttribute("username");
    List<Course> courses = (List<Course>) session.getAttribute("enrolledCourses");
%>
<!DOCTYPE html>
<html lang="en" data-theme="light">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Course List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css"/>
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">
</head>
<body>
    <!-- Navigation Menu -->
    <nav class="navbar navbar-expand-lg navbar-light">
        <div class="container-fluid">
            <a class="navbar-brand" href="${pageContext.request.contextPath}/homePage">
                <div class="book-icon-container">
                    <i class="fas fa-book-open book-icon"></i>
                    <div class="play-button" onclick="location.href='${pageContext.request.contextPath}/homePage'">
                        <svg viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
                    </div>
                </div>
            </a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav mx-auto">
                    <li class="nav-item"><a class="nav-link active" href="#">Home</a></li>
                    <% if ("teacher".equals(role)) { %>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/courseList.jsp">Course List</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/gradeAssignments.jsp">Grade Assignments</a></li>
                        <li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/view/studentList.jsp">Student List</a></li>
                    <% } else { %>
                        <li class="nav-item"><a class="nav-link" href="#">Courses</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Roadmap</a></li>
                        <li class="nav-item"><a class="nav-link" href="#">Practice</a></li>
                    <% } %>
                </ul>
                <div class="d-flex align-items-center">
                    <% if (username == null) { %>
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

    <!-- Main Content -->
    <div class="container mt-4">
        <div class="row">
            <!-- Sidebar with Learning Roadmap -->
            <div class="col-md-3">
                <div class="sidebar card p-3">
                    <h4>Learning Roadmap</h4>
                    <ul class="list-group">
                        <li class="list-group-item">Module 1: Introduction
                            <div class="progress mt-2">
                                <div class="progress-bar" role="progressbar" style="width: 60%;">60%</div>
                            </div>
                        </li>
                        <li class="list-group-item">Module 2: Intermediate
                            <div class="progress mt-2">
                                <div class="progress-bar" role="progressbar" style="width: 30%;">30%</div>
                            </div>
                        </li>
                        <li class="list-group-item">Module 3: Advanced
                            <div class="progress mt-2">
                                <div class="progress-bar" role="progressbar" style="width: 10%;">10%</div>
                            </div>
                        </li>
                    </ul>
                    <a href="roadmap.jsp" class="btn btn-primary mt-3">View Full Roadmap</a>
                </div>
            </div>

            <!-- Course Section -->
            <div class="col-md-9">
                <% if ("student".equals(role)) { %>
                    <h2>My Courses</h2>
                    <div class="row">
                        <% if (courses != null && !courses.isEmpty()) {
                            for (Course course : courses) { %>
                                <div class="col-md-6 mb-4">
                                    <div class="card course-card">
                                        <div class="card-body">
                                            <h5 class="card-title"><%= course.getName() %></h5>
                                            <p class="card-text"><strong>Due:</strong> <%= course.getDueDate() %></p>
                                            <div class="progress mb-2">
                                                <div class="progress-bar" role="progressbar" style="width: <%= course.getProgress() %>%;">
                                                    <%= course.getProgress() %>%
                                                </div>
                                            </div>
                                            <a href="courseDetail.jsp?id=<%= course.getId() %>" class="btn btn-primary">Continue</a>
                                            <%-- Show Submit Assignment button if not overdue --%>
                                            <% if (course.canSubmit()) { %>
                                                <a href="submitAssignment.jsp?courseId=<%= course.getId() %>" class="btn btn-success ms-2">Submit Assignment</a>
                                            <% } else { %>
                                                <button class="btn btn-secondary ms-2" disabled>Submission Closed</button>
                                            <% } %>
                                        </div>
                                    </div>
                                </div>
                        <%  }
                        } else { %>
                            <p>You are not enrolled in any courses.</p>
                        <% } %>
                    </div>
                <% } else if ("teacher".equals(role)) { %>
                    <h2>Teacher Panel</h2>
                    <div class="row">
                        <div class="col-md-4">
                            <div class="panel-card card">
                                <div class="card-body text-center">
                                    <span>Courses</span>
                                    <a href="courses.jsp" class="btn btn-outline-primary mt-2">Manage Courses</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="panel-card card">
                                <div class="card-body text-center">
                                    <span>Lectures</span>
                                    <a href="lectures.jsp" class="btn btn-outline-primary mt-2">Manage Lectures</a>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4">
                            <div class="panel-card card">
                                <div class="card-body text-center">
                                    <span>Assignments</span>
                                    <a href="assignment.jsp" class="btn btn-outline-primary mt-2">Edit Assignments</a>
                                </div>
                            </div>
                        </div>
                    </div>
                <% } else { %>
                    <p>Unknown user role.</p>
                <% } %>
            </div>
        </div>
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
