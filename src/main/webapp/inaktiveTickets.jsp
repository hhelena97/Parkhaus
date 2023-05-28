<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Inaktive Ticket</title>

</head>
<body>
<h1>Inaktive Tickets</h1>


<p>${parkhaus.StringFuerInaktiveTicketsAuflistung()}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="zurück">
    <button type="submit">zurück zum Parkhaus</button>
</form>
</body>
</html>
