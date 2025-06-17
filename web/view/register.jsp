<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Register Form</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/register.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
    </head>
    <body>
        <div class="center">
            <h1>Register</h1>
            <form action="register" method="post">

                <div class="txt_field">
                    <input type="text" name="fullname" placeholder="Fullname" value="${param.fullname}" required />
                </div>

                <div class="txt_field">
                    <input type="email" name="email" placeholder="Email" value="${param.email}" required />
                </div>

                <div class="txt_field">
                    <input type="text" name="phonenumber" placeholder="Phone number" value="${param.phonenumber}" required />
                </div>

                <div class="txt_field">
                    <label for="dateofbirth">Date of Birth:</label>
                    <input type="date" name="dateofbirth" id="dateofbirth" value="${param.dateofbirth}" required />
                </div>

                <div class="txt_field">
                    <input type="text" name="address" placeholder="Address" value="${param.address}" required />
                </div>

                <div class="txt_field gender-group">
                    <label>Gender:</label>
                    <div class="radio-options">
                        <label><input type="radio" name="gender" value="0" required ${param.gender == '0' ? 'checked' : ''}/> Female</label>
                        <label><input type="radio" name="gender" value="1" ${param.gender == '1' ? 'checked' : ''} /> Male</label>
                    </div>
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

                <input type="submit" value="Register" />
                <div class="auth-switch">
                    Already have an account? <a href="login">Login</a>
                </div>
                
                <div class="auth-switch">
                    <a href="home">Back to Home</a>
                </div>

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

