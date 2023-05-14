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
        anzahlFreierParkplaetze = parkplaetzeGesamt;
        anzahlFreierNormalerParkplaetze = normaleParkplaetze;
        anzahlFreierEAutoParkplaetze = behindertenParkplaetze;
        anzahlFreierBehindertenParkplaetze = behindertenParkplaetze;
        anzahlFreierMotorradParkplaetze = motoradparkplaetze;

        this.einnahmenTag = 0.0;

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
        Ticket dasTicket = new Ticket(art);
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
     * Die Methode 'bezahleTicket' ...
     *
     * @param t
     * @return den zu bezahlenden Preis als double
     */
    @Override
    public double bezahleTicket(Ticket t) {


        int dauer = t.zeitDifferenz();
        int stunden = dauer/60;

        if (dauer%60 != 0) {stunden++;}

        // TODO: Preis berechnen
        double preis = this.getStundentarif() * stunden;
        t.setPreis(preis);
        // -> angefangene Stunden berücksichtigen

        System.out.println("Zu bezahlender Preis: " + t.getPreis());

        //'preis' auf 'einnahmenTag' rechnen
        einnahmenTag += preis;

        //set parkdauer zur späteren auswertung
        t.setParkdauerMin(dauer);

        //in Real erst nach dem Bezahlen
        t.entwerten();
        return t.getPreis();
    }

    /**
     * "ausfahren" prüft, ob das Ticket entwertet wurde und die Zeit zum ausfahren noch reicht. Wenn die Bedingungen nicht
     * erfüllt sind, wird ein entsprechender Hinweis ausgegeben. Ist das Ticket entwertet und die Viertelstunde noch nicht um,
     * wird das Ticket inaktiv und die Anzahl der freien Parkplätze wird um eins erhöht, entsprechend dem Parkplatz, der belegt war.
     * @param ticket ist das eingesteckte Ticket
     * @return Nachricht, die dem Besucher angezeigt werden soll
     */
    @Override
    public String ausfahren(Ticket ticket) throws TicketNichtGefundenException{

        if(ticket == null){throw new TicketNichtGefundenException("Ticket nicht gefunden.");
        }else if (ticket.getEntwertet()) {
            LocalTime timeStamp = LocalTime.now().minusMinutes(15);
            LocalTime uhrzeit = ticket.getUhrzeit();
            if (uhrzeit.equals(timeStamp) || uhrzeit.isAfter(timeStamp)){
                //Parkplatz freigeben:
                String art = ticket.getArtDesParkplatzes();
                this.setAnzahlFreierParkplaetze(this.getAnzahlFreierParkplaetze()+1);
                //Für die speziellen Parkplätze:
                if (art.equals("Normaler Parkplatz")) {
                    this.setAnzahlFreierNormalerParkplaetze((this.getAnzahlFreierNormalerParkplaetze() + 1));
                } else if (art.equals("E-Auto-Parkplatz")) {
                    this.setAnzahlFreierEAutoParkplaetze((this.getAnzahlFreierEAutoParkplaetze() + 1));
                } else if (art.equals("Behinderten-Parkplatz")) {
                    this.setAnzahlFreierBehindertenParkplaetze((this.getAnzahlFreierBehindertenParkplaetze() + 1));
                } else {
                    this.setAnzahlFreierMotorradParkplaetze((this.getAnzahlFreierMotorradParkplaetze() + 1));
                }
                //ticket wird zu inaktiven tickets hinzugefügt
                this.getInaktiveTickets().add(ticket);
                //ticket wird aus aktiven tickets rausgenommen
                this.getAktiveTickets().remove(ticket);

                return"Auf Wiedersehen!";

            }
            else {
                ticket.setEntwertet(false);
                return"Zeit zum Ausfahren überschritten, Zeitstempel zurückgesetzt auf: " + ticket.getUhrzeit().truncatedTo(ChronoUnit.MINUTES) +". Bitte entwerten Sie das Ticket erneut am Automaten.";
            }

        }
        else {return"Ausfahrt nur mit entwertetem Ticket möglich.";}
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
}