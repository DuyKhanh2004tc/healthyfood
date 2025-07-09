<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Insert Product</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" rel="stylesheet">
        <link rel="stylesheet" href="CSS/InsertProduct.css">
    </head>
    <body>
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>
        <div class="container">
            <div class="card shadow">
                <div class="card-header">
                    <h4><i class="bi bi-plus-circle me-2"></i>Insert new Product</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <p class="general-error">${errorMessage}</p>
                    </c:if>
                    <form action="seller" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="service" value="insert"/>
                        <div class="form-group">
                            <label for="name">Product Name</label>
                            <input type="text" class="form-control" id="name" name="name" value="${param.name}">
                            <c:if test="${not empty nameError}">
                                <span class="error-message">${nameError}</span>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="description">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="4">${param.description}</textarea>
                        </div>
                        <div class="form-group">
                            <label for="price">Price</label>
                            <input type="number" step="0.01" min="0" class="form-control" id="price" name="price" value="${param.price}">
                            <c:if test="${not empty priceError}">
                                <span class="error-message">${priceError}</span>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="stock">Stock</label>
                            <input type="number" min="0" class="form-control" id="stock" name="stock" value="${param.stock}">
                            <c:if test="${not empty stockError}">
                                <span class="error-message">${stockError}</span>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="imageFile">Product Image</label>
                            <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/jpeg,image/png">
                            <c:if test="${not empty imageError}">
                                <span class="error-message">${imageError}</span>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="shelfLifeHours">Shelf Life (hours)</label>
                            <input type="number" step="0.1" min="0" class="form-control" id="shelfLifeHours" name="shelfLifeHours" value="${param.shelfLifeHours}">
                            <c:if test="${not empty shelfLifeError}">
                                <span class="error-message">${shelfLifeError}</span>
                            </c:if>
                        </div>
                        <div class="form-group">
                            <label for="categoryName">Category</label>
                            <select class="form-control" id="categoryName" name="categoryName">
                                <option value="" disabled ${empty param.categoryName ? 'selected' : ''}>Select a category</option>
                                <c:forEach var="category" items="${categories}">
                                    <option value="${category.name}" ${param.categoryName == category.name ? 'selected' : ''}:${category.name}</option>
                                </c:forEach>
                            </select>
                            <c:if test="${not empty categoryError}">
                                <span class="error-message">${categoryError}</span>
                            </c:if>
                        </div>
                        <button type="submit" class="btn btn-primary"><i class="bi bi-save me-1"></i>Insert Product</button>
                        <a href="seller?service=list" class="btn btn-secondary">Cancel</a>
                    </form>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    </body>
</html>