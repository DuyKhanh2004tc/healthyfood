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
                <th>Number</th>
                <th>
                    Product Name
                    <a href="cart?sort=nameAsc">▲</a> | 
                    <a href="cart?sort=nameDesc">▼</a>
                </th>
                <th>Quantity</th>
                <th>In stock</th>
                <th>
                    Price
                    <a href="cart?sort=priceAsc">▲</a> | 
                    <a href="cart?sort=priceDesc">▼</a>
                </th>
                <th>
                    Shelf Life Hours
                    <a href="cart?sort=shelfAsc">▲</a> | 
                    <a href="cart?sort=shelfDesc">▼</a>
                </th>
                <th><a href="removeItem?remove=all">Remove All Items</a></th>
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
                    <td colspan="5"><c:if test="${not empty sessionScope.stockError}">
                            ${sessionScope.stockError}
                        </c:if>
                        <c:if test="${not empty requestScope.stockError}">
                            <div id="stockError" style="color: red; font-weight: bold;">
                                ${requestScope.stockError}
                            </div>
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
                    <td colspan="5">
                        <c:if test="${not empty requestScope.stockError}">
                            <div id="stockError" style="color: red; font-weight: bold;">
                                ${requestScope.stockError}
                            </div>
                        </c:if>
                        <c:if test="${not empty sessionScope.stockError}">
                            <div id="stockError" style="color: red; font-weight: bold;">
                                ${sessionScope.stockError}
                            </div>
                        </c:if>
                    </td>
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
        <c:if test="${not empty requestScope.itemList or not empty sessionScope.itemList}">
            <form class="btn-buy" action="placeOrder" method="get">
                <button class="cart-button" type="submit" value="buy">💰 Buy</button>
            </form>
        </c:if>

        <div class="newProducts-list">    
            <h2>New Products:</h2>
            <c:forEach items="${requestScope.newProductList}" var="o">
                <div class="newProducts-list-card">
                    <a href="${pageContext.request.contextPath}/productDetail?productId=${o.id}">
                        <img class="card-img" src="${o.imgUrl}" alt="Product Image">
                    </a>
                    <div class="card-body">
                        <p>Product: ${o.name}</p>

                        <p>Price: ${o.price}$</p>
                        <p>Stock: ${o.stock}</p>
                        <p>Rating: ${o.rate}</p>
                        <div class="stars-average">
                            <c:set var="fullStars" value="${o.rate >= 1 ? (o.rate >= 5 ? 5 : (o.rate - o.rate % 1)) : 0}" />
                            <c:set var="halfStar" value="${(o.rate - fullStars) >= 0.5 ? true : false}" />

                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${i <= fullStars}">
                                        <span class="star full">★</span>
                                    </c:when>
                                    <c:when test="${i == (fullStars + 1) && halfStar}">
                                        <span class="star half">★</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="star empty">★</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </div>

                    </div>
                    <c:if test="${sessionScope.user.getRole().getId()== null ||sessionScope.user.getRole().getId()== 3 }">
                        <div class="twoButton">
                            <form action="cart" method="get">
                                <input type="hidden" name="productId" value="${o.id}" />
                                <input type="hidden" name="cartUrl" value="${pageContext.request.contextPath}/cart"" />
                                <button class="card-button" type="submit">🛒 Add to Cart</button>
                            </form>
                            <form class="btnBuy" action="placeOrder" method="get">
                                <input type="hidden" name="productId" value="${o.id}" />
                                <input type="hidden" name="quantity" value="1" />
                                <button class="card-button" type="submit" value="buy">💰 Buy</button>
                            </form>
                        </div>
                    </c:if> 
                </div>
            </c:forEach>
        </div>



        <jsp:include page="footer.jsp"></jsp:include>




        <script>
            window.onload = function () {
                const buyButtonForm = document.querySelector('form.btn-buy');
                const stockError = document.getElementById("stockError");
                if (buyButtonForm && stockError) {
                    buyButtonForm.addEventListener("submit", function (e) {
                        alert("Some items in your cart are out of stock. Please update your cart before proceeding.");
                        e.preventDefault();
                    });
                }
            };
        </script>
    </body>
</html>
