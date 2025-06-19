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
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>


            <table>
                <tr>
                    <th>Number</th>
                    <th>Product</th>
                    <th>Quantity</th>
                    <th>Price</th>
                    <th>Shelf Life Hours</th>
                </tr>
            <c:forEach items="${requestScope.itemList}" var="i" varStatus="loop">
                <c:set var="p" value="${requestScope.productList[loop.index]}"/>
                <tr>
                    <td>${i.id}</td>
                    <td><img src="${p.imgUrl}" width="80">${p.name}</td>
                    <td>${i.quantity}</td>
                    <td>${p.price}</td>
                    <td>${p.shelfLifeHours}</td>
                </tr>
            </c:forEach>
        </table>
        <c:if test="${empty itemList}">
            <p>Your cart is empty.</p>
        </c:if>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
