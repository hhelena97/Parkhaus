package Parkhaus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Ticket implements TicketIF {

    State zustand = null;

    private LocalDate datum;
    private LocalTime uhrzeit;
    private final String artDesParkplatzes;
    private double preis;


    private double rabattProzent;
    private double rabattEuro;
    private boolean entwertet;
    private int parkdauerMin;
    private int ticketID;
    private static int identifikationsNummer = 0;

    public Parkhaus getParkhaus() {
        return parkhaus;
    }

    private Parkhaus parkhaus;

    //Test-Konstruktor damit man sich nicht immer ein Parkhaus.Ticket mit "Parkhaus.Ticket-Art" erstellen muss zum Testen
    public Ticket() {
        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = "normaler Parkplatz";
        this.parkdauerMin = 0;
        this.entwertet = false;

        this.uhrzeit = parkhaus.getUhrzeit();
        this.datum = parkhaus.getDatum();
    }

    public Ticket(String art, Parkhaus parkhaus) {
        //this.datum = LocalDate.now();
        //this.uhrzeit = LocalTime.now();


        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = art;
        this.parkdauerMin = 0;
        this.entwertet = false;
        this.parkhaus = parkhaus;

        this.uhrzeit = parkhaus.getUhrzeit();
        this.datum = parkhaus.getDatum();
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    //public LocalTime getUhrzeit() {return uhrzeit;}
    public int getUhrzeitStunde() {
        return this.uhrzeit.getHour();
    }

    public int getUhrzeitMin() {
        return this.uhrzeit.getMinute();
    }

    public int getDatumTag() {
        return this.datum.getDayOfMonth();
    }

    public int getDatumMonat() {
        return datum.getMonthValue();
    }

    public int getDatumJahr() {
        return datum.getYear();
    }

    public LocalDate getDatum() {
        return datum;
    }

    public LocalTime getUhrzeit() {
        return uhrzeit;
    }

    public static void setIdentifikationsNummer() {
        identifikationsNummer = 0;
    }

    public String getUhrzeitString() {
        int h = this.getUhrzeitStunde();
        int m = this.getUhrzeitMin();
        return h + ":" + m;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setUhrzeit() {
        this.uhrzeit = parkhaus.getUhrzeit();
    }

    public String getArtDesParkplatzes() {
        return artDesParkplatzes;
    }

    public void setPreis(double preis) {
        this.preis = preis;
    }

    public double getPreis() {
        return this.preis;
    }

    public double getRabattProzent() {
        return rabattProzent;
    }

    public void setRabattProzent(double rabattProzent) {
        this.rabattProzent = rabattProzent;
    }

    public double getRabattEuro() {
        return rabattEuro;
    }

    public void setRabattEuro(double rabattEuro) {
        this.rabattEuro = rabattEuro;
    }

    public void setEntwertet(boolean ft) {
        this.entwertet = ft;
    }

    public boolean getEntwertet() {
        return this.entwertet;
    }

    public void setParkdauerMin(int dauer) {
        this.parkdauerMin += dauer;
    }

    public int getParkdauerMin() {
        return this.parkdauerMin;
    }
    //public double getPreisTicket() {return preisTicket;}

    @Override
    public String toString() {
        return "{" +
                "TicketID: " + ticketID +
                ", " + artDesParkplatzes + '\'' +
                ", " + this.getUhrzeitString() +
                ", entwertet: " + entwertet +
                '}';
    }


    //--------------------------------------------------------------------------------------------------------------------------------------
    //Was mit dem Parkhaus.Ticket passiert:

    /**
     * Die Methode 'entwerten' setzt die entwerten-Variable auf true
     * und aktualisiert die gespeicherte Zeit
     */
    @Override
    public void entwerten() {
        //this.zustand.entwerten();

        this.setEntwertet(true);
        this.setUhrzeit();
        System.out.println("Sie haben um " + getUhrzeitStunde() + ":" + getUhrzeitMin() + " Uhr bezahlt und koennen mit diesem Ticket in der naechsten Viertel Stunde die Schranke oeffnen.");

    }


    /**
     * Die Methode 'zeitDifferenz' vergleicht die Uhrzeit des Tickets mit der aktuellen Zeit und berechnet die Differenz zwischen beiden
     *
     * @return die Differenz zwischen der im Parkhaus.Ticket gespeicherten Uhrzeit und der aktuellen Zeit in Minuten
     */
    public int zeitDifferenz() {
        LocalTime now = parkhaus.getUhrzeit();
        int zeitJetzt = now.getMinute() + (now.getHour() * 60);
        int zeitTicket = this.uhrzeit.getMinute() + (this.uhrzeit.getHour() * 60);
        return zeitJetzt - zeitTicket;
    }

    /**
     * "ausfahren" prüft, ob das Ticket entwertet wurde und die Zeit zum ausfahren noch reicht. Wenn die Bedingungen nicht
     * erfüllt sind, wird ein entsprechender Hinweis ausgegeben. Ist das Ticket entwertet und die Viertelstunde noch nicht um,
     * wird das Ticket inaktiv und die Anzahl der freien Parkplätze wird um eins erhöht, entsprechend dem Parkplatz, der belegt war.
     *
     * @return Nachricht, die dem Besucher angezeigt werden soll
     */
    public String ausfahren() throws TicketNichtGefundenException, ParkhausGeschlossenException {
        if (this.parkhaus.getUhrzeit().isBefore(this.parkhaus.getOeffnungszeit()) | this.parkhaus.getUhrzeit().isAfter(this.parkhaus.getSchliessungszeit())) {
            throw new ParkhausGeschlossenException("Parkhaus ist geschlossen.");
        }

        String s = this.zustand.ausfahren();
        return s;
    }

    /**
     * Die Methode 'bezahleTicket' ...
     *
     * @return den zu bezahlenden Preis als double
     */
    public double bezahlen() throws TicketNichtGefundenException, ParkhausGeschlossenException {
        if (this.parkhaus.getUhrzeit().isBefore(this.parkhaus.getOeffnungszeit()) | this.parkhaus.getUhrzeit().isAfter(this.parkhaus.getSchliessungszeit())) {
            throw new ParkhausGeschlossenException("Parkhaus ist geschlossen.");
        }

        double preis = this.zustand.bezahlen();
        return preis;

    }
}