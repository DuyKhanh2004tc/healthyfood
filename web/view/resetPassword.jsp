<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Reset Password</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
    </head>
    <body>
        <div class="center">
            <h1>Reset Password</h1>
            <form action="resetPassword" method="post">
                 <div class="txt_field">
    <label for="otp" style="font-size: 16px; color: #333;">Verification Code</label>
    <input type="text" name="otp" id="otp" maxlength="6" required />
</div>
                <div class="txt_field">
                    <input type="password" name="newPassword" placeholder="New Password" required />
                </div>

                <div class="txt_field">
                    <input type="password" name="confirmPassword" placeholder="Confirm Password" required />
                </div>
                <c:if test="${not empty error}">
                            <div class="error-message">${error}</div>
                        </c:if>
                <input type="submit" value="Reset Password">
            </form>
        </div>
        
    </body>
</html>

