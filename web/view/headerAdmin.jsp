<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<div class="headeradmin">
    <a href="HomeAdmin">Admin Dashboard</a>
    <div class="user-info">
        <span>Welcome, <c:out value="${sessionScope.user.name}" /></span>
        <a href="logout">Logout</a>
    </div>
</div>