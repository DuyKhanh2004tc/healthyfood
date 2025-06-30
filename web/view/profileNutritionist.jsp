<%-- 
    Document   : profileNutritionist
    Created on : Jun 30, 2025, 1:01:20 PM
    Author     : Hoa
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Nutritionist Profile</title>
    <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/profile.css">
</head>
<body>

    <div class="nutrition-header">
        <div class="logo">
            <img src="${pageContext.request.contextPath}/images/logo_3.png" alt="Logo">
        </div>
        <div class="menu-content-left">
            <h3>Welcome Nutritionist ${sessionScope.user.name}</h3>
            <a href="${pageContext.request.contextPath}/nutritionistHome">View Product List</a> 
            <a href="${pageContext.request.contextPath}/proposeProduct">Propose new product</a>                 
            <a href="${pageContext.request.contextPath}/nutritionBlog">Manage Blog</a>
            <a href="${pageContext.request.contextPath}/logout">Logout</a>
        </div>
    </div>

    <div class="center">
        <h1>Nutritionist Profile</h1>
        <form action="updateProfile" method="post">

            <div class="txt_field">
                <label for="fullname">Full Name:</label>
                <input type="text" id="fullname" name="fullname" placeholder="Full Name" value="${sessionScope.user.name}" required />
                <span class="error-message">
                    <c:if test="${not empty errorName}">${errorName}</c:if>
                </span>
            </div>

            <div class="txt_field">
                <label for="email">Email:</label>
                <input type="email" id="email" name="email" value="${sessionScope.user.email}" readonly style="background-color:#f0f0f0; cursor: not-allowed;" />
            </div>

            <div class="txt_field">
                <label for="phonenumber">Phone Number:</label>
                <input type="text" id="phonenumber" name="phonenumber" placeholder="Phone Number" value="${sessionScope.user.phone}" required />
                <span class="error-message">
                    <c:if test="${not empty errorPhone}">${errorPhone}</c:if>
                </span>
            </div>

            <div class="txt_field">
                <label for="dateofbirth">Date of Birth:</label>
                <input type="date" id="dateofbirth" name="dateofbirth" value="${sessionScope.user.dob}" required />
                <span class="error-message">
                    <c:if test="${not empty errorDOB}">${errorDOB}</c:if>
                </span>
            </div>

            <div class="txt_field">
                <label for="address">Address:</label>
                <input type="text" id="address" name="address" placeholder="Address" value="${sessionScope.user.address}" required />
                <span class="error-message">
                    <c:if test="${not empty errorAddress}">${errorAddress}</c:if>
                </span>
            </div>

            <div class="txt_field gender-group">
                <label>Gender:</label>
                <div class="radio-options">
                    <label>
                        <input type="radio" name="gender" value="0" required <c:if test="${!sessionScope.user.gender}">checked</c:if> /> Female
                    </label>
                    <label>
                        <input type="radio" name="gender" value="1" <c:if test="${sessionScope.user.gender}">checked</c:if> /> Male
                    </label>
                </div>
                <span class="error-message">
                    <c:if test="${not empty errorGender}">${errorGender}</c:if>
                </span>
            </div>

            <c:if test="${not empty success}">
                <div class="success-message">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <input type="submit" value="Update" />
        </form>
    </div>

</body>
</html>
