<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>Register Form</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/register.css">
    </head>
    <body>
        <div class="center">
            <h1>Register</h1>
            <form action="register" method="post">

                <div class="txt_field">
                    <input type="text" name="fullname" placeholder="Fullname" required />
                </div>

                <div class="txt_field">
                    <input type="email" name="email" placeholder="Email" required />
                </div>

                <div class="txt_field">
                    <input type="text" name="phonenumber" placeholder="Phone number" pattern="(0|\+84)[0-9]{9}" required />
                </div>
                
                <div class="txt_field">
                    <label for="dateofbirth">Date of Birth:</label>
                    <input type="date" name="dateofbirth" id="dateofbirth" required />
                </div>

                <div class="txt_field">
                    <input type="text" name="address" placeholder="Address" required />
                </div>

                <div class="txt_field gender-group">
                    <label>Gender:</label>
                    <div class="radio-options">
                        <label><input type="radio" name="gender" value="0" required /> Female</label>
                        <label><input type="radio" name="gender" value="1" /> Male</label>
                    </div>
                </div>

                <div class="txt_field">
                    <input type="password" name="password" placeholder="Password" required />
                </div>

                <div class="txt_field">
                    <input type="password" name="confirmpassword" placeholder="Confirm Password" required />
                </div>
                
                <c:if test="${not empty error}">
                            <div class="error-message">${error}</div>
                        </c:if>
                
                <input type="submit" value="Register" />
                <div class="auth-switch">
                    Already have an account? <a href="login.jsp ">Login</a>
                </div>

            </form>
        </div>
    </body>
</html>

