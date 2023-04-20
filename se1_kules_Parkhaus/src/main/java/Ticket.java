import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket implements TicketIF{

    private LocalDate datum;
    private LocalTime uhrzeit;
    private final String artDesParkplatzes;
    private double preis;
    private boolean entwertet;
    private int ticketID;
    private static int identifikationsNummer = 0;

    //Test-Konstruktor damit man sich nicht immer ein Ticket mit "Ticket-Art" erstellen muss zum Testen
    public Ticket(){
        this.datum = LocalDate.now();
        this.uhrzeit = LocalTime.now();
        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = "normaler Parkplatz";
        entwertet = false;
    }

    public Ticket(String art) {
        this.datum = LocalDate.now();
        this.uhrzeit = LocalTime.now();
        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = art;
        this.entwertet = false;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    public LocalTime getUhrzeit() {return uhrzeit;}
    public int getUhrzeitStunde() {return this.uhrzeit.getHour();}
    public int getUhrzeitMin(){return this.uhrzeit.getMinute();}
    public void setUhrzeit() { this.uhrzeit = LocalTime.now();};

    public void setUhrzeitManuell(int stunden, int minuten){this.uhrzeit = LocalTime.of(stunden, minuten);};
    public LocalDate getDatum() {
        return datum;
    }
    public String getArtDesParkplatzes() {
        return artDesParkplatzes;
    }

    public void setPreis(double preis){this.preis = preis;};
    public double getPreis(){return this.preis;};

    public void setEntwertet(boolean ft) {this.entwertet = ft;};
    public boolean getEntwertet() {return this.entwertet;};
    //public double getPreisTicket() {return preisTicket;}


    //--------------------------------------------------------------------------------------------------------------------------------------
    //Was mit dem Ticket passiert:
    /**
     * Die Methode 'entwerten' setzt die entwerten-Variable auf true
     * und aktualisiert die gespeicherte Zeit
     */
    @Override
    public void entwerten() {
        this.setEntwertet(true);
        this.setUhrzeit();
        System.out.println("Sie haben um " + getUhrzeitStunde() + ":" + getUhrzeitMin() + " Uhr bezahlt und koennen mit diesem Ticket in der naechsten Viertel Stunde die Schranke oeffnen.");
    }


    /**
     * Die Methode 'zeitDifferenz' vergleicht die Uhrzeit des Tickets mit der aktuellen Zeit und berechnet die Differenz zwischen beiden
     *
     * @return die Differenz zwischen der im Ticket gespeicherten Uhrzeit und der aktuellen Zeit in Minuten
     */
    public int zeitDifferenz() {
        LocalTime now = LocalTime.now();
        int zeitJetzt = now.getMinute() + (now.getHour() * 60);
        int zeitTicket = this.uhrzeit.getMinute() + (this.uhrzeit.getHour() * 60);
        return zeitJetzt - zeitTicket;
    }
}

