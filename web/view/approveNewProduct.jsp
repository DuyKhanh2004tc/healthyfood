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
                    <th>Category</br>
                        <select name="category" onchange="location.href = 'approve?categoryId=' + this.value;">
                            <option value="0">All Products</option>
                            <c:forEach items="${requestScope.categoryList}" var="o">
                                <option value="${o.id}" ${sessionScope.categoryId == o.id ? 'selected' : ''}>${o.name}</option>
                            </c:forEach>    
                        </select></th>
                    <th>Description</th>
                    <th>Reason</th>
                    <th>Created At</th>
                    <th>Proposed By Nutritionist</th>
                    <th>Status<br/>
                        <select name="status" onchange="location.href = 'approve?status=' + this.value">
                            <option value="all" ${param.status == 'all' ? 'selected' : ''}>All Status</option>
                            <option value="pending" ${param.status == 'pending' ? 'selected' : ''}>Pending</option>
                            <option value="accept" ${param.status == 'accept' ? 'selected' : ''}>Accepted</option>
                            <option value="reject" ${param.status == 'reject' ? 'selected' : ''}>Rejected</option>
                        </select>
                    </th>
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
                        <td>${i.category.name}</td>
                        <td>${i.description}</td>
                        <td>${i.reason}</td>
                        <td>${i.createdAt}</td>
                        <td>${i.nutritionist.name}</td>
                        <td>${i.status}</td>
                        <td>
                            <form class="aprove-form" method="post" action="${pageContext.request.contextPath}/approve">
                                <input type="hidden" name="proposedId" value="${i.id}">
                                <button type="submit" name="btn_status" value="accept">Accept</button>
                                <button type="submit" name="btn_status" value="reject">Reject</button>
                            </form>                              
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
        <div class="sort-btn">
            <form class="search-form" method="get" action="${pageContext.request.contextPath}/approve">
                <input type="image" src="${pageContext.request.contextPath}/icons/search_icon.png" alt="Search" width="20" height="20">
                <input type="text" name="keyword" placeholder="Search by product or nutritionist..." value="${param.keyword != null ? param.keyword : ''}">
                <input type="hidden" name="btn_sort" value="${param.btn_sort}" />
            </form>
            <form class="sort-form" method="get" action="${pageContext.request.contextPath}/approve">  
                <p>Sort By Date:</p>
                <input type="hidden" name="keyword" value="${param.keyword}" />
                <input type="hidden" name="status" value="${sessionScope.status}" />
                <input type="hidden" name="categoryId" value="${sessionScope.categoryId}" />
                <button type="submit" name="btn_sort" value="Ascending">Ascending</button>
                <button type="submit" name="btn_sort" value="Descending">Descending</button>
            </form>

        </div>


        <div class="pagination">
            <div class="pagination">
                <c:if test="${currentPage > 1}">
                    <c:url var="prevUrl" value="/approve">
                        <c:param name="page" value="${currentPage - 1}" />
                        <c:param name="btn_sort" value="${param.btn_sort}" />
                        <c:if test="${param.keyword != null}">
                            <c:param name="keyword" value="${param.keyword}" />
                        </c:if>
                        <c:if test="${sessionScope.categoryId != null}">
                            <c:param name="categoryId" value="${sessionScope.categoryId}" />
                        </c:if>
                        <c:if test="${sessionScope.status != null}">
                            <c:param name="status" value="${sessionScope.status}" />
                        </c:if>
                    </c:url>
                    <a class="page-link prev-next" href="${prevUrl}">Previous</a>
                </c:if>
                <c:forEach var="i" begin="1" end="${totalPages}">
                    <c:url var="pageUrl" value="/approve">
                        <c:param name="page" value="${i}" />
                        <c:param name="btn_sort" value="${param.btn_sort}" />
                        <c:if test="${param.keyword != null}">
                            <c:param name="keyword" value="${param.keyword}" />
                        </c:if>
                        <c:if test="${sessionScope.categoryId != null}">
                            <c:param name="categoryId" value="${sessionScope.categoryId}" />
                        </c:if>
                        <c:if test="${sessionScope.status != null}">
                            <c:param name="status" value="${sessionScope.status}" />
                        </c:if>
                    </c:url>
                    <a href="${pageUrl}" class="page-link">${i}</a>
                </c:forEach>
                <c:if test="${currentPage < totalPages}">
                    <c:url var="nextUrl" value="/approve">
                        <c:param name="page" value="${currentPage + 1}" />
                        <c:param name="btn_sort" value="${param.btn_sort}" />
                        <c:if test="${param.keyword != null}">
                            <c:param name="keyword" value="${param.keyword}" />
                        </c:if>
                        <c:if test="${sessionScope.categoryId != null}">
                            <c:param name="categoryId" value="${sessionScope.categoryId}" />
                        </c:if>
                        <c:if test="${sessionScope.status != null}">
                            <c:param name="status" value="${sessionScope.status}" />
                        </c:if>
                    </c:url>
                    <a class="page-link prev-next" href="${nextUrl}">Next</a>
                </c:if>
            </div>
        </div>

    </body>
</html>
