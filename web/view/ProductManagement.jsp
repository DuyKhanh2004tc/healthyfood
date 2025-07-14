<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Product Seller</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" rel="stylesheet">
        <link rel="stylesheet" href="CSS/homeSeller.css">
        
    </head>
    <body>
        <div class="seller-header">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/home">
                <img src="${pageContext.request.contextPath}/images/logo_3.png" alt="Logo">
                </a>
            </div>
            <div class="menu-content-left">
                <h3>Welcome Seller : ${sessionScope.user.getName()}</h3>
                <a href="${pageContext.request.contextPath}/seller">View Product List</a>

                <a href="${pageContext.request.contextPath}/logout">Logout</a>

            </div>
        </div>

        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>

        <div class="container-fluid mt-4" ">
            <div class="d-flex justify-content-center" >
                <div class="card shadow border-0" style="width: 95%;">
                    <div class="card-header bg-success text-white fs-5">
                        <i class="bi bi-box-seam me-2"></i>Product Management
                    </div>
                    <div class="card-body">
                        <!-- Insert Button -->
                        <div class="text-center mb-4">
                            <a href="seller?service=requestInsert" class="btn btn-insert">
                                <i class="bi bi-plus-circle me-1"></i> Insert New Product
                            </a>
                        </div>

                        <!-- Search Form -->
                        <form action="seller" class="input-group search-form shadow-sm" method="get">
                            <input type="hidden" name="service" value="searchByKeywords"/>
                            <input type="text" class="form-control" id="keywords" name="keywords" placeholder="Search by product name" value="${keywords}">
                            <button class="btn btn-outline-primary" type="submit">
                                <i class="bi bi-search"></i> Search
                            </button>
                        </form>
                        <c:if test="${not empty errorMessage}">
                            <p class="text-center error-message">${errorMessage}</p>
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
                                                        <a href="seller?service=requestUpdate&productId=${product.id}" class="btn btn-success btn-edit">
                                                            <i class="bi bi-pencil"></i> Edit
                                                        </a>
                                                        <a href="seller?service=requestDelete&productId=${product.id}" class="btn btn-action btn-delete" onclick="return confirm('Are you sure you want to delete ${product.name}?');">
                                                            <i class="bi bi-trash"></i> Delete
                                                        </a>
                                                    </td>
                                                </tr>
                                                
                                            </c:forEach>
                                                
                                        </tbody>
                                        
                                    </table>
                                    
                                </div>
                                <!-- Pagination Controls -->
                                <nav aria-label="Page navigation">
                                    <ul class="pagination">
                                        <!-- Previous Button -->
                                        <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                                            <a class="page-link" href="seller?service=${service}&keywords=${keywords}&page=${currentPage - 1}" aria-label="Previous">
                                                <span aria-hidden="true">&laquo;</span>
                                            </a>
                                        </li>
                                        <!-- Page Numbers -->
                                        <c:forEach begin="1" end="${totalPages}" var="i">
                                            <li class="page-item ${currentPage == i ? 'active' : ''}">
                                                <a class="page-link" href="seller?service=${service}&keywords=${keywords}&page=${i}">${i}</a>
                                            </li>
                                        </c:forEach>
                                        <!-- Next Button -->
                                        <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                                            <a class="page-link" href="seller?service=${service}&keywords=${keywords}&page=${currentPage + 1}" aria-label="Next">
                                                <span aria-hidden="true">&raquo;</span>
                                            </a>
                                        </li>
                                    </ul>
                                </nav>
                            </c:when>
                            <c:otherwise>
                                <p class="text-center error-message">No products found!</p>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>
        </div>

        <script src="${pageContext.request.contextPath}/js/bootstrap.bundle.min.js"></script>
    </body>
</html>