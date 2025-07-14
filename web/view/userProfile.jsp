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

    <div class="profile-page">

        <div class="sidebar"><div class="sidebar-title">👤 Profile</div>
            <a href="updateProfile" class="<c:if test='${pageContext.request.servletPath eq "/updateProfile"}'>active</c:if>">📄 User Profile</a>
            <a href="changePassword" class="<c:if test='${pageContext.request.servletPath eq "/changePassword"}'>active</c:if>">🔒 Change Password</a>
            <a href="orderHistory" class="<c:if test='${pageContext.request.servletPath eq "/orderHistory"}'>active</c:if>">🛒 Order History</a>
        </div>

        <div class="center">
            <h1>User Profile</h1>
            <form action="updateProfile" method="post">

                <div class="txt_field">
                    <label for="fullname">Full Name:</label>
                    <input type="text" id="fullname" name="fullname" placeholder="Full Name" value="${sessionScope.user.name}" required />
                    <span class="error-message">
                        <c:if test="${not empty errorName}">${errorName}</c:if>
                    </span>
                </div>

                <div class="txt_field">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="${sessionScope.user.email}" readonly style="background-color:#f0f0f0; cursor: not-allowed;" />
                </div>

                <div class="txt_field">
                    <label for="phonenumber">Phone Number:</label>
                    <input type="text" id="phonenumber" name="phonenumber" placeholder="Phone Number" value="${sessionScope.user.phone}" required />
                    <span class="error-message">
                        <c:if test="${not empty errorPhone}">${errorPhone}</c:if>
                    </span>
                </div>

                <div class="txt_field">
                    <label for="dateofbirth">Date of Birth:</label>
                    <input type="date" id="dateofbirth" name="dateofbirth" value="${sessionScope.user.dob}" required />
                    <span class="error-message">
                        <c:if test="${not empty errorDOB}">${errorDOB}</c:if>
                    </span>
                </div>

                <div class="txt_field">
                    <label for="address">Address:</label>
                    <input type="text" id="address" name="address" placeholder="Address" value="${sessionScope.user.address}" required />
                    <span class="error-message">
                        <c:if test="${not empty errorAddress}">${errorAddress}</c:if>
                    </span>
                </div>

                <div class="txt_field gender-group">
                    <label>Gender:</label>
                    <div class="radio-options">
                        <label>
                            <input type="radio" name="gender" value="0" required <c:if test="${!sessionScope.user.gender}">checked</c:if> /> Female
                        </label>
                        <label>
                            <input type="radio" name="gender" value="1" <c:if test="${sessionScope.user.gender}">checked</c:if> /> Male
                        </label>
                    </div>
                    <span class="error-message">
                        <c:if test="${not empty errorGender}">${errorGender}</c:if>
                    </span>
                </div>

                <c:if test="${not empty success}">
                    <div class="success-message">${success}</div>
                </c:if>
                <c:if test="${not empty error}">
                    <div class="error-message">${error}</div>
                </c:if>

                <input type="submit" value="Update" />

                <div class="auth-switch">
                    Back to <a href="home">Home</a>
                </div>
            </form>
        </div>
    </div>

    <jsp:include page="footer.jsp"></jsp:include>
</body>
</html>
