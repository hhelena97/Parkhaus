<%@ page import="Parkhaus.Ticket" %>
<%@ page import="Parkhaus.Parkhaus" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
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
<p>Anzahl Parkplaetze in diesem Parkhaus: ${parkhaus.getParkplaetzeGesamt()}</p>
<p>Anzahl freier normaler Parkplaetze: ${parkhaus.getAnzahlFreierNormalerParkplaetze()}</p>
<p>Anzahl freier Behinderten-Parkplaetze: ${parkhaus.getAnzahlFreierBehindertenParkplaetze()}</p>
<p>Anzahl freier E-Auto-Parkplaetze: ${parkhaus.getAnzahlFreierEAutoParkplaetze()}</p>
<p>Anzahl freier Motorrad-Parkplaetze: ${parkhaus.getAnzahlFreierMotorradParkplaetze()}</p>
<p>Preis je Stunde: ${parkhaus.getStundentarif()} Euro</p>
<br>
<p>Preis je Stunde: ${parkhaus.getStundentarif()} Euro</p>
<br>
<h2>Einfahrt</h2>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet"> <!--Was ist das und was macht das?-->
    <input type="hidden" name="action" value="ticketErstellen">
    <label>Parkplatz-Art aussuchen:
        <select name="ticketArt" size="1">
            <option selected>Normaler Parkplatz</option>
            <option>Behinderten-Parkplatz</option>
            <option>E-Auto-Parkplatz</option>
            <option>Motorrad-Parkplatz</option>
        </select>
    </label>
    <button type="submit">Ticket erstellen</button>
</form>
<c:if test="${not empty TicketErstellenException}">
    <p>${TicketErstellenException}</p>
</c:if>
<br>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
<input type="hidden" name="action" value="aktiveTickets">
<button type="submit">Aktive Tickets</button>
</form>
<br>
<h2>Ticket bezahlen</h2>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="bezahlen">
    <input type="text" name="ticketID" placeholder="Ticket-ID">
    <button type="submit">Preis anzeigen</button>
</form>
<c:if test="${not empty bezahleTicketX}">
    <p>Ticket ${bezahleTicketX.getTicketID()} hat für ${zeitTicketX} Minuten geparkt und ${preisTicketX} Euro gekostet.</p>
</c:if>
<c:if test="${empty bezahleTicketX}">
    <p>${BezahlenException}</p>
</c:if>
<h2>Ausfahrt</h2>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="schrankeOeffnen">
    <input type="text" name="ticketID" placeholder="Ticket-ID">
    <button type="submit">Schranke oeffnen</button>
</form>
<c:if test="${not empty NachrichtX}">
    <p>${NachrichtX}</p>
</c:if>
<c:if test="${not empty AusfahrenException}">
    <p>${AusfahrenException}</p>
</c:if>
<p></p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <button type="submit" name="action" value="datenAuswerten">Daten auswerten</button>
</form>
<c:if test="${not empty datenauswertung}">
    <p>${datenauswertung}</p>
</c:if>

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