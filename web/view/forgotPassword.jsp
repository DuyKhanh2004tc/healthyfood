<%-- 
    Document   : forgetPassword
    Created on : Jun 9, 2025, 5:13:55 PM
    Author     : Hoa
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Forgot Password</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
    </head>
    <body>
        <div class="center">
            <h1>Forgot Password</h1>
            <form action="forgotPassword" method="post">
                <div class="txt_field">
                    <input type="email" name="email" placeholder="Email" required />
                </div>
                <c:if test="${not empty error}">
                            <div class="error-message">${error}</div>
                        </c:if>
                <input type="submit" value="Reset Password">
                <div class="auth-switch">
                    Remember your password? <a href="login">Login Here</a>
                </div>
            </form>
        </div>
        
    </body>
</html>

