<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="navbar.jsp" />
<div class="container-fluid mt-4">
    <div class="row">
        <!-- Sidebar: Lecture and Assignment List -->
        <div class="col-md-3 border-end">
            <h5 class="mt-4"><strong>${lecture.name}</strong></h5>
            <ul class="list-group mt-3">
                <c:forEach var="assignment" items="${assignments}">
                    <li class="list-group-item">
                        <a href="${pageContext.request.contextPath}/assignmentDetail?assignmentId=${assignment.id}">
                            ${assignment.name}
                        </a>
                    </li>
                </c:forEach>
            </ul>
        </div>
        <!-- Main Content: Lecture Details -->
        <div class="col-md-9">
            <h2 class="fw-bold">${lecture.name}</h2>
            <div class="mb-3">
                <strong>Description:</strong>
                <p>${lecture.description}</p>
            </div>
            <div class="mb-4">
                <strong>Content:</strong>
                <div>${lecture.content}</div>
            </div>
            <a href="${pageContext.request.contextPath}/assignments?lectureId=${lecture.id}" class="btn btn-primary mt-3">
                View All Assignments
            </a>
        </div>
    </div>
</div>