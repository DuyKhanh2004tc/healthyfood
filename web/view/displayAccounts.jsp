<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Accounts for Role</title>
        <style>
            table {
                border-collapse: collapse;
                width: 80%;
                margin: 20px auto;
            }
            th, td {
                padding: 10px;
                text-align: left;
                border: 1px solid #ddd;
            }
            th {
                background-color: #f2f2f2;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            .btn {
                padding: 5px 10px;
                background-color: #2196F3;
                color: white;
                border: none;
                border-radius: 3px;
                cursor: pointer;
                text-decoration: none;
            }
            .btn:hover {
                background-color: #0b7dda;
            }
            .btn-delete {
                background-color: #f44336;
            }
            .btn-delete:hover {
                background-color: #da190b;
            }
            button {
                padding: 5px 10px;
                margin: 20px auto;
                background-color: #4CAF50;
                color: white;
                border: none;
                border-radius: 3px;
                cursor: pointer;
                display: block;
            }
            .error {
                color: red;
                text-align: center;
                margin: 20px;
            }
            .success {
                color: green;
                text-align: center;
                margin: 20px;
            }
            .pagination {
                text-align: center;
                margin: 20px;
            }
            .pagination a {
                padding: 5px 10px;
                margin: 0 5px;
                background-color: #ddd;
                text-decoration: none;
                color: #333;
                border-radius: 3px;
            }
            .pagination a:hover {
                background-color: #bbb;
            }
            .pagination .active {
                background-color: #4CAF50;
                color: white;
            }
        </style>
    </head>
    <body>
        <div>
            <form action="DisplayAccount" method="post">                   
                <input type="hidden" name="idRole" value="${roleId}">
                <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                <input type="text" name="keyword" placeholder="Search...">
            </form>
        </div>  
        <div>          
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
                            <th>User ID</th>
                            <th>Username</th>
                            <th>Email</th>
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
                                    <a href="UpdateAccount?id=${user.id}&roleId=${roleId}" class="btn">Update</a>
                                </td>
                                <td>
                                    <a href="DeleteUser?id=${user.id}&roleId=${roleId}" 
                                       class="btn btn-delete" 
                                       onclick="return confirm('Are you sure you want to delete this user?');">Delete</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
                <div>
                    <a href="AddAccount?roleId=${roleId}" class="btn">ADD</a>
                </div>
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
            <button onclick="location.href = 'HomeAdmin'">Back to Role List</button>
        </div>
    </body>
</html>