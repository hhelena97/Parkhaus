package Parkhaus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class Parkhaus implements ParkhausIF {

    private double stundentarif;    //wie teuer ist es eine Stunde in diesem Parkhaus zu parken? kann man das final machen?
    private double einnahmenTag;
    private double parkdauerTag;
    private LocalTime uhrzeit;  // globale Uhrzeit des Parkhauses
    private LocalDate datum;    // globales Datum des Parkhauses

    private int anzahlFreierParkplaetze; //insgesamt inkl. alle arten
    private int anzahlFreierNormalerParkplaetze; //anzahl normaler
    private int anzahlFreierEAutoParkplaetze; //anzahl e auto
    private int anzahlFreierBehindertenParkplaetze; //erklärt sich denke ich
    private int anzahlFreierMotorradParkplaetze; // -----------"--------------
    private int parkplaetzeGesamt; //Anzahl der Parkplätze insgesamt, ob frei oder besetzt
    private List<Ticket> aktiveTickets = new ArrayList<Ticket>(); //Liste mit allen Tickets von Autos, die sich zur Zeit im Parkhaus befinden
    private List<Ticket> inaktiveTickets = new ArrayList<Ticket>(); //Liste mit Tickets von Besuchers, die das Pauskaus verlassen haben
    private LocalTime oeffnungszeit;
    private LocalTime schliessungszeit;


    //brauchen wie beide Konstruktoren? //ja, ist hilfreich zum Testen
    public Parkhaus() {

        this.stundentarif = 0.0;
        this.einnahmenTag = 0.0;
        this.parkdauerTag = 0.0;
        oeffnungszeit = setUhrzeitManuell(8, 0);
        schliessungszeit = setUhrzeitManuell(23, 0);

        this.uhrzeit = LocalTime.of(8,0);                           // Beim Erstellen des Parkhauses wird die Uhrzeit auf 8:00 Uhr gesetzt
        this.datum = LocalDate.of(2023, 1, 1);            // Beim Erstellen des Parkhauses wird das Datum auf 01.01.2023 gesetzt
    }

    public Parkhaus(double stdTarif) {

        this();    // rufe den Konstruktor ohne Parameter auf
        this.stundentarif = stdTarif;
        oeffnungszeit = setUhrzeitManuell(8, 0);
        schliessungszeit = setUhrzeitManuell(23, 0);

        this.uhrzeit = LocalTime.of(8,0);                           // Beim Erstellen des Parkhauses wird die Uhrzeit auf 8:00 Uhr gesetzt
        this.datum = LocalDate.of(2023, 1, 1);            // Beim Erstellen des Parkhauses wird das Datum auf 01.01.2023 gesetzt
    }

    public Parkhaus(double stundentarif, int normaleParkplaetze, int eAutoParkplaetze, int behindertenParkplaetze, int motoradparkplaetze){
        this.stundentarif = stundentarif;
        parkplaetzeGesamt = normaleParkplaetze + behindertenParkplaetze + eAutoParkplaetze + motoradparkplaetze;
        anzahlFreierParkplaetze = parkplaetzeGesamt;
        anzahlFreierNormalerParkplaetze = normaleParkplaetze;
        anzahlFreierEAutoParkplaetze = behindertenParkplaetze;
        anzahlFreierBehindertenParkplaetze = behindertenParkplaetze;
        anzahlFreierMotorradParkplaetze = motoradparkplaetze;

        this.einnahmenTag = 0.0;
        oeffnungszeit = setUhrzeitManuell(8, 0);
        schliessungszeit = setUhrzeitManuell(23, 0);

        this.uhrzeit = LocalTime.of(8,0);                           // Beim Erstellen des Parkhauses wird die Uhrzeit auf 8:00 Uhr gesetzt
        this.datum = LocalDate.of(2023, 1, 1);            // Beim Erstellen des Parkhauses wird das Datum auf 01.01.2023 gesetzt
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
    public Ticket neuesTicket(String art) throws ParkplaetzeBelegtException, ParkhausGeschlossenException{

        //falls vor oder nach Öffnungszeit werfe Exception
        if (this.getUhrzeit().isBefore(this.getOeffnungszeit()) | this.getUhrzeit().isAfter(this.getSchliessungszeit())){
            throw new ParkhausGeschlossenException("Das Parkhaus hat geschlossen.");
        }

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

    /**
     * Die Methode 'parkhauszeitAnpassen()' lässt einen die globale Zeit und das globale Datum des Parkhauses anpassen.
     * Die Methode erfüllt die in Übung 8.1. geforderte Funktionalität.
     * Es ist möglich die Zeit und das Datum des Parkhauses in die Zukunft, nicht jedoch in die Vergangenheit zu verändern.
     * Wird versucht die Zeit in die Vergangenheit zu verändern, wird eine Exception geworfen.
     *
     * @param time - Neue Zeit, die gesetzt werden soll
     * @param date - Neues Datum, das gesetzt werden soll
     * @throws ReiseInVergangenheitException
     * @author jboven2s
     */
    public void parkhauszeitAnpassen(LocalTime time, LocalDate date) throws ReiseInVergangenheitException {

        // Wenn das Datum in der Vergangenheit liegt:
        if (date.isBefore(this.getDatum()))
        {
            throw new ReiseInVergangenheitException("Reise in die Vergangenheit nicht möglich!");
        }

        // Wenn das Datum das gleiche ist, die Zeit aber in der Vergangenheit liegt:
        if (date.equals(this.getDatum()) && time.isBefore(this.getUhrzeit()))
        {
            throw new ReiseInVergangenheitException("Reise in die Vergangenheit nicht möglich!");
        }

        // Wenn Datum und/oder Zeit in der Zukunft liegen:
        this.setUhrzeit(time);      // setze Zeit neu
        this.setDatum(date);        // setze Datum neu
    }

    public void OeffnungszeitenAendern(LocalTime oe, LocalTime sc) {
        this.oeffnungszeit = oe;
        this.schliessungszeit = sc;
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

    public String StringFuerInaktiveTicketsAuflistung() {
        String htmlString = "";
        int index = 0;
        for (Ticket i : this.getInaktiveTickets()) {
            htmlString += "<p>Ticket-ID: " + this.getInaktiveTickets().get(index).getTicketID()+ ", ";
            htmlString += "Datum: " + this.getInaktiveTickets().get(index).getDatum()+", ";
            htmlString += "Dauer: " + this.getInaktiveTickets().get(index).getParkdauerMin()+", ";
            htmlString += "Preis: " + this.getInaktiveTickets().get(index).getPreis()+", ";
            htmlString += "Parkplatzart: " + this.getInaktiveTickets().get(index).getArtDesParkplatzes()+ "</p>";
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
        int thisMonth = datum.getMonthValue();
        double einnahmenMonat = 0;
        int besucherCount = this.getAktiveTickets().size();
        int besucherInsgesamt = besucherCount + this.getInaktiveTickets().size();

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

        String statsString = "Stand: "+datum+", "+uhrzeit.truncatedTo(ChronoUnit.SECONDS)+"<br>";
        statsString += "<p>Besucherzahl aktuell: "+besucherCount+"<br>"+"Besucher insgesamt: " +besucherInsgesamt+"<br>";
        statsString += "Tageseinnahmen: " +this.getEinnahmenTag()+" Euro<br>"+"Monatseinnahmen: "+einnahmenMonat+" Euro<br>";
        statsString += "Durchschnittliche Parkdauer: "+av_parkdauer+" min<br>"+"Durchschnittlicher Ticketpreis: "+av_preis+" Euro</p>";

        return statsString;
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
        return oeffnungszeit;
    }

    public void setOeffnungszeit(LocalTime oeffnungszeit) {
        this.oeffnungszeit = oeffnungszeit;
    }

    public LocalTime getSchliessungszeit() {
        return schliessungszeit;
    }

    public void setSchliessungszeit(LocalTime schliessungszeit) {
        this.schliessungszeit = schliessungszeit;
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

    public LocalTime setUhrzeitManuell(int stunden, int minuten){return LocalTime.of(stunden, minuten);}
    public LocalDate setDatumManuell(int tag, int monat, int jahr){return LocalDate.of(jahr, monat, tag);}

    public LocalTime getUhrzeit() {return uhrzeit;}
    public void setUhrzeit(LocalTime uhrzeit) {this.uhrzeit = uhrzeit;}
    public LocalDate getDatum() {return datum;}
    public void setDatum(LocalDate datum) {this.datum = datum;}

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