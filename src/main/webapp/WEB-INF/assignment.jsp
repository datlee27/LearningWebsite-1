<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${assignment.title}" /> - Learning Platform</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/global.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
</head>
<body>

    <%@ include file="/WEB-INF/jsp/header.jspf" %>

    <div class="page-header">
        <div class="container">
            <p>Assignment</p>
            <h1><c:out value="${assignment.title}" /></h1>
        </div>
    </div>

    <main class="main-content">
        <div class="container">

            <!-- Assignment Description -->
            <div class="card">
                <h2>Assignment Details</h2>
                <p><strong>Due Date:</strong> <c:out value="${assignment.dueDate}" /></p>
                <hr style="margin: 15px 0;">
                <p><c:out value="${assignment.description}" /></p>
            </div>


            <!-- STUDENT VIEW -->
            <c:if test="${sessionScope.user.role == 'STUDENT'}">
                <div class="card">
                    <h2>My Submission</h2>
                    
                    <%-- Check if already submitted --%>
                    <c:if test="${not empty submission}">
                         <p><strong>Status:</strong> <span class="submission-status graded">Graded</span></p>
                         <p><strong>Grade:</strong> ${submission.grade}/100</p>
                         <p><strong>Submitted on:</strong> ${submission.submissionDate}</p>
                         <hr style="margin: 15px 0;">
                         <h4>Your Submission:</h4>
                         <p>${submission.content}</p>
                    </c:if>

                    <c:if test="${empty submission}">
                        <form action="${pageContext.request.contextPath}/submitAssignment" method="post">
                            <input type="hidden" name="assignmentId" value="${assignment.assignmentId}">
                            <div class="form-group">
                                <label for="submissionContent">Your Submission</label>
                                <textarea id="submissionContent" name="submissionContent" class="form-control" placeholder="Enter your submission text here..."></textarea>
                            </div>
                             <div class="form-group">
                                <label for="file">Attach File (Optional)</label>
                                <input type="file" id="file" name="file" class="form-control">
                            </div>
                            <button type="submit" class="btn btn-primary btn-full-width">Submit Assignment</button>
                        </form>
                    </c:if>
                </div>
            </c:if>

            <!-- TEACHER VIEW -->
            <c:if test="${sessionScope.user.role == 'TEACHER'}">
                <div class="card">
                    <h2>Student Submissions</h2>
                    <div class="student-submission-list">
                        <c:forEach var="sub" items="${submissions}">
                             <div class="content-list-item">
                                <div class="submission-info">
                                    <div>
                                        <strong><c:out value="${sub.student.username}" /></strong>
                                        <div class="item-meta">Submitted on: <c:out value="${sub.submissionDate}" /></div>
                                    </div>
                                    <c:choose>
                                        <c:when test="${not empty sub.grade}">
                                            <span class="submission-status graded">Graded: ${sub.grade}/100</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="submission-status submitted">Awaiting Grade</span>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div class="submission-content" style="width:100%; margin-top:15px;">
                                    <p>${sub.content}</p>
                                </div>
                                <c:if test="${empty sub.grade}">
                                    <form class="grade-form" action="${pageContext.request.contextPath}/gradeAssignment" method="post">
                                        <input type="hidden" name="submissionId" value="${sub.submissionId}">
                                        <div class="form-group" style="margin-bottom:0;">
                                            <input type="number" name="grade" class="form-control" placeholder="Grade" min="0" max="100" required>
                                        </div>
                                        <button type="submit" class="btn btn-success btn-sm">Save Grade</button>
                                    </form>
                                </c:if>
                            </div>
                        </c:forEach>

                        <c:if test="${empty submissions}">
                            <p>No students have submitted this assignment yet.</p>
                        </c:if>
                    </div>
                </div>
            </c:if>

        </div>
    </main>

    <%@ include file="/WEB-INF/jsp/footer.jspf" %>

</body>
</html>
