package Parkhaus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;
import java.time.LocalTime;


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
     *
     * @param t
     * @return den zu bezahlenden Preis als double
     */
    @Override
    public double bezahleTicket(Ticket t) {

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
        return t.getPreis();
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
    //Wo kommt diese "anzahlFreierParkplaetze" her, wenn man die nicht berechnet? Das ist doch keine Konstante, die man beim Konstruktor festlegt.

    public double getStundentarif() {
        return stundentarif;
    }

    public double getEinnahmenTag() {
        return einnahmenTag;
    }

    public double getParkdauerTag() {
        return parkdauerTag;
    }

    //Sarah: Wofür ist diese Methode? Ich dachte, freie Parkplätze ergeben sich aus den Gesamtparkplätzen - allen belegten Parkplätzen
    //oder ist das eine Methode für manche Tests?
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

    public String StringFuerAktiveTicketsAuflistung() {
        String htmlString = "";
        htmlString += "<h2>Zurzeit aktive Tickets: </h2>";
        int index = 0;
        for (Ticket i : this.getAktiveTickets()) {
            htmlString += "<p>Ticket-ID: " + this.getAktiveTickets().get(index).getTicketID()+ ", ";
            htmlString += "Datum: " + this.getAktiveTickets().get(index).getDatum()+", ";
            htmlString += "Ankunftszeit: " + this.getAktiveTickets().get(index).getUhrzeit().getHour();
            if(this.getAktiveTickets().get(index).getUhrzeit().getMinute() <10) {
                htmlString += ":0" + this.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            } else {
                htmlString += ":" + this.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            }
            htmlString += "Parkplatzart: " + this.getAktiveTickets().get(index).getArtDesParkplatzes()+ "</p>";
            index++;
        }
        return htmlString;
    }

}