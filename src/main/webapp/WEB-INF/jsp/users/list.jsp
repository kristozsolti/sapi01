<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
</head>
<body>
    <h1>Users list</h1>
    <div>
        <a href="/users/add" id="add-button" class="btn btn-primary">Add user</a>
    </div>
    <div id="users-list" class="page-content">
        <c:choose>
            <c:when test="${empty users}">
                <p>No users found.</p>
            </c:when>
            <c:otherwise>
                <c:forEach items="${ users}" var="user">
                    <div class="well well-small">
                        <a href="/user/${user.id}"><c:out value="${user.name}"/></a>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>