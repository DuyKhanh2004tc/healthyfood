<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<link rel="stylesheet" href="CSS/homeSeller.css">
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
                <c:if test="${sessionScope.user.getRole().getId()==2}">
                        <a>Welcome Manager ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                        <a href="${pageContext.request.contextPath}/ManagerCategoriesServlet">Manage Category</a>
                        <a href="${pageContext.request.contextPath}/seller">Manage Product</a>
                        <a href="${pageContext.request.contextPath}/approveProduct">Approve new products</a>                   
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        
                    </c:if>
                    <c:if test="${sessionScope.user.getRole().getId()==5}">
                        <a>Welcome Seller ${sessionScope.user.getName()}</a>    
                        <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                                         <a href="${pageContext.request.contextPath}/ConfirmedOrders">Order Status</a>
                        <a href="${pageContext.request.contextPath}/nutritionBlog">Blog</a>
                        <a href="${pageContext.request.contextPath}/logout">Logout</a>
                        
                    </c:if>       
            </div>
        </div>
    </body>
</html>
