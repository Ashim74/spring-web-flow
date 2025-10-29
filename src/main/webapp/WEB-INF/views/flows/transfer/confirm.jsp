<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://jakarta.ee/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Funds Transfer - Confirm</title>
</head>
<body>
<h1>Confirm Transfer</h1>

<p><strong>From Account:</strong> ${transferForm.fromAccount}</p>
<p><strong>To Account:</strong> ${transferForm.toAccount}</p>
<p><strong>Amount:</strong> ${transferForm.amount}</p>

<form action="${flowExecutionUrl}" method="post">
    <input type="hidden" name="_flowExecutionKey" value="${flowExecutionKey}"/>
    <button type="submit" name="_eventId_confirm" value="confirm">Confirm</button>
    <button type="submit" name="_eventId_back" value="back">Back</button>
</form>
</body>
</html>
