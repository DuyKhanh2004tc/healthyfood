<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ include file="SideBarOfSheller.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Order History</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #eef2f7;
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
        }
        .container {
            padding: 40px 15px;
        }
        .table {
            border-radius: 8px;
            overflow: hidden;
        }
        .table-dark {
            background: linear-gradient(135deg, #28a745, #218838);
        }
        .alert {
            border-radius: 8px;
        }
        .btn-primary {
            background-color: #28a745;
            border: none;
            border-radius: 8px;
        }
        .btn-primary:hover {
            background-color: #218838;
        }
    </style>
</head>
<body>
    
    <div class="container mt-5">
        <h2 class="mb-4">Order History</h2>
        <div class="alert alert-info" role="alert">
            Logged-in Seller ID: ${sellerId != null ? sellerId : 'Not available'}
        </div>
        <c:choose>
            <c:when test="${empty orders}">
                <div class="alert alert-warning" role="alert">
                    No orders found for Seller ID: ${sellerId != null ? sellerId : 'Unknown'}. 
                    Please ensure orders exist for your products.
                </div>
            </c:when>
            <c:otherwise>
                <table class="table table-striped table-bordered">
                    <thead class="table-dark">
                        <tr>
                            <th>#</th>
                            <th>Order Date</th>
                            <th>Total Amount</th>
                            <th>Payment Method</th>
                            <th>Status</th>
                            <th>Description</th>
                            <th>Receiver Name</th>
                            <th>Receiver Phone</th>
                            <th>Receiver Email</th>
                            <th>Shipping Address</th>
                            <th>Shipper</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="order" items="${orders}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${order.orderDate}</td>
                                <td>${String.format("%.2f", order.totalAmount)}</td>
                                <td>${order.paymentMethod}</td>
                                <td>${order.status.statusName}</td>
                                <td>${order.status.description != null ? order.status.description : 'N/A'}</td>
                                <td>${order.receiverName}</td>
                                <td>${order.receiverPhone}</td>
                                <td>${order.receiverEmail}</td>
                                <td>${order.shippingAddress}</td>
                                <td>${order.shipper != null ? order.shipper.name : 'None'}</td>
                                <td>
                                    <a href="OrderDetailServlet?id=${order.id}" class="btn btn-primary btn-sm">View Details</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:otherwise>
        </c:choose>
    </div>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>