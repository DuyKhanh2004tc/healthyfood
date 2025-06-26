<%-- 
    Document   : cart
    Created on : Jun 18, 2025, 11:33:33 PM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
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
                    <th>In stock</th>
                    <th>Total Price</th>
                    <th>Shelf Life Hours</th>
                    <th><a href="home">Back to Home</a></th>
                </tr>
            <c:if test="${sessionScope.user.getRole().getId()== 3}">
                <c:set var="totalAmount" value="0"/>
                <c:forEach items="${requestScope.itemList}" var="i" varStatus="loop">
                    <tr class="cartItem">
                        <td>${loop.index + 1}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/productDetail?productId=${i.product.id}">
                                <img src="${i.product.imgUrl}" width="80" alt="Product"></a>
                                ${i.product.name}
                        </td>
                        <td>
                            <button class="btnSub" onclick="location.href = 'cart?number=-1&id=${i.product.id}'">-</button>
                            ${i.quantity}
                            <button class="btnAdd" onclick="location.href = 'cart?number=1&id=${i.product.id}'">+</button>  
                        </td>
                        <td>${i.product.stock}</td>
                        <td><fmt:formatNumber value="${i.product.price * i.quantity}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                        <td>${i.product.shelfLifeHours}</td>
                        <td><button class="btnRemove" onclick="location.href = 'removeItem?removePId=${i.product.id}'">Remove</button></td>
                    </tr>
                    <c:set var="totalAmount" value="${totalAmount + (i.product.price * i.quantity)}"/>
                </c:forEach>
                <tr class="totalAmount">
                    <td colspan="5"><c:if test="${not empty stockError}">
                            ${stockError}
                        </c:if></td>
                    <td>Total Amount:</td>
                    <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                </tr>
            </c:if>
            <c:if test="${sessionScope.user == null}">
                <c:set var="totalAmount" value="0"/>
                <c:forEach items="${sessionScope.itemList}" var="i" varStatus="loop">
                    <tr class="cartItem">
                        <td>${loop.index + 1}</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/productDetail?productId=${i.product.id}">
                                <img src="${i.product.imgUrl}" width="80"></a>
                                ${i.product.name}
                        </td>
                        <td>
                            <button class="btnSub" onclick="location.href = 'cart?number=-1&id=${i.product.id}'">-</button>
                            ${i.quantity}
                            <button class="btnAdd" onclick="location.href = 'cart?number=1&id=${i.product.id}'">+</button>  

                        </td>
                        <td>${i.product.stock}</td>
                        <td><fmt:formatNumber value="${i.product.price * i.quantity}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                        <td>${i.product.shelfLifeHours}</td>
                        <td><button class="btnRemove" onclick="location.href = 'removeItem?removePId=${i.product.id}'">Remove</button></td>
                    </tr>
                    <c:set var="totalAmount" value="${totalAmount + (i.product.price * i.quantity)}"/>
                </c:forEach>
                <tr class="totalAmount">
                    <td colspan="5"></td>
                    <td>Total Amount:</td>
                    <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                </tr>
            </c:if>



        </table>
        <c:if test="${empty requestScope.itemList and empty sessionScope.itemList}">
            <p>Your cart is empty.</p>
        </c:if>

        <% if(request.getAttribute("itemList")!=null){
            HttpSession Session = request.getSession();
            List<CartItem> itemList = (List<CartItem>)request.getAttribute("itemList");
            session.setAttribute("itemList",itemList);
        }      
        %>
        <form class="btnBuy" action="placeOrder" method="get">
            <button class="card-button" type="submit" value="buy">💰 Buy</button>
        </form>

        <jsp:include page="footer.jsp"></jsp:include>


    </body>
</html>
