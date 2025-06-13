<%-- Document: nutritionBlog --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Nutrition Blog</title>
    <style>
        .description {
            display: -webkit-box;
            -webkit-line-clamp: 5; /* Giới hạn 5 dòng */
            -webkit-box-orient: vertical;
            overflow: hidden;
            text-overflow: ellipsis; /* Thêm dấu ... */
        }
    </style>
</head>
<jsp:include page="header.jsp"></jsp:include>
<body>
<div class="article">
    <h1>Welcome to Our Nutrition Blog</h1>
    <p>Discover healthy eating tips and nutritious recipes to improve your lifestyle. Stay tuned for our latest posts!</p>
    <div class="blog-list">
        <c:forEach items="${requestScope.blogList}" var="o">
            <div class="card">
                <a href="${pageContext.request.contextPath}/nutritionBlog?blogId=${o.id}">
                    <img class="card-img" src="${o.image}" alt="Blog Image">
                </a>
                <div class="card-body">
                    <p>${o.title}</p>
                    <p class="description">${o.description}</p>
                    <p>${o.created_at}</p>
                </div>
            </div>
        </c:forEach>
    </div>
</div>
<jsp:include page="footer.jsp"></jsp:include>
</body>
</html>