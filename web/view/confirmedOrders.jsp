<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Confirmed Orders</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <link rel="stylesheet" href="CSS/processingOrders.css">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <style>
            .order-card {
                cursor: pointer;
                transition: background-color 0.3s ease;
            }
            .order-card:hover {
                background-color: #f0f8ff;
            }
            .order-link {
                color: #1a73e8;
                text-decoration: none;
            }
            .order-link:hover {
                text-decoration: underline;
            }
        </style>
    </head>
    <body>
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>

            <div class="container mt-4">
                <h1 class="page-title text-center fw-bold mb-4">Pending Orders (Waiting for Confirmation)</h1>

            <c:if test="${not empty error}">
                <p class="text-danger text-center">${error}</p>
            </c:if>

            <div class="row">
                <c:choose>
                    <c:when test="${not empty pendingOrders}">
                        <c:forEach var="order" items="${pendingOrders}">
                            <div class="col-md-6 mb-4">
                                <div class="order-card shadow-sm">
                                    <div class="card-body">
                                        <p><strong>Order ID:</strong> <a href="${pageContext.request.contextPath}/ConfirmedOrders?orderId=${order.id}" class="order-link">${order.id}</a></p>
                                        <p><strong>Customer:</strong> ${order.receiverName}</p>
                                        <p><strong>Customer Phone:</strong> ${order.receiverPhone}</p>
                                        <p><strong>Address:</strong> ${order.shippingAddress}</p>
                                        <p><strong>Delivery Message:</strong> ${order.deliveryMessage}</p>
                                        <p><strong>Time:</strong> <fmt:formatDate value="${order.orderDate}" pattern="hh:mm a z"/></p>
                                        <p><strong>Total:</strong> $<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></p>
                                        <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                                        <p><strong>Status:</strong> ${order.status.statusName}</p>
                                        <form action="${pageContext.request.contextPath}/SellerUpdateStatus" method="post">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <select class="form-select mb-2" name="statusId">
                                                <option value="2">Confirm</option>
                                                <option value="7">Cancel</option>
                                            </select>
                                            <button type="submit" class="btn btn-primary w-100">Update Status</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">No pending orders.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>