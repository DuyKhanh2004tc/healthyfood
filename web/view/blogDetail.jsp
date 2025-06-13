<%-- 
    Document   : blogDetail
    Created on : Jun 13, 2025, 12:53:42 PM
    Author     : HP
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="java.sql.Timestamp" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head
    <%
      Integer blogId = (Integer) request.getAttribute("blogId");
            String title = (String) request.getAttribute("title");
            String description = (String) request.getAttribute("description");
           Timestamp created_at = (Timestamp) request.getAttribute("created_at");
            String image = (String) request.getAttribute("image");
            String createBy = (String) request.getAttribute("createBy");
             Integer size = (Integer) request.getAttribute("size");
    %>
    <jsp:include page="header.jsp"></jsp:include>
        <body>

            <form action="${pageContext.request.contextPath}/blogDetail" method="get">
            <input type="hidden" name="blogId" value="<%= blogId %>">
            <% if(blogId ==1 ){%>
            <a href="${pageContext.request.contextPath}/blogDetail?blogId=<%= size%>">
                <p><=</p>
            </a>
            <%}else{%>
            <a href="${pageContext.request.contextPath}/blogDetail?blogId=<%= blogId-1%>">
                <p><=</p>
            </a>
            <%}%>
             <% if(blogId ==size ){%>
            <a href="${pageContext.request.contextPath}/blogDetail?blogId=1">
                <p>=></p>
            </a>
            <%}else{%>
            <a href="${pageContext.request.contextPath}/blogDetail?blogId=<%= blogId+1%>">
                <p>=></p>
            </a>
            <%}%>
            <div>
                <a href="${pageContext.request.contextPath}/blogDetail?blogId=<%= blogId%>">
                    <img class="card-img" src="<%= image%>" alt="Blog Image">
                </a>

                <h3><%= title%></h3

                       </div>
            <p><%= created_at%></p>
            <p><%= createBy%></p>
            <p><%= description%></p>

        </form>
    </body>
    <jsp:include page="footer.jsp"></jsp:include>
</html>
