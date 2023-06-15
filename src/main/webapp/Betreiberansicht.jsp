<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Betreiberansicht</title>
</head>
<body>
<h1>Betreiberansicht</h1>
<br>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Datenauswertungen</h2>
<p>${parkhaus.stringFuerStats()}</p>
<br>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="aktiveTickets">
    <button type="submit">Aktive Tickets</button>
</form>
<br>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="inaktiveTickets">
    <button type="submit">Inaktive Tickets</button>
</form>
<br>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Öffnungszeiten ändern</h2>
<p>Aktuelle Öffnungszeiten: ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getOeffnungszeit())} bis ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getSchliessungszeit())}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="OeffnungszeitenAendern">
    <!--ich komme an Öffnen und Schließen variablen mit getServletContext().getAttribute("Öffnen")-->
    <label>Öffnen:  </label>
    <input type="time" step="60" name="Oeffnen" value="10:00">
    <br>
    <br>
    <label>Schließen:</label>
    <input type="time" step="60" name="Schliessen" value="23:00">
    <br>
    <br>
    <button type="submit">Ändern</button>
</form>
<br>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Preis ändern</h2>
<p>Aktueller Preis: ${parkhaus.getStundentarif()}</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="StundentarifAendern">
    <label>neuer Preis:  </label>
    <input type=double name="Preis" value="1.00">
    <button type="submit">Ändern</button>
</form>
<br>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Rabatt geben</h2>       <!-- Darstellung der Funktion "Rabatt geben" -->
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="rabattGeben">     <!-- Wert von 'action' wird auf "rabattGeben" gesetzt -->
    <label>Rabatt wählen:       <!-- Beschriftung für das Auswahlfeld -->
    <input type="number" min="0" name="ticketID" placeholder="Ticket-ID">       <!-- Eingabefeld für die TicketID -->
    <select name="rabatt" size="1">     <!-- Es kann nur eine der Optionen ausgewählt werden -->
        <option selected>keinen (0 %)</option>      <!-- Standardmäßig ausgewählter Rabatt -->
        <option>Personalrabatt (10 %)</option>
        <option>Besucher EKZ (20 %)</option>
        <option>Treuerabatt (25 %)</option>
    </select>
    </label>
    <button type="submit">Rabatt geben</button>     <!-- Button zum Rabatt geben -->
</form>
<!-- Wenn ein Rabatt gegeben wurde: -->
<c:if test="${not empty rabattTicketX}">        <!-- Anzeige des gegebenen Rabatts auf das entsprechende Ticket -->
    <p>Ticket ${rabattTicketX.getTicketID()} hat einen Rabatt von ${rabattX} % bekommen.</p>
</c:if>
<br>

<br>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="zurück">
    <button type="submit">zurück zum Parkhaus</button>
</form>
</body>
</html>
