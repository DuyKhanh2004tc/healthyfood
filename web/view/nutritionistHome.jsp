<%-- 
    Document   : nutritionistHome
    Created on : Jun 12, 2025, 11:42:51 PM
    Author     : ASUS
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Nutritionist Home</title>
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="nutrition-header">
            <div class="logo">
<!--                <a href="${pageContext.request.contextPath}/home">-->
                <img src="${pageContext.request.contextPath}/images/logo_3.png" alt="Logo">
                <!--</a>-->
            </div>
            <div class="menu-content-left">
                <h3>Welcome Nutritionist ${sessionScope.user.getName()}</h3>
                <a href="${pageContext.request.contextPath}/nutritionistHome">View Product List</a>
                <a href="${pageContext.request.contextPath}/profile">Profile</a>
                <a href="${pageContext.request.contextPath}/proposeProduct">Propose new product</a>                 
                <a href="${pageContext.request.contextPath}/nutritionBlog">Manage Blog</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>

            </div>
        </div>
        <div class="content-filter">

            <div>
                <b>Categories:</b>
                <select name="category" onchange="location.href = 'category?categoryId=' + this.value;">
                    <option value="0">All Products</option>
                    <c:forEach items="${requestScope.categoryList}" var="o">
                        <option value="${o.id}" ${sessionScope.categoryId == o.id ? 'selected' : ''}>${o.name}</option>
                    </c:forEach>    
                </select>

            </div>


            <div>
                <b>Sort Product:</b>
                <button onclick="location.href = 'sortproduct?orderBy=desc'">Descending</button>
                <button onclick="location.href = 'sortproduct?orderBy=asc'">Ascending</button>
            </div>
            <div class="search-content">
                <c:if test="${not empty sessionScope.keyword}">
                    <button onclick="location.href = 'nutritionistHome'">Back</button>
                </c:if>
                <form class ="search-form" action="search" method="get">                   
                    <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                    <input type="text" name="keyword" value="${sessionScope.keyword}" placeholder="Search...">               
                </form>
            </div>
        </div>

        <div class="product-list">     

            <c:forEach items="${requestScope.productList}" var="o">
                <div class="card">
                    <a href="${pageContext.request.contextPath}/productDetail?productId=${o.id}">
                        <img class="card-img" src="${o.imgUrl}" alt="Product">
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
                </div>
            </c:forEach>
        </div>

    </body>
</html>
