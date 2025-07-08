<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>BMI Calculator</title>
    <link rel="stylesheet" href="css/bmi.css">
</head>
<body>

    <h2>BMI Calculator</h2>

    <form action="bmi" method="post">
        <label for="height">Height (cm):</label>
        <input type="number" name="height" id="height" step="0.1" min="1" required placeholder="e.g., 170">

        <label for="weight">Weight (kg):</label>
        <input type="number" name="weight" id="weight" step="0.1" min="1" required placeholder="e.g., 60">

        <label for="goal">Select your goal:</label>
        <select name="goal" id="goal" required>
            <option value="">-- Select your goal --</option>
            <c:forEach var="tag" items="${tags}">
                <option value="${tag.slug}" <c:if test="${tag.slug == goal}">selected</c:if>>${tag.name}</option>
            </c:forEach>
        </select>

        <button type="submit">Calculate BMI</button>
    </form>

    <c:if test="${not empty bmi}">
        <div class="result">
            <h3>Your BMI Result</h3>
            <p>Your BMI is: <strong>${bmi}</strong></p>
            <p>Category: <strong>${category}</strong></p>
            <p>Your goal: <strong>${goalName}</strong></p>
            <p>Suggestion: ${suggestion}</p>
            <a href="blog.jsp?tag=${goal}">View related articles for your goal</a>
        </div>
    </c:if>

    <c:if test="${not empty error}">
        <p class="error">${error}</p>
    </c:if>

</body>
</html>
