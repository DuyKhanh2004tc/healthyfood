<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.*" %>
<%@ page import="java.sql.Timestamp, java.util.*, java.text.*" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
       <style>
/* General reset and base styles */
* {
    
    margin: 0;
    padding: 0;
}

body {
    font-family: 'Arial', sans-serif;
    background-color: #f8f8f8;
    color: #333;
    line-height: 1.6;
    margin: 0;
    padding: 0;
}

/* Main content container */
.main-content {
    max-width: 900px;
    margin: 2rem auto;
    background: #fff;
    padding: 1.5rem;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
    border-radius: 8px;
}

/* Navigation buttons */
.nav-buttons {
    display: flex;
    justify-content: space-between;
    gap: 1rem;
    margin-bottom: 1.5rem;
}

.nav-button {
    background-color: #4CAF50;
    color: #fff;
    padding: 0.75rem 1.5rem;
    border: none;
    text-decoration: none;
    font-weight: 600;
    border-radius: 5px;
    transition: background-color 0.3s ease;
}

.nav-button:hover {
    background-color: #45a049;
}

.nav-button.disabled {
    background-color: #ccc;
    cursor: not-allowed;
    pointer-events: none;
    opacity: 0.6;
}

/* Recipe content */
.recipe-content img {
    max-width: 100%;
    height: auto;
    border-radius: 10px;
    margin-bottom: 1rem;
    align-items: center;
     
}

.recipe-content h3 {
    margin: 1rem 0 0.5rem;
    font-size: 1.75rem;
    color: #222;
}

.recipe-content p {
    line-height: 1.7;
    margin: 0.75rem 0;
    font-size: 1rem;
}

.recipe-content .ingredients-list {
    margin: 0.75rem 0;
    font-size: 1rem;
}

.recipe-content .ingredients-list span {
    display: block; /* Each span on a new line */
    margin-bottom: 0.25rem; /* Optional: Add spacing between items */
}

.meta {
    color: #666;
    font-size: 0.9rem;
    margin: 0.5rem 0;
}

/* Overlay and Popup */
.overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    z-index: 999;
}

.popup {
    display: none;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 90%;
    max-width: 600px;
    background: #fff;
    padding: 2rem;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.2);
    border-radius: 10px;
    z-index: 1000;
    max-height: 80vh;
    overflow-y: auto;
}

.popup h3 {
    margin: 0 0 1rem;
    font-size: 1.5rem;
}

.popup textarea,
.popup select,
.popup input[type="file"] {
    width: 100%;
    padding: 0.75rem;
    margin-bottom: 1rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1rem;
    font-family: inherit;
}

.popup textarea {
    height: 80px;
    resize: vertical;
}

.popup .button {
    background-color: #4CAF50;
    color: #fff;
    border: none;
    padding: 0.75rem 1.5rem;
    cursor: pointer;
    border-radius: 5px;
    font-weight: 600;
    transition: background-color 0.3s ease;
}

.popup .button:hover {
    background-color: #45a049;
}

/* Form buttons */
form button[type="submit"],
form button[type="button"] {
    background-color: #4CAF50;
    color: #fff;
    border: none;
    padding: 0.75rem 1.5rem;
    border-radius: 5px;
    font-weight: 600;
    cursor: pointer;
    margin: 0.5rem 0.5rem 0 0;
    transition: background-color 0.3s ease;
}

form button[type="submit"]:hover,
form button[type="button"]:hover {
    background-color: #45a049;
}

/* Product search and list container */
.popup .product-search {
    width: 100%;
    padding: 0.75rem;
    margin-bottom: 0.5rem;
    border: 1px solid #ddd;
    border-radius: 5px;
    font-size: 1rem;
    font-family: inherit;
    transition: border-color 0.3s ease;
}

.popup .product-search:focus {
    outline: none;
    border-color: #4CAF50;
    box-shadow: 0 0 5px rgba(76, 175, 80, 0.3);
}

.popup .product-list {
    max-height: 200px;
    width: 100%;
    overflow-y: auto;
    border: 1px solid #ddd;
    border-radius: 5px;
    padding: 0.5rem;
    background-color: #f9f9f9;
    margin-bottom: 1rem;
}

.popup .product-list input[type="checkbox"] {
    margin-right: 0.5rem;
    accent-color: #4CAF50;
}

/* Accessibility improvements */
.nav-button:focus,
.popup .button:focus,
form button[type="submit"]:focus,
form button[type="button"]:focus,
.popup .product-search:focus {
    outline: 2px solid #4CAF50;
    outline-offset: 2px;
}
.popup-image {
    display: block;
    max-width: 80%;
    height: auto;
    margin: 1rem auto;
    border-radius: 10px;
}
/* Recipe content image */
.recipe-content img {
    width: 80%; /* Set width to 90% of the parent container */
    height: auto; /* Maintain aspect ratio */
    border-radius: 10px;
    margin-bottom: 1rem;
    display: block; /* Ensure the image is centered */
    margin-left: auto;
    margin-right: auto;
}

/* Popup image */
.popup-image {
    width: 90%; /* Set width to 90% of the parent container */
    height: auto; /* Maintain aspect ratio */
    margin: 1rem auto;
    border-radius: 10px;
    display: block; /* Ensure the image is centered */
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
                    <a href="${pageContext.request.contextPath}/allRecipe">Manage Recipe</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>
                </div>
            </div>
        </c:if>

        <div class="main-content">
            <div class="container">
                <%
                    String message = (String) request.getAttribute("message");
                    if (message != null) {
                %>
                    <p style="color: green; text-align: center; margin-bottom: 10px;"><%= message %></p>
                <%
                        request.removeAttribute("message");
                    }
                    String error = (String) request.getAttribute("error");
                    if (error != null) {
                %>
                    <p style="color: red; text-align: center; margin-bottom: 10px;"><%= error %></p>
                <%
                        request.removeAttribute("error");
                    }
                %>
                <div class="nav-buttons">
                    <c:choose>
                        <c:when test="${not empty prevId}">
                            <a href="${pageContext.request.contextPath}/recipeDetail?recipeId=${prevId}" class="nav-button">⇐ Previous</a>
                        </c:when>
                        <c:otherwise>
                            <a class="nav-button disabled">⇐ Previous</a>
                        </c:otherwise>
                    </c:choose>
                    <c:choose>
                        <c:when test="${not empty nextId}">
                            <a href="${pageContext.request.contextPath}/recipeDetail?recipeId=${nextId}" class="nav-button">Next ⇒</a>
                        </c:when>
                        <c:otherwise>
                            <a class="nav-button disabled">Next ⇒</a>
                        </c:otherwise>
                    </c:choose>
                </div>
                <div class="recipe-content">
                    <img src="${pageContext.request.contextPath}/images/${image}" alt="${name}">
                    
                    <h3>${name}</h3>
                    <p>Food type : ${type}</p>
                    <p>Main ingredients: </p>
                    <div class="ingredients-list">
                        <%
                            ArrayList<Product> pro = (ArrayList<Product>)request.getAttribute("productByRecipeId");
                            if (pro != null && !pro.isEmpty()) {
                                for (Product p : pro) {
                        %>
                           <a href="${pageContext.request.contextPath}/productDetail?productId=<%= p.getId()%>" ><span>- <%= p.getName() %></span></a> 
                        <%
                                }
                            }
                        %>
                    </div>
                    <div class="meta">
                        <fmt:formatDate value="${createdAt}" pattern="dd/MM/yyyy" />
                        <span class="author">By ${createBy}</span>
                    </div>
                    <p class="description">${description}</p>
                </div>
                <c:if test="${sessionScope.user.getId() == recipe.getNutritionist().getId()}">
                    <div>
                        <form method="post"  action="${pageContext.request.contextPath}/recipeDetail">
                            <input type="hidden" name="recipeId" value="${recipeId}">
                            <button type="button" onclick="openPopup('${recipeId}', '${image}', '${name}', '${description}', '${type}')">Edit</button>
                            <button type="submit" name="action" value="deleteRecipe">Delete</button>
                        </form>
                    </div>
                </c:if>
            </div>
            <div class="overlay" id="overlay"></div>
            <div class="popup" id="popup">
                <h3>Edit Recipe</h3>
                <form id="editRecipe" method="post" enctype="multipart/form-data" accept-charset="UTF-8" action="${pageContext.request.contextPath}/recipeDetail">
                    <input type="hidden" id="popupRecipeId" name="recipeId" value="${recipeId}">
                    <input type="hidden" name="action" value="editRecipe">
                    <input type="hidden" name="image" id="popupImage" value="${image}">
                    <p>Image</p>
                    <input  type="file" name="file" accept="image/*" />
                    <img class="popup-image" src="${pageContext.request.contextPath}/images/${image}" alt="${name}">
                    <p>Type</p>
                    <select name="typeId" id="popupType">
                        <option value="" ${param.typeId == null || param.typeId == '' ? 'selected' : ''}>All Types</option>
                        <c:forEach items="${requestScope.typeList}" var="i">
                            <option value="${i.id}" ${type == i.name ? 'selected' : ''}>${i.name}</option>
                        </c:forEach>
                    </select>
                    <p>Main ingredients</p>
                    <input type="text" id="productSearch" class="product-search" placeholder="Search products..." oninput="filterProducts()">
                    <div class="product-list">
                        <%
                            ArrayList<Product> productList = (ArrayList<Product>)request.getAttribute("productList");
                            if (productList != null && !productList.isEmpty()) {
                                for (Product product : productList) {
                                    boolean check = false;
                                    if (product != null) {
                                        for (Product p : pro) {
                                            if (product.getId() == p.getId()) {
                                                check = true;
                                                break;
                                            }
                                        }
                                    }
                        %>
                            <div class="checkbox-item">
                                <input type="checkbox" name="chooseProduct" value="<%= product.getId() %>" <%= check ? "checked" : "" %>>
                                <%= product.getName() %>
                            </div>
                        <%
                                }
                            }
                        %>
                    </div>
                    <p>Name</p>
                    <textarea id="popupName" name="name" placeholder="Enter name here..." required></textarea>
                    <p>Description</p>
                    <textarea id="popupDescription" name="description" placeholder="Enter description here..." required></textarea>
                    <button type="submit" class="button">Save</button>
                    <button type="button" class="button" onclick="closePopup()">Cancel</button>
                </form>
            </div>
        </div>

        <script>
            function openPopup(recipeId, image, name, description, type) {
                document.getElementById('popup').style.display = 'block';
                document.getElementById('overlay').style.display = 'block';
                document.getElementById('popupImage').value = image;
                document.getElementById('popupRecipeId').value = recipeId;
                document.getElementById('popupName').value = name;
                document.getElementById('popupDescription').value = description || '';
                const typeSelect = document.getElementById('popupType');
                for (let option of typeSelect.options) {
                    if (option.text === type) {
                        option.selected = true;
                        break;
                    }
                }
            }

            function closePopup() {
                document.getElementById('popup').style.display = 'none';
                document.getElementById('overlay').style.display = 'none';
            }

            function filterProducts() {
                const searchTerm = document.getElementById('productSearch').value.toLowerCase();
                const checkboxItems = document.querySelectorAll('.checkbox-item');
                checkboxItems.forEach(item => {
                    const label = item.textContent.toLowerCase();
                    item.style.display = label.includes(searchTerm) ? 'flex' : 'none';
                });
            }
        </script>

        <jsp:include page="footer.jsp" />
    </body>
</html>
