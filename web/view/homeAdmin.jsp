<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin - Role Management</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
            }
            .header {
                background: linear-gradient(to right, #00BCD4, #00E676);
                padding: 10px 20px;
                display: flex;
                justify-content: space-between;
                align-items: center;
            }
            .header a {
                color: white;
                text-decoration: none;
                font-size: 20px;
            }
            .header .user-info {
                display: flex;
                align-items: center;
            }
            .header .user-info span {
                margin-right: 10px;
                color: white;
            }
            .welcome {
                text-align: center;
                margin: 20px 0;
            }
            .welcome img {
                border-radius: 50%;
                width: 100px;
                cursor: pointer;
            }
            .dashboard {
                display: grid;
                grid-template-columns: repeat(3, 1fr);
                gap: 20px;
                max-width: 1000px;
                margin: 0 auto;
                padding: 20px;
            }
            .dashboard .card {
                background: #f9f9f9;
                border: 1px solid #ddd;
                border-radius: 5px;
                text-align: center;
                padding: 20px;
                cursor: pointer;
            }
            .dashboard .card img {
                width: 50px;
            }
        </style>
    </head>
    <body>

        <div class="welcome">
            <img src="images/admin.jpg" alt="Admin Avatar" onclick="location.href='DisplayAccount?idRole=1'">
            <h2>Welcome "Admin 1"</h2>
        </div>
        <div class="dashboard">
            <div class="card" onclick="location.href='DisplayAccount?idRole=1'">
                <img src="images/systemadmin.jpg" alt="System Admin">
                <h3>System Admin</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=2'">
                <img src="images/managershop.jpg" alt="Manager">
                <h3>Manager</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=3'">
                <img src="images/customer.jpg" alt="Customer">
                <h3>Customer</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=4'">
                <img src="images/nutritionist.jpg" alt="Nutritionist">
                <h3>Nutritionist</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=5'">
                <img src="images/seller.jpg"  alt="Seller">
                <h3>Seller</h3>
            </div>
            <div class="card" onclick="location.href='DisplayAccount?idRole=6'">
                <img src="images/shipper.jpg"  alt="Shipper">
                <h3>Shipper</h3>
            </div>
        </div>
    </body>
</html>