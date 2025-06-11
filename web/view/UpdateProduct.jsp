<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Product</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body { background-color: #f8f9fa; }
            .card { max-width: 600px; margin: 50px auto; }
        </style>
    </head>
    <body>
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>
        <div class="container">
            <div class="card shadow">
                <div class="card-header bg-primary text-white">
                    <h4>Update Product</h4>
                </div>
                <div class="card-body">
                    <c:if test="${not empty errorMessage}">
                        <p class="text-danger">${errorMessage}</p>
                    </c:if>
                    <c:if test="${not empty product}">
                        <p>Product ID: ${product.id}</p>
                        <p>Name: ${product.name}</p>
                        <p>Price: ${product.price}</p>
                        <p><a href="manageproduct?service=list" class="btn btn-secondary">Back to Product List</a></p>
                    </c:if>
                    <c:if test="${empty product}">
                        <p class="text-danger">Product not found.</p>
                    </c:if>
                </div>
            </div>
        </div>
        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    </body>
</html>