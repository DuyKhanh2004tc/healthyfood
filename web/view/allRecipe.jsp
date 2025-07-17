<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="model.*, java.sql.Timestamp, java.util.*, java.text.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form method="get" action="${pageContext.request.contextPath}/allRecipe">
            <input type="text" value="" name="productName"/>    
            <select name="typeId">
                <option value="">All Types</option>
                <c:forEach items="${requestScope.typeList}" var="i">
                    <option value="${i.id}">${i.name}</option>
                </c:forEach>
            </select>
            <button type="submit" >Search</button>
            <c:forEach items="${requestScope.cookingRecipeList}" var="i">
                <img src="${i.image}" alt="${i.name}"/>
                <p>${i.name}</p>
                <p>${i.description}</p>
                <p>${i.createdAt}</p>
                
            </c:forEach>
        </form>

    </body>
</html>
