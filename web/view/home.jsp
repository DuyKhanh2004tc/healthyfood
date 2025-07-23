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
        <title>Home Page</title>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <!-- Main Content -->
            <div class="content-welcome">
                <h1>Welcome to Healthy Food Store🥦</h1>
                <p>Enjoy shopping fresh and nutritious food tailored to your health goals.</p>
            </div>

            <div class="content-main">
                <div class="content-left">
                    <form class ="search-form" action="search" method="get">                   
                        <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                        <input type="text" name="keyword" placeholder="Search for products...">
                    </form>
                <b>New Product:</b>

                <div class="newProduct">                  

                    <div class="card">
                        <a href="${pageContext.request.contextPath}/productDetail?productId=${requestScope.newProduct.id}">
                            <img class="card-img" src="${requestScope.newProduct.imgUrl}" alt="Product Image">
                        </a>
                        <div class="card-body">
                            <p>Product: ${requestScope.newProduct.name}</p>
                            <p>Price: ${requestScope.newProduct.price}$</p>
                            <p>Stock: ${requestScope.newProduct.stock}</p>
                            <p>Rating: ${requestScope.newProduct.rate}</p>
                            <div class="stars-average">
                                <c:set var="fullStars" value="${requestScope.newProduct.rate >= 1 ? (requestScope.newProduct.rate >= 5 ? 5 : (requestScope.newProduct.rate - requestScope.newProduct.rate % 1)) : 0}" />
                                <c:set var="halfStar" value="${(requestScope.newProduct.rate - fullStars) >= 0.5 ? true : false}" />

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
                            <div class="twoButton">
                                <form action="cart" method="get">
                                    <input type="hidden" name="productId" value="${requestScope.newProduct.id}" />
                                    <button class="card-button" type="submit">🛒 Add to Cart</button>
                                </form>
                                <form class="btnBuy" action="placeOrder" method="get">
                                    <input type="hidden" name="productId" value="${requestScope.newProduct.id}" />
                                    <input type="hidden" name="quantity" value="1" />
                                    <button class="card-button" type="submit" value="buy">💰 Buy</button>
                                </form>
                            </div>
                        </c:if> 
                    </div>                   
                </div>

                <div class="home-feature">
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
                    <b>Sort By Price:</b>
                    <button onclick="location.href = 'sortproduct?orderBy=desc'">Descending</button>
                    <button onclick="location.href = 'sortproduct?orderBy=asc'">Ascending</button>
                </div>
                <div>
                    <b>Sort By Name:</b>
                    <button onclick="location.href = 'sortproduct?nameOrderBy=desc'">Name: A-Z</button>
                    <button onclick="location.href = 'sortproduct?nameOrderBy=asc'">Name: Z-A</button>
                </div>
                <div>
                    <b>Sort By Rating:</b>
                    <button onclick="location.href = 'sortproduct?rateOrderBy=desc'">Name: High To Low</button>
                    <button onclick="location.href = 'sortproduct?rateOrderBy=asc'">Name: Low To High</button>
                </div>
                <div>
                    <b>Sort By Date:</b>
                    <button onclick="location.href = 'sortproduct?dateOrderBy=desc'">Newest First</button>
                    <button onclick="location.href = 'sortproduct?dateOrderBy=asc'">Oldest First</button>
                </div>   
            </div>
            <!-- hien thi product -->
            <div class="content-right">
                <div class="list-title">
                    <h2>Product List:</h2>
                    <%  if(request.getAttribute("errorMessage")!=null){
                        String em =(String)request.getAttribute("errorMessage"); 
                    %>
                    <h2 class="notFoundMessage"><%= em%> </h2>
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
                                <div class="twoButton">
                                    <form action="cart" method="get">
                                        <input type="hidden" name="productId" value="${o.id}" />
                                        <button class="card-button" type="submit">🛒 Add to Cart</button>
                                    </form>
                                    <form class="btnBuy" action="placeOrder" method="get">
                                        <input type="hidden" name="productId" value="${o.id}" />
                                        <input type="hidden" name="quantity" value="1" />
                                        <button class="card-button" type="submit" value="buy">💰 Buy</button>
                                    </form>
                                </div>
                            </c:if> 
                        </div>
                    </c:forEach>
                </div>


                <div class="pagination">                                     
                    <c:if test="${currentPage > 1}">
                        <c:url var="prevUrl" value="/home">
                            <c:param name="index" value="${currentPage - 1}" />
                            <c:if test="${not empty param.keyword}">
                                <c:param name="keyword" value="${param.keyword}" />
                            </c:if>
                            <c:if test="${not empty param.categoryId}">
                                <c:param name="categoryId" value="${param.categoryId}" />
                            </c:if>
                            <c:if test="${not empty param.minPrice}">
                                <c:param name="minPrice" value="${param.minPrice}" />
                            </c:if>
                            <c:if test="${not empty param.maxPrice}">
                                <c:param name="maxPrice" value="${param.maxPrice}" />
                            </c:if>
                            <c:if test="${not empty param.orderBy}">
                                <c:param name="orderBy" value="${param.orderBy}" />
                            </c:if>
                        </c:url>
                        <a class="page-link prev-next" href="${prevUrl}">Previous</a>
                    </c:if>

                    <c:forEach var="i" begin="1" end="${totalPage}">
                        <c:url var="pageUrl" value="/home">
                            <c:param name="index" value="${i}" />
                            <c:if test="${not empty param.keyword}">
                                <c:param name="keyword" value="${param.keyword}" />
                            </c:if>
                            <c:if test="${not empty param.categoryId}">
                                <c:param name="categoryId" value="${param.categoryId}" />
                            </c:if>
                            <c:if test="${not empty param.minPrice}">
                                <c:param name="minPrice" value="${param.minPrice}" />
                            </c:if>
                            <c:if test="${not empty param.maxPrice}">
                                <c:param name="maxPrice" value="${param.maxPrice}" />
                            </c:if>
                            <c:if test="${not empty param.orderBy}">
                                <c:param name="orderBy" value="${param.orderBy}" />
                            </c:if>
                        </c:url>
                        <a class="page-link ${i == currentPage ? 'active-page' : ''}" href="${pageUrl}">
                            ${i}
                        </a>
                    </c:forEach>

                    <c:if test="${currentPage < totalPage}">
                        <c:url var="nextUrl" value="/home">
                            <c:param name="index" value="${currentPage + 1}" />
                            <c:if test="${not empty param.keyword}">
                                <c:param name="keyword" value="${param.keyword}" />
                            </c:if>
                            <c:if test="${not empty param.categoryId}">
                                <c:param name="categoryId" value="${param.categoryId}" />
                            </c:if>
                            <c:if test="${not empty param.minPrice}">
                                <c:param name="minPrice" value="${param.minPrice}" />
                            </c:if>
                            <c:if test="${not empty param.maxPrice}">
                                <c:param name="maxPrice" value="${param.maxPrice}" />
                            </c:if>
                            <c:if test="${not empty param.orderBy}">
                                <c:param name="orderBy" value="${param.orderBy}" />
                            </c:if>
                        </c:url>
                        <a class="page-link prev-next" href="${nextUrl}">Next</a>
                    </c:if>
                </div>
            </div>   

        </div>

        <c:if test="${not empty sessionScope.stockError}">
            <script>
        alert("${sessionScope.stockError}");
            </script>
            <%
                session.removeAttribute("stockError");
            %>
        </c:if>
        <jsp:include page="chatbot.jsp"></jsp:include>
        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
