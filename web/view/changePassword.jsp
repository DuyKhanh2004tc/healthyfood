<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8" />
    <title>Change Password</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/profile.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/changePassword.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
</head>
<body>
    <jsp:include page="header.jsp" />

    <div class="center">
        <h1>Change Password</h1>
        <form action="" method="post">

            <div class="txt_field password-wrapper">
                <label for="oldPassword">Old Password:</label>
                <input type="password" id="oldPassword" name="oldpassword" placeholder="Old Password" required />
                <i class="fa fa-eye-slash" id="toggleOldPassword" onclick="togglePassword('oldPassword', 'toggleOldPassword')"></i>
                <span class="error-message">
                    <c:if test="${not empty errorOldPassword}">${errorOldPassword}</c:if>
                </span>
            </div>

            <div class="txt_field password-wrapper">
                <label for="newPassword">New Password:</label>
                <input type="password" id="newPassword" name="newpassword" placeholder="New Password" required />
                <i class="fa fa-eye-slash" id="toggleNewPassword" onclick="togglePassword('newPassword', 'toggleNewPassword')"></i>
                <span class="error-message">
                    <c:if test="${not empty errorNewPassword}">${errorNewPassword}</c:if>
                </span>
            </div>

            <div class="txt_field password-wrapper">
                <label for="confirmPassword">Confirm Password:</label>
                <input type="password" id="confirmPassword" name="confirmpassword" placeholder="Confirm Password" required />
                <i class="fa fa-eye-slash" id="toggleConfirmPassword" onclick="togglePassword('confirmPassword', 'toggleConfirmPassword')"></i>
                <span class="error-message">
                    <c:if test="${not empty errorConfirmPassword}">${errorConfirmPassword}</c:if>
                </span>
            </div>

            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <input type="submit" value="Update Password" />
        </form>
    </div>

    <jsp:include page="footer.jsp" />

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
