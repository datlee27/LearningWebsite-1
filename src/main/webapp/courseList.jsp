<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>My Courses - Learning Platform</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
</head>
<body>

    <header class="main-header">
        <div class="container">
            <a href="${pageContext.request.contextPath}/home" class="branding">LearnHub</a>
            <nav class="main-nav">
                <ul>
                    <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                    <li><a href="${pageContext.request.contextPath}/courses" class="active">My Courses</a></li>
                    <li><a href="${pageContext.request.contextPath}/profile">Profile</a></li>
                    <li><a href="${pageContext.request.contextPath}/logout">Logout</a></li>
                </ul>
            </nav>
        </div>
    </header>

    <main class="main-content">
        <div class="container">
            <h1 class="mb-40">My Courses</h1>

            <!-- Teacher view: Button to create a new course -->
            <c:if test="${sessionScope.user.role == 'TEACHER'}">
                <div style="margin-bottom: 20px;">
                    <a href="#" class="btn btn-primary" style="width: auto;">Create New Course</a>
                </div>
            </c:if>

            <div class="course-list">
                <c:forEach var="course" items="${courses}">
                    <div class="course-list-item">
                        <div class="course-info">
                            <h3><c:out value="${course.courseName}" /></h3>
                            <p><c:out value="${course.description}" /></p>
                        </div>
                        <div class="course-actions">
                             <a href="${pageContext.request.contextPath}/courses?id=${course.courseId}" class="btn btn-primary" style="width: auto;">View Details</a>
                        </div>
                    </div>
                </c:forEach>

                <c:if test="${empty courses}">
                    <p>No courses to display.</p>
                </c:if>
            </div>
        </div>
    </main>

    <footer class="main-footer">
        <p>&copy; 2025 LearnHub. All rights reserved.</p>
    </footer>

</body>
</html>
