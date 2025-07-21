<%-- view/sellerDeliveringOrders.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Delivering and Waiting Orders</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
    <link rel="stylesheet" href="CSS/processingOrders.css"> <!-- Dùng style đồng nhất -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
    <jsp:include page="SideBarOfSheller.jsp" />

    <div class="container mt-4">
        <h1 class="page-title text-center fw-bold mb-4">Delivering and Waiting Orders</h1>

        <c:if test="${not empty error}">
            <p class="text-danger text-center">${error}</p>
        </c:if>

        <div class="row">
            <c:choose>
                <c:when test="${not empty deliveringOrders}">
                    <c:forEach var="order" items="${deliveringOrders}">
                        <div class="col-md-6 mb-4">
                            <div class="order-card shadow-sm">
                                <div class="card-body">
                                    <p><strong>Order ID:</strong> ${order.id}</p>
                                    <p><strong>Customer:</strong> ${order.receiverName}</p>
                                    <p><strong>Customer Phone:</strong> ${order.receiverPhone}</p>
                                    <p><strong>Address:</strong> ${order.shippingAddress}</p>
                                    <p><strong>Delivery Message:</strong> ${order.deliveryMessage}</p>
                                    <p><strong>Time:</strong>
                                        <fmt:formatDate value="${order.orderDate}" pattern="hh:mm a z"/>
                                    </p>
                                    <p><strong>Total:</strong>
                                        $<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/>
                                    </p>
                                    <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                                    <p><strong>Status:</strong> ${order.status.statusName}</p>

                                    <c:choose>
                                        <c:when test="${not empty order.shipper}">
                                            <p><strong>Shipper Name:</strong> ${order.shipper.name}</p>
                                            <p><strong>Shipper Phone:</strong> ${order.shipper.phone}</p>
                                        </c:when>
                                        <c:otherwise>
                                            <p><strong>Shipper:</strong> Not assigned</p>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <p class="text-center text-muted">No delivering or waiting orders.</p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</body>
</html>
