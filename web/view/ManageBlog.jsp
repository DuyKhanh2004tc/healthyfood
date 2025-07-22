<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="manage-blog-content">
    <c:choose>
        <c:when test="${requestScope.action == 'addBlog'}">
            <h2>Add Blog</h2>
            <form action="${pageContext.request.contextPath}/manageBlog" method="post" enctype="multipart/form-data">
                <input type="hidden" name="action" value="addBlog">
                <label>Title:</label>
                <input type="text" name="title" required>
                <label>Description:</label>
                <textarea name="description" required></textarea>
                <label>Upload Image:</label>
                <input type="file" name="image" accept="image/*" required>
                <label>Select Tags:</label>
                <c:forEach items="${requestScope.tagList}" var="tag">
                    <label>
                        <input type="checkbox" name="tags" value="${tag.id}"> ${tag.name}
                    </label>
                </c:forEach>
                <button type="submit">Add Blog</button>
            </form>
            <c:if test="${not empty requestScope.success && requestScope.action == 'addBlog'}">
                <div style="color: green; margin-top: 10px;">
                    ${requestScope.success}
                </div>
            </c:if>
            <c:if test="${not empty requestScope.error && requestScope.action == 'addBlog'}">
                <div style="color: red; margin-top: 10px;">
                    ${requestScope.error}
                </div>
            </c:if>
        </c:when>

        <c:when test="${requestScope.action == 'deleteBlog'}">
            <h2>Delete Blog</h2>
            <form action="${pageContext.request.contextPath}/manageBlog" method="post">
                <input type="hidden" name="action" value="deleteBlog">
                <c:forEach items="${requestScope.blogList}" var="blog">
                    <label>
                        <input type="checkbox" name="blogIds" value="${blog.id}">
                        ${blog.title}
                    </label><br>
                </c:forEach>
                <button type="submit">Delete Selected Blogs</button>
            </form>
            <c:if test="${not empty requestScope.success && requestScope.action == 'deleteBlog'}">
                <div style="color: green; margin-top: 10px;">
                    ${requestScope.success}
                </div>
            </c:if>
            <c:if test="${not empty requestScope.error && requestScope.action == 'deleteBlog'}">
                <div style="color: red; margin-top: 10px;">
                    ${requestScope.error}
                </div>
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
                <button type="submit">Add Tag</button>
            </form>
            <c:if test="${not empty requestScope.success && requestScope.action == 'addTag'}">
                <div style="color: green; margin-top: 10px;">
                    ${requestScope.success}
                </div>
            </c:if>
            <c:if test="${not empty requestScope.error && requestScope.action == 'addTag'}">
                <div style="color: red; margin-top: 10px;">
                    ${requestScope.error}
                </div>
            </c:if>
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
                <button type="submit">Edit Tag</button>
            </form>
            <c:if test="${not empty requestScope.success && requestScope.action == 'editTag'}">
                <div style="color: green; margin-top: 10px;">
                    ${requestScope.success}
                </div>
            </c:if>
            <c:if test="${not empty requestScope.error && requestScope.action == 'editTag'}">
                <div style="color: red; margin-top: 10px;">
                    ${requestScope.error}
                </div>
            </c:if>
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
                <button type="submit">Delete Selected Tags</button>
            </form>
            <c:if test="${not empty requestScope.success && requestScope.action == 'deleteTag'}">
                <div style="color: green; margin-top: 10px;">
                    ${requestScope.success}
                </div>
            </c:if>
            <c:if test="${not empty requestScope.error && requestScope.action == 'deleteTag'}">
                <div style="color: red; margin-top: 10px;">
                    ${requestScope.error}
                </div>
            </c:if>
        </c:when>

        <c:otherwise>
            <p>No action selected.</p>
        </c:otherwise>
    </c:choose>
</div>