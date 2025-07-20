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
        .current-image {
            max-width: 120px;
            margin-bottom: 10px;
            border-radius: 8px;
            border: 1px solid #ccc;
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
        .notification-bar {
            position: fixed;
            top: 20px;
            left: 50%;
            transform: translateX(-50%);
            background-color: #dc3545;
            color: white;
            padding: 10px 20px;
            border-radius: 5px;
            box-shadow: 0 2px 10px rgba(0,0,0,0.2);
            z-index: 1000;
            display: none;
            max-width: 500px;
            text-align: center;
        }
    </style>
</head>
<body>
    <jsp:include page="header.jsp"></jsp:include>
    <jsp:include page="SideBarOfSheller.jsp" />
    <div class="notification-bar" id="notificationBar">
        <span id="notificationMessage"></span>
    </div>
    <div class="container">
        <div class="card">
            <div class="card-header text-center">
                <h4><i class="bi bi-pencil-square me-2"></i>Update Product</h4>
            </div>
            <div class="card-body">
                
                <form action="seller" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="service" value="update"/>
                    <input type="hidden" name="productId" value="${product.id}"/>

                    <!-- Product Name -->
                    <div class="form-group">
                        <label for="name" class="form-label">Product Name <span>*</span></label>
                        <input type="text" class="form-control" id="name" name="name" value="${product.name}" maxlength="255" required>
                    </div>

                    <!-- Description -->
                    <div class="form-group">
                        <label for="description" class="form-label">Description <span>*</span></label>
                        <textarea class="form-control" id="description" name="description" rows="4" maxlength="1000" required>${product.description}</textarea>
                    </div>

                    <!-- Price -->
                    <div class="form-group">
                        <label for="price" class="form-label">Price ($) <span>*</span></label>
                        <input type="number" step="0.01" min="0.01" class="form-control" id="price" name="price" value="${product.price}" required>
                    </div>

                    <!-- Stock -->
                    <div class="form-group">
                        <label for="stock" class="form-label">Stock <span>*</span></label>
                        <input type="number" min="0" class="form-control" id="stock" name="stock" value="${product.stock}" required>
                    </div>

                    <!-- Image -->
                    <div class="form-group">
                        <label for="imageFile" class="form-label">Product Image <span>*</span></label><br>
                        <c:if test="${not empty product.imgUrl}">
                            <img src="${product.imgUrl}" alt="Current Image" class="current-image">
                        </c:if>
                        <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/jpeg,image/png" required>
                    </div>

                    <!-- Shelf Life -->
                    <div class="form-group">
                        <label for="shelfLifeHours" class="form-label">Shelf Life (hours) <span>*</span></label>
                        <input type="number" step="0.1" min="0" class="form-control" id="shelfLifeHours" name="shelfLifeHours" value="${product.shelfLifeHours}" required>
                    </div>

                    <!-- Category -->
                    <div class="form-group">
                        <label for="categoryId" class="form-label">Category <span>*</span></label>
                        <select class="form-control" id="categoryId" name="categoryId" required>
                            <option value="" disabled <c:if test="${empty product.category.id || empty param.categoryId}">selected</c:if>>Select a category</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.id}" <c:if test="${category.id == product.category.id || category.id == param.categoryId}">selected</c:if>>${category.name}</option>
                            </c:forEach>
                        </select>
                    </div>

                    <!-- Submit Buttons -->
                    <div class="text-center">
                        <button type="submit" class="btn btn-primary">
                            <i class="bi bi-save me-1"></i>Update Product
                        </button>
                        <a href="seller?service=list" class="btn btn-secondary">Cancel</a>
                    </div>
                </form>
            </div>
        </div>
    </div>
    <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
                            <script>
        
        window.onload = function() {
            const errorMessage = document.getElementById('generalError').innerText;
            if (errorMessage) {
                const notificationBar = document.getElementById('notificationBar');
                const notificationMessage = document.getElementById('notificationMessage');
                notificationMessage.innerText = errorMessage;
                notificationBar.classList.add('show');

                //  5 
                setTimeout(() => {
                    notificationBar.classList.remove('show');
                }, 5000);
            }
        };
    </script>
</body>
</html>