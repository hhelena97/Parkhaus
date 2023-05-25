package Parkhaus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
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
    private LocalTime Oeffnungszeit;
    private LocalTime Schliessungszeit;




    //brauchen wie beide Konstruktoren? //ja, ist hilfreich zum Testen
    public Parkhaus() {

        this.stundentarif = 0.0;
        this.einnahmenTag = 0.0;
        this.parkdauerTag = 0.0;
        Oeffnungszeit = setUhrzeitManuell(8, 0);
        Schliessungszeit = setUhrzeitManuell(23, 0);
    }

    public Parkhaus(double stdTarif) {

        this();    // rufe den Konstruktor ohne Parameter auf
        this.stundentarif = stdTarif;
        Oeffnungszeit = setUhrzeitManuell(8, 0);
        Schliessungszeit = setUhrzeitManuell(23, 0);
    }


    public Parkhaus(double stundentarif, int normaleParkplaetze, int EAutoParkplaetze, int behindertenParkplaetze, int motoradparkplaetze){
        this.stundentarif = stundentarif;
        parkplaetzeGesamt = normaleParkplaetze + behindertenParkplaetze + EAutoParkplaetze + motoradparkplaetze;
        anzahlFreierParkplaetze = parkplaetzeGesamt;
        anzahlFreierNormalerParkplaetze = normaleParkplaetze;
        anzahlFreierEAutoParkplaetze = behindertenParkplaetze;
        anzahlFreierBehindertenParkplaetze = behindertenParkplaetze;
        anzahlFreierMotorradParkplaetze = motoradparkplaetze;

        this.einnahmenTag = 0.0;
        Oeffnungszeit = setUhrzeitManuell(8, 0);
        Schliessungszeit = setUhrzeitManuell(23, 0);

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
    public Ticket neuesTicket(String art) throws ParkplaetzeBelegtException{

        if (this.anzahlFreierParkplaetze == 0){throw new ParkplaetzeBelegtException("Keine freien Parkplaetze verfuegbar!");}
        Ticket dasTicket = new Ticket(art, this);
        anzahlFreierParkplaetze--;
        if(art.equals("Normaler Parkplatz")) {
            if (this.anzahlFreierNormalerParkplaetze == 0){throw new ParkplaetzeBelegtException("Keine freien normalen Parkplaetze verfuegbar!");}
            else anzahlFreierNormalerParkplaetze--;
        } else if(art.equals("E-Auto-Parkplatz")){
            if (this.anzahlFreierEAutoParkplaetze == 0){throw new ParkplaetzeBelegtException("Keine freien E-Auto-Parkplaetze verfuegbar!");}
            else anzahlFreierEAutoParkplaetze--;
        } else if(art.equals("Behinderten-Parkplatz")) {
            if (this.anzahlFreierBehindertenParkplaetze == 0){throw new ParkplaetzeBelegtException("Keine freien Behindertenparkplaetze verfuegbar!");}
            else anzahlFreierBehindertenParkplaetze--;
        } else {if (this.anzahlFreierMotorradParkplaetze == 0){throw new ParkplaetzeBelegtException("Keine freien Motorradparkplaetze verfuegbar!");}
            else anzahlFreierMotorradParkplaetze--;
        }
        //in aktiveTickets Liste schieben
        aktiveTickets.add(dasTicket);
        return dasTicket;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    public int getParkplaetzeGesamt(){
        return parkplaetzeGesamt;
    }
    public int getAnzahlFreierParkplaetze() {
        anzahlFreierParkplaetze = anzahlFreierNormalerParkplaetze + anzahlFreierEAutoParkplaetze + anzahlFreierBehindertenParkplaetze + anzahlFreierMotorradParkplaetze;
        return anzahlFreierParkplaetze;
    }

    public LocalTime getOeffnungszeit() {
        return Oeffnungszeit;
    }

    public void setOeffnungszeit(LocalTime oeffnungszeit) {
        Oeffnungszeit = oeffnungszeit;
    }

    public LocalTime getSchliessungszeit() {
        return Schliessungszeit;
    }

    public void setSchliessungszeit(LocalTime schliessungszeit) {
        Schliessungszeit = schliessungszeit;
    }

    public double getStundentarif() {
        return stundentarif;
    }
    public void setStundentarif(double neuerPreis) {this.stundentarif = neuerPreis; }

    public double getEinnahmenTag() {
        return einnahmenTag;
    }
    public void setEinnahmenTag(double einnahmenTag) {this.einnahmenTag = einnahmenTag;}

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

    public String getUhrzeitStringParkhaus(LocalTime time) {
        if(time.getMinute()<10) {
            return time.getHour() + ":0" + time.getMinute();
        } else {
            return time.getHour() + ":" + time.getMinute();
        }
    }

    public int getAnzahlFreierBehindertenParkplaetze() {
        return anzahlFreierBehindertenParkplaetze;
    }

    public void setAnzahlFreierBehindertenParkplaetze(int i) {
        this.anzahlFreierBehindertenParkplaetze = i;
    }

    public LocalTime setUhrzeitManuell(int stunden, int minuten){return LocalTime.of(stunden, minuten);};

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

    public void resetTicketListen(){
        this.aktiveTickets = new ArrayList<Ticket>();
        this.inaktiveTickets = new ArrayList<Ticket>();
        Ticket.setIdentifikationsNummer();
    }

    public String ausfahrenNachrichten(String nachricht) {
        return "<p>" + nachricht + "</p>";
    }

    public String StringFuerStats(){

        int av_parkdauer = 0;
        double av_preis = 0.0;
        int size = this.getInaktiveTickets().size();
        int thisMonth = LocalDate.now().getMonthValue();
        double einnahmenMonat = 0;
        int besucherCount = this.getInaktiveTickets().size()+this.getAktiveTickets().size();

        if(size != 0) {
            for (int i = 0; i < size; i++) {
                av_parkdauer += this.getInaktiveTickets().get(i).getParkdauerMin();
                av_preis += this.getInaktiveTickets().get(i).getPreis();
                //Monatseinnahmen
                if (this.getInaktiveTickets().get(i).getDatum().getMonthValue() == thisMonth){
                    einnahmenMonat += this.getInaktiveTickets().get(i).getPreis();
                }
            }
            av_parkdauer /= size;
            av_preis /= size;
        }

        String statsString = "<h2>Datenauswertungen: </h2><br>"+"Stand: "+LocalDate.now()+", "+LocalTime.now().truncatedTo(ChronoUnit.SECONDS)+"<br>";
        statsString += "<p>Durchschnittliche Parkdauer: "+av_parkdauer+" min<br>"+"Durchschnittlicher Ticketpreis: "+av_preis+" Euro<br>";
        statsString += "Tageseinnahmen: " +this.getEinnahmenTag()+" Euro<br>"+"Monatseinnahmen: "+einnahmenMonat+" Euro<br>"+"Besucher insgesamt: " +besucherCount+"</p>";

        return statsString;
    }

    public void OeffnungszeitenAendern(LocalTime oe, LocalTime sc) {
        this.Oeffnungszeit = oe;
        this.Schliessungszeit = sc;
    }
}