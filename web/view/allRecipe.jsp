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
            <input type="text" value="${param.productName}" name="productName"/>    
            <select name="typeId">
                <option value="" ${param.typeId == null || param.typeId == '' ? 'selected' : ''}>All Types</option>
                <c:forEach items="${requestScope.typeList}" var="i">
                    <option value="${i.id}" ${param.typeId == i.id ? 'selected' : ''}>${i.name}</option>
                </c:forEach>
            </select>
            <button type="submit">Search</button>
        </form> 
        <button type="button" onclick="openAddPopup()">Add</button>
        <div class="overlay" id="overlay"></div>
        <div class="popup" id="popupadd">
            <h3>Add Product</h3>
            <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/allRecipe">
                <input type="hidden" name="action" value="add">
                <p>Image</p>
                <input type="file" name="file" accept="image/*" />
                <p>Name</p>
                <input type="text"  name="name" placeholder="Enter name here..." required>
                <p>Type</p>
                <select name="typeId">
                    <option value="" ${param.typeId == null || param.typeId == '' ? 'selected' : ''}>All Types</option>
                    <c:forEach items="${requestScope.typeList}" var="i">
                        <option value="${i.id}" ${param.typeId == i.id ? 'selected' : ''}>${i.name}</option>
                    </c:forEach>
                </select>
                <p>Description</p>
                <textarea  name="description" placeholder="Enter description here..." required></textarea>              
                <button type="submit" class="button">Save</button>
                <button type="button" class="button" onclick="closePopup()">Cancel</button>
            </form>
        </div> 
        <c:forEach items="${requestScope.cookingRecipeList}" var="i">
            <img src="${i.image}" alt="${i.name}"/>
            <p>${i.name}</p>
            <p>${i.description}</p>
            <p>${i.createdAt}</p>
        </c:forEach>  
        <script>
            function openAddPopup() {
                document.getElementById('popupadd').style.display = 'block';
                document.getElementById('overlay').style.display = 'block';
            }
        </script>            
    </body>
</html>