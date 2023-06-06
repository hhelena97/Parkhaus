<%@ page import="Parkhaus.Parkhaus" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="de">
<head>
    <title>Das kule Parkhaus</title>
    <style> .error {color: red;} </style>
</head>
<body>
<h1>Das kule Parkhaus</h1>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="start">
    <button type="submit">Start/Reset</button>
</form>
<br>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="Betreiberansicht">
    <label> Zur Betreiberansicht:  </label>
    <button type="submit">Admin</button>
</form>
<br>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Parkhauszeit anpassen</h2>          <!-- Darstellung der Funktion "Parkzeit anpassen" -->
<p>Aktuelle Parkhauszeit: ${parkhaus.getDatum()} , ${parkhaus.getUhrzeit()} Uhr</p>     <!-- Zeige die aktuelle Parkhauszeit an -->
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="ParkhauszeitenAnpassen">
    <label>neue Parkhauszeit:  </label>             <!-- Beschriftung für die Eingabefelder -->
    <input type="date" step="1" name="Datum" value=${parkhaus.getDatum()}>          <!-- Eingabefeld für das Datum (in 1er-Schritten, wegen des Datumsformat)-->
    <input type="time" step="60" name="Zeit" value=${parkhaus.getUhrzeit()}>        <!-- Eingabefeld für die Zeit (in 60er-Schritten, wegen des Zeitformates) -->
    <button type="submit">Anpassen</button>         <!-- Button zum Anpassen der Parkhauszeit -->
</form>

<br>
<p>Öffnungszeiten: ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getOeffnungszeit())} bis ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getSchliessungszeit())}</p>
<p>Anzahl Parkplaetze in diesem Parkhaus: ${parkhaus.getParkplaetzeGesamt()}, Preis je Stunde: ${parkhaus.getStundentarif()} Euro</p>
Freie regulaere Parkplaetze: ${parkhaus.getAnzahlFreierNormalerParkplaetze()}, freie Behinderten-Parkplaetze: ${parkhaus.getAnzahlFreierBehindertenParkplaetze()}
, freie E-Auto-Parkplaetze: ${parkhaus.getAnzahlFreierEAutoParkplaetze()}, freie Motorrad-Parkplaetze: ${parkhaus.getAnzahlFreierMotorradParkplaetze()}</p>
<br>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Einfahrt</h2>       <!-- Darstellung der Funktion "Einfahrt ins Parkhaus" -->
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="ticketErstellen">     <!-- Wert von 'action' wird auf "ticketErstellen" gesetzt -->
    <label>Parkplatz-Art aussuchen:     <!-- Beschriftung für das Auswahlfeld -->
        <select name="ticketArt" size="1">      <!-- Es kann nur eine der Optionen ausgewählt werden -->
            <option selected>Normaler Parkplatz</option>    <!-- Standardmäßig ausgewählte Parkplatzart -->
            <option>Behinderten-Parkplatz</option>
            <option>E-Auto-Parkplatz</option>
            <option>Motorrad-Parkplatz</option>
        </select>
    </label>
    <button type="submit">Ticket erstellen</button>     <!-- Button zum Erstellen des Tickets -->
</form>
<c:if test="${not empty TicketErstellenException}">
    <p>${TicketErstellenException}</p>
</c:if>
<c:if test="${not empty ParkhausGeschlossenException}">
    <p>${ParkhausGeschlossenException}</p>
</c:if>
<br>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Ticket bezahlen</h2>    <!-- Darstellung der Funktion "Ticket bezahlen" -->
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="bezahlen">    <!-- Wert von 'action' wird auf "bezahlen" gesetzt -->
    <input type="number" min="0" name="ticketID" placeholder="Ticket-ID">         <!-- Eingabefeld für die TicketID -->
    <button type="submit">Preis anzeigen</button>       <!-- Button zum Anpassen der Parkhauszeit -->
</form>
<!-- Wenn ein Ticket bezahlt wurde: -->
<c:if test="${not empty bezahleTicketX}">   <!-- Anzeige von Preis, Parkdauer und Rabatt des Tickets -->
    <p>Ticket ${bezahleTicketX.getTicketID()} hat für ${zeitTicketX} Minuten geparkt und ${preisTicketX} Euro gekostet.</p>
    <p>Für dieses Ticket wurden ${rabattBezahlenX} Euro Rabatt gegeben.</p>
</c:if>
<c:if test="${empty bezahleTicketX}">
    <p>${BezahlenException}</p>
</c:if>
<c:if test="${not empty ParkhausGeschlossenException}">
    <p>${ParkhausGeschlossenException}</p>
</c:if>

<!-- --------------------------------------------------------------------------------------------------------------- -->
<h2>Ausfahrt</h2>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="schrankeOeffnen">
    <input type="number" min="0" name="ticketID" placeholder="Ticket-ID">
    <button type="submit">Schranke oeffnen</button>
</form>
<c:if test="${not empty NachrichtX}">
    <p>${NachrichtX}</p>
</c:if>
<c:if test="${not empty AusfahrenException}">
    <p>${AusfahrenException}</p>
</c:if>
<c:if test="${not empty ParkhausGeschlossenException}">
    <p>${ParkhausGeschlossenException}</p>
</c:if>
<p></p>

<% if (request.getAttribute("parkhaus") != null){ %>
    <% if (request.getAttribute("ticketliste") != null) { %>
        <% Parkhaus p = (Parkhaus) request.getAttribute("parkhaus"); %>
        <p> <%p.StringFuerAktiveTicketsAuflistung(); %></p>
    <% } %>
<% } %>

<!-- für später -->
<% if (request.getAttribute("message") != null) { %>
    <p><%= request.getAttribute("message") %></p>
<% } %>

<!-- für Fehlermeldungen -->
<% if (request.getAttribute("error") != null) { %>
    <p class="error" id="error"><%= request.getAttribute("error") %></p>
<% } %>




</body>
</html>