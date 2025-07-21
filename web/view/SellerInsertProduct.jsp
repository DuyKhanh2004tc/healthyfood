<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>Insert Product</title>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/login.css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/register.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.0/css/all.min.css">
        <style>
            body {
                background-color: #f0f2f5;
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                margin: 0;
                padding: 0;
                
            }
            
            .main-content {
                margin: 80px 20px 10px 20px; 
                padding: 20px;
                width: 90%;
            }
            .card {
                width: 70%;
                max-width: 1000px;
                margin: 40px auto;
                padding: 30px;
                background: white;
                border-radius: 15px;
                border: solid 1px;
                box-shadow: 0 8px 16px rgba(0,0,0,0.1);
            }
            .card h1 {
                text-align: center;
                color: #333;
                margin-bottom: 30px;
                font-size: 24px;
            }
            .txt_field {
                position: relative;
                margin-bottom: 25px;
            }
            .txt_field label {
                display: block;
                margin-bottom: 8px;
                font-weight: 500;
                color: #555;
            }
            .txt_field span {
                color: #dc3545;
            }
            .txt_field input, .txt_field textarea, .txt_field select {
                width: 100%;
                padding: 12px;
                border: 1px solid #ccc;
                border-radius: 8px;
                font-size: 16px;
                outline: none;
                transition: border-color 0.3s;
            }
            .txt_field input:focus, .txt_field textarea:focus, .txt_field select:focus {
                border-color: #28a745;
            }
            .txt_field textarea {
                resize: vertical;
                min-height: 100px;
            }
            .error-message {
                color: #dc3545;
                font-size: 14px;
                margin-top: 5px;
                display: block;
            }
            .btn-submit {
                width: 100%;
                padding: 12px;
                margin-top: 10px;
                border: none;
                border-radius: 8px;
                background: #28a745;
                color: white;
                font-size: 16px;
                cursor: pointer;
                transition: background 0.3s;
            }
            .btn-submit:hover {
                background: #218838;
               
            }
            .btn-cancel {
                width: 100%;
                padding: 12px;
                margin-top: 10px;
                border: none;
                border-radius: 8px;
                background: #6c757d;
                color: white;
                font-size: 16px;
                cursor: pointer;
                text-decoration: none;
                display: inline-block;
                text-align: center;
                transition: background 0.3s;
            }
            .btn-cancel:hover {
                background: #5a6268;
            }
            
            
            
            .notification-bar {
                position: fixed;
                top: 20px;
                left: 50%;
                transform: translateX(-50%);
                background-color: #dc3545;
                color: white;
                padding: 12px 20px;
                border-radius: 8px;
                box-shadow: 0 4px 12px rgba(0,0,0,0.2);
                z-index: 1000;
                display: none;
                max-width: 500px;
                text-align: center;
            }
            .notification-bar.show {
                display: block;
            }
            
             .button-group {
         display: flex;
    justify-content: space-between;
    gap: 20px;
    margin-top: 30px;
    }
        </style>
    </head>
    <body>
 
        <div class="notification-bar" id="notificationBar">
            <span id="notificationMessage">${errorMessage}</span>
        </div>
        <div class="main-content">
            <div class="card">
                <h1>Insert Product</h1>
                <form action="${pageContext.request.contextPath}/insertProduct?service=insert" method="post" enctype="multipart/form-data">
                    <input type="hidden" name="service" value="insert"/>
                    <!-- Product Name -->
                    <div class="txt_field">
                        <label for="name">Product Name <span>*</span></label>
                        <input type="text" id="name" name="name" placeholder="Enter product name" value="${product.name}" maxlength="255" required>
                        <c:if test="${not empty nameError}">
                            <span class="error-message">${nameError}</span>
                        </c:if>
                    </div>

                    <!-- Description -->
                    <div class="txt_field">
                        <label for="description">Description <span>*</span></label>
                        <textarea id="description" name="description" placeholder="Enter product description" required>${product.description}</textarea>
                        <c:if test="${not empty descriptionError}">
                            <span class="error-message">${descriptionError}</span>
                        </c:if>
                    </div>

                    <!-- Price -->
                    <div class="txt_field">
                        <label for="price">Price (USD) <span>*</span></label>
                        <input type="number" step="0.01" min="0.01" id="price" name="price" placeholder="Enter price" value="${product.price > 0 ? product.price : ''}" required>
                        <c:if test="${not empty priceError}">
                            <span class="error-message">${priceError}</span>
                        </c:if>
                    </div>

                    <!-- Stock -->
                    <div class="txt_field">
                        <label for="stock">Stock <span>*</span></label>
                        <input type="number" min="0" id="stock" name="stock" placeholder="Enter stock quantity" value="${product.stock > 0 ? product.stock : ''}" required>
                        <c:if test="${not empty stockError}">
                            <span class="error-message">${stockError}</span>
                        </c:if>
                    </div>

                    <!-- Image -->
                    <div class="txt_field">
                        <label for="imageFile">Product Image <span>*</span></label>
                        <input type="file" id="imageFile" name="imageFile" accept="image/jpeg,image/png" required>
                        <c:if test="${not empty imageError}">
                            <span class="error-message">${imageError}</span>
                        </c:if>
                    </div>

                    <!-- Shelf Life -->
                    <div class="txt_field">
                        <label for="shelfLifeHours">Shelf Life (hours) <span>*</span></label>
                        <input type="number" step="0.1" min="0" id="shelfLifeHours" name="shelfLifeHours" placeholder="Enter shelf life" value="${product.shelfLifeHours > 0 ? product.shelfLifeHours : ''}" required>
                        <c:if test="${not empty shelfLifeError}">
                            <span class="error-message">${shelfLifeError}</span>
                        </c:if>
                    </div>

                    <!-- Category -->
                    <div class="txt_field">
                        <label for="categoryId">Category <span>*</span></label>
                        <select id="categoryId" name="categoryId" required>
                            <option value="" disabled <c:if test="${empty product.category.id}">selected</c:if>>Select a category</option>
                            <c:forEach var="category" items="${categories}">
                                <option value="${category.id}" <c:if test="${category.id == product.category.id}">selected</c:if>>${category.name}</option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty categoryError}">
                            <span class="error-message">${categoryError}</span>
                        </c:if>
                    </div>

                    <!-- Submit and Cancel -->
                    <div class="button-group">
                    <button type="submit" class="btn-submit">Insert Product</button>
                    <a href="${pageContext.request.contextPath}/seller?service=list" class="btn-cancel">Cancel</a>
                            </div>
                </form>
            </div>
        </div>
        <script>
            window.onload = function() {
                const notificationMessage = document.getElementById('notificationMessage').innerText;
                if (notificationMessage && notificationMessage.trim() !== '') {
                    const notificationBar = document.getElementById('notificationBar');
                    notificationBar.classList.add('show');
                    setTimeout(() => {
                        notificationBar.classList.remove('show');
                    }, 5000);
                }
            };
        </script>
        
    </body>
    
</html>