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
<br>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="Betreiberansicht">
    <label> Zur Betreiberansicht:  </label>
    <button type="submit">Admin</button>
</form>
<br>
<h2>Parkhauszeit anpassen</h2>
<p>Aktuelle Parkhauszeit: ${parkhaus.getDatum()} , ${parkhaus.getUhrzeit()} Uhr</p>
<form method="POST" action="${pageContext.request.contextPath}/parkhaus-servlet">
    <input type="hidden" name="action" value="ParkhauszeitenAnpassen">
    <label>neue Parkhauszeit:  </label>
    <input type="date" step="1" name="Datum" value=${parkhaus.getDatum()}>
    <input type="time" step="60" name="Zeit" value=${parkhaus.getUhrzeit()}>
    <button type="submit">Anpassen</button>
</form>
<c:if test="${not empty VergangenheitException}">
    <p>${VergangenheitException}</p>
</c:if>
<br>
<p>Öffnungszeiten: ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getOeffnungszeit())} bis ${parkhaus.getUhrzeitStringParkhaus(parkhaus.getSchliessungszeit())}</p>
<p>Anzahl Parkplaetze in diesem Parkhaus: ${parkhaus.getParkplaetzeGesamt()}, Preis je Stunde: ${parkhaus.getStundentarif()} Euro</p>
freier normaler Parkplaetze: ${parkhaus.getAnzahlFreierNormalerParkplaetze()}, Behinderten-Parkplaetze: ${parkhaus.getAnzahlFreierBehindertenParkplaetze()}
, freier E-Auto-Parkplaetze: ${parkhaus.getAnzahlFreierEAutoParkplaetze()}, freier Motorrad-Parkplaetze: ${parkhaus.getAnzahlFreierMotorradParkplaetze()}</p>
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
<c:if test="${not empty ParkhausGeschlossenException}">
    <p>${ParkhausGeschlossenException}</p>
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
    <p>Für dieses Ticket wurden ${rabattBezahlenX} Euro Rabatt gegeben.</p>
</c:if>
<c:if test="${empty bezahleTicketX}">
    <p>${BezahlenException}</p>
</c:if>
<c:if test="${not empty ParkhausGeschlossenException}">
    <p>${ParkhausGeschlossenException}</p>
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