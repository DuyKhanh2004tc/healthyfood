<%-- 
    Document   : orderCheckout
    Created on : Jun 26, 2025, 12:20:49 AM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List" %>
<%@ page import="model.CartItem" %>
<%@ page import="model.Order" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="${pageContext.request.contextPath}/CSS/cart.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/orderCheckout.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            

                <h1>Place order successfully, please wait us to confirm your order information!</h1>
            <% if (request.getAttribute("order")!=null && request.getAttribute("itemList")!=null){
                Order order = (Order)request.getAttribute("order");
                List<CartItem> itemList = (List<CartItem>)request.getAttribute("itemList");
            %>
            <div class="order-infor"> 
                <div class ="left">
                    <% if (order.getPaymentMethod().equalsIgnoreCase("online")) { %>
                    <img src="images/qrcode.jpg" width="400" height="400" alt="Online Payment" />
                    <% } %>
                </div>
                <div class="right">
                    <h2>Your order information:</h2>
                    <table class='Table-OrderInfor'>
                        <tr>
                            <td>Order ID:</td> 
                            <td><%= order.getId()%></td> 
                        </tr>
                        <tr>
                            <td>Receiver Name:</td>
                            <td><%= order.getReceiverName() %></td>
                        </tr>
                        <tr>
                            <td>Phone Number:</td> 
                            <td><%= order.getReceiverPhone() %></td>
                        </tr>
                        <tr>
                            <td>Address:</td> 
                            <td><%= order.getShippingAddress() %></td> 
                        </tr>
                        <tr>
                            <td>Payment Method:</td> <td><%= order.getPaymentMethod()%></td>
                        </tr>
                        <tr>
                            <td>Total Amount:</td> <td><fmt:formatNumber value="<%= order.getTotalAmount() %>" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td> 
                        </tr>
                        <tr>
                            <td>Order Status:</td> <td>Pending</td>
                        </tr>
                    </table>
                </div>
            </div>

            <h2>List of ordered products:</h2>
            <table class='Table-Product'>
                <tr>
                    <th>Product</th>
                    <th>Name</th>
                    <th>Quantity</th>
                    <th>Price</th>
                </tr>
                <c:forEach var="item" items="${itemList}">
                    <tr>
                        <td><a href="productDetail?productId=${item.product.id}">
                                <img src="${item.product.imgUrl}" alt="Product"/>
                            </a>
                        </td>
                        <td>${item.product.name}</td>
                        <td>${item.quantity}</td>
                        <td><fmt:formatNumber value="${item.product.price}" type="number" />$/product</td>
                    </tr>
                </c:forEach>
            </table>
            <% } %>

            <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
