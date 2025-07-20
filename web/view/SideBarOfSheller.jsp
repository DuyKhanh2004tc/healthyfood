<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Manager Menu</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <!-- Bootstrap CSS & Icons -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/CSS/homeManager.css" rel="stylesheet" type="text/css"/>

        
    </head>
    <body class="sidebar">

        <!-- Navbar chứa nút mở sidebar -->
        
        <nav class="navbar navbar-light bg-light">
            <div class="container-fluid">
                <button class="btn btn-success" type="button" data-bs-toggle="offcanvas" data-bs-target="#sidebarMenu">
                    <i class="bi bi-list"></i> Menu
                </button>

            </div>
        </nav>

        <!-- Sidebar Offcanvas -->
        <div class="offcanvas offcanvas-start" tabindex="-1" id="sidebarMenu">
            <div class="offcanvas-header">
                <a href="home">
                <h5 class="offcanvas-title">Dashboard Menu</h5>
                </a>
                <button type="button" class="btn-close btn-close-white" data-bs-dismiss="offcanvas"></button>
            </div >
            <div class="offcanvas-body">
                <c:if test="${sessionScope.user.getRole().getId()==2}">
                <a href="seller"><i class="bi bi-boxes me-2"></i>Product Management</a>
                </c:if>
                <a href="ConfirmedOrders"><i class="bi bi-check2-circle me-2"></i>Confirm Order</a>
                <a href="ProcessingOrders"><i class="bi bi-gear-wide-connected me-2"></i>Processing Order</a>
                <a href="SellerWaitingOrders"><i class="bi bi-truck me-2"></i>Waiting for Delivery</a>
                <a href="SellerDeliveringOrders"><i class="bi bi-truck me-2"></i>Delivering and Waiting Orders</a>
                <a href="SellerCanceledOrders"><i class="bi bi-x-circle me-2"></i>Cancel Order</a>
                <a href="SellerOrderHistory"><i class="bi bi-clock-history me-2"></i>Order History</a>
                
            </div>


        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
