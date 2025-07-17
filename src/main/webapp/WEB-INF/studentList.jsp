<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en" data-theme="light">
<head>
    <meta charset="UTF-8">
    <title>Student List</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/homePage.css"/>
</head>
<body>
<jsp:include page="/navbar.jsp" />

<!-- Exception output block -->
<c:if test="${not empty exception}">
    <div class="alert alert-danger">
        <c:choose>
            <c:when test="${exception.message == 'No students have enrolled in this course.'}">
                <strong>Notice:</strong> No students have enrolled in this course yet.
            </c:when>
            <c:otherwise>
                <strong>Exception:</strong> ${exception}<br/>
                <c:if test="${not empty exception.message}">
                    <strong>Message:</strong> ${exception.message}
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</c:if>

<div class="container mt-5">
    <h2 class="mb-4 fw-bold">Enrolled Students</h2>
    <form method="get" action="${pageContext.request.contextPath}/students" class="mb-4">
        <label for="courseId" class="form-label">Select Course:</label>
        <select name="courseId" id="courseId" class="form-select" onchange="this.form.submit()">
            <c:forEach var="course" items="${courses}">
                <option value="${course.idCourse}" <c:if test="${selectedCourse != null && selectedCourse.idCourse == course.idCourse}">selected</c:if>>
                    ${course.name}
                </option>
            </c:forEach>
        </select>
    </form>

    <c:if test="${selectedCourse != null}">
        <div class="card p-3 mb-4">
            <h4>${selectedCourse.name}</h4>
            <table class="table table-striped">
                <thead>
                    <tr>
                        <th>Student Name</th>
                        <th>Email</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach var="student" items="${students}">
                        <tr>
                            <td>${student.name}</td>
                            <td>${student.email}</td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/students" style="display:inline;">
                                    <input type="hidden" name="action" value="remove">
                                    <input type="hidden" name="courseId" value="${selectedCourse.idCourse}">
                                    <input type="hidden" name="studentId" value="${student.idStudent}">
                                    <button type="submit" class="btn btn-danger btn-sm">Remove</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
            <form method="post" action="${pageContext.request.contextPath}/students" class="mt-3">
                <input type="hidden" name="action" value="add">
                <input type="hidden" name="courseId" value="${selectedCourse.idCourse}">
                <div class="row g-2 align-items-center">
                    <div class="col-auto">
                        <input type="number" name="studentId" class="form-control" placeholder="Student ID">
                    </div>
                    <div class="col-auto">
                        <button type="submit" class="btn btn-success">Add Student</button>
                    </div>
                </div>
            </form>
        </div>
    </c:if>
</div>
</body>
</html>