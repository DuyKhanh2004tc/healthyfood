<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Login Form</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>
        <div class="center">
            <h1>Login</h1>
            <form action="login" method="post">
                <div class="txt_field">
                    <input type="email" name="email" placeholder="Email" required />
                </div>
                <div class="txt_field password-wrapper">
    <input type="password" id="password" name="password" placeholder="Password" required />
    <i class="fa fa-eye-slash" id="togglePassword" onclick="togglePassword()"></i>
</div>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>
                <div class="auth-switch">
                    <a href="forgotPassword">Forgot Password?</a>        
                    <input type="submit" value="Login">
                </div>
                <div class="auth-switch">
                    Don't have an account? <a href="register">Register</a>
                </div>
            </form>
            <script>
                function togglePassword() {
                    var passwordInput = document.getElementById("password");
                    var eyeIcon = document.getElementById("togglePassword");

                    if (passwordInput.type === "password") {
                        passwordInput.type = "text";
                        eyeIcon.className = "fa fa-eye"; 
                    } else {
                        passwordInput.type = "password";
                        eyeIcon.className = "fa fa-eye-slash";
                    }
                }
            </script>
    </body>
</html>
