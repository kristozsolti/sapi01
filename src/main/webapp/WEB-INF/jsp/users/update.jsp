<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<html>
<head>
    <title></title>
    <script type="text/javascript" src="/static/js/story.form.js"></script>
</head>
<body>
    <h1>Update user</h1>
    <div class="well page-content">
        <form:form action="/users/update" commandName="user" method="POST" enctype="utf8">
            <form:hidden path="id"/>
            <div id="control-group-title" class="control-group">
                <label for="user-name">Username:</label>

                <div class="controls">
                    <form:input id="user-name" path="name"/>
                    <form:errors id="error-name" path="name" cssClass="help-inline"/>
                </div>
            </div>
            <div id="control-group-bio" class="control-group">
                <label for="user-bio">Biography:</label>

                <div class="controls">
                    <form:textarea id="user-bio" path="bio"/>
                    <form:errors id="error-bio" path="bio" cssClass="help-inline"/>
                </div>
            </div>
            <div class="action-buttons">
                <a href="/users/${user.id}" class="btn"><spring:message code="label.cancel"/></a>
                <button id="update-story-button" type="submit" class="btn btn-primary">Update user</button>
            </div>
        </form:form>
    </div>
</body>
</html>