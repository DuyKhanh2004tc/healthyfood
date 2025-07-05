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
                        <a href="${pageContext.request.contextPath}/cart">Cart</a>
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <form class ="search-form" action="search" method="get">                   
                            <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                            <input type="text" name="keyword" placeholder="Search for products...">
                        </form>
                    </c:if>
                    <c:if test="${sessionScope.user.getRole().getId()==3}">
                        <a>Welcome Customer ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                        <a href="${pageContext.request.contextPath}/cart">Cart</a>
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        <form class ="search-form" action="search" method="get">                   
                            <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                            <input type="text" name="keyword" value="${param.keyword}" placeholder="Search for products...">
                        </form>
                    </c:if>
                    <c:if test="${sessionScope.user.getRole().getId()==2}">
                        <a>Welcome Manager ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                        <a href="${pageContext.request.contextPath}/manageproduct">Manage Product</a>
                        <a href="${pageContext.request.contextPath}/approveProduct">Approve new products</a>                   
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        <form class ="search-form" action="search" method="get">                   
                            <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                            <input type="text" name="keyword" placeholder="Search for products...">
                        </form>
                    </c:if>
                    <c:if test="${sessionScope.user.getRole().getId()==5}">
                        <a>Welcome Seller ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                        <a href="${pageContext.request.contextPath}/manageproduct">Manage Product</a>                 
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        <form class ="search-form" action="search" method="get">                   
                            <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                            <input type="text" name="keyword" placeholder="Search for products...">
                        </form>
                    </c:if>       


                </nav>
            </div>
        </div>


    </body>
</html>
