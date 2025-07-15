<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Accounts for Role</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />      
        <link rel="stylesheet" href="CSS/displayAccount.css">
        <script>
            function showLoading() {
                document.querySelector('.loading').style.display = 'block';
                document.querySelector('.container').style.opacity = '0.5';
            }
        </script>
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp"></jsp:include>
        <div class="container">
            <div class="loading"><i class="fas fa-spinner"></i> Loading...</div>
            <div class="role-title">Users for Role: ${roleName}</div>
            <div class="header">
                <form action="DisplayAccount" method="post" class="search-form" onsubmit="showLoading()">                   
                    <input type="hidden" name="idRole" value="${roleId}">
                    <i class="fas fa-search"></i>
                    <input type="text" name="keyword" placeholder="Search users...">
                </form>
                <a href="AddAccount?roleId=${roleId}" class="btn btn-primary">
                    Add New User
                    <span class="tooltip">Create a new user</span>
                </a>
            </div>  
            <c:if test="${not empty success}">
                <p class="success">${success}</p>
            </c:if>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <c:if test="${empty uList}">
                <p class="error">No users found for this role.</p>
            </c:if>
            <c:if test="${not empty uList}">
                <table>
                    <thead>
                        <tr>
                            <th>
                                <a href="DisplayAccount?idRole=${roleId}&sortBy=id&sortOrder=${sortBy == 'id' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}">
                                    User ID
                                    <c:if test="${sortBy == 'id'}">
                                        <c:choose>
                                            <c:when test="${sortOrder == 'asc'}"><i class="fas fa-sort-up"></i></c:when>
                                            <c:otherwise><i class="fas fa-sort-down"></i></c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </a>
                            </th>
                            <th>
                                <a href="DisplayAccount?idRole=${roleId}&sortBy=name&sortOrder=${sortBy == 'name' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}">
                                    Username
                                    <c:if test="${sortBy == 'name'}">
                                        <c:choose>
                                            <c:when test="${sortOrder == 'asc'}"><i class="fas fa-sort-up"></i></c:when>
                                            <c:otherwise><i class="fas fa-sort-down"></i></c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </a>
                            </th>
                            <th>
                                <a href="DisplayAccount?idRole=${roleId}&sortBy=email&sortOrder=${sortBy == 'email' && sortOrder == 'asc' ? 'desc' : 'asc'}&page=${currentPage}">
                                    Email
                                    <c:if test="${sortBy == 'email'}">
                                        <c:choose>
                                            <c:when test="${sortOrder == 'asc'}"><i class="fas fa-sort-up"></i></c:when>
                                            <c:otherwise><i class="fas fa-sort-down"></i></c:otherwise>
                                        </c:choose>
                                    </c:if>
                                </a>
                            </th>
                            <th>Update</th>
                            <th>Delete</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${uList}" var="user">
                            <tr>
                                <td>${user.id}</td>
                                <td>${user.name}</td>
                                <td>${user.email}</td>
                                <td>
                                    <a href="UpdateAccount?id=${user.id}&roleId=${roleId}" class="btn btn-primary">
                                        Update
                                        <span class="tooltip">Edit user details</span>
                                    </a>
                                </td>
                                <td>
                                    <a href="DeleteUser?id=${user.id}&roleId=${roleId}" 
                                       class="btn btn-delete" 
                                       onclick="return confirm('Are you sure you want to delete this user?');">
                                        Delete
                                        <span class="tooltip">Remove user</span>
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div class="pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="DisplayAccount?idRole=${roleId}&page=${currentPage - 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">Previous</a>
                    </c:if>
                    <c:forEach begin="1" end="${totalPages}" var="page">
                        <c:choose>
                            <c:when test="${page == currentPage}">
                                <span class="current-page">${page}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="DisplayAccount?idRole=${roleId}&page=${page}&sortBy=${sortBy}&sortOrder=${sortOrder}">${page}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                    <c:if test="${currentPage < totalPages}">
                        <a href="DisplayAccount?idRole=${roleId}&page=${currentPage + 1}&sortBy=${sortBy}&sortOrder=${sortOrder}">Next</a>
                    </c:if>
                </div>
            </c:if>
            <button class="btn-back" onclick="location.href = 'HomeAdmin'">Back to Role List</button>
        </div>
    </body>
</html>