<%-- 
    Document   : bmi
    Created on : May 29, 2025
    Author     : ASUS
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>BMI Calculator</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/CSS/bmi.css">
    </head>
    <body>
        <jsp:include page="header.jsp"></jsp:include>
        
        <div class="bmi-container">
            <div class="bmi-form">
                <h2>BMI Calculator</h2>
                <form action="bmi" method="post">
                    <label for="height">Height (cm):</label>
                    <input type="number" name="height" id="height" step="0.1" min="1" required placeholder="e.g., 170">

                    <label for="weight">Weight (kg):</label>
                    <input type="number" name="weight" id="weight" step="0.1" min="1" required placeholder="e.g., 60">

                    <label for="goal">Select your goal:</label>
                    <select name="goal" id="goal">
                        <option value="">-- Select your goal --</option>
                        <c:forEach var="tag" items="${goal}">
                            <option value="${tag.slug}" <c:if test="${param.goal == tag.slug}">selected</c:if>>${tag.name}</option>
                        </c:forEach>
                    </select>

                    <button type="submit">Calculate BMI</button>
                </form>
            </div>

            <c:if test="${not empty bmi}">
                <div class="result">
                    <h3>Your BMI Result</h3>
                    <p>Your BMI is: <strong>${bmi}</strong></p>
                    <p>Category: <strong>${category}</strong></p>
                    <c:if test="${not empty bmiTagSlug}">
                        <p>View related articles for your BMI: 
                            <a href="nutritionBlog?tag=<c:out value="${bmiTagSlug}"/>">${bmiTagName}</a></p>
                    </c:if>
                    <c:if test="${not empty param.goal}">
                        <p>Your Goal: <strong>${goalName}</strong></p>
                        <p>View related articles for your goal: 
                            <a href="nutritionBlog?tag=<c:out value="${param.goal}"/>">${goalName}</a></p>
                    </c:if>
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <p class="error">${error}</p>
            </c:if>

            <div class="bmi-table">
                <h3>BMI Categories</h3>
                <table>
                    <thead>
                        <tr>
                            <th>BMI Range</th>
                            <th>Category</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>Below 18.5</td>
                            <td>Underweight</td>
                        </tr>
                        <tr>
                            <td>18.5 - 24.9</td>
                            <td>Normal</td>
                        </tr>
                        <tr>
                            <td>25 - 29.9</td>
                            <td>Overweight</td>
                        </tr>
                        <tr>
                            <td>30 and above</td>
                            <td>Obese</td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>

        <jsp:include page="footer.jsp"></jsp:include>
    </body>
</html>