

import java.time.LocalTime;

public class Schranke implements SchrankeIF {

    private Parkhaus parkhaus;

    public Schranke(Parkhaus zugehoerigesParkhaus){this.parkhaus = zugehoerigesParkhaus;}

    /**
     * "einfahren" soll die Schranke öffnen, wenn man das Ticket gezogen hat
     */
    @Override
    public void einfahren() {
        //US02?
    }

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
            LocalTime timeStamp = LocalTime.now();
            if (ticket.getUhrzeit().plusMinutes(15).isBefore(timeStamp)){
                //Parkplatz freigeben:
                String art = ticket.getArtDesParkplatzes();
                this.parkhaus.setAnzahlFreieEAutoParkplaetze(this.parkhaus.getAnzahlFreierParkplaetze()+1);
                //Für die speziellen Parkplätze:
                if (art.equals("Normaler Parkplatz")) {
                    this.parkhaus.setAnzahlFreierNormalerParkplaetze((this.parkhaus.getAnzahlFreierNormalerParkplaetze() + 1));
                } else if (art.equals("E-Auto-Parkplatz")) {
                    this.parkhaus.setAnzahlFreierNormalerParkplaetze((this.parkhaus.getAnzahlFreierEAutoParkplaetze() + 1));
                } else if (art.equals("Behinderten-Parkplatz")) {
                    this.parkhaus.setAnzahlFreierNormalerParkplaetze((this.parkhaus.getAnzahlFreierBehindertenParkplaetze() + 1));
                } else {
                    this.parkhaus.setAnzahlFreierNormalerParkplaetze((this.parkhaus.getAnzahlFreierMotorradParkplaetze() + 1));
                }
                //ticket = null; würde ich nicht machen hier, da ich die Daten von den Tickets noch auf der Website anzeigen will
            }
            else {
                ticket.setUhrzeit(); //Parkzeit neu starten
                ticket.setEntwertet(false);
                System.out.println("Zeit zum Ausfahren überschritten, Zeitstempel zurückgesetzt auf: " + ticket.getUhrzeitStunde() + ":" + ticket.getUhrzeitMin() +". Bitte entwerten Sie das Ticket erneut am Automaten.");
            }

        }
        else {System.out.println("Ausfahrt nur mit entwertetem Ticket möglich.");}
    }
}
