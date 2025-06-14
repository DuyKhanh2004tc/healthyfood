<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Add New Account</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="CSS/addAccount.css">
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp"></jsp:include>
        <div class="container">
            <h1>Add New Account</h1>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <c:if test="${not empty success}">
                <p class="success">${success}</p>
            </c:if>
            <form action="AddAccount" method="post">
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name">
                </div>
                <div class="form-group">
                    <label for="email">Email (required):</label>
                    <input type="email" id="email" name="email" required>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password">
                </div>
                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="text" id="phone" name="phone">
                </div>
                <div class="form-group">
                    <label for="dob">Date of Birth:</label>
                    <input type="date" id="dob" name="dob">
                </div>
                <div class="form-group">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address">
                </div>
                <div class="form-group">
                    <label for="gender">Gender:</label>
                    <select id="gender" name="gender">
                        <option value="1">Male</option>
                        <option value="0">Female</option>
                    </select>
                </div>
                <input type="hidden" name="roleId" value="${roleId}">
                <div class="button-group">
                    <button type="submit" class="btn btn-submit">
                        Add
                        <span class="tooltip">Create new account</span>
                    </button>
                    <button type="button" class="btn btn-back" onclick="location.href='DisplayAccount?idRole=${roleId}&page=1'">
                        Back to List
                        <span class="tooltip">Return to user list</span>
                    </button>
                </div>
            </form>
        </div>
    </body>
</html>