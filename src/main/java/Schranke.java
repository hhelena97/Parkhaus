

import java.time.LocalTime;

public class Schranke implements SchrankeIF {

    private Parkhaus parkhaus;

    public Schranke(Parkhaus zugehoerigesParkhaus){this.parkhaus = zugehoerigesParkhaus;}

    /**
     * "ausfahren" prüft, ob das Ticket entwertet wurde und die Zeit zum ausfahren noch reicht. Wenn die Bedingungen nicht
     * erfüllt sind, wird ein entsprechender Hinweis ausgegeben. Ist das Ticket entwertet und die Viertelstunde noch nicht um,
     * wird das Ticket auf null gesetzt, was das "schlucken" simulieren soll. Die Anzahl der freien Parkplätze wird um eins
     * erhöht, entsprechend dem Parkplatz, der belegt war.
     * @param ticket ist das eingesteckte Ticket
     */
    @Override
    public void ausfahren(Ticket ticket) {
        if (ticket.getEntwertet()) {
            LocalTime timeStamp = LocalTime.now().minusMinutes(15);
            LocalTime uhrzeit = ticket.getUhrzeit();
            if (uhrzeit.isAfter(timeStamp)){
                //Parkplatz freigeben:
                String art = ticket.getArtDesParkplatzes();
                this.parkhaus.setAnzahlFreierParkplaetze(this.parkhaus.getAnzahlFreierParkplaetze()+1);

                //Für die speziellen Parkplätze:
                if (art.equals("Normaler Parkplatz")) {
                    this.parkhaus.setAnzahlFreierNormalerParkplaetze((this.parkhaus.getAnzahlFreierNormalerParkplaetze() + 1));
                } else if (art.equals("E-Auto-Parkplatz")) {
                    this.parkhaus.setAnzahlFreierEAutoParkplaetze((this.parkhaus.getAnzahlFreierEAutoParkplaetze() + 1));
                } else if (art.equals("Behinderten-Parkplatz")) {
                    this.parkhaus.setAnzahlFreierBehindertenParkplaetze((this.parkhaus.getAnzahlFreierBehindertenParkplaetze() + 1));
                } else {
                    this.parkhaus.setAnzahlFreierMotorradParkplaetze((this.parkhaus.getAnzahlFreierMotorradParkplaetze() + 1));
                }
                System.out.println("Auf Wiedersehen!");
                //ticket = null;
            }
            else {
                ticket.setUhrzeit(); //Parkzeit neu starten
                ticket.setEntwertet(false);
                System.out.println("Zeit zum Ausfahren ueberschritten, Zeitstempel zurueckgesetzt auf: " + ticket.getUhrzeitStunde() + ":" + ticket.getUhrzeitMin() +". Bitte entwerten Sie das Ticket erneut am Automaten.");
            }

        }
        else {System.out.println("Ausfahrt nur mit entwertetem Ticket moeglich.");}
    }
}
