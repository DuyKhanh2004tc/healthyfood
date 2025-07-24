<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Recipe Type Management</title>
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

h2 {
    color: #28a745;
    margin-bottom: 20px;
    text-align: center;
    font-size: 1.8rem;
}

/* Table styling */
table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 20px;
    background-color: #fff;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

th, td {
    padding: 12px;
    text-align: left;
    border-bottom: 1px solid #ddd;
}

th {
    background-color: #28a745;
    color: white;
    font-weight: bold;
}

tr:hover {
    background-color: #f9f9f9;
}

td form {
    display: flex;
    gap: 10px;
}

td button {
    padding: 8px 16px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

td button:first-child {
    background-color: #28a745;
    color: white;
}

td button:last-child {
    background-color: #dc3545;
    color: white;
}

td button:hover {
    opacity: 0.9;
    transform: translateY(-2px);
}

/* Add button */
button[onclick="openAddPopup()"] {
    display: block;
    margin: 0 auto 20px;
    padding: 10px 20px;
    background-color: #28a745;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease, transform 0.2s ease;
}

button[onclick="openAddPopup()"]:hover {
    background-color: #218838;
    transform: translateY(-2px);
}

/* Popup overlay */
.overlay {
    display: none;
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.6);
    z-index: 999;
    transition: opacity 0.3s ease;
}

/* Popup styling */
.popup {
    display: none;
    position: fixed;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    background: white;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
    width: 90%;
    max-width: 500px;
    max-height: 80vh;
    overflow-y: auto;
    z-index: 1000;
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

.popup p {
    margin: 5px 0;
    font-weight: bold;
    color: #333;
}

.popup input[type="text"] {
    width: 100%;
    padding: 10px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
    transition: border-color 0.3s ease;
}

.popup input[type="text"]:focus {
    outline: none;
    border-color: #28a745;
    box-shadow: 0 0 5px rgba(40, 167, 69, 0.3);
}

/* Button container for Save and Cancel */
.button-container {
    display: flex;
    gap: 10px;
    width: 100%;
}

.popup .button {
    padding: 10px;
    border-radius: 4px;
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

/* Accessibility improvements */
:focus {
    outline: 2px solid #28a745;
    outline-offset: 2px;
}

button, input {
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

/* Error message styling */
p[style*="color: red"] {
    background-color: #f8d7da;
    color: #721c24;
    padding: 10px;
    border-radius: 4px;
    margin-bottom: 20px;
    text-align: center;
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
                    <a href="${pageContext.request.contextPath}/allRecipe">Manage Cooking Recipe</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>

                </div>
            </div>
        </c:if>
    <h2>Recipe Type Management</h2>
    <c:if test="${not empty errorMessage}">
        <p style="color: red;">${errorMessage}</p>
    </c:if>
    <button type="button" onclick="openAddPopup()">Add New Type</button>
    <c:choose>
        <c:when test="${empty typeList}">
            <p>No recipe types available.</p>
        </c:when>
        <c:otherwise>
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        <th>Name</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:forEach items="${typeList}" var="i" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <td>${i.name}</td>
                            <td>
                                <form method="post" action="${pageContext.request.contextPath}/recipeType" onsubmit="return confirm('Are you sure you want to delete this type?');">
                                    <input type="hidden" name="typeId" value="${i.id}">
                                    <button type="button" onclick="openPopup('${i.id}', '${i.name}')">Edit</button>
                                    <button type="submit" name="action" value="delete">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                </tbody>
            </table>
        </c:otherwise>
    </c:choose>
    <div class="overlay" id="overlay"></div>
    <div class="popup" id="popup">
        <h3>Edit Type</h3>
        <form id="editProduct" method="post" action="${pageContext.request.contextPath}/recipeType">
            <input type="hidden" id="popupTypeId" name="typeId">
            <input type="hidden" name="action" value="edit">
            <p>Name</p>
            <input type="text" id="popupName" name="name" placeholder="Enter name here..." required>
            <div class="button-container">
                <button type="submit" class="button save">Save</button>
                <button type="button" class="button cancel" onclick="closePopup()">Cancel</button>
            </div>
        </form>
    </div>
    <div class="popup" id="popupadd">
        <h3>Add Type</h3>
        <form id="addProduct" method="post" action="${pageContext.request.contextPath}/recipeType">
            <input type="hidden" name="action" value="add">
            <p>Name</p>
            <input type="text" name="name" placeholder="Enter name here..." required>
            <div class="button-container">
                <button type="submit" class="button save">Save</button>
                <button type="button" class="button cancel" onclick="closePopup()">Cancel</button>
            </div>
        </form>
    </div>
    <script>
        function openPopup(typeId, name) {
            document.getElementById('popup').style.display = 'block';
            document.getElementById('overlay').style.display = 'block';
            document.getElementById('popupTypeId').value = typeId;
            document.getElementById('popupName').value = name;
        }
        function openAddPopup() {
            document.getElementById('popupadd').style.display = 'block';
            document.getElementById('overlay').style.display = 'block';
        }
        function closePopup() {
            document.getElementById('popup').style.display = 'none';
            document.getElementById('popupadd').style.display = 'none';
            document.getElementById('overlay').style.display = 'none';
        }
    </script>
</body>
</html>