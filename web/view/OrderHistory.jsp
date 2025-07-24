<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Seller Order History - Healthy Food</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    
    <style>
        body {
    background-color: #E2FEE5;
    font-family: 'Arial', sans-serif;
    margin: 0;
    padding: 0;
}

.page-content {
    padding-top: 180px; 
    padding-left: 30px;
    padding-right: 30px;
}


.container {
    
    background-color: #ffffff;
    border-radius: 15px;
    border: 1px solid black;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    padding: 30px;
    max-width: 1200px;
    margin-top: 30PX;
}


.table th, .table td {
    vertical-align: middle;
    text-align: center;
}


.order-details {
    display: none;
    background-color: #fefefe;
    padding: 20px;
    border-radius: 10px;
    margin-top: 15px;
    border: 1px solid #ddd;
}


.alert {
    margin-bottom: 20px;
    border-radius: 5px;
}


.pagination {
    justify-content: center;
    margin-top: 30px;
}

.pagination .page-item .page-link {
    color: #2c3e50;
}

.pagination .page-item.active .page-link {
    background-color: #A8E4A0;
    border-color: #A8E4A0;
    color: white;
}

.btn-toggle-details {
    padding: 5px 10px;
    font-size: 14px;
}

    </style>
</head>
<body>
    
  <c:if test="${sessionScope.user.getRole().getId()==2||sessionScope.user.getRole().getId()==3||sessionScope.user.getRole().getId()==5}">
    <jsp:include page="header.jsp"></jsp:include>
    </c:if>
    
    
    <div class="container">
        <h2 class="mb-4 text-center">Order History</h2>
        

        <!-- Display error messages -->
        <c:if test="${not empty errorMessage}">
            <div class="alert alert-danger">${errorMessage}</div>
        </c:if>

        <!-- Orders Table -->
        <c:choose>
            <c:when test="${empty orders}">
                <div class="alert alert-warning">
                    No orders found. Please ensure orders exist in the system.
                </div>
            </c:when>
            <c:otherwise>
                <table class="table table-striped table-bordered table-hover">
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
                                <td>${(currentPage - 1) * 15 + loop.index + 1}</td>
                                <td><fmt:formatDate value="${order.orderDate}" pattern="dd-MM-yyyy HH:mm"/></td>
                                <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencySymbol="$"/></td>
                                <td>${order.paymentMethod}</td>
                                <td>${order.status.statusName}</td>
                                <td>${order.status.description}</td>
                                <td>${order.receiverName}</td>
                                <td>${order.receiverPhone}</td>
                                <td>${order.receiverEmail}</td>
                                <td>${order.shippingAddress}</td>
                                <td>${order.shipper != null ? order.shipper.name : 'None'}</td>
                                <td>
                                    <a href="SellerOrderHistory?orderId=${order.id}" class="btn btn-sm btn-info">View Details</a>
                                </td>
                            </tr>
                            <!-- Order Details Sub-table -->
                            <tr class="order-details" id="details-${order.id}">
                                <td colspan="12">
                                    <h5 class="mb-3">Order Details</h5>
                                    <table class="table table-sm table-bordered">
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
                                                    <td>${detail.product.name}</td>
                                                    <td>${detail.quantity}</td>
                                                    <td><fmt:formatNumber value="${detail.price}" type="currency" currencySymbol="$"/></td>
                                                    <td><fmt:formatNumber value="${detail.quantity * detail.price}" type="currency" currencySymbol="$"/></td>
                                                </tr>
                                            </c:forEach>
                                            <c:if test="${empty order.orderDetails}">
                                                <tr>
                                                    <td colspan="4" class="text-center">No items in this order.</td>
                                                </tr>
                                            </c:if>
                                        </tbody>
                                    </table>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <!-- Pagination -->
                <c:if test="${totalPages > 1}">
                    <nav aria-label="Page navigation">
                        <ul class="pagination">
                            <c:if test="${currentPage > 1}">
                                <li class="page-item">
                                    <a class="page-link" href="SellerOrderHistory?page=${currentPage - 1}">Previous</a>
                                </li>
                            </c:if>
                            <c:forEach begin="1" end="${totalPages}" var="i">
                                <li class="page-item ${i == currentPage ? 'active' : ''}">
                                    <a class="page-link" href="SellerOrderHistory?page=${i}">${i}</a>
                                </li>
                            </c:forEach>
                            <c:if test="${currentPage < totalPages}">
                                <li class="page-item">
                                    <a class="page-link" href="SellerOrderHistory?page=${currentPage + 1}">Next</a>
                                </li>
                            </c:if>
                        </ul>
                    </nav>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        document.querySelectorAll('.btn-toggle-details').forEach(button => {
            button.addEventListener('click', () => {
                const orderId = button.getAttribute('data-order-id');
                const detailsRow = document.getElementById(`details-${orderId}`);
                detailsRow.style.display = detailsRow.style.display === 'table-row' ? 'none' : 'table-row';
            });
        });
    </script>
</body>
</html>