<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <!-- (Include your styles and libraries as before) -->
</head>
<body>
<div class="container">
    <div class="card">
        <div class="card-header">
            <i class="bi bi-file-earmark-text me-2"></i>Assignment Manager
        </div>
        <div class="card-body">
            <!-- Course Selection -->
            <div class="mb-3">
                <label class="form-label">Choose Course</label>
                <select class="form-select" id="courseDropdown">
                    <option value="">Select Course</option>
                    <c:forEach var="course" items="${courseList}">
                        <option value="${course.id}">${course.name}</option>
                    </c:forEach>
                </select>
            </div>

            <!-- Lecture Selection (populated based on selected course) -->
            <div class="mb-3">
                <label class="form-label">Choose Lecture</label>
                <select class="form-select" id="lectureDropdown" disabled>
                    <option value="">Select Lecture</option>
                </select>
            </div>

            <!-- Assignment list -->
            <div id="assignmentSection" style="display:none;">
                <h5>Assignments</h5>
                <table class="table">
                    <thead>
                        <tr>
                            <th>Title</th>
                            <th>Description</th>
                            <th>Due Date</th>
                            <th>Actions</th>
                        </tr>
                    </thead>
                    <tbody id="assignmentTableBody">
                        <!-- Populated via JS -->
                    </tbody>
                </table>
                <button class="btn btn-primary" id="addAssignmentBtn">Add Assignment</button>
            </div>
        </div>
    </div>
</div>

<!-- Assignment Modal (Add/Edit) -->
<div class="modal fade" id="assignmentModal" tabindex="-1">
    <div class="modal-dialog">
        <form id="assignmentForm">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="assignmentModalTitle">Add/Edit Assignment</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="assignmentId" id="assignmentId" />
                    <input type="hidden" name="lectureId" id="modalLectureId" />
                    <div class="mb-3">
                        <label for="title" class="form-label">Title</label>
                        <input type="text" class="form-control" name="title" id="title" required>
                    </div>
                    <div class="mb-3">
                        <label for="description" class="form-label">Description</label>
                        <textarea class="form-control" name="description" id="description" required></textarea>
                    </div>
                    <div class="mb-3">
                        <label for="dueDate" class="form-label">Due Date</label>
                        <input type="datetime-local" class="form-control" name="dueDate" id="dueDate" required>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script>
document.getElementById('courseDropdown').addEventListener('change', function() {
    const courseId = this.value;
    // AJAX: fetch lectures for selected course
    fetch(`/api/lectures?courseId=${courseId}`)
        .then(res => res.json())
        .then(data => {
            const lectureDropdown = document.getElementById('lectureDropdown');
            lectureDropdown.innerHTML = '<option value="">Select Lecture</option>';
            data.forEach(lecture => {
                lectureDropdown.innerHTML += `<option value="${lecture.id}">${lecture.title}</option>`;
            });
            lectureDropdown.disabled = false;
        });
});

document.getElementById('lectureDropdown').addEventListener('change', function() {
    const lectureId = this.value;
    // AJAX: fetch assignments for selected lecture
    fetch(`/api/assignments?lectureId=${lectureId}`)
        .then(res => res.json())
        .then(data => {
            const tbody = document.getElementById('assignmentTableBody');
            tbody.innerHTML = '';
            data.forEach(ass => {
                tbody.innerHTML += `
                    <tr>
                        <td>${ass.title}</td>
                        <td>${ass.description}</td>
                        <td>${ass.dueDate}</td>
                        <td>
                            <button class="btn btn-sm btn-warning" onclick="editAssignment(${ass.id})">Edit</button>
                            <button class="btn btn-sm btn-danger" onclick="deleteAssignment(${ass.id})">Delete</button>
                        </td>
                    </tr>
                `;
            });
            document.getElementById('assignmentSection').style.display = 'block';
        });
});

document.getElementById('addAssignmentBtn').addEventListener('click', function() {
    document.getElementById('assignmentForm').reset();
    document.getElementById('assignmentId').value = '';
    document.getElementById('modalLectureId').value = document.getElementById('lectureDropdown').value;
    document.getElementById('assignmentModalTitle').textContent = 'Add Assignment';
    new bootstrap.Modal(document.getElementById('assignmentModal')).show();
});

function editAssignment(id) {
    // AJAX: fetch assignment details
    fetch(`/api/assignment?id=${id}`)
        .then(res => res.json())
        .then(ass => {
            document.getElementById('assignmentId').value = ass.id;
            document.getElementById('modalLectureId').value = ass.lectureId;
            document.getElementById('title').value = ass.title;
            document.getElementById('description').value = ass.description;
            document.getElementById('dueDate').value = ass.dueDate.replace(' ', 'T');
            document.getElementById('assignmentModalTitle').textContent = 'Edit Assignment';
            new bootstrap.Modal(document.getElementById('assignmentModal')).show();
        });
}

function deleteAssignment(id) {
    if (confirm('Are you sure you want to delete this assignment?')) {
        fetch(`/api/assignment?id=${id}`, { method: 'DELETE' })
            .then(() => location.reload());
    }
}

document.getElementById('assignmentForm').addEventListener('submit', function(e) {
    e.preventDefault();
    const formData = new FormData(this);
    fetch('/AddAssignmentServlet', {
        method: 'POST',
        body: formData
    })
    .then(res => {
        if (res.ok) location.reload();
        else alert('Failed to save assignment.');
    });
});
</script>
</body>
</html>
