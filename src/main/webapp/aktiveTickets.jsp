<%--
  Created by IntelliJ IDEA.
  User: marie
  Date: 11.05.2023
  Time: 15:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Aktive Ticket</title>

</head>
<body>
<h1>Aktive Ticket</h1>


<p>${parkhaus.StringFuerAktiveTicketsAuflistung()}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="zurück">
    <button type="submit">zurück zum Parkhaus</button>
</form>
</body>
</html>
