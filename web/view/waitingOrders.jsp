<%-- view/waitingOrders.jsp --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Waiting for Delivery Orders</title>
        <script src="https://cdn.tailwindcss.com"></script>
    </head>
    <body class="bg-gray-100 font-sans">


        <h1 class="text-3xl font-bold text-center mt-6">Waiting for Delivery Orders</h1>
        <div class="container mx-auto p-4">
            <c:if test="${not empty error}">
                <p class="text-red-600 text-center mb-4">${error}</p>
            </c:if>
            <div class="space-y-4">
                <c:choose>
                    <c:when test="${not empty waitingOrders}">
                        <c:forEach var="order" items="${waitingOrders}">
                            <div class="bg-white p-4 rounded-lg shadow-md">
                                <p><strong>Order ID:</strong> ${order.id}</p>
                                <p><strong>Customer:</strong> ${order.user.name}</p>
                                <p><strong>Address:</strong> ${order.shippingAddress}</p>
                                <p><strong>Time:</strong> <fmt:formatDate value="${order.orderDate}" pattern="hh:mm a z"/></p>
                                <p><strong>Total:</strong> $<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></p>
                                <p><strong>Status:</strong> ${order.status.statusName}</p>
                                <p><strong>Shipper:</strong> ${order.shipper != null ? order.shipper.name : 'Not assigned'}</p>
                                <form action="${pageContext.request.contextPath}/ShipperUpdateStatusServlet" method="post">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <select class="mt-2 p-2 border rounded w-full" name="statusId">
                                        <c:forEach var="status" items="${order.validStatuses}">
                                            <option value="${status.id}" ${order.status.id == status.id ? 'selected' : ''}>${status.statusName}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Update Status</button>
                                </form>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center text-gray-500">No orders waiting for delivery.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </body>
</html>