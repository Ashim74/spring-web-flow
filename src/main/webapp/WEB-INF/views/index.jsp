<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Spring Web Flow JSP Demo</title>
</head>
<body>
<h1>Spring Web Flow JSP Demo</h1>
<p>This demo showcases a simple three-step funds transfer flow powered by Spring Web Flow.</p>
<p><a href="${pageContext.request.contextPath}/app/transfer">Start Transfer</a></p>
<p>The flow is exposed under the <code>/app</code> path via <code>FlowHandlerMapping</code>.</p>
</body>
</html>
