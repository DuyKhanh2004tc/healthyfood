<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add New Account</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                margin: 0;
                padding: 20px;
            }

            h1 {
                text-align: center;
                color: #333;
            }

            form {
                max-width: 500px;
                margin: 0 auto;
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            }

            .form-group {
                margin-bottom: 15px;
            }

            label {
                display: block;
                margin-bottom: 5px;
                font-weight: bold;
                color: #555;
            }

            input[type="text"],
            input[type="email"],
            input[type="password"],
            input[type="date"],
            select {
                width: 100%;
                padding: 8px 10px;
                border: 1px solid #ccc;
                border-radius: 4px;
                box-sizing: border-box;
            }

            .error {
                color: red;
                text-align: center;
                margin-bottom: 10px;
            }

            .success {
                color: green;
                text-align: center;
                margin-bottom: 10px;
            }

            .btn {
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                margin-right: 10px;
            }

            .btn-submit {
                background-color: #28a745;
                color: white;
            }

            .btn-submit:hover {
                background-color: #218838;
            }

            .btn-back {
                background-color: #007bff;
                color: white;
            }

            .btn-back:hover {
                background-color: #0069d9;
            }
        </style>
    </head>
    <body>
        <h1>Add New Account</h1>
        <c:if test="${not empty error}">
            <p class="error">${error}</p>
        </c:if>
        <c:if test="${not empty success}">
            <p class="success">${success}</p>
        </c:if>
        <form action="AddAccount" method="post">
            <div class="form-group">
                <label for="name">Name:</label>
                <input type="text" id="name" name="name">
            </div>
            <div class="form-group">
                <label for="email">Email (required):</label>
                <input type="email" id="email" name="email" required>
            </div>
            <div class="form-group">
                <label for="password">Password:</label>
                <input type="password" id="password" name="password">
            </div>
            <div class="form-group">
                <label for="phone">Phone:</label>
                <input type="text" id="phone" name="phone">
            </div>
            <div class="form-group">
                <label for="dob">Date of Birth:</label>
                <input type="date" id="dob" name="dob">
            </div>
            <div class="form-group">
                <label for="address">Address:</label>
                <input type="text" id="address" name="address">
            </div>
            <div class="form-group">
                <label for="gender">Gender:</label>
                <select id="gender" name="gender">
                    <option value="1">Male</option>
                    <option value="0">Female</option>
                </select>
            </div>

            <!-- Hidden input để giữ lại roleId -->
            <input type="hidden" name="roleId" value="${roleId}">

            <div>
                <button type="submit" class="btn btn-submit">ADD</button>
                <button type="button" class="btn btn-back"
                        onclick="location.href = 'DisplayAccount?idRole=${roleId}&page=1'">Back to List</button>
            </div>
        </form>
    </body>
</html>