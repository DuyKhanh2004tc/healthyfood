<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Order Details</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="CSS/processingOrders.css">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <style>
        .detail-container {
            max-width: 800px;
            margin: 20px auto;
            padding: 20px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
        }
        .detail-item {
            margin-bottom: 10px;
            padding: 5px;
            border-bottom: 1px solid #eee;
        }
        .back-link {
            display: inline-block;
            margin-top: 20px;
            color: #1a73e8;
            text-decoration: none;
        }
        .back-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <jsp:include page="SideBarOfSheller.jsp"></jsp:include>

    <div class="container detail-container">
        <c:if test="${not empty error}">
            <p class="text-danger text-center">${error}</p>
        </c:if>
        <c:if test="${not empty order}">
            <h2 class="text-center mb-4">Order Details - ID: ${order.id}</h2>
            <p><strong>Customer:</strong> ${order.receiverName}</p>
            <p><strong>Address:</strong> ${order.shippingAddress}</p>
            <p><strong>Delivery Message:</strong> ${order.deliveryMessage}</p>
            <p><strong>Time:</strong> <fmt:formatDate value="${order.orderDate}" pattern="hh:mm a z"/></p>
            <p><strong>Total:</strong> $<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></p>
            <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
            <p><strong>Status:</strong> ${order.status.statusName}</p>
            <hr>
            <h4>Order Items:</h4>
            <c:choose>
                <c:when test="${not empty order.orderDetails}">
                    <c:forEach var="detail" items="${order.orderDetails}" varStatus="loop">
                        <div class="detail-item">
                            <p><strong>Item ${loop.count}:</strong> ${detail.product.name} - Quantity: ${detail.quantity}, Price: $<fmt:formatNumber value="${detail.price}" type="number" maxFractionDigits="2"/></p>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-center text-muted">No items found for this order.</p>
                </c:otherwise>
            </c:choose>
            <c:choose>
                <c:when test="${param.fromPage eq 'processing'}">
                    <a href="${pageContext.request.contextPath}/ProcessingOrders" class="back-link">Back to Processing Orders</a>
                </c:when>
                <c:when test="${param.fromPage eq 'waiting'}">
                    <a href="${pageContext.request.contextPath}/SellerWaitingOrders" class="back-link">Back to Waiting Orders</a>
                </c:when>
                <c:when test="${param.fromPage eq 'delivering'}">
                    <a href="${pageContext.request.contextPath}/SellerDeliveringOrders" class="back-link">Back to Delivering Orders</a>
                </c:when>
                <c:when test="${param.fromPage eq 'canceled'}">
                    <a href="${pageContext.request.contextPath}/SellerCanceledOrders" class="back-link">Back to Canceled Orders</a>
                </c:when>
                <c:otherwise>
                    <a href="${pageContext.request.contextPath}/ConfirmedOrders" class="back-link">Back to Pending Orders</a>
                </c:otherwise>
            </c:choose>
        </c:if>
    </div>
</body>
</html>