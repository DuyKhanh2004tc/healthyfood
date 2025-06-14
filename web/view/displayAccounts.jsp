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
                <div class="role-title">Users for Role: ${roleId}</div>
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
                            <th onclick="window.location.href = 'DisplayAccount?idRole=${roleId}&sortBy=id'">User ID</th>
                            <th onclick="window.location.href = 'DisplayAccount?idRole=${roleId}&sortBy=name'">Username</th>
                            <th onclick="window.location.href = 'DisplayAccount?idRole=${roleId}&sortBy=email'">Email</th>
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
                        <a href="DisplayAccount?idRole=${roleId}&page=${currentPage - 1}">Previous</a>
                    </c:if>
                    <span>Page ${currentPage} of ${totalPages}</span>
                    <c:if test="${currentPage < totalPages}">
                        <a href="DisplayAccount?idRole=${roleId}&page=${currentPage + 1}">Next</a>
                    </c:if>
                </div>
            </c:if>
            <button class="btn-back" onclick="location.href = 'HomeAdmin'">Back to Role List</button>
        </div>
    </body>
</html>