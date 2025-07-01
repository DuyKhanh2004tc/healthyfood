<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Update Status</title>
    <script src="https://cdn.tailwindcss.com"></script>
</head>
<body class="bg-gray-100 font-sans">
    <div class="container mx-auto p-4">
        <!-- Header -->
        <h1 class="text-3xl font-bold text-center mb-6">Seller Update Status</h1>
        <p class="text-center text-gray-600 mb-6">Current Time: <fmt:formatDate value="<%= new java.util.Date() %>" pattern="hh:mm a z 'on' EEEE, MMMM dd, yyyy"/></p>

        <!-- Error or Success Message -->
        <c:if test="${not empty message}">
            <p class="text-green-600 text-center mb-4">${message}</p>
        </c:if>
        <c:if test="${not empty error}">
            <p class="text-red-600 text-center mb-4">${error}</p>
        </c:if>

        <!-- Orders List -->
        <div class="mb-6">
            <h2 class="text-2xl font-semibold mb-4">Orders to Update</h2>
            <div class="space-y-4">
                <c:choose>
                    <c:when test="${not empty orders}">
                        <c:forEach var="order" items="${orders}" varStatus="loop">
                            <div class="bg-white p-4 rounded-lg shadow-md">
                                <p><strong>Order ID:</strong> ${order.id}</p>
                                <p><strong>Customer:</strong> ${order.user.name}</p>
                                <p><strong>Address:</strong> ${order.shippingAddress}</p>
                                <p><strong>Time:</strong> <fmt:formatDate value="${order.orderDate}" pattern="hh:mm a z"/></p>
                                <p><strong>Total:</strong> $<fmt:formatNumber value="${order.totalAmount}" type="number" maxFractionDigits="2"/></p>
                                <p><strong>Current Status:</strong> ${order.status.statusName}</p>
                                <form action="${pageContext.request.contextPath}/SellerUpdateStatus" method="post">
                                    <input type="hidden" name="orderId" value="${order.id}">
                                    <select class="mt-2 p-2 border rounded w-full" name="statusId">
                                        <c:forEach var="status" items="${validStatusesList[loop.index]}">
                                            <option value="${status.id}" ${order.status.id == status.id ? 'selected' : ''}>${status.statusName}</option>
                                        </c:forEach>
                                    </select>
                                    <button type="submit" class="mt-2 px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600">Update Status</button>
                                </form>
                            </div>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <p class="text-center text-gray-500">No orders available to update.</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</body>
</html>