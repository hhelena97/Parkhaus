

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Parkhaus implements ParkhausIF {

    private double stundentarif;    //wie teuer ist es eine Stunde in diesem Parkhaus zu parken? kann man das final machen?
    private double einnahmenTag;
    private double parkdauerTag;

    private int anzahlFreierParkplaetze; //insgesamt inkl. alle arten
    private int anzahlFreierNormalerParkplaetze; //anzahl normaler
    private int anzahlFreierEAutoParkplaetze; //anzahl e auto
    private int anzahlFreierBehindertenParkplaetze; //erklärt sich denke ich
    private int anzahlFreierMotorradParkplaetze; // -----------"--------------
    private int parkplaetzeGesamt; //Anzahl der Parkplätze insgesamt, ob frei oder besetzt
    private List<Ticket> aktiveTickets = new ArrayList<Ticket>();
    private List<Ticket> inaktiveTickets = new ArrayList<Ticket>();




    //brauchen wie beide Konstruktoren? //ja, ist hilfreich zum Testen
    public Parkhaus() {

        this.stundentarif = 0.0;
        this.einnahmenTag = 0.0;
        this.parkdauerTag = 0.0;
    }

    public Parkhaus(double stdTarif) {

        this();    // rufe den Konstruktor ohne Parameter auf
        this.stundentarif = stdTarif;
    }

    //Konstruktor für Zwischenstufe bei dem man Anzahl normale Parkplätze festlegt
    public Parkhaus(int normale_Parkplaetze){
        this();
        this.anzahlFreierNormalerParkplaetze = normale_Parkplaetze;
    }

    public Parkhaus(double stundentarif, int normaleParkplaetze, int EAutoParkplaetze, int behindertenParkplaetze, int motoradparkplaetze){
        this.stundentarif = stundentarif;
        parkplaetzeGesamt = normaleParkplaetze + behindertenParkplaetze + EAutoParkplaetze + motoradparkplaetze;
        anzahlFreierNormalerParkplaetze = normaleParkplaetze;
        anzahlFreierEAutoParkplaetze = behindertenParkplaetze;
        anzahlFreierBehindertenParkplaetze = behindertenParkplaetze;
        anzahlFreierMotorradParkplaetze = motoradparkplaetze;

    }


    /**
     * die Methode "neuesTicket" ruft den Konstruktor für ein neues Ticket auf (setzt die Uhrzeit auf die aktuelle Uhrzeit, das Datum auf
     * das aktuelle Datum und die Art auf den mitgegebenen String "art"). Danach wird die Anzahl der freien Parkplätze um 1 verringert.
     * Und die Anzahl der jeweiligen freien Plätze der bestimmten Art werden auch um 1 verringert.
     *
     * @param art String in welchem steht, welche Art des Parkplatzes der Kunde gewählt hat
     * @return ein neues Ticket mit gesetzten Instanzvariablen
     */
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
        //in aktiveTickets Liste schieben
        aktiveTickets.add(dasTicket);
        return dasTicket;
    }


    /**
     * Die Methode 'bezahleTicket' ...
     * @param t
     */
    @Override
    public void bezahleTicket(Ticket t) {

        // TODO: Parkzeit berechnen
        int dauer = t.zeitDifferenz();
        int stunden = dauer/60;

        if (dauer%60 != 0) {
            stunden++;
        }

        // TODO: Preis berechnen
        t.setPreis(this.getStundentarif() * stunden);
        // -> angefangene Stunden berücksichtigen

        System.out.println("Zu bezahlender Preis: " + t.getPreis());

        // für spätere Tasks: 'preis' auf 'einnahmenTag' rechnen :)
        //einnahmenTag += preis;     //TODO: Test schreiben (eigentlich bevor ich das hier schreibe!)

        //in Real erst nach dem Bezahlen
        t.entwerten();
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    public int getParkplaetzeGesamt(){
        return parkplaetzeGesamt;
    }
    public int getAnzahlFreierParkplaetze() {
        //anzahlFreierParkplaetze = anzahlFreierNormalerParkplaetze + anzahlFreierEAutoParkplaetze + anzahlFreierBehindertenParkplaetze + anzahlFreierMotorradParkplaetze;
        return anzahlFreierParkplaetze;
    }

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

    public void setAnzahlFreierEAutoParkplaetze(int i) {
        anzahlFreierEAutoParkplaetze = i;
    }

    public int getAnzahlFreierEAutoParkplaetze() {
        return anzahlFreierEAutoParkplaetze;
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

    public void setAnzahlFreierMotorradParkplaetze(int i) {
        this.anzahlFreierMotorradParkplaetze = i;
    }

    public int getAnzahlFreierNormalerParkplaetze() {
        return anzahlFreierNormalerParkplaetze;
    }

    public void setAnzahlFreierNormalerParkplaetze(int i) {
        this.anzahlFreierNormalerParkplaetze = i;
    }

    public List<Ticket> getAktiveTickets() {
        return aktiveTickets;
    }

    public List<Ticket> getInaktiveTickets() {
        return inaktiveTickets;
    }

}