package parkhaus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Ticket implements TicketIF {

    State zustand = null; //Zustand des Tickets (aktiv, entwertet, inaktiv)

    private LocalDate datum;        // Datum des Ticktes
    private LocalTime uhrzeit;      // Uhrzeit des Tickets
    private final String artDesParkplatzes;
    private double preis;           // Preis des Tickets


    private double rabattProzent;   // Rabatt des Tickets in Prozent
    private double rabattEuro;      // Rabatt des Tickets in Euro
    private boolean entwertet;
    private int parkdauerMin;       // Parkdauer des Tickets in Minuten
    private int ticketID;           // ID des Tickets
    private static int identifikationsNummer = 0; //zur Vergabe der Ticket-ID als Klassenvariable

    public Parkhaus getParkhaus() {
        return parkhaus;
    }

    private Parkhaus parkhaus;

    /*
    //Test-Konstruktor damit man sich nicht immer ein Ticket mit "Ticket-Art" erstellen muss zum Testen
    public Ticket() {
        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = "normaler Parkplatz";
        this.parkdauerMin = 0;
        this.entwertet = false;

        this.uhrzeit = parkhaus.getUhrzeit();       // nehme als Ticketzeit die aktuelle Parkhauszeit
        this.datum = parkhaus.getDatum();           // nehme als Ticketdatum das aktuelle Parkhausdatum
    }*/

    public Ticket(String art, Parkhaus parkhaus) {

        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = art;
        this.parkdauerMin = 0;
        this.entwertet = false;
        this.parkhaus = parkhaus;

        this.uhrzeit = parkhaus.getUhrzeit();       // nehme als Ticketzeit die aktuelle Parkhauszeit
        this.datum = parkhaus.getDatum();           // nehme als Ticketdatum das aktuelle Parkhausdatum

        this.zustand = new StateAktiv(this);
    }


    //------------------------------------------------------------------------------------------------------------------
    //Was mit dem Parkhaus.Ticket passiert:

    /**
     * Die Methode 'entwerten' setzt die entwerten-Variable auf true
     * und aktualisiert die gespeicherte Zeit
     */
    @Override
    public void entwerten() {
        this.setEntwertet(true);
        this.setUhrzeit();
        System.out.println("Sie haben um " + getUhrzeitStunde() + ":" + getUhrzeitMin() +
                " Uhr bezahlt und koennen mit diesem Ticket in der naechsten Viertel Stunde die Schranke oeffnen.");
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
        if(parkhaus.getDatum().equals(this.getDatum())) {
            return zeitJetzt - zeitTicket;
        } else {
            long daysBetween = ChronoUnit.DAYS.between(this.getDatum(), parkhaus.getDatum());
            int days = (int)daysBetween * 1440;  // 24 * 60 = 1440
            return (zeitJetzt - zeitTicket) + days;

        }
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

        return this.zustand.ausfahren();
    }

    /**
     * Die Methode 'bezahlen' ruft die 'bezahlen'-Methode des Zustandes auf, in dem sich das Ticket aktuell befindet.
     *
     * @return den zu bezahlenden Preis als double
     */
    public double bezahlen() throws TicketNichtGefundenException, ParkhausGeschlossenException
    {
        if (this.parkhaus.getUhrzeit().isBefore(this.parkhaus.getOeffnungszeit()) || this.parkhaus.getUhrzeit().isAfter(this.parkhaus.getSchliessungszeit()))
        {
            throw new ParkhausGeschlossenException("Parkhaus ist geschlossen.");
        }

        return this.zustand.bezahlen();     // rufe bezahlen Methode des Zustands auf + gebe den zu bezahlenden Preis zurück
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    public int getUhrzeitStunde() {
        return this.uhrzeit.getHour();
    }

    public int getUhrzeitMin() {
        return this.uhrzeit.getMinute();
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

    public static int getIdentifikationsNummer() {
        return identifikationsNummer;
    }

    public String getUhrzeitString() {
        int h = this.getUhrzeitStunde();
        int m = this.getUhrzeitMin();
        return h + ":" + m;
    }

    public int getTicketID() {
        return ticketID;
    }

    public void setUhrzeit() {this.uhrzeit = parkhaus.getUhrzeit();}

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

    public double getRabattEuro() {return rabattEuro;}

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


    //------------------------------------------------------------------------------------------------------------------
    // to-String Methode:
    @Override
    public String toString() {
        return "{" +
                "TicketID: " + ticketID +
                ", " + artDesParkplatzes + '\'' +
                ", " + this.getUhrzeitString() +
                ", entwertet: " + entwertet +
                '}';
    }
}