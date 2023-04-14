import java.time.LocalDate;
import java.time.LocalTime;

public class Parkhaus implements ParkhausInterface{

    private double stundentarif;
    private double einnahmenTag;
    private double parkdauerTag;

    public Parkhaus() {

        this.stundentarif = 0.0;
        this.einnahmenTag = 0.0;
        this.parkdauerTag = 0.0;
    }

    public Parkhaus(double stdTarif) {

        super();    // Frage: rufe ich damit den Konstruktor ohne Parameter auf?
        this.stundentarif = stdTarif;
    }

    @Override
    public Ticket neuesTicket(String art) {
        return null;
        //Konstruktor von Ticket aufrufen
        //Parkplätze ändern
    }


    /**
     * Die Methode 'bezahleTicket' ...
     * @param t
     */
    @Override
    public void bezahleTicket(Ticket t) {

        // TODO: Parkzeit berechnen
        // -> aktuelle Zeit - TicketZeit

        double preis = 0;

        // TODO: Preis berechnen
        // -> Parkzeit * Tarif
        // -> angefangene Stunden berücksichtigen

        System.out.println("Zu bezahlender Preis:" + preis);

        // für spätere Tasks: 'preis' auf 'einnahmenTag' rechnen :)
        einnahmenTag += preis;     //TODO: Test schreiben (eigentlich bevor ich das hier schreibe!)
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    public double getStundentarif() {
        return stundentarif;
    }

    public double getEinnahmenTag() {
        return einnahmenTag;
    }

    public double getParkdauerTag() {
        return parkdauerTag;
    }

}