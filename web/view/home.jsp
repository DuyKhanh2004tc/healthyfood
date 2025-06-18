<%-- 
    Document   : home
    Created on : May 29, 2025, 5:25:36 PM
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
        <jsp:include page="header.jsp"></jsp:include>
            <!-- Main Content -->
            <div>
                <h1>Welcome to Healthy Food!</h1>
                <p>Enjoy shopping fresh and nutritious food tailored to your health goals.</p>
            </div>

            <div class="content-main">
                <div class="content-left">

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
                    <form action="pricefilter" method="get">
                        <table border="1">
                            <b>Price Filter:</b>
                            <tr>
                                <td>Min price:</td>
                                <td><input type="number" name="minPrice" value="${minPrice}" min="0" max="1000" placeholder="Enter min price"></td>
                            </tr>
                            <tr>
                                <td>Max price:</td>
                                <td><input type="number" name="maxPrice" value="${maxPrice}" min="0" max="1000" placeholder="Enter max price"></td>
                            </tr>

                        </table>
                        <c:if test="${not empty eMessage}">
                            <div style="color:red">${eMessage}</div>
                        </c:if>

                        <c:if test="${not empty notFoundMessage}">
                            <div style="color:red">${notFoundMessage}</div>
                        </c:if>
                        <input type="submit" value="Filter">
                    </form>
                </div>
                <div>
                    <b>Sort Product:</b>
                    <button onclick="location.href = 'sortproduct?orderBy=desc'">Descending</button>
                    <button onclick="location.href = 'sortproduct?orderBy=Asc'">Ascending</button>
                </div>
            </div>
            <!-- hien thi product -->
            <div class="content-right">
                <div class="list-title">
                    <h2>Product List:</h2>
                    <%  if(request.getAttribute("errorMessage")!=null){
                        String em =(String)request.getAttribute("errorMessage"); 
                    %>
                    <h2><%= em%> </h2>
                    <% 
                        }    
                    %>
                </div>
                <div class="product-list">                  
                    <c:forEach items="${requestScope.productList}" var="o">
                        <div class="card">
                            <a href="${pageContext.request.contextPath}/productDetail?productId=${o.id}">
                                <img class="card-img" src="${o.imgUrl}" alt="Product Image">
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
                            <c:if test="${sessionScope.user.getRole().getId()== null ||sessionScope.user.getRole().getId()== 3 }">
                                <form action="cart" method="get">
                                    <input type="hidden" name="productId" value="${o.id}" />
                                    <button class="card-button" type="submit">🛒 Add to Cart</button>
                                </form>
                                <button class="card-button" type="submit" value="buy">💰 Buy</button>
                            </c:if> 
                        </div>
                    </c:forEach>
                </div>
                <ul class="pagination">
                    <c:if test="${sessionScope.categoryId == null}">
                        <c:forEach begin="1" end="${requestScope.totalPage}" var="i">
                            <li class="page-item"><a href="home?index=${i}" class="page-link">${i}</a></li>
                            </c:forEach>
                        </c:if>    
                        <c:if test="${sessionScope.categoryId != null}">
                            <c:forEach begin="1" end="${requestScope.totalPage}" var="o">
                            <li class="page-item"><a href="category?categoryId=${sessionScope.categoryId}&index=${o}" class="page-link">${o}</a></li>
                            </c:forEach>
                        </c:if>    
                </ul>
            </div>   

        </div>

        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
