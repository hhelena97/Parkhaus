<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Inaktive Ticket</title>

</head>
<body>
<h1>Inaktive Tickets</h1>


<p>${parkhaus.stringFuerInaktiveTicketsAuflistung()}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="zurück">
    <button type="submit">zurück zum parkhaus</button>
</form>
</body>
</html>
