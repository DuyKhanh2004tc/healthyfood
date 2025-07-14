<%-- 
    Document   : headerSeller
    Created on : Jul 14, 2025, 3:19:22 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>header Seller</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">
        <link href="${pageContext.request.contextPath}/css/bootstrap-icons.css" rel="stylesheet">
        <link rel="stylesheet" href="CSS/homeSeller.css">
    </head>
    <body>
        <div class="seller-header">
            <div class="logo">
                <a href="${pageContext.request.contextPath}/home">
                <img src="${pageContext.request.contextPath}/images/logo_3.png" alt="Logo">
                </a>
            </div>
            <div class="menu-content-left">
                <h3>Welcome Seller : ${sessionScope.user.getName()}</h3>
                <a href="${pageContext.request.contextPath}/seller">View Product List</a>

                <a href="${pageContext.request.contextPath}/logout">Logout</a>

            </div>
        </div>
    </body>
</html>
