<%-- 
    Document   : customerOrderHistory
    Created on : Jul 24, 2025, 9:34:35 AM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/CSS/customerOrderHistory.css" rel="stylesheet" type="text/css"/>
        
        <title>Order History</title>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <table class="order-history" border="1">
                <thead>
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
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                <c:forEach var="order" items="${orderList}" varStatus="loop">
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
                        <td class="actions">
                            <a href="SellerOrderHistory?orderId=${order.id}" >View Details</a>
                            <c:if test="${order.status.id == 1}">
                            <a href="customerOrderHistory?cancelOrderId=${order.id}" >Cancel</a>
                            </c:if>
                        </td>
                    </tr>

                </c:forEach>
            </tbody>
        </table>
        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <c:url var="prevUrl" value="/customerOrderHistory">
                    <c:param name="page" value="${currentPage - 1}" />                 
                </c:url>
                <a class="page-link prev-next" href="${prevUrl}">Previous</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:url var="pageUrl" value="/customerOrderHistory">
                    <c:param name="page" value="${i}" />
                </c:url>
                <a href="${pageUrl}" class="page-link">${i}</a>
            </c:forEach>

            <c:if test="${currentPage < totalPages}">
                <c:url var="nextUrl" value="/customerOrderHistory">
                    <c:param name="page" value="${currentPage + 1}" />
                </c:url>
                <a class="page-link prev-next" href="${nextUrl}">Next</a>
            </c:if>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
