<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order History - Healthy Food</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body {
            background-color: #f8f9fa;
        }
        .container {
            margin-top: 30px;
        }
        .table th, .table td {
            vertical-align: middle;
        }
        .order-details {
            display: none;
            background-color: #fff;
            padding: 15px;
            border-radius: 5px;
            margin-top: 10px;
        }
        .message, .error-message {
            margin-bottom: 20px;
        }
        .pagination {
            justify-content: center;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1 class="mb-4">Order History</h1>

        <!-- Display success or error messages -->
        <c:if test="${not empty message}">
            <div class="alert alert-success message">${message}</div>
        </c:if>
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger error-message">${errorMessage}</div>
        </c:if>

        <!-- Orders Table -->
        <table class="table table-striped table-bordered">
            <thead class="table-dark">
                <tr>
                    <th>Order ID</th>
                    <th>Customer Name</th>
                    <th>Order Date</th>
                    <th>Total Amount</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="order" items="${orders}">
                    <tr>
                        <td>${order.id}</td>
                        <td>${order.receiverName}</td>
                        <td><fmt:formatDate value="${order.orderDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                        <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/></td>
                        <td>${order.status}</td>
                        <td>
                            <button class="btn btn-sm btn-info toggle-details" data-order-id="${order.id}">View Details</button>
                        </td>
                    </tr>
                    <!-- Order Details Sub-table -->
                    <tr class="order-details" id="details-${order.id}">
                        <td colspan="6">
                            <h5>Order Details</h5>
                            <table class="table table-sm">
                                <thead>
                                    <tr>
                                        <th>Product Name</th>
                                        <th>Quantity</th>
                                        <th>Price</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="detail" items="${order.orderDetails}">
                                        <tr>
                                            <td>${detail.productName}</td>
                                            <td>${detail.quantity}</td>
                                            <td><fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="$"/></td>
                                            <td><fmt:formatNumber value="${detail.quantity * detail.price}" type="currency" currencySymbol="$"/></td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                </c:forEach>
                <c:if test="${empty orders}">
                    <tr>
                        <td colspan="6" class="text-center">No orders found.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>

        <!-- Pagination -->
        <c:if test="${totalPages > 1}">
            <nav aria-label="Page navigation">
                <ul class="pagination">
                    <c:if test="${currentPage > 1}">
                        <li class="page-item">
                            <a class="page-link" href="seller?service=orderHistory&page=${currentPage - 1}">Previous</a>
                        </li>
                    </c:if>
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <li class="page-item ${i == currentPage ? 'active' : ''}">
                            <a class="page-link" href="seller?service=orderHistory&page=${i}">${i}</a>
                        </li>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <li class="page-item">
                            <a class="page-link" href="seller?service=orderHistory&page=${currentPage + 1}">Next</a>
                        </li>
                    </c:if>
                </ul>
            </nav>
        </c:if>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.querySelectorAll('.toggle-details').forEach(button => {
            button.addEventListener('click', () => {
                const orderId = button.getAttribute('data-order-id');
                const detailsRow = document.getElementById(`details-${orderId}`);
                detailsRow.style.display = detailsRow.style.display === 'table-row' ? 'none' : 'table-row';
            });
        });
    </script>
</body>
</html>