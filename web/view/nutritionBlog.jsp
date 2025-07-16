<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Nutrition Blog</title>
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/nutritionBlog.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <c:if test="${sessionScope.user.getRole().getId() != 4}">
            <jsp:include page="header.jsp" />
        </c:if>
        <c:if test="${sessionScope.user.getRole().getId() == 4}">
            <div class="nutrition-header">
                <div class="logo">
                    <a href="${pageContext.request.contextPath}/nutritionistHome">
                        <img src="${pageContext.request.contextPath}/images/logo_3.png" alt="Logo">
                    </a>
                </div>
                <div class="menu-content-left">
                    <h3>Welcome Nutritionist ${sessionScope.user.getName()}</h3>
                    <a href="${pageContext.request.contextPath}/nutritionistHome">View Product List</a>
                    <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                    <a href="${pageContext.request.contextPath}/proposeProduct">Propose new product</a>
                    <a href="${pageContext.request.contextPath}/nutritionBlog">Manage Blog</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </c:if>
        <div class="nb-main-content">
            <form class ="search-form" action="searchBlog" method="get">                   
                            <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                            <input type="text" name="keyword" value="${param.keyword}" placeholder="Search blogs by title or content">
                        </form>
            <div class="nb-content-wrapper">
                <div class="nb-filter-box">
                    <h3>Filter by Tags</h3>
                    <c:forEach items="${requestScope.tagList}" var="tag">
                        <form method="get" action="${pageContext.request.contextPath}/nutritionBlog">
                            <label>
                                <input type="checkbox" name="tag" value="${tag.slug}"
                                       <c:if test="${param.tag == tag.slug}">checked</c:if>
                                       onchange="this.form.submit()">
                                ${tag.name}
                            </label>
                        </form>
                    </c:forEach>
                    <form method="get" action="${pageContext.request.contextPath}/nutritionBlog">
                        <label>
                            <input type="submit" value="Clear Filter" class="nb-clear-filter">
                        </label>
                    </form>
                </div>
                <div class="nb-container">
                    <div class="nb-head">
                        <h1>Welcome to Our Nutrition Blog</h1>
                        <p>Discover healthy eating tips and nutritious recipes to improve your lifestyle. Stay tuned for our latest posts!</p>
                    </div>
                    <section class="nb-blog-grid">
                        <c:forEach items="${requestScope.blogList}" var="o">
                            <article class="nb-blog-card">
                                <a href="${pageContext.request.contextPath}/blogDetail?blogId=${o.id}">
                                    <img src="${pageContext.request.contextPath}/images/${o.image}" alt="${o.title}">
                                    <div class="nb-blog-card-content">
                                        <h2>${o.title}</h2>
                                        <p class="nb-description">${o.description}</p>
                                        <p class="nb-date">${o.created_at}</p>
                                    </div>
                                </a>
                            </article>
                        </c:forEach>
                    </section>
                </div>
            </div>
        </div>
        <jsp:include page="footer.jsp" />
    </body>
</html>