<%-- 
    Document   : changePasswordNutritionist
    Created on : Jul 1, 2025, 4:39:33 PM
    Author     : Hoa
--%>

<%-- 
    Document   : profileNutritionist
    Created on : Jun 30, 2025, 1:01:20 PM
    Author     : Hoa
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Change Password</title>
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/profile.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/changePassword.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>

        <div class="nutrition-header">
            <div class="logo">
                <img src="${pageContext.request.contextPath}/images/logo_3.png" alt="Logo">
            </div>
            <div class="menu-content-left">
                <h3>Welcome Nutritionist ${sessionScope.user.name}</h3>
                <a href="${pageContext.request.contextPath}/nutritionistHome">View Product List</a> 
                <a href="${pageContext.request.contextPath}/proposeProduct">Propose new product</a>                 
                <a href="${pageContext.request.contextPath}/nutritionBlog">Manage Blog</a>
                <a href="${pageContext.request.contextPath}/logout">Logout</a>
            </div>
        </div>

        <div class="center">
            <h1>Change Password</h1>
            <form action="changePassword" method="post">

                <div class="txt_field password-wrapper">
                    <label for="currentPassword">Current Password:</label>
                    <input type="password" id="currentPassword" name="currentpassword" placeholder="Current Password" required />
                    <i class="fa fa-eye-slash" id="toggleCurrentPassword" onclick="togglePassword('currentPassword', 'toggleCurrentPassword')"></i>
                    </div>


                    <div class="txt_field password-wrapper">
                        <label for="newPassword">New Password:</label>
                        <input type="password" id="newPassword" name="newpassword" placeholder="New Password" required />
                        <i class="fa fa-eye-slash" id="toggleNewPassword" onclick="togglePassword('newPassword', 'toggleNewPassword')"></i>                        
                    </div>

                    <div class="txt_field password-wrapper">
                        <label for="confirmPassword">Confirm Password:</label>
                        <input type="password" id="confirmPassword" name="confirmpassword" placeholder="Confirm Password" required />
                        <i class="fa fa-eye-slash" id="toggleConfirmPassword" onclick="togglePassword('confirmPassword', 'toggleConfirmPassword')"></i>
                    </div>

                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>
                <c:if test="${not empty success}">
                    <div class="success-message">${success}</div>
                </c:if>


                <input type="submit" value="Update Password" />
            </form>
        </div>
        <script>
            function togglePassword(inputId, iconId) {
                var input = document.getElementById(inputId);
                var icon = document.getElementById(iconId);

                if (input.type === "password") {
                    input.type = "text";
                    icon.className = "fa fa-eye";
                } else {
                    input.type = "password";
                    icon.className = "fa fa-eye-slash";
                }
            }
        </script>
    </body>
</html>
