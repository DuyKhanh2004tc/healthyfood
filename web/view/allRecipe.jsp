
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.util.List" %>
<%@ page import="model.*, java.sql.Timestamp, java.util.*, java.text.*" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Recipe Page</title>
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
        <style>

            * {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
    font-family: 'Arial', sans-serif;
}

body {
    background-color: #f4f4f9;
    color: #333;
    line-height: 1.6;
    padding: 20px;
    min-height: 100vh;
}

form {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    margin-bottom: 20px;
    align-items: center;
    justify-content: center;
}

input[type="text"], select {
    padding: 10px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 16px;
    width: 100%;
    max-width: 200px;
    transition: border-color 0.3s ease;
}

input[type="text"]:focus, select:focus {
    outline: none;
    border-color: #28a745;
    box-shadow: 0 0 5px rgba(40, 167, 69, 0.3);
}

/* Button styling */
button[type="submit"], button[type="button"] {
    padding: 10px 20px;
    background-color: #28a745;
    color: white;
    border: none;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

button[type="submit"]:hover, button[type="button"]:hover {
    background-color: #218838;
    transform: translateY(-2px);
}

button[onclick="openAddPopup()"] {
    display: block;
    margin: 0 auto 20px;
}

.overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    z-index: 1000;
    transition: opacity 0.3s ease;
}

.popup {
    display: none;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: white;
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    width: 90%;
    max-width: 500px;
    max-height: 80vh;
    overflow-y: auto;
    z-index: 1001;
    animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translate(-50%, -60%);
    }
    to {
        opacity: 1;
        transform: translate(-50%, -50%);
    }
}

.popup h3 {
    margin-bottom: 20px;
    color: #28a745;
    text-align: left;
    font-size: 1.5rem;
}

.popup form {
    display: flex;
    flex-direction: column;
    gap: 15px;
    align-items: flex-start;
}

.popup input[type="text"],
.popup textarea,
.popup select,
.popup input[type="file"] {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 14px;
    transition: border-color 0.3s ease;
}

.popup input[type="text"]:focus,
.popup textarea:focus,
.popup select:focus {
    outline: none;
    border-color: #28a745;
    box-shadow: 0 0 5px rgba(40, 167, 69, 0.3);
}

.popup textarea {
    resize: vertical;
    min-height: 100px;
}

/* Checkbox container */
.checkbox-container {
    display: flex;
    flex-wrap: wrap;
    gap: 10px;
    max-height: 150px;
    width: 100%;
    overflow-y: auto;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 5px;
    background-color: #f9f9f9;
}

.checkbox-item {
    display: flex;
    align-items: center;
    gap: 5px;
    font-size: 14px;
}

.popup input[type="checkbox"] {
    margin: 0;
    accent-color: #28a745;
    width: 16px;
    height: 16px;
}

/* Button container for Save and Cancel */
.button-container {
    display: flex;
    gap: 10px;
    width: 100%;
}

.popup .button {
    padding: 10px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s ease, transform 0.2s ease;
    flex: 1;
}

.popup .button.save {
    background-color: #28a745;
    color: white;
    border: none;
}

.popup .button.cancel {
    background-color: #dc3545;
    color: white;
    border: none;
}

.popup .button:hover {
    opacity: 0.9;
    transform: translateY(-2px);
}

/* Recipe list styling */
.recipe-list {
    display: flex;
    flex-wrap: wrap;
    gap: 20px;
    justify-content: center;
    padding: 20px 0;
}

.recipe-card {
    background: white;
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    padding: 15px;
    width: 100%;
    max-width: 300px;
    text-align: center;
    transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.recipe-card:hover {
    transform: translateY(-5px);
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

img {
    max-width: 100%;
    height: auto;
    border-radius: 8px;
    margin-bottom: 10px;
    object-fit: cover;
    aspect-ratio: 4/3;
}

p {
    margin: 5px 0;
    font-size: 14px;
}

p:first-of-type {
    font-weight: bold;
    font-size: 16px;
    color: #28a745;
}

.recipe-card p:nth-of-type(1) {
    text-transform: capitalize;
    font-weight: bold;
    font-size: 16px;
    color: #28a745;
}

.recipe-card p:nth-of-type(2) {
    text-transform: lowercase;
    display: -webkit-box;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    overflow: hidden;
    text-overflow: ellipsis;
    height: 4.5em;
    font-size: 14px;
}

:focus {
    outline: 2px solid #28a745;
    outline-offset: 2px;
}

button, input, select, textarea {
    font-size: 16px;
}

.sr-only {
    position: absolute;
    width: 1px;
    height: 1px;
    padding: 0;
    margin: -1px;
    overflow: hidden;
    clip: rect(0, 0, 0, 0);
    border: 0;
    
}

.recipe-list a {
    text-decoration: none;
    color: inherit;
}

.recipe-list a:visited {
    color: inherit;
}

.recipe-list a p {
    text-decoration: none;
    color: inherit;
}

        </style>
    </head>
    <body>
        <c:if test="${sessionScope.user.getRole().getId()!=4}">
            <jsp:include page="header.jsp" />
        </c:if>
        <c:if test="${sessionScope.user.getRole().getId()==4}">

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
        <form method="get" action="${pageContext.request.contextPath}/allRecipe">
            <input type="text" value="${param.productName}" placeholder="Search product name" name="productName"/>    
            <select name="typeId">
                <option value="" ${param.typeId == null || param.typeId == '' ? 'selected' : ''}>All Types</option>
                <c:forEach items="${requestScope.typeList}" var="i">
                    <option value="${i.id}" ${param.typeId == i.id ? 'selected' : ''}>${i.name}</option>
                </c:forEach>
            </select>
            <button type="submit">Search</button>
        </form> 
        <button type="button" onclick="openAddPopup()">Add Recipe</button>
        <div class="overlay" id="overlay"></div>
        <div class="popup" id="popupadd">
            <h3>Add Recipe</h3>
            <form method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/allRecipe">
                <input type="hidden" name="action" value="add">
                <p>Image</p>
                <input type="file" name="file" accept="image/*" />
                <p>Name</p>
                <input type="text" name="name" placeholder="Enter name here..." required>
                <p>Product in Cooking Recipe</p>
                <input type="text" id="productSearch" placeholder="Search products..." oninput="filterProducts()">
                <div class="checkbox-container">
                    <%
                        List<Product> productList = (List<Product>)request.getAttribute("productList");
                        for(Product p : productList) {
                    %>
                    <div class="checkbox-item">
                        <input type="checkbox" name="chooseProduct" value="<%= p.getId() %>" id="product-<%= p.getId() %>">
                        <label for="product-<%= p.getId() %>"><%= p.getName() %></label>
                    </div>
                    <%
                        }
                    %>
                </div>
                <p>Type</p>
                <select name="typeId">
                    <option value="" ${param.typeId == null || param.typeId == '' ? 'selected' : ''}>All Types</option>
                    <c:forEach items="${requestScope.typeList}" var="i">
                        <option value="${i.id}" ${param.typeId == i.id ? 'selected' : ''}>${i.name}</option>
                    </c:forEach>
                </select>
                <p>Description</p>
                <textarea name="description" placeholder="Enter description here..." required></textarea>
                <div class="button-container">
                    <button type="submit" class="button save">Save</button>
                    <button type="button" class="button cancel" onclick="closePopup()">Cancel</button>
                </div>
            </form>
        </div> 
        <div class="recipe-list">
            <c:forEach items="${requestScope.cookingRecipeList}" var="i">
                <a href="${pageContext.request.contextPath}/recipeDetail?recipeId=${i.id}">
                    <div class="recipe-card">
                        <img src="${pageContext.request.contextPath}/images/${i.image}" alt="${name}">
                        <p>${i.name}</p>
                        <p>${i.description}</p>
                         <fmt:formatDate value="${i.createdAt}" pattern="dd/MM/yyyy" />
                    </div>
                </a>
            </c:forEach></div>
        </div>
        <script>
            function openAddPopup() {
                document.getElementById('popupadd').style.display = 'block';
                document.getElementById('overlay').style.display = 'block';
            }

            function closePopup() {
                document.getElementById('popupadd').style.display = 'none';
                document.getElementById('overlay').style.display = 'none';
                document.getElementById('productSearch').value = '';
                filterProducts();
            }

            function filterProducts() {
                const searchTerm = document.getElementById('productSearch').value.toLowerCase();
                const checkboxItems = document.querySelectorAll('.checkbox-item');
                checkboxItems.forEach(item => {
                    const label = item.querySelector('label').textContent.toLowerCase();
                    item.style.display = label.includes(searchTerm) ? 'flex' : 'none';
                });
            }
        </script>            
    </body>
</html>
