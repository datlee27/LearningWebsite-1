<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="navbar.jsp" />
<div class="container mt-4">
    <h2>Courses</h2>
    <div class="list-group">
        <c:forEach var="course" items="${courses}">
            <a href="${pageContext.request.contextPath}/lectures?courseId=${course.id}" class="list-group-item list-group-item-action">
                <strong>${course.name}</strong>
                <span class="text-muted">${course.description}</span>
                <c:if test="${role == 'teacher'}">
                    <span class="badge bg-info ms-2">Teacher</span>
                </c:if>
                <c:if test="${role == 'student'}">
                    <span class="badge bg-success ms-2">Student</span>
                </c:if>
            </a>
        </c:forEach>
    </div>
    <c:forEach var="course" items="${courses}">
        <c:choose>
            <c:when test="${not empty lastLectureMap[course.idCourse]}">
                <a href="${pageContext.request.contextPath}/lectureDetail?lectureId=${lastLectureMap[course.idCourse]}" class="btn btn-primary">
                    Continue Course
                </a>
            </c:when>
            <c:otherwise>
                <span class="btn btn-secondary disabled">No Lectures</span>
            </c:otherwise>
        </c:choose>
    </c:forEach>
    <c:forEach var="course" items="${courses}">
        <a href="${pageContext.request.contextPath}/lectures?courseId=${course.idCourse}" class="btn btn-primary">
            View Course
        </a>
    </c:forEach>
</div>
