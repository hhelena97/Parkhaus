<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Betreiberansicht</title>
</head>
<body>
<h1>Betreiberansicht</h1>
<br>
<h2>Öffnungszeiten ändern</h2>
<p>Aktuelle Öffnungszeiten: ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getOeffnungszeit())} bis ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getSchliessungszeit())}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="ÖffnungszeitenÄndern">
    <!--ich komme an Öffnen und Schließen variablen mit getServletContext().getAttribute("Öffnen")-->
    <label>Öffnen:  </label>
    <input type="time" step="60" name="Öffnen" value="10:00">
    <br>
    <br>
    <label>Schließen:</label>
    <input type="time" step="60" name="Schließen" value="23:00">
    <br>
    <br>
    <button type="submit">Ändern</button>
</form>
<br>
<h2>Preis ändern</h2>
<p>Aktueller Preis: ${parkhaus.getStundentarif()}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="StudentarifAendern">
    <!--ich komme an Öffnen und Schließen variablen mit getServletContext().getAttribute("Öffnen")-->
    <label>neuer Preis  </label>
    <input type=double name="Preis" value="1.00">
    <br>
    <button type="submit">Ändern</button>
</form>
<br>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="zurück">
    <button type="submit">zurück zum Parkhaus</button>
</form>
</body>
</html>
