package Parkhaus;

import java.time.LocalTime;

public class Schranke implements SchrankeIF {


    /**
     * "ausfahren" prüft, ob das Ticket entwertet wurde und die Zeit zum ausfahren noch reicht. Wenn die Bedingungen nicht
     * erfüllt sind, wird ein entsprechender Hinweis ausgegeben. Ist das Ticket entwertet und die Viertelstunde noch nicht um,
     * wird das Ticket inaktiv und die Anzahl der freien Parkplätze wird um eins erhöht, entsprechend dem Parkplatz, der belegt war.
     * @param ticket ist das eingesteckte Ticket
     */
    @Override
    public void ausfahren(Ticket ticket, Parkhaus parkhaus) {
        if (ticket.getEntwertet()) {
            LocalTime timeStamp = LocalTime.now().minusMinutes(15);
            LocalTime uhrzeit = ticket.getUhrzeit();
            if (uhrzeit.equals(timeStamp) || uhrzeit.isAfter(timeStamp)){
                //Parkplatz freigeben:
                String art = ticket.getArtDesParkplatzes();
                parkhaus.setAnzahlFreierParkplaetze(parkhaus.getAnzahlFreierParkplaetze()+1);
                //Für die speziellen Parkplätze:
                if (art.equals("Normaler Parkplatz")) {
                    parkhaus.setAnzahlFreierNormalerParkplaetze((parkhaus.getAnzahlFreierNormalerParkplaetze() + 1));
                } else if (art.equals("E-Auto-Parkplatz")) {
                    parkhaus.setAnzahlFreierEAutoParkplaetze((parkhaus.getAnzahlFreierEAutoParkplaetze() + 1));
                } else if (art.equals("Behinderten-Parkplatz")) {
                    parkhaus.setAnzahlFreierBehindertenParkplaetze((parkhaus.getAnzahlFreierBehindertenParkplaetze() + 1));
                } else {
                    parkhaus.setAnzahlFreierMotorradParkplaetze((parkhaus.getAnzahlFreierMotorradParkplaetze() + 1));
                }
                //ticket wird zu inaktiven tickets hinzugefügt
                parkhaus.getInaktiveTickets().add(ticket);
                //ticket wird aus aktiven tickets rausgenommen
                parkhaus.getAktiveTickets().remove(ticket);

                System.out.println("Auf Wiedersehen!");

            }
            else {
                ticket.setEntwertet(false);
                System.out.println("Zeit zum Ausfahren ueberschritten, Zeitstempel zurueckgesetzt auf: " + ticket.getUhrzeitStunde() + ":" + ticket.getUhrzeitMin() +". Bitte entwerten Sie das Ticket erneut am Automaten.");
            }

        }
        else {System.out.println("Ausfahrt nur mit entwertetem Ticket moeglich.");}
    }
}
