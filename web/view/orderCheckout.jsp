<%-- 
    Document   : orderCheckout
    Created on : Jun 26, 2025, 12:20:49 AM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="${pageContext.request.contextPath}/CSS/cart.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <h1>Place order successfully, please wait us to check your order information!</h1>
            <h2>Your order:</h2>
            <div class="order-itemTable">
                <table class="cart-table">
                    <tr class="table-header">
                        <th>Number</th>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Total Price</th>
                        <th>Shelf Life Hours</th>
                    </tr>
                <c:if test="${sessionScope.user.getRole().getId()== 3}">
                    <c:set var="totalAmount" value="0"/>
                    <c:forEach items="${sessionScope.itemList}" var="i" varStatus="loop">
                        <tr class="cartItem">
                            <td>${loop.index + 1}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/productDetail?productId=${i.product.id}">
                                    <img src="${i.product.imgUrl}" width="80" alt="Product"></a>
                                    ${i.product.name}
                            </td>
                            <td>${i.quantity}</td>
                            <td><fmt:formatNumber value="${i.product.price * i.quantity}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                            <td>${i.product.shelfLifeHours}</td>
                        </tr>
                        <c:set var="totalAmount" value="${totalAmount + (i.product.price * i.quantity)}"/>
                    </c:forEach>
                    <tr class="totalAmount">
                        <td colspan="3"></td>
                        <td>Total Amount:</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                    </tr>
                </c:if>
                <c:if test="${sessionScope.user == null}">
                    <c:forEach items="${sessionScope.itemList}" var="i" varStatus="loop">
                        <tr class="cartItem">
                            <td>${loop.index + 1}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/productDetail?productId=${i.product.id}">
                                    <img src="${i.product.imgUrl}" width="80" alt="Product"></a>
                                    ${i.product.name}
                            </td>
                            <td>${i.quantity}</td>
                            <td><fmt:formatNumber value="${i.product.price * i.quantity}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                            <td>${i.product.shelfLifeHours}</td>
                        </tr>
                        <c:set var="totalAmount" value="${totalAmount + (i.product.price * i.quantity)}"/>
                    </c:forEach>
                    <tr class="totalAmount">
                        <td colspan="3"></td>
                        <td>Total Amount:</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                    </tr>
                </c:if>
            </table>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
