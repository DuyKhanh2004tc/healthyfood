<%-- 
    Document   : cart
    Created on : Jun 18, 2025, 11:33:33 PM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Cart Page</title>
        <link href="${pageContext.request.contextPath}/CSS/cart.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>


            <table class="cart-table">
                <tr class="table-header">
                    <th>Number</th>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Total Price</th>
                    <th>Shelf Life Hours</th>
                </tr>
            <c:forEach items="${requestScope.itemList}" var="i">
                <tr class="cartItem">
                    <td>${i.id}</td>
                    <td>
                        <img src="${i.product.imgUrl}" width="80">
                        ${i.product.name}
                    </td>
                    <td>
                        <button class="btnSub" onclick="location.href = 'cart?number=-1&id=${i.product.id}'">-</button>
                        ${i.quantity}</span>
                        <button class="btnAdd" onclick="location.href = 'cart?number=1&id=${i.product.id}'">+</button>   
                    </td>
                    <td>${i.product.price * i.quantity}</td>
                    <td>${i.product.shelfLifeHours}</td>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${empty itemList}">
            <p>Your cart is empty.</p>
        </c:if>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
