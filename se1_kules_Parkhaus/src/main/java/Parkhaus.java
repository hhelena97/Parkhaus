import java.time.LocalDate;
import java.time.LocalTime;

public class Parkhaus implements ParkhausInterface{

    private float stundentarif;
    private float tageseinahmen;
    private float parkdauerTag;

    @Override
    public Ticket neuesTicket(String art) {
        return null;
        //Konstruktor von Ticket aufrufen
        //Parkplätze ändern
    }

    @Override
    public void bezahleTicket(Ticket t) {
        // TODO: Parkzeit berechnen
        // -> aktuelle Zeit - TicketZeit

        float preis = 0;

        // TODO: Preis berechnen
        // -> Parkzeit * Tarif
        // -> angefangene Stunden berücksichtigen

        System.out.println("Zu bezahlender Preis:" + preis);

        // für spätere Tasks: 'preis' auf 'tageseinahmen' rechnen :)
        tageseinahmen += preis;     //TODO: Test schreiben (eigentlich bevor ich das hier schreibe!)
    }
}