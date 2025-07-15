

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <form method="get" action="${pageContext.request.contextPath}/allRecipe">
            <input type="text" value="" name="search"/>    
            <select name="dietType">
                <option value="a">a</option>
            </select>
            <button type="submit" name="action" value="search">Search</button>
        </form>
    </body>
</html>
