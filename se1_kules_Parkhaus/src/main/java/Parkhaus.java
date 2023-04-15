import java.time.LocalDate;
import java.time.LocalTime;

public class Parkhaus implements ParkhausInterface{

    private double stundentarif;
    private double einnahmenTag;
    private double parkdauerTag;
    private int anzahlFreierParkplaetze; //insgesamt inkl. alle arten
    private int anzahlFreierNormalerParkplaetze; //anzahl normaler
    private int anzahlFreierEAutoParkplaetze; //anzahl e auto
    private int anzahlFreierBehindertenParkplaetze; //erklärt sich denke ich
    private int anzahlFreierMotorradParkplaetze; // -----------"--------------

    public int getAnzahlFreierParkplaetze() {
        return anzahlFreierParkplaetze;
    }
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
        Ticket dasTicket = new Ticket(art);
        anzahlFreierParkplaetze--;
        if(art.equals("Normaler Parkplatz")) {
            anzahlFreierNormalerParkplaetze--;
        } else if(art.equals("E-Auto-Parkplatz")) {
            anzahlFreierEAutoParkplaetze--;
        } else if(art.equals("Behinderten-Parkplatz")) {
            anzahlFreierBehindertenParkplaetze--;
        } else {
            anzahlFreierMotorradParkplaetze--;
        }
        return dasTicket;
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

    public void setAnzahlFreierParkplaetze(int i) {
        this.anzahlFreierParkplaetze = i;
    }

    public void setAnzahlFreieEAutoParkplaetze(int i) {
        anzahlFreierEAutoParkplaetze = i;
    }

    public int getAnzahlFreierEAutoParkplaetze() {
        return anzahlFreierEAutoParkplaetze;
    }

    public void setAnzahlFreierEAutoParkplaetze(int anzahlFreierEAutoParkplaetze) {
        this.anzahlFreierEAutoParkplaetze = anzahlFreierEAutoParkplaetze;
    }

    public int getAnzahlFreierBehindertenParkplaetze() {
        return anzahlFreierBehindertenParkplaetze;
    }

    public void setAnzahlFreierBehindertenParkplaetze(int i) {
        this.anzahlFreierBehindertenParkplaetze = i;
    }

    public int getAnzahlFreierMotorradParkplaetze() {
        return anzahlFreierMotorradParkplaetze;
    }

    public void setAnzahlFreierMotorradParkplaetze(int anzahlFreierMotorradParkplaetze) {
        this.anzahlFreierMotorradParkplaetze = anzahlFreierMotorradParkplaetze;
    }

    public int getAnzahlFreierNormalerParkplaetze() {
        return anzahlFreierNormalerParkplaetze;
    }

    public void setAnzahlFreierNormalerParkplaetze(int i) {
        this.anzahlFreierNormalerParkplaetze = i;
    }
}