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
        <style>
            body {
                background-color: #eef2f7;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            }
            .container {
                padding: 40px 15px;
            }
            .card {
                border-radius: 15px;
                box-shadow: 0 8px 16px rgba(0,0,0,0.1);
                max-width: 700px;
                margin: auto;
            }
            .card-header {
                background: linear-gradient(135deg, #28a745, #218838);
                color: white;
                border-top-left-radius: 15px;
                border-top-right-radius: 15px;
                padding: 20px;
            }
            .card-header h4 {
                margin: 0;
            }
            .form-group {
                margin-bottom: 20px;
            }
            .form-label {
                font-weight: 600;
                margin-bottom: 5px;
            }
            .form-control {
                border-radius: 8px;
            }
            .error-message {
                color: #dc3545;
                font-size: 13px;
                margin-top: 5px;
                display: block;
            }
            .general-error {
                color: #dc3545;
                font-weight: 500;
                text-align: center;
                margin-bottom: 20px;
            }
            .btn {
                border-radius: 8px;
                padding: 10px 20px;
            }
            .btn-primary {
                background-color: #28a745;
                border: none;
            }
            .btn-primary:hover {
                background-color: #218838;
            }
            .btn-secondary {
                margin-left: 10px;
            }
        </style>
    </head>
    <body>
        <jsp:include page="headerSeller.jsp"></jsp:include>
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>
        <div class="container">
            <div class="card">
                <div class="card-header text-center">
                    <h4><i class="bi bi-plus-circle me-2"></i>Insert New Product</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <p class="general-error">${errorMessage}</p>
                    </c:if>
                    <form action="seller" method="post" enctype="multipart/form-data">
                        <input type="hidden" name="service" value="insert"/>
                        <!-- Product Name -->
                        <div class="form-group">
                            <label for="name" class="form-label">Product Name</label>
                            <input type="text" class="form-control" id="name" name="name" value="${param.name}">
                            <c:if test="${not empty nameError}">
                                <span class="error-message">${nameError}</span>
                            </c:if>
                        </div>
                        <!-- Description -->
                        <div class="form-group">
                            <label for="description" class="form-label">Description</label>
                            <textarea class="form-control" id="description" name="description" rows="4">${param.description}</textarea>
                            <c:if test="${not empty descriptionError}">
                                <span class="error-message">${descriptionError}</span>
                            </c:if>
                        </div>
                        <!-- Price -->
                        <div class="form-group">
                            <label for="price" class="form-label">Price ($)</label>
                            <input type="number" step="0.01" min="0" class="form-control" id="price" name="price" value="${param.price}">
                            <c:if test="${not empty priceError}">
                                <span class="error-message">${priceError}</span>
                            </c:if>
                        </div>
                        <!-- Stock -->
                        <div class="form-group">
                            <label for="stock" class="form-label">Stock</label>
                            <input type="number" min="0" class="form-control" id="stock" name="stock" value="${param.stock}">
                            <c:if test="${not empty stockError}">
                                <span class="error-message">${stockError}</span>
                            </c:if>
                        </div>
                        <!-- Image -->
                        <div class="form-group">
                            <label for="imageFile" class="form-label">Product Image</label>
                            <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/jpeg,image/png">
                            <c:if test="${not empty imageError}">
                                <span class="error-message">${imageError}</span>
                            </c:if>
                        </div>
                        <!-- Shelf Life -->
                        <div class="form-group">
                            <label for="shelfLifeHours" class="form-label">Shelf Life (hours)</label>
                            <input type="number" step="0.1" min="0" class="form-control" id="shelfLifeHours" name="shelfLifeHours" value="${param.shelfLifeHours}">
                            <c:if test="${not empty shelfLifeError}">
                                <span class="error-message">${shelfLifeError}</span>
                            </c:if>
                        </div>
                        <!-- Category -->
                        <!-- Sửa: Thay select bằng input text để nhất quán với UpdateProduct.jsp và getOrInsertCategory -->
                        <div class="form-group">
                            <label for="categoryName" class="form-label">Category</label>
                            <input type="text" class="form-control" id="categoryName" name="categoryName" value="${param.categoryName}" maxlength="100">
                            <c:if test="${not empty categoryError}">
                                <span class="error-message">${categoryError}</span>
                            </c:if>
                        </div>
                        <!-- Submit Buttons -->
                        <div class="text-center">
                            <button type="submit" class="btn btn-primary"><i class="bi bi-save me-1"></i>Insert Product</button>
                            <a href="seller?service=list" class="btn btn-secondary">Cancel</a>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    </body>
</html>