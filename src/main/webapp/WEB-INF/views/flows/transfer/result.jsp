<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://jakarta.ee/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Funds Transfer - Result</title>
</head>
<body>
<h1>Transfer Result</h1>

<c:choose>
    <c:when test="${not empty flashScope.successMessage}">
        <p style="color: green;">${flashScope.successMessage}</p>
    </c:when>
    <c:otherwise>
        <p>Transfer completed successfully.</p>
    </c:otherwise>
</c:choose>

<form action="${flowExecutionUrl}" method="post">
    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
    <button type="submit" name="_eventId_finish" value="finish">Finish</button>
</form>
</body>
</html>
