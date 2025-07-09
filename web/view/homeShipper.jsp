<%-- 
    Document   : homeShipper
    Created on : Jul 9, 2025, 9:10 AM
    Author     : Cuong
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Shipper Home</title>
        <link rel="stylesheet" href="CSS/homeShipper.css"> <!-- File CSS riêng -->
    </head>
    <body>
        <!-- Thêm header nếu cần, tương tự headerAdmin.jsp -->


        <div class="welcome">
            <img src="images/shipper.jpg" alt="Shipper Avatar" onclick="location.href='HomeShipperServlet'">
            <h2>Welcome <c:out value="${sessionScope.user.name}" /></h2>
        </div>
        <div class="dashboard">
            <div class="card" onclick="location.href='WaitingOrders'">
                <img src="images/waiting_delivery.jpg" alt="Waiting for Delivery">
                <h3>Waiting for Delivery</h3>
            </div>
            <div class="card" onclick="location.href='DeliveringOrders'">
                <img src="images/delivering.jpg" alt="Delivering">
                <h3>Delivering</h3>
            </div>
            <div class="card" onclick="location.href='DeliveredOrders'">
                <img src="images/delivered.jpg" alt="Delivered">
                <h3>Delivered</h3>
            </div>
        </div>
    </body>
</html>