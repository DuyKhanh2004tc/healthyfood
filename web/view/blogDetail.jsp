<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Blog Detail</title>
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
    </style>
</head>
<body>
    <jsp:include page="header.jsp" />
    <div class="main-content">
        <div class="container">
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
                <a href="${pageContext.request.contextPath}/blogDetail?blogId=${blogId}">
                    <img src="${pageContext.request.contextPath}/images/${image}" alt="${title}">
                </a>
                <h3>${title}</h3>
                <div class="meta">
                    <span class="date">${created_at}</span>
                    <span class="author">By ${createBy}</span>
                </div>
                <p class="description">${description}</p>
            </div>
            <% if (sessionUser != null && sessionUser.getId() ==%>${blogId}<%){ %>
                    <div>
                        <form method="post" action="${pageContext.request.contextPath}/blogDetail" >                       
                            <input type="hidden" name="blogId" value="${blogId} %>">
                            <button type="button" onclick="openPopup(${blogId}, '<%= f.getContent().replace("'", "\\'") %>', <%= f.getRate() %>)">Edit</button>
                            <button type="submit" name="action" value="deleteFeedback">Delete</button>
                        </form>
                    </div>
                    <% } %>
        </div>
            <div class="overlay" id="overlay"></div>
                <div class="popup" id="popup">
                    <h3>Edit Blog</h3>
                    <form id="editBlog" method="post" action="${pageContext.request.contextPath}/blogDetail">
                        <input type="hidden" name="productId" value="<%= productId %>">
                        <input type="hidden" name="feedbackId" id="feedbackId">
                        <input type="hidden" name="action" value="editBlog">
                        <p>ảnh sửa</p>
                        <textarea id="title" name="title" placeholder="Enter your content here..." required></textarea>
                        <textarea id="content" name="content" placeholder="Enter your content here..." required></textarea>
                        <button type="submit" class="button">Save</button>
                        <button type="button" class="button" onclick="closePopup()">Cancel</button>
                    </form>
                </div>
    </div>
        <script>     
            function openPopup(blogId, image, title,content ) {
                document.getElementById('popup').style.display = 'block';
                document.getElementById('overlay').style.display = 'block';
                document.getElementById('tittle').value = title;
                document.getElementById('blogid').value = blogId;
                document.getElementById('content').value = content;
                document.getElementById('image').value = image;
            }

            function closePopup() {
                document.getElementById('popup').style.display = 'none';
                document.getElementById('overlay').style.display = 'none';
            }

        </script>           
    <jsp:include page="footer.jsp" />
</body>
</html>