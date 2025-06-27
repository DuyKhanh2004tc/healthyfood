<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <title>User Profile</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/profile.css">
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
            <div class="center">
                <h1>User Profile</h1>
                <form action="updateProfile" method="post">
                    <div class="txt_field">
                        Full Name:<input type="text" name="fullname" placeholder="Fullname" value="${sessionScope.user.name}" required />
                </div>

                <div class="txt_field">
                    Email:
                    <input type="email" name="email" value="${sessionScope.user.email}" readonly style="background-color:#f0f0f0; cursor: not-allowed;" />
                </div>

                <div class="txt_field">
                    Phone number:<input type="text" name="phonenumber" placeholder="Phone number" value="${sessionScope.user.phone}" required />
                </div>
                <c:if test="${not empty errorPhone}">
                    <div class="error-message">${errorPhone}</div>
                </c:if>


                <div class="txt_field">
                    <label for="dateofbirth">Date of Birth:</label>
                    <input type="date" name="dateofbirth" id="dateofbirth" value="${sessionScope.user.dob}" required />
                </div>
                <c:if test="${not empty errorDOB}">
                    <div class="error-message">${errorDOB}</div>
                </c:if>


                <div class="txt_field">
                    Address:<input type="text" name="address" placeholder="Address" value="${sessionScope.user.address}    " required />
                </div>

                <div class="txt_field gender-group">
                    <label>Gender:</label>
                    <div class="radio-options">
                        <label><input type="radio" name="gender" value="0" required <c:if test="${!sessionScope.user.gender}">checked</c:if> /> Female</label>
                        <label><input type="radio" name="gender" value="1" <c:if test="${sessionScope.user.gender}">checked</c:if> /> Male</label>
                        </div>
                    </div>
                <c:if test="${not empty success}">
                    <div class="success-message">${success}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>

                <input type="submit" value="Update" />
            </form>
        </div>
        <jsp:include page="footer.jsp"></jsp:include>            
    </body>

</html>


