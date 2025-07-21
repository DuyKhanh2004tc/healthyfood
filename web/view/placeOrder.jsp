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
                        <th>In stock</th>
                        <th>Total Price</th>
                        <th>Shelf Life Hours</th>
                    </tr>
                    <!-- for customer buy in cart -->
                <c:if test="${sessionScope.user.getRole().getId()== 3 && requestScope.product == null}">
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
                            <td>${i.product.stock}</td>
                            <td><fmt:formatNumber value="${i.product.price * i.quantity}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                            <td>${i.product.shelfLifeHours}</td>
                        </tr>
                        <c:set var="totalAmount" value="${totalAmount + (i.product.price * i.quantity)}"/>
                    </c:forEach>
                    <tr class="totalAmount">
                        <td colspan="4"></td>
                        <td>Total Amount:</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                    </tr>
                </c:if>
                <!-- for guest buy in cart -->
                <c:if test="${sessionScope.user == null && requestScope.product == null}">
                    <c:forEach items="${sessionScope.itemList}" var="i" varStatus="loop">
                        <tr class="cartItem">
                            <td>${loop.index + 1}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/productDetail?productId=${i.product.id}">
                                    <img src="${i.product.imgUrl}" width="80" alt="Product"></a>
                                    ${i.product.name}
                            </td>
                            <td>${i.quantity}</td>
                            <td>${i.product.stock}</td>
                            <td><fmt:formatNumber value="${i.product.price * i.quantity}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                            <td>${i.product.shelfLifeHours}</td>
                        </tr>
                        <c:set var="totalAmount" value="${totalAmount + (i.product.price * i.quantity)}"/>
                    </c:forEach>
                    <tr class="totalAmount">
                        <td colspan="4"></td>
                        <td>Total Amount:</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                    </tr>

                </c:if>
                <!-- for customer buy in home -->
                <c:if test="${sessionScope.user.getRole().getId()== 3 && requestScope.quantity != null && requestScope.product != null}">
                    <c:set var="totalAmount" value="${requestScope.product.price * requestScope.quantity}" />
                    <tr class="cartItem">
                        <td>1</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/productDetail?productId=${requestScope.product.id}">
                                <img src="${requestScope.product.imgUrl}" width="80" alt="Product"></a>
                                ${requestScope.product.name}
                        </td>
                        <td>${requestScope.quantity}</td>
                        <td>${requestScope.product.stock}</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                        <td>${requestScope.product.shelfLifeHours}</td>
                    </tr>
                    <tr class="totalAmount">
                        <td colspan="4"></td>
                        <td>Total Amount:</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                    </tr>
                </c:if>
                <!-- for guest buy in home -->
                <c:if test="${sessionScope.user == null && requestScope.quantity != null && requestScope.product != null}">
                    <c:set var="totalAmount" value="${requestScope.product.price * requestScope.quantity}" />
                    <tr class="cartItem">
                        <td>1</td>
                        <td>
                            <a href="${pageContext.request.contextPath}/productDetail?productId=${requestScope.product.id}">
                                <img src="${requestScope.product.imgUrl}" width="80" alt="Product"></a>
                                ${requestScope.product.name}
                        </td>
                        <td>${requestScope.quantity}</td>
                        <td>${requestScope.product.stock}</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                        <td>${requestScope.product.shelfLifeHours}</td>
                    </tr>
                    <tr class="totalAmount">
                        <td colspan="4"></td>
                        <td>Total Amount:</td>
                        <td><fmt:formatNumber value="${totalAmount}" type="number" maxFractionDigits="2" minFractionDigits="2" />$</td>
                    </tr>
                </c:if>
            </table>


        </div>
        <c:if test="${sessionScope.user != null}">        
        <form class="form-placeOrder" action="orderCheckout" method="post" onsubmit="return confirmOrder()">
            <table>
                <tr>
                    <td>Receiver Name:</td>
                    <td>Phone Number:</td>
                </tr>
                <tr>
                    <td><input type="text" id="userName" name="userName" maxlength="30" value="${sessionScope.user.name}"></td>
                    <td><input type="text" id="phone" name="phone" maxlength="11" value="${sessionScope.user.phone}"></td>

                </tr>
                <tr>
                    <td><p class="error-message" id="errorName"></p></td>
                    <td><p class="error-message" id="errorPhone"></p></td>
                </tr>

                <tr>
                    <td>House number / Area:</td>
                    <td>Ward / Commune:</td>
                </tr>
                <tr>
                    <td><input type="text" id="street" name="street" maxlength="30" value=""></td>
                    <td><input type="text" id="ward" name="ward" maxlength="40" value=""></td>
                </tr>
                <tr>
                    <td><p class="error-message" id="errorStreet"></p></td>
                    <td><p class="error-message" id="errorWard"></p></td>
                </tr>

                <tr>
                    <td>District:</td>
                    <td>Province / City:</td>
                </tr>
                <tr>
                    <td><input type="text" id="district" name="district" maxlength="40" value=""></td>
                    <td><input type="text" id="province" name="province" maxlength="50" value=""></td>
                </tr>
                <tr>
                    <td><p class="error-message" id="errorDistrict"></p></td>
                    <td><p class="error-message" id="errorProvince"></p></td>
                </tr>

                <tr>
                    <td>Email:</td>
                    <td></td>
                </tr>
                <tr>
                    <td><input type="email" id="email" name="email" maxlength="40" value="${sessionScope.user.email}"></td>
                </tr>
                <tr>
                    <td><p class="error-message" id="errorEmail"></p></td>
                </tr>
                <tr>
                    <td>Message for delivery:
                    </td>

                </tr>
                <tr></td>
                    <td><textarea id="deliveryMes" name="deliveryMessage" value="" rows="10" cols="50" maxlength="300"></textarea></td>
                    <td><p class="error-message" id="errorMessage"></td>
                </tr>
                <tr>
                    <td>Select payment method:</td>
                    <td>
                        <input type="radio" id="pmOnline" name="paymentMethod" value="online">Online Payment
                        <input type="radio" id="pmOffline" name="paymentMethod" value="offline">Payment after received                        
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><p class="error-message" id="errorPayment"></td></p>
                </tr>
            </table>
            <input type="hidden" name="totalAmount" value="${totalAmount}">
            <c:if test="${requestScope.product != null && requestScope.quantity != null}">
                <input type="hidden" name="productId" value="${requestScope.product.id}">
                <input type="hidden" name="quantity" value="${requestScope.quantity}">
            </c:if>
            <input class="btn_placeOrder" type="submit" value="Place Order">
        </form>
        </c:if>
        
        <c:if test="${sessionScope.user == null}">        
        <form class="form-placeOrder" action="orderCheckout" method="post" onsubmit="return confirmOrder()">
            <table>
                <tr>
                    <td>Receiver Name:</td>
                    <td>Phone Number:</td>
                </tr>
                <tr>
                    <td><input type="text" id="userName" name="userName" maxlength="30" value=""></td>
                    <td><input type="text" id="phone" name="phone" maxlength="11" value=""></td>

                </tr>
                <tr>
                    <td><p class="error-message" id="errorName"></p></td>
                    <td><p class="error-message" id="errorPhone"></p></td>
                </tr>

                <tr>
                    <td>House number / Area:</td>
                    <td>Ward / Commune:</td>
                </tr>
                <tr>
                    <td><input type="text" id="street" name="street" maxlength="30" value=""></td>
                    <td><input type="text" id="ward" name="ward" maxlength="40" value=""></td>
                </tr>
                <tr>
                    <td><p class="error-message" id="errorStreet"></p></td>
                    <td><p class="error-message" id="errorWard"></p></td>
                </tr>

                <tr>
                    <td>District:</td>
                    <td>Province / City:</td>
                </tr>
                <tr>
                    <td><input type="text" id="district" name="district" maxlength="40" value=""></td>
                    <td><input type="text" id="province" name="province" maxlength="50" value=""></td>
                </tr>
                <tr>
                    <td><p class="error-message" id="errorDistrict"></p></td>
                    <td><p class="error-message" id="errorProvince"></p></td>
                </tr>

                <tr>
                    <td>Email:</td>
                    <td></td>
                </tr>
                <tr>
                    <td><input type="email" id="email" name="email" maxlength="40" value=""></td>
                </tr>
                <tr>
                    <td><p class="error-message" id="errorEmail"></p></td>
                </tr>
                <tr>
                    <td>Message for delivery:
                    </td>

                </tr>
                <tr></td>
                    <td><textarea id="deliveryMes" name="deliveryMessage" value="" rows="10" cols="50" maxlength="300"></textarea></td>
                    <td><p class="error-message" id="errorMessage"></td>
                </tr>
                <tr>
                    <td>Select payment method:</td>
                    <td>
                        <input type="radio" id="pmOnline" name="paymentMethod" value="online">Online Payment
                        <input type="radio" id="pmOffline" name="paymentMethod" value="offline">Payment after received                        
                    </td>
                </tr>
                <tr>
                    <td></td>
                    <td><p class="error-message" id="errorPayment"></td></p>
                </tr>
            </table>
            <input type="hidden" name="totalAmount" value="${totalAmount}">
            <c:if test="${requestScope.product != null && requestScope.quantity != null}">
                <input type="hidden" name="productId" value="${requestScope.product.id}">
                <input type="hidden" name="quantity" value="${requestScope.quantity}">
            </c:if>
            <input class="btn_placeOrder" type="submit" value="Place Order">
        </form>
        </c:if>

        <jsp:include page="footer.jsp"></jsp:include>
        <script>
            document.querySelector(".form-placeOrder").addEventListener("submit", function (e) {
                let valid = true;


                document.querySelectorAll(".error-message").forEach(el => el.innerText = "");

                const name = document.getElementById("userName").value.trim();
                const phone = document.getElementById("phone").value.trim();
                const street = document.getElementById("street").value.trim();
                const ward = document.getElementById("ward").value.trim();
                const district = document.getElementById("district").value.trim();
                const province = document.getElementById("province").value.trim();
                const email = document.getElementById("email").value.trim();
                const deliveryMessage = document.getElementById("deliveryMes").value.trim();
                const paymentOnline = document.getElementById("pmOnline").checked;
                const paymentOffline = document.getElementById("pmOffline").checked;


                if (name === "") {
                    document.getElementById("errorName").innerText = "Receiver name is required.";
                    valid = false;
                } else if (name.split(" ").length < 2) {
                    document.getElementById("errorName").innerText = "Name must contain at least two words.";
                    valid = false;
                } else if (!/^[^0-9$%!@#^&*()+=\[\]{};:"\\|<>\/?~`]+$/.test(name)) {
                    document.getElementById("errorName").innerText = "Name must not contain numbers or special characters.";
                    valid = false;
                }

                if (phone === "") {
                    document.getElementById("errorPhone").innerText = "Phone number is required.";
                    valid = false;
                } else if (!/^\d{10,11}$/.test(phone)) {
                    document.getElementById("errorPhone").innerText = "Phone number must be 10 or 11 digits.";
                    valid = false;
                }

                if (street === "") {
                    document.getElementById("errorStreet").innerText = "Please input your house number or area.";
                    valid = false;

                } else if (!/^[^$%!@#^*=\[\]{};:"\\|<>?~`]+$/.test(street)) {
                    document.getElementById("errorStreet").innerText = "House number or area contains invalid special characters.";
                    valid = false;
                }
                if (ward === "") {
                    document.getElementById("errorWard").innerText = "Ward is required.";
                    valid = false;

                } else if (ward !== "" && !/^[^$%!@#^*=\[\]{};:"\\|<>?~`]+$/.test(ward)) {
                    document.getElementById("errorWard").innerText = "Ward contains invalid special characters.";
                    valid = false;
                }

                if (district === "") {
                    document.getElementById("errorDistrict").innerText = "District is required.";
                    valid = false;
                }

                if (province === "") {
                    document.getElementById("errorProvince").innerText = "Province/City is required.";
                    valid = false;
                }


                if (email === "") {
                    document.getElementById("errorEmail").innerText = "Email is required.";
                    valid = false;
                } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
                    document.getElementById("errorEmail").innerText = "Invalid email format.";
                    valid = false;
                }

                if (!paymentOnline && !paymentOffline) {
                    document.getElementById("errorPayment").innerText = "Please select a payment method.";
                    valid = false;
                }

                if (deliveryMessage.length >= 150) {
                    document.getElementById("errorMessage").innerText = "Messages must not exceed 150 characters.";
                    valid = false;
                }

                if (!valid)
                    e.preventDefault();
            });

            function confirmOrder() {
                return confirm("Are you sure to place the order?");
            }
        </script>
    </body>
</html>
