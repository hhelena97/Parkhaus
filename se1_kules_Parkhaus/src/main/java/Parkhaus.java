import java.time.LocalDate;
import java.time.LocalTime;

public class Parkhaus implements ParkhausInterface{

    private double stundentarif; //wie teuer ist es eine Stunde in diesem Parkhaus zu parken? TODO: kann man das final machen?
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

    //brauchen wie beide Konstruktoren?
    public Parkhaus() {

        this.stundentarif = 0.0;
        this.einnahmenTag = 0.0;
        this.parkdauerTag = 0.0;
    }

    public Parkhaus(double stdTarif) {

        super();    // Frage: rufe ich damit den Konstruktor ohne Parameter auf? Katharina: du rufst damit den Konstruktor auf aus der Klasse von der Parkhaus erbt, aber Parkhaus erbt doch von keiner Klasse... ich meine du müsstest da this(); benutzen statt super(); dann sollte der den Parkhaus() aufrufen
        this.stundentarif = stdTarif;
    }

    /**
     * die Methode "neuesTicket" ruft den Konstruktor für ein neues Ticket auf (setzt die Uhrzeit auf die aktuelle Uhrzeit, das Datum auf
     * das aktuelle Datum und die Art auf den mitgegebenen String "art"). Danach wird die Anzahl der freien Parkplätze um 1 verringert.
     * Und die Anzahl der jeweiligen freien Plätze der bestimmten Art werden auch um 1 verringert.
     *
     * @param art String in welchem steht, welche Art des Parkplatzes der Kunde gewählt hat
     * @return ein neues Ticket mit gesetzten Instanzvariablen
     */
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

        // TODO: Parkzeit berechnen     Katharina: kannst du jetzt mit der guten neuen Methode "zeitDifferenz" machen :)
        // -> aktuelle Zeit - TicketZeit

        double preis = 0;

        // TODO: Preis berechnen
        // -> Parkzeit * Tarif
        // -> angefangene Stunden berücksichtigen

        System.out.println("Zu bezahlender Preis:" + preis);

        // für spätere Tasks: 'preis' auf 'einnahmenTag' rechnen :)
        einnahmenTag += preis;     //TODO: Test schreiben (eigentlich bevor ich das hier schreibe!)

        //in Real erst nach dem Bezahlen
        t.entwerten();
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