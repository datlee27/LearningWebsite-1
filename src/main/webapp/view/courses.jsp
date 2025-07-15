<%@ page import="java.sql.*, java.util.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Courses Management</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
</head>
<body>
<div class="container mt-5">
    <h1>Courses</h1>
    <button class="btn btn-primary" data-toggle="modal" data-target="#addModal">Add Course</button>
    <table class="table mt-3">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
        <%
            // JDBC connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/YOUR_DB", "USERNAME", "PASSWORD");
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Courses");
            while (rs.next()) {
        %>
            <tr>
                <td><%= rs.getInt("id") %></td>
                <td><%= rs.getString("name") %></td>
                <td><%= rs.getString("description") %></td>
                <td>
                    <button class="btn btn-warning editBtn" 
                            data-id="<%= rs.getInt("id") %>" 
                            data-name="<%= rs.getString("name") %>" 
                            data-description="<%= rs.getString("description") %>">Edit</button>
                    <form action="CourseServlet" method="post" style="display:inline;">
                        <input type="hidden" name="action" value="delete"/>
                        <input type="hidden" name="id" value="<%= rs.getInt("id") %>"/>
                        <button type="submit" class="btn btn-danger" onclick="return confirm('Delete this course?')">Delete</button>
                    </form>
                </td>
            </tr>
        <%
            }
            rs.close();
            st.close();
            con.close();
        %>
        </tbody>
    </table>
</div>

<!-- Add Modal -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <form action="CourseServlet" method="post">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Add Course</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="action" value="add"/>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" class="form-control" required/>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input type="text" name="description" class="form-control"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Add</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </form>
    </div>
</div>

<!-- Edit Modal -->
<div class="modal fade" id="editModal" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <form action="CourseServlet" method="post">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Edit Course</h5>
                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="action" value="edit"/>
                    <input type="hidden" name="id" id="edit-id"/>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" name="name" id="edit-name" class="form-control" required/>
                    </div>
                    <div class="form-group">
                        <label>Description</label>
                        <input type="text" name="description" id="edit-description" class="form-control"/>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-primary">Save</button>
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">Cancel</button>
                </div>
            </div>
        </form>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.2/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Handle Edit Button Click
    $('.editBtn').on('click', function() {
        $('#edit-id').val($(this).data('id'));
        $('#edit-name').val($(this).data('name'));
        $('#edit-description').val($(this).data('description'));
        $('#editModal').modal('show');
    });
</script>
</body>
</html>
