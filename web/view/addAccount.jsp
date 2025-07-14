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
            <form action="AddAccount" method="post" novalidate>
                <div class="form-group">
                    <label for="name">Name:</label>
                    <input type="text" id="name" name="name" value="${param.name}" required>
                    <c:if test="${not empty nameError}">
                        <p class="error">${nameError}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="email">Email (required):</label>
                    <input type="email" id="email" name="email" value="${param.email}" required>
                    <c:if test="${not empty emailError}">
                        <p class="error">${emailError}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="password">Password:</label>
                    <input type="password" id="password" name="password" value="${param.password}" required minlength="6">
                    <c:if test="${not empty passwordError}">
                        <p class="error">${passwordError}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="phone">Phone:</label>
                    <input type="tel" id="phone" name="phone" value="${param.phone}" pattern="[0-9]{10}" placeholder="10 digits">
                    <c:if test="${not empty phoneError}">
                        <p class="error">${phoneError}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="dob">Date of Birth:</label>
                    <input type="date" id="dob" name="dob" value="${param.dob}" required>
                    <c:if test="${not empty dobError}">
                        <p class="error">${dobError}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" value="${param.address}" required>
                    <c:if test="${not empty addressError}">
                        <p class="error">${addressError}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="gender">Gender:</label>
                    <select id="gender" name="gender" required>
                        <option value="">Select Gender</option>
                        <option value="1" ${param.gender == '1' ? 'selected' : ''}>Male</option>
                        <option value="0" ${param.gender == '0' ? 'selected' : ''}>Female</option>
                    </select>
                    <c:if test="${not empty genderError}">
                        <p class="error">${genderError}</p>
                    </c:if>
                </div>
                <div class="form-group">
                    <label for="roleId">Role:</label>
                    <input type="hidden" name="roleId" value="${roleId}">
                    <p>${roles.stream().filter(r -> r.id == roleId).findFirst().get().roleName}</p> <!-- Hiển thị roleName -->
                </div>
                <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}"> <!-- Thêm CSRF token nếu có -->
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