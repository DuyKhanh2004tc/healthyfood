<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
                    <a href="${pageContext.request.contextPath}/allRecipe">Manage Cooking Recipe</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </c:if>
        <div class="nb-main-content">
            <div class="nb-content-wrapper">
                <div class="nb-left-column">
                    <form class="search-form" action="searchBlog" method="get">                   
                        <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                        <input type="text" name="keyword" value="${param.keyword}" placeholder="Search blogs by title">
                    </form>

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

                    <c:if test="${sessionScope.user.getRole().getId() == 4}">        
                        <div class="nb-filter-box">
                            <h3>Manage Blog</h3>
                            <form method="post" action="${pageContext.request.contextPath}/manageBlog">
                                <input type="hidden" name="action" value="addBlog">
                                <button type="submit">Add Blog</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/manageBlog">
                                <input type="hidden" name="action" value="deleteBlog">
                                <button type="submit">Delete Blog</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/manageBlog">
                                <input type="hidden" name="action" value="addTag">
                                <button type="submit">Add Tag</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/manageBlog">
                                <input type="hidden" name="action" value="editTag">
                                <button type="submit">Edit Tag</button>
                            </form>
                            <form method="post" action="${pageContext.request.contextPath}/manageBlog">
                                <input type="hidden" name="action" value="deleteTag">
                                <button type="submit">Delete Tag</button>
                            </form>
                        </div>
                    </c:if>
                </div>


                <div class="nb-container">
                    <div>
                        <c:choose>
                            <c:when test="${requestScope.showManageBlog == true}">
                                <jsp:include page="ManageBlog.jsp" />
                            </c:when>
                            <c:otherwise>
                                <div class="nb-head">
                                    <h1>Welcome to Our Nutrition Blog</h1>
                                    <p>Discover healthy eating tips and nutritious recipes to improve your lifestyle. Stay tuned for our latest posts!</p>
                                </div>
                                <c:if test="${not empty requestScope.selectedTag}">
                                    <div class="nb-selected-tag">
                                        <p>${requestScope.selectedTag.name}:${requestScope.selectedTag.description}</p>
                                    </div>
                                </c:if>
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
                            </div>                
                            <div class="pagination">                                     
                                <c:if test="${currentPage > 1}">
                                    <c:url var="prevUrl" value="/nutritionBlog">
                                        <c:param name="page" value="${currentPage - 1}" />
                                        <c:if test="${not empty param.keyword}">
                                            <c:param name="keyword" value="${param.keyword}" />
                                        </c:if>
                                        <c:if test="${not empty param.tag}">
                                            <c:param name="tag" value="${param.tag}" />
                                        </c:if>
                                    </c:url>
                                    <a class="page-link prev-next" href="${prevUrl}">Previous</a>
                                </c:if>

                                <c:forEach var="i" begin="1" end="${totalPage}">
                                    <c:url var="pageUrl" value="/nutritionBlog">
                                        <c:param name="page" value="${i}" />
                                        <c:if test="${not empty param.keyword}">
                                            <c:param name="keyword" value="${param.keyword}" />
                                        </c:if>
                                        <c:if test="${not empty param.tag}">
                                            <c:param name="tag" value="${param.tag}" />
                                        </c:if>
                                    </c:url>
                                    <a class="page-link ${i == currentPage ? 'active-page' : ''}" href="${pageUrl}">
                                        ${i}
                                    </a>
                                </c:forEach>

                                <c:if test="${currentPage < totalPage}">
                                    <c:url var="nextUrl" value="/nutritionBlog">
                                        <c:param name="page" value="${currentPage + 1}" />
                                        <c:if test="${not empty param.keyword}">
                                            <c:param name="keyword" value="${param.keyword}" />
                                        </c:if>
                                        <c:if test="${not empty param.tag}">
                                            <c:param name="tag" value="${param.tag}" />
                                        </c:if>
                                    </c:url>
                                    <a class="page-link prev-next" href="${nextUrl}">Next</a>
                                </c:if>
                            </div>
                            </section>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
        <c:if test="${sessionScope.user.getRole().getId() != 4}">
            <jsp:include page="chatbot.jsp"></jsp:include>
            <jsp:include page="footer.jsp" />
        </c:if>
    </body>
</html>
