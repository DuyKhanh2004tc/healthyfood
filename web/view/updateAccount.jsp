<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Update Account</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" integrity="sha512-z3gLpd7yknf1YoNbCzqRKc4qyor8gaKU1qmn+CShxbuBusANI9QpRohGBreCFkKxLhei6S9CQXFEbbKuqLg0DA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
        <link rel="stylesheet" href="CSS/updateAccount.css">
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp"></jsp:include>

            <div class="container">
                <h2>Update Account</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="UpdateAccount" method="post">
                <input type="hidden" name="id" value="${user.id}">
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="${user.name}">
                    <p class="note">Optional: Leave blank if no change needed.</p>
                </div>
                <div class="form-group">
                    <label for="email">Email (required):</label>
                    <input type="email" id="email" name="email" value="${user.email}" required>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" value="${user.password}">
                    <p class="note">Optional: Leave blank to keep current password.</p>
                </div>
                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="text" id="phone" name="phone" value="${user.phone}">
                    <p class="note">Optional: Enter a valid phone number.</p>
                </div>
                <div class="form-group">
                    <label for="dob">Date of Birth:</label>
                    <input type="date" id="dob" name="dob" value="${user.dob != null ? user.dob : ''}">
                    <p class="note">Optional: Format YYYY-MM-DD.</p>
                </div>
                <div class="form-group">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" value="${user.address}">
                    <p class="note">Optional: Leave blank if no change needed.</p>
                </div>
                <div class="form-group">
                    <label for="gender">Gender:</label>
                    <select id="gender" name="gender">
                        <option value="1" ${user.gender ? 'selected' : ''}>Male</option>
                        <option value="0" ${user.gender ? '' : 'selected'}>Female</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="roleId">Role:</label>
                    <select id="roleId" name="roleId">
                        <c:forEach items="${roles}" var="role">
                            <option value="${role.id}" ${user.role.id == role.id ? 'selected' : ''}>${role.roleName}</option>
                        </c:forEach>
                    </select>
                </div>
                <div class="button-group">
                    <button type="submit" class="btn btn-submit">
                        Update
                        <span class="tooltip">Save changes</span>
                    </button>
                    <button type="button" class="btn btn-back" onclick="location.href = 'DisplayAccount?idRole=${user.role.id}&page=1'">
                        Back to List
                        <span class="tooltip">Return to user list</span>
                    </button>
                </div>
            </form>
        </div>
    </body>
</html>