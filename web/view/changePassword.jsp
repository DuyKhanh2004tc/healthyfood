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
    <c:if test="${sessionScope.user.getRole().getId()==2||sessionScope.user.getRole().getId()==3||sessionScope.user.getRole().getId()==5}">
    <jsp:include page="header.jsp"></jsp:include>
    </c:if>

<div class="profile-page">
    
        <c:if test="${sessionScope.user.getRole().getId()==2||sessionScope.user.getRole().getId()==3||sessionScope.user.getRole().getId()==5}">
        <div class="sidebar"><div class="sidebar-title">👤 Profile</div>
            <a href="updateProfile" class="<c:if test='${pageContext.request.servletPath eq "/updateProfile"}'>active</c:if>">📄 User Profile</a>
            <a href="changePassword" class="<c:if test='${pageContext.request.servletPath eq "/changePassword"}'>active</c:if>">🔒 Change Password</a>
            <a href="orderHistory" class="<c:if test='${pageContext.request.servletPath eq "/orderHistory"}'>active</c:if>">🛒 Order History</a>
        </div>
        </c:if>
        
        <c:if test="${sessionScope.user.getRole().getId()==6}">
        <div class="sidebar"><div class="sidebar-title">👤 Profile</div>
            <a href="updateProfile" class="<c:if test='${pageContext.request.servletPath eq "/updateProfile"}'>active</c:if>">📄 User Profile</a>
            <a href="changePassword" class="<c:if test='${pageContext.request.servletPath eq "/changePassword"}'>active</c:if>">🔒 Change Password</a>
        </div>
        </c:if>
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

            <c:if test="${not empty success}">
                <div class="success-message">${success}</div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="error-message">${error}</div>
            </c:if>

            <input type="submit" value="Update Password" />
            <c:if test="${sessionScope.user.getRole().getId()==2||sessionScope.user.getRole().getId()==3||sessionScope.user.getRole().getId()==5}">
                <div class="auth-switch">
                    Back to <a href="home">Home</a>
                </div>
                </c:if>
                <c:if test="${sessionScope.user.getRole().getId()==6}">
                <div class="auth-switch">
                    Back to <a href="HomeShipper">Home</a>
                </div>
                </c:if>
        </form>
    </div>
</div>
    
    <c:if test="${sessionScope.user.getRole().getId()==2||sessionScope.user.getRole().getId()==3||sessionScope.user.getRole().getId()==5}">               
    <jsp:include page="footer.jsp"></jsp:include>
    </c:if>

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
