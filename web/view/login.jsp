<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Login Form</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
    </head>
    <body>
        <div class="center">
            <h1>Login</h1>
            <form action="login" method="post">
                <div class="txt_field">
                    <input type="email" name="email" placeholder="Email" required />
                </div>
                <div class="txt_field">
                    <input type="password" name="password" placeholder="Password" required />
                </div>
                <input type="submit" value="Login">
                <div class="auth-switch">
                    Don't have an account? <a href="register.jsp">Register</a>
                </div>
            </form>
        </div>
    </body>
</html>
