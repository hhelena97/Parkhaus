import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket implements TicketIF{

    //private LocalTime parkzeitTicket;
    //private double preisTicket;
    private LocalDate datum;
    private LocalTime uhrzeit;
    private final String artDesParkplatzes;

    private double preis;
    private boolean entwertet;

    //Test-Konstruktor damit man sich nicht immer ein Ticket mit "Ticket-Art" erstellen muss zum Testen
    public Ticket(){
        datum = LocalDate.now();
        uhrzeit = LocalTime.now();
        //parkzeitTicket = LocalTime.now();
        artDesParkplatzes = "normaler Parkplatz";
        entwertet = false;
    }

    public Ticket(String art) {
        datum = LocalDate.now();
        uhrzeit = LocalTime.now();
        //parkzeitTicket = LocalTime.now();
        artDesParkplatzes = art;
        entwertet = false;
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
     * Die Methode 'zeitDifferenz' vergleicht die 'anfangsZeit' mit der aktuellen Zeit und berechnet die Differenz zwischen beiden
     *
     * @return die Differenz zwischen der mitgegebenen 'anfangszeit' und der aktuellen Zeit in Minuten
     */
    public int zeitDifferenz() {
        LocalTime now = LocalTime.now();
        int zeitJetzt = now.getMinute() + (now.getHour() * 60);
        int zeitTicket = this.uhrzeit.getMinute() + (this.uhrzeit.getHour() * 60);
        return zeitJetzt - zeitTicket;
    }
}

