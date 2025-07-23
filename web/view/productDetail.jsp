<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.*" %>
<%@ page import="java.sql.Timestamp, java.util.*, java.text.*" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Product Detail</title>
        <meta charset="UTF-8">
        <link href="${pageContext.request.contextPath}/CSS/nutritionistHome.css" rel="stylesheet" type="text/css"/>
        <link href="${pageContext.request.contextPath}/CSS/home.css" rel="stylesheet" type="text/css"/>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background-color: #f6fff8;
                color: #2e4d32;
                margin: 0;
                padding: 0;
            }
            .container {
                max-width: 1200px;
                margin: 30px auto;
                padding: 20px;
                background-color: #ffffff;
                border-radius: 15px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.05);
            }
            .back-links {
                margin-bottom: 20px;
            }
            .back-link {
                margin-right: 10px;
                text-decoration: none;
                color: #4CAF50;
                font-weight: bold;
            }
            .back-link:hover {
                text-decoration: underline;
            }
            .nav-buttons {
                display: flex;
                gap: 10px;
                margin-bottom: 20px;
            }
            .nav-button {
                padding: 8px 15px;
                background-color: #a8e6a1;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                color: #2e4d32;
                text-decoration: none;
                display: inline-block;
                transition: background-color 0.3s ease;
            }
            .nav-button:hover {
                background-color: #7ed957;
            }
            .nav-button:disabled {
                background-color: #ccc;
                cursor: not-allowed;
                opacity: 0.5;
                pointer-events: none;
            }
            .product-details {
                display: flex;
                gap: 20px;
                flex-wrap: wrap;
            }
            .product-img {
                max-width: 400px;
                border-radius: 10px;
                box-shadow: 0 2px 6px rgba(0,0,0,0.3);
            }
            .product-details .name-rate {
                display: flex;
                align-items: center;
                gap: 10px;
                margin-top: 0;
            }
            .product-details h3 {
                margin: 0;
                color: #3e7d4d;
            }
            .product-details p {
                margin: 8px 0;
            }
            .quantity-controls {
                margin: 20px 0;
                display: flex;
                align-items: center;
                gap: 10px;
            }
            .quantity-controls input[type="text"] {
                width: 50px;
                text-align: center;
                padding: 5px;
                border: 1px solid #c1e1c1;
                border-radius: 5px;
            }
            .quantity-controls button {
                padding: 6px 12px;
                background-color: #a8e6a1;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
            }
            .quantity-controls button:hover {
                background-color: #7ed957;

            }
            button[type="submit"] {
                padding: 10px 20px;
                background-color: #4CAF50;
                color: #fff;
                font-weight: bold;
                border: none;
                border-radius: 6px;
                cursor: pointer;
                margin-right: 10px;
                justify-content: space-between; /* ⚠ chỉ có tác dụng nếu dùng flex */
                display: flex; /* ⚠ icon và text phải là 2 phần tử khác nhau */
            }

            button[type="submit"]:hover {
                background-color: #3c8d40;
            }
            .error {
                color: red;
                font-weight: bold;
            }
            .success {
                color: green;
                font-weight: bold;
            }
            .feedback-list {
                margin-top: 40px;
            }
            .feedback-card {
                background-color: #f1fff0;
                border: 1px solid #d9f5dc;
                border-radius: 10px;
                padding: 15px;
                margin-bottom: 15px;
            }
            .feedback-card h3 {
                margin: 0 0 5px 0;
                color: #4CAF50;
            }
            textarea[name="content"] {
                width: 100%;
                min-height: 100px;
                padding: 10px;
                border: 1px solid #cdeccd;
                border-radius: 8px;
                resize: vertical;
                margin-top: 10px;
                font-size: 14px;
                font-family: inherit;
            }
            .no-results {
                color: #888;
                font-style: italic;
                margin-top: 10px;
            }
            .breadcrumb-separator {
                margin: 0 8px;
                color: #6a9e6d;
                font-weight: bold;
            }
            .stars {
                display: flex;
                flex-direction: row-reverse;
                justify-content: flex-end;
            }

            .stars input {
                display: none;
            }

            .stars label {
                font-size: 30px;
                color: #ccc;
                cursor: pointer;
                transition: color 0.2s;
            }

            /* Hover */
            .stars label:hover,
            .stars label:hover ~ label {
                color: gold;
            }

            /* Checked */
            .stars input:checked ~ label {
                color: gold;
            }
            .twoButtonP{
                display: flex;
                width: 276px;
            }
            .twoButtonPopup{
                display: flex;
                
            }
            .twoButtonEdit{
                display: flex;
               
            }
            .productCart{
                width: 500px;
            }
            .overlay {
                display: none;
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background: rgba(0, 0, 0, 0.5);
                z-index: 999;
            }
            .popup {
                display: none;
                position: fixed;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                background-color: white;
                padding: 20px;
                border: 1px solid #ccc;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                z-index: 1000;
                width: 400px;
                border-radius: 10px;
            }
            .popup h3 {
                margin: 0 0 10px 0;
                color: #4CAF50;
            }
            .popup textarea {
                width: 100%;
                height: 100px;
                margin-bottom: 10px;
                padding: 10px;
                border: 1px solid #cdeccd;
                border-radius: 8px;
            }
            .popup .stars {
                margin-bottom: 10px;
            }
            .popup .button {
                padding: 8px 15px;
                background-color: #a8e6a1;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                color: #2e4d32;
            }
            .popup .button:hover {
                background-color: #7ed957;
            }
            .feedback-button {
                padding: 8px 15px;
                background-color: #a8e6a1;
                border: none;
                border-radius: 5px;
                cursor: pointer;
                font-weight: bold;
                color: #2e4d32;
                width: 80px;
                margin-right: 5px;
                text-align: center;
            }

            .feedback-button:hover {
                background-color: #7ed957;
            }


        </style>
    </head>
    <body>
        <c:if test="${sessionScope.user.getRole().getId()!=4}">
            <jsp:include page="header.jsp" />
        </c:if>
        <c:if test="${sessionScope.user.getRole().getId()==4}">

            <div class="nutrition-header">
                <div class="logo">
                    <a href="${pageContext.request.contextPath}/nutritionistHome">
                        <img src="${pageContext.request.contextPath}/images/logo_3.png" alt="Logo">
                    </a>
                </div>
                <div class="menu-content-left">
                    <h3>Welcome Nutritionist ${sessionScope.user.getName()}</h3>
                    <a href="${pageContext.request.contextPath}/nutritionistHome">View Product List</a>
                    <a href="${pageContext.request.contextPath}/updateProfile">Profile</a>
                    <a href="${pageContext.request.contextPath}/proposeProduct">Propose new product</a>                 
                    <a href="${pageContext.request.contextPath}/nutritionBlog">Manage Blog</a>
                    <a href="${pageContext.request.contextPath}/logout">Logout</a>

                </div>
            </div>
        </c:if>

        <%
            Integer productId = (Integer) request.getAttribute("productId");
            String productName = (String) request.getAttribute("productName");
            String description = (String) request.getAttribute("description");
            Integer stock = (Integer) request.getAttribute("stock");
            Double price = (Double) request.getAttribute("price");
            Double shelfLifeHours = (Double) request.getAttribute("time");
            String img = (String) request.getAttribute("img");
            String categoryName = (String) request.getAttribute("categoryName");
            Integer categoryId = (Integer) request.getAttribute("categoryId"); 
            Double rate = (Double) request.getAttribute("rate");
            Integer prevId = (Integer) request.getAttribute("prevId"); 
            Integer nextId = (Integer) request.getAttribute("nextId"); 
        %>

        <div class="container">
            <div class="back-links">
                <c:if test="${sessionScope.user.getRole().getId()!=4}">
                    <a href="${pageContext.request.contextPath}/home" class="back-link">Home</a>
                </c:if>
                <c:if test="${sessionScope.user.getRole().getId()==4}">
                    <a href="${pageContext.request.contextPath}/nutritionistHome" class="back-link">Nutritionist Home</a>
                </c:if>
                <span class="breadcrumb-separator">»</span>
                <a href="${pageContext.request.contextPath}/category?categoryId=<%= categoryId %>" class="back-link"><%= categoryName %></a>
            </div>              
            <div class="nav-buttons">
                <a href="${pageContext.request.contextPath}/productDetail?productId=<%= prevId%>" class="nav-button" <%= prevId == null ? "disabled" : "" %>>⇐ Previous</a>
                <a href="${pageContext.request.contextPath}/productDetail?productId=<%= nextId%>" class="nav-button" <%= nextId == null ? "disabled" : "" %>>Next ⇒</a>
            </div>


            <div class="product-details">
                <% if (productId != null) { %>
                <div>
                    <img src="<%= img %>" alt="<%= productName %>" class="product-img"/>
                </div>
                <div class="productCart">
                    <div class="name-rate">
                        <h3><%= productName %></h3>
                        <p><%= String.format("%.1f", rate) %> ★</p> 
                    </div>
                    <p>Category: <%= categoryName %></p>
                    <p>Price: $<%= String.format("%.2f", price) %></p>
                    <p>Stock: <%= stock %></p>
                    <p>Description: <%= description %></p>
                    <p>Shelf Life: <%= shelfLifeHours %> hours</p>


                    <%
                        String error = (String) session.getAttribute("error");
                        if (error != null) {
                    %>
                    <p class="error"><%= error %></p>
                    <% session.removeAttribute("error");  } %>
                    <%
           String message = (String) session.getAttribute("message");
           if (message != null) {
                    %>
                    <p class="success"><%= message %></p>
                    <% session.removeAttribute("message");  } %> 
                    <div class="twoButton">
                        <c:if test="${sessionScope.user.getRole().getId()== null ||sessionScope.user.getRole().getId()== 3 }">
                            <form action="productDetail" method="post">
                                <input type="hidden" name="productId" value="<%= productId%>" />
                                <div class="quantity-controls">
                                    <button type="button" onclick="updateQuantity(-1)">-</button>
                                    <input type="text" name="number" id="quantity" value="1" min="1" max="<%= stock %>">
                                    <button type="button" onclick="updateQuantity(1)">+</button>
                                </div>
                                <div class = "twoButtonP">
                                    <button class="cardP-button" type="submit" name="action" value="add">🛒 Add to Cart</button>
                                    <button class="cardP-button" type="submit" name= "action"value="buy">💰 Buy</button>
                                </div>
                            </form>

                        </c:if> 
                    </div>
                </div>
                <% } else { %>
                <p class="error">Product not found.</p>
                <% } %>
            </div>

            <div class="feedback-list">
                <%
                              String errorf = (String) session.getAttribute("errorFeedback");
                              if (errorf != null) {
                %>
                <p class="error"><%= errorf %></p>
                <% session.removeAttribute("errorf");}   %>
                <%
       String messagef = (String) session.getAttribute("messageFeedback");
       if (messagef != null) {
                %>
                <p class="success"><%= messagef %></p>
                <% session.removeAttribute("messagef");  } %> 
                <%
                    ArrayList<Feedback> feedback = (ArrayList<Feedback>) request.getAttribute("feedbackList");
                    User sessionUser = (User) session.getAttribute("user");
                    if (feedback != null && !feedback.isEmpty()) {
                        for (Feedback f : feedback) {
                %>
                <div class="feedback-card">
                    <div>
                        <h3><%= f.getUser().getName() %></h3>
                        <p><%= f.getContent() %></p>
                        <fmt:formatDate value="<%= f.getCreatedAt() %>" pattern="dd/MM/yyyy" />
                        <p><%= f.getRate() %> ★</p>
                    </div>
                    <% if (sessionUser != null && sessionUser.getId() == f.getUser().getId()) { %>
                    <div>
                        <form method="post" action="${pageContext.request.contextPath}/productDetail" >
                            <input type="hidden" name="productId" value="<%= productId %>">
                            <input type="hidden" name="feedbackId" value="<%= f.getId() %>">
                            <div class="twoButtonEdit">
                                <button type="button" class="feedback-button" onclick="openPopup(<%= f.getId() %>, '<%= f.getContent().replace("'", "\\'") %>', <%= f.getRate() %>)">Edit</button>
                                <button type="submit" class="feedback-butt</div>on" name="action" value="del</div>eteFeedback">Delete</button>
                            </div>
                        </form>
                    </div>
                    <% } %>
                </div>
                <%
                        }
                    } else {
                %>
                <p class="no-results">This product doesn't have feedback.</p>
                <% } %>
                <div class="overlay" id="overlay"></div>
                <div class="popup" id="popup">
                    <h3>Edit Feedback</h3>
                    <form id="editFeedbackForm" method="post" action="${pageContext.request.contextPath}/productDetail">
                        <input type="hidden" name="productId" value="<%= productId %>">
                        <input type="hidden" name="feedbackId" id="feedbackId">
                        <input type="hidden" name="action" value="editFeedback">
                       <div class="stars" >
                                <input type="radio" id="star5" name="rating" value="5"><label for="star5">★</label>
                                <input type="radio" id="star4" name="rating" value="4"><label for="star4">★</label>
                                <input type="radio" id="star3" name="rating" value="3"><label for="star3">★</label>
                                <input type="radio" id="star2" name="rating" value="2"><label for="star2">★</label>
                                <input type="radio" id="star1" name="rating" value="1"><label for="star1">★</label>
                            </div>

                        <textarea id="feedbackText" name="content" placeholder="Enter your feedback here..." required></textarea>
                        <div class="twoButtonPopup">
                        <button type="submit" class="button">Save</button>
                        <button type="button" class="button" onclick="closePopup()">Cancel</button>
                        </div>
                    </form>
                </div>

                <div>
                    <form method="post" action="${pageContext.request.contextPath}/productDetail">
                        <input type="hidden" name="productId" value="<%= productId %>">
                        <c:if test="${sessionScope.user.role.id == 3&&productOrdered == true}">
                           <div class="stars" >
                                <input type="radio" id="star5" name="rating" value="5"><label for="star5">★</label>
                                <input type="radio" id="star4" name="rating" value="4"><label for="star4">★</label>
                                <input type="radio" id="star3" name="rating" value="3"><label for="star3">★</label>
                                <input type="radio" id="star2" name="rating" value="2"><label for="star2">★</label>
                                <input type="radio" id="star1" name="rating" value="1"><label for="star1">★</label>
                            </div >
                            <textarea name="content" placeholder="Write your comment..." required></textarea>
                            <button type="submit" name="action" value="comment">Submit Comment</button>
                        </c:if>
                    </form>
                </div>
            </div>
        </div>

        <script>
            function updateQuantity(change) {
                let quantityInput = document.getElementById('quantity');
                let currentQuantity = parseInt(quantityInput.value);
                let newQuantity = currentQuantity + change;
                if (newQuantity >= 1 && newQuantity <= <%= stock %>) {
                    quantityInput.value = newQuantity;
                }
            }

            function openPopup(feedbackId, content, rating) {
                document.getElementById('popup').style.display = 'block';
                document.getElementById('overlay').style.display = 'block';
                document.getElementById('feedbackText').value = content;
                document.getElementById('feedbackId').value = feedbackId;
                document.getElementById('star' + rating).checked = true;

            }

            function closePopup() {
                document.getElementById('popup').style.display = 'none';
                document.getElementById('overlay').style.display = 'none';
            }

        </script>

        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>
