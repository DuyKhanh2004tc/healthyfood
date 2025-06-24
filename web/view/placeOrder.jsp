<%-- 
    Document   : placeOrder
    Created on : Jun 24, 2025, 1:45:36 PM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Page</title>
        <link href="${pageContext.request.contextPath}/CSS/cart.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/placeOrder.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>


            <div>
                <table class="cart-table">
                    <tr class="table-header">
                        <th>Number</th>
                        <th>Product</th>
                        <th>Quantity</th>
                        <th>Total Price</th>
                        <th>Shelf Life Hours</th>
                    </tr>
                <c:if test="${sessionScope.user.getRole().getId()== 3}">
                    <c:forEach items="${requestScope.itemList}" var="i" varStatus="loop">
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
                    </c:forEach>
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
                    </c:forEach>
                </c:if>
            </table>
        </div>
        <form class="form-placeOrder" action="orderHistory" method="get">
            <table>
                <tr>
                    <td>Receiver Name:</td>
                    <td>Phone Number:</td>
                </tr>
                <tr>
                    <td><input type="text" id="userName" name="userName" value=""><span class="error-message" id="errorName"></span></td>

                    <td><input type="text" id="phone" name="phone" value=""><span class="error-message" id="errorPhone"></span></td>

                </tr>
                <tr>
                    <td>Address:</td>
                    <td>Email:</td>
                </tr>
                <tr>
                    <td><input type="text" id="address" name="address" value=""><span class="error-message" id="errorAddress"></span></td>

                    <td><input type="email" id="email" name="email" value=""><span class="error-message" id="errorEmail"></span></td>

                </tr>
                <tr>
                    <td>Select payment method:</td>
                    <td>
                        <input type="radio" id="pmOnline" name="paymentMethod" value="online">Online Payment
                        <input type="radio" id="pmOffline" name="paymentMethod" value="offline">Payment after received
                        <span class="error-message" id="errorPayment"></span>
                    </td>
                </tr>
            </table>
            <input class="btn_placeOrder" type="submit" value="Place Order">
        </form>
        <jsp:include page="footer.jsp"></jsp:include>
        <script>
            document.querySelector(".form-placeOrder").addEventListener("submit", function (e) {
                let valid = true;

                // Clear old errors
                document.querySelectorAll(".error-message").forEach(el => el.innerText = "");

                // Get values
                const name = document.getElementById("userName").value.trim();
                const phone = document.getElementById("phone").value.trim();
                const address = document.getElementById("address").value.trim();
                const email = document.getElementById("email").value.trim();
                const paymentOnline = document.getElementById("pmOnline").checked;
                const paymentOffline = document.getElementById("pmOffline").checked;

                // Validate Name
                if (name === "") {
                    document.getElementById("errorName").innerText = "Receiver name is required.";
                    valid = false;
                } else if (name.split(" ").length < 2) {
                    document.getElementById("errorName").innerText = "Name must contain at least two words.";
                    valid = false;
                }

                // Validate Phone
                if (phone === "") {
                    document.getElementById("errorPhone").innerText = "Phone number is required.";
                    valid = false;
                } else if (!/^\d{10,11}$/.test(phone)) {
                    document.getElementById("errorPhone").innerText = "Phone must be 10 or 11 digits.";
                    valid = false;
                }

                // Validate Address
                if (address === "") {
                    document.getElementById("errorAddress").innerText = "Address is required.";
                    valid = false;
                }

                // Validate Email
                if (email === "") {
                    document.getElementById("errorEmail").innerText = "Email is required.";
                    valid = false;
                } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                    document.getElementById("errorEmail").innerText = "Invalid email format.";
                    valid = false;
                }

                // Validate Payment
                if (!paymentOnline && !paymentOffline) {
                    document.getElementById("errorPayment").innerText = "Please select a payment method.";
                    valid = false;
                }

                if (!valid)
                    e.preventDefault();
            });

        </script>
    </body>
</html>
