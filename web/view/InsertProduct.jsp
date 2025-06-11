<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Insert New Product</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body { background-color: #f8f9fa; }
            .card { max-width: 600px; margin: 50px auto; }
            .form-group { margin-bottom: 15px; }
        </style>
    </head>
    <body>
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>
        <div class="container">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4>Insert New Product</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <p class="text-danger">${errorMessage}</p>
                    </c:if>
                    <form action="manageproduct" method="post">
                        <input type="hidden" name="service" value="insert"/>
                        <div class="form-group">
                            <label for="name">Product Name</label>
                            <input type="text" class="form-control" id="name" name="name" required>
                        </div>
                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="4"></textarea>
                        </div>
                        <div class="form-group">
                            <label for="price">Price</label>
                            <input type="number" step="0.01" min="0" class="form-control" id="price" name="price" required>
                        </div>
                        <div class="form-group">
                            <label for="stock">Stock</label>
                            <input type="number" min="0" class="form-control" id="stock" name="stock" required>
                        </div>
                        <div class="form-group">
                            <label for="imgUrl">Image URL</label>
                            <input type="text" class="form-control" id="imgUrl" name="imgUrl">
                        </div>
                        <div class="form-group">
                            <label for="shelfLifeHours">Shelf Life (hours)</label>
                            <input type="number" step="0.1" min="0" class="form-control" id="shelfLifeHours" name="shelfLifeHours" required>
                        </div>

                        <div class="form-group">
                            <label for="categoryId">Category</label>
                            <select class="form-control" id="categoryId" name="categoryId" required>
                                <option value="">Select Category</option>
                                <c:forEach var="category" items="${categoryList}">
                                    <option value="${category.id}">${category.name}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <button type="submit" class="btn btn-primary">Save Product</button>
                        <a href="manageproduct?service=list" class="btn btn-secondary">Cancel</a>
                    </form>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    </body>
</html>