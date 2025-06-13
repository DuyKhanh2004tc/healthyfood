<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Product</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" rel="stylesheet">
        <style>
            body {
                background-color: #f8f9fa;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .card {
                max-width: 600px;
                margin: 50px auto;
                border-radius: 10px;
                overflow: hidden;
            }
            .card-header {
                background: linear-gradient(90deg, #28a745, #218838);
                color: white;
            }
            .form-group {
                margin-bottom: 15px;
            }
            .btn-primary {
                background-color: #28a745;
                border-color: #28a745;
            }
            .btn-primary:hover {
                background-color: #218838;
                border-color: #218838;
            }
            .error-message {
                font-size: 16px;
                margin-bottom: 20px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>
        <div class="container">
            <div class="card shadow">
                <div class="card-header">
                    <h4><i class="bi bi-pencil me-2"></i>Update Product</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <p class="text-center text-danger error-message">${errorMessage}</p>
                    </c:if>
                    <c:choose>
                        <c:when test="${not empty product}">
                            <form action="manageproduct" method="post" onsubmit="return validateForm()">
                                <input type="hidden" name="service" value="update"/>
                                <input type="hidden" name="id" value="${product.id}"/>
                                <div class="form-group">
                                    <label for="name">Product Name</label>
                                    <input type="text" class="form-control" id="name" name="name" value="${product.name}" required>
                                </div>
                                <div class="form-group">
                                    <label for="description">Description</label>
                                    <textarea class="form-control" id="description" name="description" rows="4">${product.description}</textarea>
                                </div>
                                <div class="form-group">
                                    <label for="price">Price</label>
                                    <input type="number" step="0.01" min="0" class="form-control" id="price" name="price" value="${product.price}" required>
                                </div>
                                <div class="form-group">
                                    <label for="stock">Stock</label>
                                    <input type="number" min="0" class="form-control" id="stock" name="stock" value="${product.stock}" required>
                                </div>
                                <div class="form-group">
                                    <label for="imgUrl">Image URL</label>
                                    <input type="text" class="form-control" id="imgUrl" name="imgUrl" value="${product.imgUrl}">
                                </div>
                                <div class="form-group">
                                    <label for="shelfLifeHours">Shelf Life (hours)</label>
                                    <input type="number" step="0.1" min="0" class="form-control" id="shelfLifeHours" name="shelfLifeHours" value="${product.shelfLifeHours}" required>
                                </div>
                                <div class="form-group">
                                    <label for="categoryId">Category</label>
                                    <select class="form-control" id="categoryId" name="categoryId" required>
                                        <option value="">Select Category</option>
                                        <c:forEach var="category" items="${categoryList}">
                                            <option value="${category.id}" ${category.id == product.category.id ? 'selected' : ''}>${category.name}</option>
                                        </c:forEach>
                                    </select>
                                </div>
                                <button type="submit" class="btn btn-primary"><i class="bi bi-save me-1"></i>Save Changes</button>
                                <a href="manageproduct?service=list" class="btn btn-secondary">Cancel</a>
                            </form>
                        </c:when>
                        <c:otherwise>
                            <p class="text-center text-danger error-message">Product not found.</p>
                            <a href="manageproduct?service=list" class="btn btn-secondary">Back to Product List</a>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
        
    </body>
</html>