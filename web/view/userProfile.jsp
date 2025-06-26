<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:useBean id="user" class="model.User" scope="session" />
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>User Profile</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/register.css">
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
        <div class="center">
            <h1>User Profile</h1>
            <form action="updateProfile" method="post">
                <div class="txt_field">
                    <input type="text" name="fullname" placeholder="Fullname" value="${sessionScope.user.name}" required />
                </div>

                <div class="txt_field">
                    <input type="email" name="email" placeholder="Email" value="${sessionScope.user.getEmail()}" required />
                </div>

                <div class="txt_field">
                    <input type="text" name="phonenumber" placeholder="Phone number" value="${sessionScope.user.getPhone()}" required />
                </div>

                <div class="txt_field">
                    <label for="dateofbirth">Date of Birth:</label>
                    <input type="date" name="dateofbirth" id="dateofbirth" value="${sessionScope.user.getDob()}" required />
                </div>

                <div class="txt_field">
                    <input type="text" name="address" placeholder="Address" value="${sessionScope.user.getAddress()}    " required />
                </div>

                <div class="txt_field gender-group">
                    <label>Gender:</label>
                    <div class="radio-options">
                        <label><input type="radio" name="gender" value="0" required ${user.gender == '0' ? 'checked' : ''}/> Female</label>
                        <label><input type="radio" name="gender" value="1" ${user.gender == '1' ? 'checked' : ''} /> Male</label>
                    </div>
                </div>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>

                <input type="submit" value="Update" />
                 </form>
    </div>
    <jsp:include page="footer.jsp"></jsp:include>            
    </body>
    
</html>


