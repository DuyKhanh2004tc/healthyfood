<%-- view/processingOrders.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Processing Orders</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </head>
    <body class="bg-gray-100">
        <jsp:include page="SideBarOfSheller.jsp"></jsp:include>
            <h1 class="text-3xl font-bold text-center mt-6">Processing Orders</h1>
            <div class="container mt-4">
            <c:if test="${not empty error}">
                <p class="text-danger text-center">${error}</p>
            </c:if>
            <div class="row">
                <c:choose>
                    <c:when test="${not empty processingOrders}">
                        <c:forEach var="order" items="${processingOrders}">
                            <div class="col-md-6 mb-3">
                                <div class="card">
                                    <div class="card-body">
                                        <p><strong>Order ID:</strong> ${order.id}</p>
                                        <p><strong>Customer:</strong> ${order.receiverName}</p>
                                        <p><strong>Customer Phone:</strong> ${order.receiverPhone}</p>
                                        <p><strong>Address:</strong> ${order.shippingAddress}</p>
                                        <p><strong>Time:</strong> <fmt:formatDate value="${order.orderDate}" pattern="hh:mm a z"/></p>
                                        <p><strong>Delivery Message:</strong> ${order.deliveryMessage}</p>
                                        <p><strong>Total:</strong> $<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></p>
                                        <p><strong>Payment Method:</strong> ${order.paymentMethod}</p>
                                        <p><strong>Status:</strong> ${order.status.statusName}</p>
                                        <form action="${pageContext.request.contextPath}/SellerUpdateStatus" method="post">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <select class="form-select mb-2" name="statusId">
                                                <option value="3" ${order.status.id == 3 ? 'selected' : ''}>Processing</option>
                                                <option value="7" ${order.status.id == 7 ? 'selected' : ''}>Cancel</option>
                                            </select>
                                            <button type="submit" class="btn btn-primary">Update Status</button>
                                        </form>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center">No processing orders.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>