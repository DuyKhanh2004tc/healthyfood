<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Details - Healthy Food</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
            font-family: Arial, sans-serif;
        }
        .container {
            margin-top: 30px;
            max-width: 1200px;
            
        }
        .table th, .table td {
            vertical-align: middle;
            text-align: center;
        }
        .alert {
            margin-bottom: 20px;
            border-radius: 5px;
        }
        .card {
            background-color: #fff;
            border: 1px solid black;
            border-radius: 7px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
            padding: 20px;
            margin-bottom: 20px;
        }
        .btn-back {
            margin-bottom: 20px;
        }
        .item {
            border: 1px solid black;
            border-radius: 7px;
            
        }
        .mb-3 {
            text-align: center;
        }
        .back {
            text-align: center; 
            margin-top: 10px;
        }
    </style>
</head>
<body>
  
    <div class="container">
        <h2 class="mb-4 text-center">Order Details</h2>
        
        <!-- Display error messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <!-- Order Information -->
        <c:choose>
            <c:when test="${empty order}">
                <div class="alert alert-warning">
                    No order details found for Order ID: ${param.orderId != null ? param.orderId : 'Unknown'}. Please ensure the order exists.
                </div>
            </c:when>
            <c:otherwise>
                <div class="card">
                    <h4 class="card-title">Order ID: ${order.id}</h4>
                    <div class="row">
                        <div class="col-md-6">
                            <p><strong>Order Date:</strong> <fmt:formatDate value="${order.orderDate}" pattern="dd-MM-yyyy HH:mm"/></p>
                            <p><strong>Total Amount:</strong> <fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/></p>
                            <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                            <p><strong>Status:</strong> ${order.status.statusName}</p>
                            <p><strong>Status Description:</strong> ${order.status.description}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Receiver Name:</strong> ${order.receiverName}</p>
                            <p><strong>Receiver Phone:</strong> ${order.receiverPhone}</p>
                            <p><strong>Receiver Email:</strong> ${order.receiverEmail}</p>
                            <p><strong>Shipping Address:</strong> ${order.shippingAddress}</p>
                            <p><strong>Shipper:</strong> ${order.shipper != null ? order.shipper.name : 'None'}</p>
                        </div>
                    </div>
                </div>

                <!-- Order Details Table -->
                <div class="item">
                <h4 class="mb-3">Order Items</h4>
                <table class="table table-striped table-bordered table-hover">
                    <thead class="table-dark">
                        <tr>
                            <th>#</th>
                            <th>Product Name</th>
                            <th>Quantity</th>
                            <th>Price</th>
                            <th>Subtotal</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="detail" items="${order.orderDetails}" varStatus="loop">
                            <tr>
                                <td>${loop.index + 1}</td>
                                <td>${detail.product.name}</td>
                                <td>${detail.quantity}</td>
                                <td><fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="$"/></td>
                                <td><fmt:formatNumber value="${detail.quantity * detail.price}" type="currency" currencySymbol="$"/></td>
                            </tr>
                        </c:forEach>
                        <c:if test="${empty order.orderDetails}">
                            <tr>
                                <td colspan="5" class="text-center">No items in this order.</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
</div>
                <!-- Back Button -->
                <div class="back">
                <a href="SellerOrderHistory" class="btn btn-primary btn-back">Back to Order History</a>
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>