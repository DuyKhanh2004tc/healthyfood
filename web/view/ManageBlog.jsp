<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Blog</title>
    <link href="${pageContext.request.contextPath}/CSS/manageBlog.css" rel="stylesheet" type="text/css"/>
</head>

<div class="manage-blog-content">
    <c:choose>

        <c:when test="${requestScope.action == 'addBlog'}">
            <h2>Add Blog</h2>
            <form action="${pageContext.request.contextPath}/manageBlog" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="addBlog">
                <p>Title</p>
                <textarea name="title" required></textarea>
                <p>Description</p>
                <textarea name="description" required></textarea>
                <p>Image</p>
                <input type="file" name="image" accept="image/*" required>
                <p>Tag</p>
                <c:forEach items="${requestScope.tagList}" var="tag">
                    <label>
                        <input type="checkbox" name="chooseTag" value="${tag.id}" >
                        ${tag.name}
                    </label><br>
                </c:forEach>
                <button type="submit" name="addBlogSubmit" value="true">Add Blog</button>
            </form>
        </c:when>

        <c:when test="${requestScope.action == 'deleteBlog'}">
            <h2>Delete Blog</h2>
            <c:if test="${empty requestScope.blogList}">
                <p>There are no blogs to delete.</p>
            </c:if>
            <c:if test="${not empty requestScope.blogList}">
                <form action="${pageContext.request.contextPath}/manageBlog" method="post">
                    <input type="hidden" name="action" value="deleteBlog">
                    <c:forEach items="${requestScope.blogList}" var="blog">
                        <label>
                            <input type="checkbox" name="blogIds" value="${blog.id}">
                            ${blog.title}
                        </label><br>
                    </c:forEach>
                    <button type="submit" name="deleteBlogSubmit" value="true">Delete Selected Blogs</button>
                </form>
            </c:if>
        </c:when>

        <c:when test="${requestScope.action == 'addTag'}">
            <h2>Add Tag</h2>
            <form action="${pageContext.request.contextPath}/manageBlog" method="post">
                <input type="hidden" name="action" value="addTag">
                <label>Tag Name:</label>
                <input type="text" name="tagName" required>
                <label>Description:</label>
                <textarea name="description"></textarea>
                 <button type="submit" name="addSubmit" value="true">Add Tag</button>
            </form>
        </c:when>

        <c:when test="${requestScope.action == 'editTag'}">
            <h2>Edit Tag</h2>
            <form action="${pageContext.request.contextPath}/manageBlog" method="post">
                <input type="hidden" name="action" value="editTag">
                <label>Select Tag:</label>
                <select name="tagId" onchange="this.form.submit()">
                    <option value="">--Select tag--</option>
                    <c:forEach items="${requestScope.tagList}" var="tag">
                        <option value="${tag.id}" ${tag.id == requestScope.selectedTagId ? 'selected' : ''}>${tag.name}</option>
                    </c:forEach>
                </select>
                <label>New Tag Name:</label>
                <input type="text" name="newTagName" value="${requestScope.selectedTagName}" required>
                <label>Description:</label>
                <textarea name="description">${requestScope.selectedTagDescription}</textarea>
                 <button type="submit" name="editSubmit" value="true">Edit Tag</button>
            </form>
        </c:when>

        <c:when test="${requestScope.action == 'deleteTag'}">
            <h2>Delete Tag</h2>
            <form action="${pageContext.request.contextPath}/manageBlog" method="post">
                <input type="hidden" name="action" value="deleteTag">
                <c:forEach items="${requestScope.tagList}" var="tag">
                    <label>
                        <input type="checkbox" name="tagIds" value="${tag.id}">
                        ${tag.name}
                    </label><br>
                </c:forEach>
                <button type="submit" name="deleteSubmit" value="true">Delete Selected Tags</button>
            </form>
        </c:when>

        <c:otherwise>
            <p>No action selected.</p>
        </c:otherwise>


    </c:choose>
    <c:if test="${not empty requestScope.success}">
        <div style="color: green; margin-top: 10px;">
            ${requestScope.success}
        </div>
    </c:if>
    <c:if test="${not empty requestScope.error}">
        <div style="color: red; margin-top: 10px;">
            ${requestScope.error}
        </div>
    </c:if>
</div>