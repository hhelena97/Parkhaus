<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Aktive Ticket</title>

</head>
<body>
<h1>Aktive Ticket</h1>


<p>${parkhaus.stringFuerAktiveTicketsAuflistung()}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="zurück">
    <button type="submit">zurück zum Parkhaus</button>
</form>
</body>
</html>
