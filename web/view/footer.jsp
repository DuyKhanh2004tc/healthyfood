<%-- 
    Document   : footer
    Created on : May 29, 2025, 6:03:15 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="../CSS/home.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <footer class="footer">
            <div class="footer-container">
                <div class="footer-comment">
                    <h3>Healthy Food</h3>
                    <p>We are committed to providing fresh, safe, and nutritious food for your health.</p>
                </div>
                <div class="footer-links">
                    <h4>Links:</h4>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/home">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/nutritionBlog">Nutrition Blogs</a></li>
                        <li><a href="https://www.facebook.com/" target="_blank">Facebook</a></li>
                        <li><a href="https://www.youtube.com/ target="_blank">Youtube</a></li>
                    </ul>
                </div>
                <div class="footer-contact">
                    <h4>Contact us:</h4>
                    <p>Owner Email: duykhanh2004tcmc@gmail.com</p>
                    <p>Supporting Email: support@healthyfood.com</p>
                    <p>Phone: 0123 456 789</p>
                    <p>Address: 113 Wall St., Gotham City</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p> 2025 Healthy Food.</p>
            </div>
        </footer>
    </body>
</html>
