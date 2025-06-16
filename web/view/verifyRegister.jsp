<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Register</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
    </head>
    <body>
        <div class="center">
            <h1>Register</h1>
            <form action="verifyRegister" method="post">
                <div class="txt_field">
                    <label for="otp" style="font-size: 16px; color: #333;">Verification Code</label>
                    <input type="text" name="otp" id="otp" maxlength="6" />
                </div>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>
                <input type="submit" value="Register">
                <div class="auth-switch">
                </div>
            </form>
            <div class="resend-otp">
                <form action="resendOTP" method="post">
                    <input type="hidden" name="type" value="register" />
                    <button type="submit" class="link-button">Resend OTP</button>
                </form>
            </div>
        </div>

    </body>
</html>