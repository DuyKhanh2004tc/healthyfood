<%-- 
    Document   : approveNewProduct
    Created on : Jul 14, 2025, 10:20:21 PM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="${pageContext.request.contextPath}/CSS/approveNewProduct.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <jsp:include page="header.jsp" />
        <table class="proposed-table" border="1">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Image</th>
                    <th>Name</th>
                    <th>Category Name</th>
                    <th>Description</th>
                    <th>Reason</th>
                    <th>Created At</th>
                    <th>Proposed By Nutritionist</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach items="${requestScope.proposedList}" var="i" varStatus="loop">
                    <tr>
                        <td>${loop.index + 1}</td>
                        <td>
                            <img  src="${pageContext.request.contextPath}/images/${i.image}"  width="80" alt="${i.name}">
                        </td>
                        <td>${i.name}</td>
                        <td>${i.categoryName}</td>
                        <td>${i.description}</td>
                        <td>${i.reason}</td>
                        <td>${i.createdAt}</td>
                        <td>${i.nutritionist.name}</td>
                        <td>${i.status}</td>
                        <td>
                            <form class="aprove-form" method="post" action="${pageContext.request.contextPath}/approve">
                                <input type="hidden" name="proposedId" value="${i.id}">
                                <button type="submit" name="btn_status" value="accept">Accept</button>
                                <button type="submit" name="btn_status" value="cancel">Cancel</button>
                            </form>                              
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="sort-btn">
            <form class="sort-form" method="get" action="${pageContext.request.contextPath}/approve">  
                <p>Sort By Date:</p>
                <button type="submit" name="btn_sort" value="Ascending">Ascending</button>
                <button type="submit" name="btn_sort" value="Descending">Descending</button>
            </form>
        </div>

        <div class="pagination">
            <c:if test="${currentPage > 1}">
                <c:url var="prevUrl" value="/approveNewProduct">
                    <c:param name="index" value="${currentPage - 1}" />                           
                </c:url>
                <a class="page-link prev-next" href="${prevUrl}">Previous</a>
            </c:if>
            <c:forEach var="i" begin="1" end="${totalPages}">
                <a href="${pageContext.request.contextPath}/approve?page=${i}&btn_sort=${param.btn_sort}" class="page-link">
                    ${i}
                </a>
            </c:forEach>
            <c:if test="${currentPage < totalPage}">
                <c:url var="nextUrl" value="/approveNewProduct">
                    <c:param name="index" value="${currentPage + 1}" />                  
                </c:url>
                <a class="page-link prev-next" href="${nextUrl}">Next</a>
            </c:if>
        </div>

    </body>
</html>
