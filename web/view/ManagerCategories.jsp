<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ include file="SideBarOfSheller.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Categories</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .container {
            padding: 30px;
            max-width: 1200px;
           
        }
        .card {
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            width: 1200px;
            text-align: center;
        }
        .card-header {
            background: linear-gradient(135deg, #28a745, #218838);
            color: white;
            border-radius: 10px 10px 0 0;
        }
        .table {
            border-radius: 0 0 10px 10px;
            overflow: hidden;
        }
        .table th {
            background-color: #28a745;
            color: white;
        }
        .btn-success {
            background-color: #28a745;
            border: none;
            border-radius: 5px;
        }
        .btn-success:hover {
            background-color: #218838;
        }
        .btn-danger {
            border-radius: 5px;
        }
        .form-inline {
            display: flex;
            gap: 10px;
        }
        .alert {
            border-radius: 5px;
        }
        
    </style>
</head>
<body>
    <div class="container mt-4">
        <div class="card">
            <div class="card-header">
                <h3>Manage Categories</h3>
            </div>
            <div class="card-body">
                <!-- Form to add category -->
                <div class="mb-4">
                    <form action="ManagerCategoriesServlet" method="post" class="form-inline">
                        <input type="hidden" name="action" value="add">
                        <div class="input-group">
                            <input type="text" name="name" class="form-control" placeholder="Enter new category name" required>
                            <button type="submit" class="btn btn-success ms-2">Add Category</button>
                        </div>
                    </form>
                </div>

                <!-- Category list -->
                <c:choose>
                    <c:when test="${empty categories}">
                        <div class="alert alert-warning" role="alert">
                            No categories found. Please add a category to manage.
                        </div>
                    </c:when>
                    <c:otherwise>
                        <table class="table table-striped table-hover">
                            <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Name</th>
                                    <th>Actions</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="category" items="${categories}" varStatus="loop">
                                    <tr>
                                        <td>${category.id}</td>
                                        <td>
                                            <form action="ManagerCategoriesServlet" method="post" class="form-inline" style="display:inline-flex; align-items:center; gap:10px;">
                                                <input type="hidden" name="action" value="update">
                                                <input type="hidden" name="id" value="${category.id}">
                                                <input type="text" name="name" value="${category.name}" class="form-control w-50" required>
                                                <button type="submit" class="btn btn-success btn-sm">Update</button>
                                            </form>
                                        </td>
                                        <td>
                                            <form action="ManagerCategoriesServlet" method="post" onsubmit="return confirm('Are you sure you want to delete category ID ${category.id}?');">
                                                <input type="hidden" name="action" value="delete">
                                                <input type="hidden" name="id" value="${category.id}">
                                                <button type="submit" class="btn btn-danger btn-sm">Delete</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                        <!-- Submit Buttons -->
                        <div class="text-center">
                            
                            <a href="home" class="btn btn-secondary">Cancel</a>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>