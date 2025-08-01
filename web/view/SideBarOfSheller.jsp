<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Manager Menu</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap CSS & Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">

        <style>
            body {
                background-color: #f8f9fa;
            }
            .offcanvas-header {
                background-color: #4CAF50;
                color: white;
            }
            .offcanvas-body {
                background-color: #4CAF50;
                padding: 0;
            }
            .offcanvas-body a {
                color: #ffffff;
                padding: 12px 20px;
                display: block;
                text-decoration: none;
            }
            .offcanvas-body a:hover {
                background-color: #495057;
                color: #ffc107;
            }
        </style>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <div class="container-fluid">
            <!-- Nút mở sidebar -->
            <button class="btn btn-success mt-2 ms-2" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu">
                <i class="bi bi-list"></i> Menu
            </button>
        </div>

        <!-- Sidebar Offcanvas (chỉ load khi cần) -->
        <div class="offcanvas offcanvas-start" tabindex="-1" id="sidebarMenu">
            <div class="offcanvas-header">
                <h5 class="offcanvas-title">Dashboard Menu</h5>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas"></button>
            </div>
            <c:if test="${sessionScope.user.getRole().getId()== 2}">
            <div class="offcanvas-body">
                <a href="seller"><i class="bi bi-boxes me-2"></i>Product Management</a>
                <a href="ManagerCategoriesServlet"><i class="bi bi-boxes me-2"></i>Category Management</a>
                <a href="approve"><i class="bi bi-check2-circle me-2"></i>Approve New Product</a>
                <a href="SellerCanceledOrders"><i class="bi bi-x-circle me-2"></i>Cancel Order</a>
                <a href="SellerOrderHistory"><i class="bi bi-clock-history me-2"></i>Order History</a>
            </div>
            </c:if>
            <c:if test="${sessionScope.user.getRole().getId()== 5}">
            <div class="offcanvas-body">             
                <a href="ConfirmedOrders"><i class="bi bi-check2-circle me-2"></i>Confirm Order</a>
                <a href="ProcessingOrders"><i class="bi bi-gear-wide-connected me-2"></i>Processing Order</a>
                <a href="SellerWaitingOrders"><i class="bi bi-truck me-2"></i>Waiting for Delivery</a>
                <a href="SellerDeliveringOrders"><i class="bi bi-truck me-2"></i>Delivering and Waiting Orders</a>
                <a href="SellerCanceledOrders"><i class="bi bi-x-circle me-2"></i>Cancel Order</a>
                <a href="SellerOrderHistory"><i class="bi bi-clock-history me-2"></i>Order History</a>
            </div>
            </c:if>
        </div>

        <!-- Page content -->
        <div class="container mt-4">
            <!-- your processing orders content here -->
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>

</html>