<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/user.view.js"></script>
</head>
<body>
    <div id="user-id" class="hidden">${user.id}</div>
    <h1>User</h1>
    <div class="well page-content">
        <h2 id="story-title"><c:out value="${user.name}"/></h2>
        <div>
            <p><c:out value="${user.bio}"/></p>
        </div>
        <div class="action-buttons">
            <a href="/users/update/${user.id}" class="btn btn-primary">Update user</a>
            <a id="delete-story-link" class="btn btn-primary">Delete user</a>
        </div>
    </div>
    <script id="template-delete-story-confirmation-dialog" type="text/x-handlebars-template">
        <div id="delete-story-confirmation-dialog" class="modal">
            <div class="modal-header">
                <button class="close" data-dismiss="modal">×</button>
                <h3><spring:message code="label.story.delete.dialog.title"/></h3>
            </div>
            <div class="modal-body">
                <p><spring:message code="label.story.delete.dialog.message"/></p>
            </div>
            <div class="modal-footer">
                <a id="cancel-story-button" href="#" class="btn"><spring:message code="label.cancel"/></a>
                <a id="delete-story-button" href="#" class="btn btn-primary"><spring:message code="label.delete.story.button"/></a>
            </div>
        </div>
    </script>
</body>
</html>