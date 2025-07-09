<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="model.*" %>
<%@ page import="java.sql.Timestamp, java.util.*, java.text.*" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Blog Detail</title>
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
        <style>
            /* Reset default styles for main content only */
            .main-content * {
                margin: 0;
                padding: 0;
                box-sizing: border-box;
            }

            /* Main content container */
            .main-content {
                color: #333;
                background-color: #f9fafb;
                font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            }

            /* Container */
            .container {
                max-width: 800px;
                margin: 0 auto;
                padding: 2rem 1rem;
            }

            /* Blog content */
            .blog-content {
                background: #fff;
                border-radius: 12px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                padding: 2rem;
                margin-bottom: 2rem;
            }

            .blog-content img {
                width: 100%;
                height: auto;
                max-height: 400px;
                object-fit: cover;
                border-radius: 8px;
                margin-bottom: 1.5rem;
            }

            .blog-content h3 {
                font-size: 2rem;
                color: #1a3c34;
                margin-bottom: 1rem;
                font-weight: 600;
            }

            .blog-content .meta {
                font-size: 0.9rem;
                color: #9ca3af;
                margin-bottom: 1rem;
                font-style: italic;
            }

            .blog-content .meta span {
                margin-right: 1rem;
            }

            .blog-content .description {
                font-size: 1rem;
                color: #4b5e5a;
                line-height: 1.8;
            }

            /* Navigation buttons */
            .nav-buttons {
                display: flex;
                justify-content: space-between;
                margin-bottom: 2rem;
            }

            .nav-button {
                display: inline-block;
                padding: 0.75rem 1.5rem;
                background-color: #1a3c34;
                color: #fff;
                text-decoration: none;
                border-radius: 6px;
                font-size: 1rem;
                transition: background-color 0.3s ease, transform 0.3s ease;
            }

            .nav-button:hover {
                background-color: #2e5a50;
                transform: translateY(-2px);
            }

            .nav-button.disabled {
                background-color: #d1d5db;
                cursor: not-allowed;
                pointer-events: none;
            }

            /* Responsive design */
            @media (max-width: 768px) {
                .container {
                    padding: 1.5rem;
                }

                .blog-content h3 {
                    font-size: 1.75rem;
                }

                .blog-content img {
                    max-height: 300px;
                }

                .nav-buttons {
                    flex-direction: column;
                    gap: 1rem;
                }

                .nav-button {
                    text-align: center;
                    width: 100%;
                }
            }

            @media (max-width: 480px) {
                .container {
                    padding: 1rem;
                }

                .blog-content {
                    padding: 1.5rem;
                }

                .blog-content h3 {
                    font-size: 1.5rem;
                }
            }

            /* Popup styles */
            .overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                right: 0;
                bottom: 0;
                background: rgba(0, 0, 0, 0.5);
                z-index: 999;
            }

            /* ======== Popup Edit Blog Clean Style ======== */
            .popup {
                display: none;
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background: #ffffff;
                padding: 2rem;
                border-radius: 12px;
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.2);
                z-index: 1000;
                max-width: 500px;
                width: 100%;
                max-height: 90vh;
                overflow-y: auto;
            }

            .popup h3 {
                font-size: 1.5rem;
                margin-bottom: 1rem;
                color: #1a3c34;
                text-align: center;
            }

            .popup p {
                margin: 0.5rem 0 0.25rem;
                font-weight: 500;
            }

            .popup input[type="file"] {
                width: 100%;
                padding: 0.5rem;
                margin-bottom: 1rem;
                border: 1px solid #ccc;
                border-radius: 6px;
            }

            .popup img {
                display: block;
                width: 100%;
                max-height: 250px;
                object-fit: cover;
                border-radius: 8px;
                margin: 0.5rem 0 1rem;
            }

            .popup textarea {
                width: 100%;
                min-height: 80px;
                padding: 0.75rem;
                border: 1px solid #ccc;
                border-radius: 6px;
                resize: vertical;
            }

            .popup .button {
                display: inline-block;
                background-color: #1a3c34;
                color: #fff;
                border: none;
                padding: 0.75rem 1.5rem;
                margin-top: 1rem;
                margin-right: 0.5rem;
                border-radius: 6px;
                cursor: pointer;
                transition: background 0.3s ease, transform 0.3s ease;
            }

            .popup .button:hover {
                background-color: #2e5a50;
                transform: translateY(-2px);
            }

            .popup .button:last-child {
                background-color: #d1d5db;
                color: #333;
            }

            .popup .button:last-child:hover {
                background-color: #cbd5e1;
                transform: none;
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
        <% String fileName = (String) request.getAttribute("fileName");%>
        <div class="main-content">
            <div class="container">

                <!-- Navigation Buttons -->
                <div class="nav-buttons">
                    <c:choose>
                        <c:when test="${not empty prevId}">
                            <a href="${pageContext.request.contextPath}/blogDetail?blogId=${prevId}" class="nav-button">⇐ Previous</a>
                        </c:when>
                        <c:otherwise>
                            <a class="nav-button disabled">⇐ Previous</a>
                        </c:otherwise>
                    </c:choose>

                    <c:choose>
                        <c:when test="${not empty nextId}">
                            <a href="${pageContext.request.contextPath}/blogDetail?blogId=${nextId}" class="nav-button">Next ⇒</a>
                        </c:when>
                        <c:otherwise>
                            <a class="nav-button disabled">Next ⇒</a>
                        </c:otherwise>
                    </c:choose>
                </div>


                <div class="blog-content">

                    <img src="${pageContext.request.contextPath}/images/${image}" alt="${title}">

                    <h3>${title}</h3>
                     <p> 
                    <%
                                            ArrayList<Tag> tag = (ArrayList<Tag>)request.getAttribute("tag");
                                            if(tag!=null&&!tag.isEmpty()){
                                            for(Tag t : tag){
                        %>
                      #<%= t.getName()%> 
                        <%
                                             }
                                      }
                        %>
                        </p>
                    <div class="meta">
                        <span class="date">${created_at}</span>
                        <span class="author">By ${createBy}</span>
                    </div>
                    <p class="description">${description}</p>
                </div>

                <c:if test="${sessionScope.user.getId()== blog.getUser().getId()}">
                    <div>
                        <form method="post" action="${pageContext.request.contextPath}/blogDetail">
                            <input type="hidden" name="blogId" value="${blogId}">
                            <button type="button" onclick="openPopup('${blogId}', '${image}', '${title}', '${description}')">Edit</button>
                            <button type="submit" name="action" value="deleteBlog">Delete</button>
                        </form>
                    </div>
                </c:if>

            </div>


            <div class="overlay" id="overlay"></div>
            <div class="popup" id="popup">
                <h3>Edit Blog</h3>
                <form id="editBlog" method="post" enctype="multipart/form-data" action="${pageContext.request.contextPath}/blogDetail">
                    <input type="hidden" id="popupBlogId" name="blogId" value="${blogId}">
                    <input type="hidden" name="action" value="editBlog">
                    <input type="hidden" name="image" value="${image}"> 
                    <p>Image</p>
                    <input type="file" name="file" accept="image/*" />
                    <img src="${pageContext.request.contextPath}/images/${image}" alt="${title}">
                    <p>Title</p>
                    <textarea id="popupTitle" name="title" placeholder="Enter title here..." required>${title}</textarea>
                    <p>Description</p>
                    <textarea id="popupDescription" name="description" placeholder="Enter description here..." required>${description}</textarea>
                    <button type="submit" class="button">Save</button>
                    <button type="button" class="button" onclick="closePopup()">Cancel</button>
                </form>
            </div>
        </div>

        <script>
            function openPopup(blogId, image, title, description) {
                document.getElementById('popup').style.display = 'block';
                document.getElementById('overlay').style.display = 'block';
                document.getElementById('popupImage').value = image;
                document.getElementById('popupBlogId').value = blogId;
                document.getElementById('popupTitle').value = title;
                document.getElementById('popupDescription').value = description;
            }

            function closePopup() {
                document.getElementById('popup').style.display = 'none';
                document.getElementById('overlay').style.display = 'none';
            }
        </script>

        <jsp:include page="footer.jsp" />
    </body>
</html>
