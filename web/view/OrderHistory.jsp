<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History - Healthy Food Management System</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f4f4f4;
        }
        .container {
            max-width: 1200px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        .total-revenue {
            text-align: right;
            font-size: 18px;
            font-weight: bold;
            margin-bottom: 20px;
        }
        .search-filter {
            display: flex;
            gap: 20px;
            margin-bottom: 20px;
        }
        .search-filter input, .search-filter select, .search-filter button {
            padding: 8px;
            font-size: 16px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .search-filter button {
            background-color: #28a745;
            color: white;
            border: none;
            cursor: pointer;
        }
        .search-filter button:hover {
            background-color: #218838;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        th, td {
            padding: 12px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }
        th {
            background-color: #28a745;
            color: white;
        }
        tr:hover {
            background-color: #f5f5f5;
        }
        .pagination {
            text-align: center;
            margin-top: 20px;
        }
        .pagination a {
            display: inline-block;
            padding: 8px 16px;
            margin: 0 4px;
            text-decoration: none;
            color: #333;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        .pagination a.active {
            background-color: #28a745;
            color: white;
            border-color: #28a745;
        }
        .pagination a:hover {
            background-color: #ddd;
        }
        .error, .message {
            text-align: center;
            padding: 10px;
            margin-bottom: 20px;
            border-radius: 4px;
        }
        .error {
            background-color: #f8d7da;
            color: #721c24;
        }
        .message {
            background-color: #d4edda;
            color: #155724;
        }
        .details-link {
            color: #28a745;
            text-decoration: none;
        }
        .details-link:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Order History</h1>
        
        <c:if test="${not empty errorMessage}">
            <div class="error">${errorMessage}</div>
        </c:if>
        <c:if test="${not empty message}">
            <div class="message">${message}</div>
        </c:if>

        <div class="total-revenue">
            Total Revenue: ${totalRevenue != null ? totalRevenue : '0.00'} VND
        </div>

        <form class="search-filter" action="${pageContext.request.contextPath}/seller?service=requestOrderHistory" method="get">
            <input type="hidden" name="service" value="requestOrderHistory">
            <input type="text" name="search" placeholder="Search by Order ID or Customer Name" value="${search}">
            <select name="status">
                <option value="">All Statuses</option>
                <option value="Pending" ${status == 'Pending' ? 'selected' : ''}>Pending</option>
                <option value="Out for Delivery" ${status == 'Out for Delivery' ? 'selected' : ''}>Out for Delivery</option>
                <option value="Delivered" ${status == 'Delivered' ? 'selected' : ''}>Delivered</option>
                <option value="Cancelled" ${status == 'Cancelled' ? 'selected' : ''}>Cancelled</option>
            </select>
            <button type="submit">Filter</button>
        </form>

        <table>
            <tr>
                <th>Order ID</th>
                <th>Customer Name</th>
                <th>Order Date</th>
                <th>Total Amount (VND)</th>
                <th>Payment Method</th>
                <th>Status</th>
                <th>Details</th>
            </tr>
            <c:forEach var="order" items="${orderList}">
                <tr>
                    <td>${order.id}</td>
                    <td>${order.customerName}</td>
                    <td>${order.orderDate}</td>
                    <td>${order.totalAmount}</td>
                    <td>${order.paymentMethod}</td>
                    <td>${order.status}</td>
                    <td><a class="details-link" href="${pageContext.request.contextPath}/seller?service=requestOrderDetails&orderId=${order.id}">View Details</a></td>
                </tr>
            </c:forEach>
            <c:if test="${empty orderList}">
                <tr>
                    <td colspan="7" style="text-align: center;">No orders found.</td>
                </tr>
            </c:if>
        </table>

        <div class="pagination">
            <c:if test="${totalPages > 1}">
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <a href="${pageContext.request.contextPath}/seller?service=requestOrderHistory&page=${i}&search=${search}&status=${status}"
                       class="${i == currentPage ? 'active' : ''}">${i}</a>
                </c:forEach>
            </c:if>
        </div>
    </div>
</body>
</html>