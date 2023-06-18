## Das kule parkhaus ##

Im Rahmen des Moduls "Software Engineering 1" im SoSe23 haben wir eine Website erstellt, über welche man verschiedene Prozesse in einem parkhaus steuern kann.

Es gibt die Kundenansicht des Parkhauses, auf der alle Prozessen geschehen, die ein Kunde in einem parkhaus machen kann und die Betreiberansicht, auf welcher man Hintergrundinformationen einsehen sowie verschiedene Einstellungen des Parkhauses verändern kann.

Es existiert eine globale Zeit, welche fest zum parkhaus gehört und nach der sich alle Tickets richten müssen. Diese wird zu Beginn dem parkhaus zugewiesen:      
```java
LocalTime time = LocalTime.of(8,0);              
LocalDate date = LocalDate.of(2023,1,1);         
p.setUhrzeit(time); 
p.setDatum(date); 
```

Und anschließend in der Kundenansicht angezeigt:
```html
<p>Aktuelle Parkhauszeit: ${parkhaus.getDatum()} , ${parkhaus.getUhrzeit()} Uhr</p>
```

Alle Daten die zum parkhaus gehören werden im Context gespeichert. Werden Daten im parkhaus geändert, muss dieses immer auch im Context aktualisiertn werden: 
```java
getServletContext().setAttribute("parkhaus", p);
```

Diese globale parkhaus-Zeit kann man (zu Testzwecken) auf der Kundenansicht einstellen. Man kann die Zeit nur vorstellen und niemals in die Vergangenheit.

Das parkhaus hat zudem festgelegte Öffnungszeiten in welchen Besucher rein- und rausfahren können.


# Kundenansicht #

In der Kundenansicht des Parkhauses kann man Tickets ziehen, Tickets bezahlen und mit einem Ticket das parkhaus verlassen. Beim Ziehen des Tickets kann der Kunde entscheiden, ob er einen normalen, Behinderten, Motorrad oder E-Auto Parkplatz haben möchte. Beim bezahlen des Tickets wird der Stundentarif des parkhaus pro angefangene Stunde abgerechnet, anschließend gilt das Ticket als entwertet und der Besucher kann mit diesem Ticken in den nächsten 15min das parkhaus verlassen. Falls er jedoch noch länger als 15min im parkhaus bleibt und dann rausfahren möchte, muss nachgezahlt werden.

Es gibt hier zudem einen Knopf über den man zur Betreiberansicht wechseln kann.


# Betreiberansicht #

In der Betreiberansicht kann man verschiedene Statistiken zum parkhaus einsehen, welche über einen Tag gesammelt werden sowie sich eine Liste von allen zur Zeit aktiven oder inaktiven Tickets anzeigen lassen.

Außerdem können hier die Öffnungszeiten sowie der Stundentarif des Parkhauses verändert werden und es können verschiedene Rabatte auf Tickets gegeben werden. 




https://markdown.de/
