<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://jakarta.ee/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Funds Transfer - Enter Details</title>
</head>
<body>
<h1>Enter Transfer Details</h1>

<c:if test="${not empty flashScope.errorMessage}">
    <div style="color: red;">
        ${flashScope.errorMessage}
    </div>
</c:if>

<spring:hasBindErrors name="transferForm">
    <div style="color: red;">
        <ul>
            <c:forEach var="error" items="${errors.allErrors}">
                <li><spring:message message="${error}"/></li>
            </c:forEach>
        </ul>
    </div>
</spring:hasBindErrors>

<form:form modelAttribute="transferForm" action="${flowExecutionUrl}" method="post">
    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
    <div>
        <label for="fromAccount">From Account:</label>
        <form:input path="fromAccount" id="fromAccount"/>
        <form:errors path="fromAccount" cssStyle="color:red;"/>
    </div>
    <div>
        <label for="toAccount">To Account:</label>
        <form:input path="toAccount" id="toAccount"/>
        <form:errors path="toAccount" cssStyle="color:red;"/>
    </div>
    <div>
        <label for="amount">Amount:</label>
        <form:input path="amount" id="amount"/>
        <form:errors path="amount" cssStyle="color:red;"/>
    </div>
    <div>
        <button type="submit" name="_eventId_next" value="next">Next</button>
    </div>
</form:form>

<p><a href="${pageContext.request.contextPath}/">Home</a></p>
</body>
</html>
