<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Reset Password</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>
        <div class="center">
            <h1>Reset Password</h1>
            <form action="resetPassword" method="post">
                 <div class="txt_field">
    <label for="otp" style="font-size: 16px; color: #333;">Verification Code</label>
    <input type="text" name="otp" id="otp" maxlength="6" required />
</div>
                 <div class="txt_field password-wrapper">
                    <input type="password" id="password" name="password" placeholder="Password" required />
                    <i class="fa fa-eye-slash" id="togglePassword" onclick="togglePassword('password', 'togglePassword')"></i>
                </div>
                <div class="txt_field password-wrapper">
                    <input type="password" id="confirmPassword" name="confirmpassword" placeholder="Confirm Password" required />
                    <i class="fa fa-eye-slash" id="toggleConfirmPassword" onclick="togglePassword('confirmPassword', 'toggleConfirmPassword')"></i>
                </div>   
                <c:if test="${not empty error}">
                            <div class="error-message">${error}</div>
                        </c:if>
                <input type="submit" value="Reset Password">
            </form>
                        
                        <div style="text-align: center; margin-top: 10px;">
                <form action="resendOTP" method="post" style="display: inline;">
                    <input type="hidden" name="type" value="reset" /> 
                    <button type="submit" class="link-button">Resend OTP</button>
                </form>
            </div>
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

