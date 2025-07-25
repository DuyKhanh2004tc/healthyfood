<%-- 
    Document   : header
    Created on : May 29, 2025, 6:03:01 PM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="header">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/home">
                    <img src="images/logo_1.png" alt="Logo">
                </a>
            </div>
            <div class="header-right">
                <h2 class="shop_name">Healthy Food</h2>
                <nav class="menu">
                    <c:if test="${sessionScope.user.getRole().getId()==null}">

                        <a href="${pageContext.request.contextPath}/login">Login</a>
                        <a href="${pageContext.request.contextPath}/register">Register</a>
                        <a href="${pageContext.request.contextPath}/bmi">BMI Calculator</a>
                        <a href="${pageContext.request.contextPath}/cart">Cart</a>
                        <a href="${pageContext.request.contextPath}/allRecipe">Cooking Recipe</a>
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        
                        
                    </c:if>
                    <c:if test="${sessionScope.user.getRole().getId()==3}">
                        <a>Welcome Customer ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                        <a href="${pageContext.request.contextPath}/bmi">BMI Calculator</a>
                        <a href="${pageContext.request.contextPath}/cart">Cart</a>
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/allRecipe">Cooking Recipe</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        
                    </c:if>
                    <c:if test="${sessionScope.user.getRole().getId()==2}">
                        <a>Welcome Manager ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                        <a href="${pageContext.request.contextPath}/seller">Management</a>                                        
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/allRecipe">Cooking Recipe</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        
                    </c:if>
                    <c:if test="${sessionScope.user.getRole().getId()==5}">
                        <a>Welcome Seller ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                                         <a href="${pageContext.request.contextPath}/ConfirmedOrders">Order Status</a>
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/allRecipe">Cooking Recipe</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        
                    </c:if>       


                </nav>
            </div>
        </div>


    </body>
</html>
