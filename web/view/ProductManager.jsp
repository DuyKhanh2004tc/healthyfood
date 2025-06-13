<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Manager</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .card-header {
                font-weight: bold;
                background: linear-gradient(90deg, #28a745, #218838);
            }
            .card {
                border-radius: 10px;
                overflow: hidden;
            }
            .table th, .table td {
                vertical-align: middle;
                padding: 12px;
            }
            .table img {
                border-radius: 5px;
                object-fit: cover;
            }
            .btn-action {
                margin: 0 5px;
                padding: 6px 12px;
                font-size: 14px;
                border-radius: 5px;
            }
            .btn-edit {
                background-color: #ffc107;
                border-color: #ffc107;
            }
            .btn-edit:hover {
                background-color: #e0a800;
                border-color: #e0a800;
            }
            .btn-delete {
                background-color: #dc3545;
                border-color: #dc3545;
            }
            .btn-delete:hover {
                background-color: #c82333;
                border-color: #c82333;
            }
            .btn-insert {
                background-color: #28a745;
                border-color: #28a745;
                padding: 8px 16px;
                font-size: 16px;
            }
            .btn-insert:hover {
                background-color: #218838;
                border-color: #218838;
            }
            .error-message {
                font-size: 16px;
                margin-bottom: 20px;
            }
            .table-hover tbody tr:hover {
                background-color: #f1f1f1;
            }
            .search-form {
                max-width: 600px;
                margin: 20px auto;
            }
        </style>
    </head>
    <body>
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>

        <div class="container-fluid mt-4">
            <div class="d-flex justify-content-center">
                <div class="card shadow border-0" style="width: 95%;">
                    <div class="card-header bg-success text-white fs-5">
                        <i class="bi bi-box-seam me-2"></i>Product Management
                    </div>
                    <div class="card-body">
                        
                        <!-- Insert Button -->
                        <div class="text-center mb-4">
                            <a href="manageproduct?service=requestInsert" class="btn btn-warning">
                                <i class="bi bi-plus-circle me-1"></i> Insert New Product
                            </a>
                        </div>

                        <!-- Search Form -->
<form action="manageproduct" class="input-group search-form shadow-sm" method="get" onsubmit="return validateSearch()">
    <input type="hidden" name="service" value="searchByKeywords"/>
    <input type="text" class="form-control" id="keywords" name="keywords" placeholder="Search by product name" value="${keywords}">
    <button class="btn btn-outline-primary" type="submit">
        <i class="bi bi-search"></i> Search
    </button>
</form>

<script>
function validateSearch() {
    var keywords = document.getElementById("keywords").value.trim();
    if (keywords === "") {
        alert("Please enter a search keyword.");
        return false;
    }
    return true;
}
</script>

                        <c:if test="${not empty errorMessage}">
                            <p class="text-center text-danger error-message">${errorMessage}</p>
                        </c:if>
                        <c:choose>
                            <c:when test="${not empty allProducts}">
                                <h4 class="text-center mb-4 text-success fw-bold">All Products</h4>
                                <div class="table-responsive">
                                    <table class="table table-bordered table-striped table-hover align-middle">
                                        <thead class="table-primary text-center">
                                            <tr>
                                                <th>ID</th>
                                                <th>Product</th>
                                                <th>Price</th>
                                                <th>Stock</th>
                                                <th>Description</th>
                                                <th>Shelf Life (hours)</th>
                                                <th>Category</th>
                                                <th>Rate</th>
                                                <th>Actions</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="product" items="${allProducts}">
                                                <tr>
                                                    <td>${product.id}</td>
                                                    <td class="text-center">
                                                        <div class="d-flex flex-column align-items-center">
                                                            <img src="${product.imgUrl != null ? product.imgUrl : '/images/default.jpg'}" alt="${product.name}" style="width: 60px; height: 60px; object-fit: cover; border-radius: 5px;">
                                                            <span class="mt-1">${product.name}</span>
                                                        </div>
                                                    </td>
                                                    <td class="text-end">${String.format("%.2f", product.price)}</td>
                                                    <td class="text-center">${product.stock}</td>
                                                    <td>${product.description != null ? product.description : 'N/A'}</td>
                                                    <td class="text-center">${String.format("%.1f", product.shelfLifeHours)}</td>
                                                    <td class="text-center">${product.category != null ? product.category.name : 'N/A'}</td>
                                                    <td class="text-center">${String.format("%.1f", product.rate)}</td>
                                                    <td class="text-center">
                                                        <a href="manageproduct?service=requestUpdate&productId=${product.id}" class="btn btn-success btn-edit">
                                                            <i class="bi bi-pencil"></i> Edit
                                                        </a>
                                                        <a href="manageproduct?service=requestDelete&productId=${product.id}" class="btn btn-action btn-delete" onclick="return confirm('Are you sure you want to delete ${product.name}?');">
                                                            <i class="bi bi-trash"></i> Delete
                                                        </a>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <p class="text-center text-danger error-message">No products found!</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    </body>
</html>