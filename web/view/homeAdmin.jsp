<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin - Role Management</title>
        <link rel="stylesheet" href="CSS/homeAdmin.css">
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp"></jsp:include>

            <div class="welcome">
                <img src="images/admin.jpg" alt="Admin Avatar" onclick="location.href = 'DisplayAccount?idRole=1'">
                <h2>Welcome <c:out value="${sessionScope.user.name}" /></h2>
        </div>
        <div class="dashboard">
            <div class="card" onclick="location.href = 'DisplayAccount?idRole=1'">
                <img src="images/systemadmin.jpg" alt="System Admin">
                <h3>System Admin</h3>
            </div>
            <div class="card" onclick="location.href = 'DisplayAccount?idRole=2'">
                <img src="images/managershop.jpg" alt="Manager">
                <h3>Manager</h3>
            </div>
            <div class="card" onclick="location.href = 'DisplayAccount?idRole=3'">
                <img src="images/customer.jpg" alt="Customer">
                <h3>Customer</h3>
            </div>
            <div class="card" onclick="location.href = 'DisplayAccount?idRole=4'">
                <img src="images/nutritionist.jpg" alt="Nutritionist">
                <h3>Nutritionist</h3>
            </div>
            <div class="card" onclick="location.href = 'DisplayAccount?idRole=5'">
                <img src="images/seller.jpg" alt="Seller">
                <h3>Seller</h3>
            </div>
            <div class="card" onclick="location.href = 'DisplayAccount?idRole=6'">
                <img src="images/shipper.jpg" alt="Shipper">
                <h3>Shipper</h3>
            </div>
        </div>
    </body>
</html>
