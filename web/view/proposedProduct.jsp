<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.*, java.sql.Timestamp, java.util.*, java.text.*" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
        <style>
            .popup {
                display: none;
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background-color: white;
                padding: 20px;
                border: 1px solid #ccc;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                z-index: 1000;
                width: 400px;
                max-height: 80vh;
                overflow-y: auto;
                border-radius: 8px;
            }

            .overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                z-index: 999;
            }

            .popup h3 {
                margin-top: 0;
                font-size: 1.5em;
                text-align: center;
            }

            .popup p {
                margin: 10px 0 5px;
                font-weight: bold;
            }

            .popup input[type="text"],
            .popup textarea {
                width: 100%;
                padding: 8px;
                margin-bottom: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-sizing: border-box;
            }

            .popup input[type="file"] {
                margin-bottom: 10px;
            }

            .popup img {
                max-width: 100%;
                height: auto;
                margin-bottom: 10px;
            }

            .popup .button {
                padding: 10px 20px;
                margin-right: 10px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .popup .button:last-child {
                margin-right: 0;
            }

            .popup .button:hover {
                background-color: #f0f0f0;
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

        <table border="1">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Description</th>
                    <th>Reason</th>
                    <th>Created At</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requestScope.proposedProductList}" var="i" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>
                            <img src="${i.image}" width="80" alt="${i.name}">
                        </td>
                        <td>${i.name}</td>
                        <td>${i.categoryName}</td>
                        <td>${i.description}</td>
                        <td>${i.reason}</td>
                        <td>${i.createdAt}</td>
                        <td>${i.status}</td>
                        <td>
                            <form method="post" action="${pageContext.request.contextPath}/proposeProduct">
                                <input type="hidden" name="proposedId" value="${i.id}">
                                <c:if test="${i.status == 'pending'}">
                                    <button type="button" onclick="openPopup('${i.id}', '${i.image}', '${i.name}', '${i.categoryName}', '${i.description}', '${i.reason}')">Edit</button>
                                    <button type="submit" name="action" value="delete">Delete</button>
                                </c:if>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <button type="button" onclick="openAddPopup()">Add</button>

        <div class="overlay" id="overlay"></div>
        <div class="popup" id="popup">
            <h3>Edit Product</h3>
            <form id="editProduct" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/proposeProduct">
                <input type="hidden" id="popupProposedId" name="proposedId">
                <input type="hidden" name="action" value="edit">
                <input type="hidden" name="image" id="popupImage">     
                <p>Image</p>
                <input type="file" name="file" accept="image/*" />

                <p>Name</p>
                <input type="text" id="popupName" name="name" placeholder="Enter name here..." required>
                <p>Category Name</p>
                <input type="text" id="popupCategoryName" name="categoryName" placeholder="Enter category name here..." required>
                <p>Description</p>
                <textarea id="popupDescription" name="description" placeholder="Enter description here..." required></textarea>
                <p>Reason</p>
                <textarea id="popupReason" name="reason" placeholder="Enter reason here..." required></textarea>

                <button type="submit" class="button">Save</button>
                <button type="button" class="button" onclick="closePopup()">Cancel</button>
            </form>
        </div>
        <div class="popup" id="popupadd">
            <h3>Add Product</h3>
            <form id="addProduct" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/proposeProduct">

                <input type="hidden" name="action" value="add">
                <p>Image</p>
                <input type="file" name="file" accept="image/*" />
                <p>Name</p>
                <input type="text"  name="name" placeholder="Enter name here..." required>
                <p>Category Name</p>
                <input type="text" name="categoryName" placeholder="Enter category name here..." required>
                <p>Description</p>
                <textarea  name="description" placeholder="Enter description here..." required></textarea>
                <p>Reason</p>
                <textarea  name="reason" placeholder="Enter reason here..." required></textarea>

                <button type="submit" class="button">Save</button>
                <button type="button" class="button" onclick="closePopup()">Cancel</button>
            </form>
        </div>

        <script>
            function openPopup(proposedId, image, name, categoryName, description, reason) {
                document.getElementById('popup').style.display = 'block';
                document.getElementById('overlay').style.display = 'block';
                document.getElementById('popupProposedId').value = proposedId;
                document.getElementById('popupImage').value = image;
                document.getElementById('popupName').value = name;
                document.getElementById('popupCategoryName').value = categoryName;
                document.getElementById('popupDescription').value = description;
                document.getElementById('popupReason').value = reason;
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