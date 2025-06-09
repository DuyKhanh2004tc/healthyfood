<%-- 
    Document   : header
    Created on : May 29, 2025, 6:03:01 PM
    Author     : ASUS
--%>

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
                <img src="images/logo_1.png" alt="Logo Image">
            </div>
            <div class="header-right">
            <h2 class="shop_name">Healthy Food</h2>
            <nav class="menu">
                <a href="${pageContext.request.contextPath}/home">Home</a>
                <a href="${pageContext.request.contextPath}/login">Login</a>
                <a href="cart.jsp">Cart</a>
                <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                <form action="search" method="get">                   
                    <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                    <input type="text" name="keyword" placeholder="Search...">
                </form>


            </nav>
            </div>
        </div>


    </body>
</html>
