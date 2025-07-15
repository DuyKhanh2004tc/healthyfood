<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Update Account</title>
        <link rel="stylesheet" href="CSS/updateAccount.css">
    </head>
    <body>
        <jsp:include page="headerAdmin.jsp"/>
        <div class="container">
            <h2>Update Account</h2>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>
            <form action="UpdateAccount" method="post">
                <input type="hidden" name="id" value="${user.id}">

                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="${param.name != null ? param.name : user.name}">
                    <c:if test="${not empty nameError}">
                        <p class="error">${nameError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="email">Email (required):</label>
                    <input type="email" id="email" name="email" value="${param.email != null ? param.email : user.email}" required>
                    <c:if test="${not empty emailError}">
                        <p class="error">${emailError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" required>
                    <c:if test="${not empty passwordError}">
                        <p class="error">${passwordError}</p>
                    </c:if>

                    <p class="note">Optional: Leave blank to keep current password.</p>
                </div>

                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="text" id="phone" name="phone" value="${param.phone != null ? param.phone : user.phone}">
                    <c:if test="${not empty phoneError}">
                        <p class="error">${phoneError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="dob">Date of Birth:</label>
                    <input type="date" id="dob" name="dob" value="${param.dob != null ? param.dob : user.dob}">
                    <c:if test="${not empty dobError}">
                        <p class="error">${dobError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" value="${param.address != null ? param.address : user.address}">
                    <c:if test="${not empty addressError}">
                        <p class="error">${addressError}</p>
                    </c:if>
                </div>

                <div class="form-group">
                    <label for="gender">Gender:</label>
                    <select id="gender" name="gender">
                        <option value="1" <c:if test="${param.gender == '1' || (empty param.gender && user.gender)}">selected</c:if>>Male</option>
                        <option value="0" <c:if test="${param.gender == '0' || (empty param.gender && !user.gender)}">selected</c:if>>Female</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="roleId">Role:</label>
                        <select id="roleId" name="roleId">
                        <c:forEach items="${roles}" var="role">
                            <option value="${role.id}" <c:if test="${param.roleId == role.id || (empty param.roleId && user.role.id == role.id)}">selected</c:if>>
                                ${role.roleName}
                            </option>
                        </c:forEach>
                    </select>
                </div>

                <div class="button-group">
                    <button type="submit" class="btn btn-submit">Update</button>
                    <button type="button" class="btn btn-back" onclick="location.href = 'DisplayAccount?idRole=${user.role.id}&page=1'">Back to List</button>
                </div>
            </form>
        </div>
    </body>
</html>
