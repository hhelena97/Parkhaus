import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket implements TicketIF{

    //private LocalTime parkzeitTicket;
    //private double preisTicket;
    private LocalDate datum;
    private LocalTime uhrzeit;
    private final String artDesParkplatzes;

    private boolean entwertet;

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

    public LocalTime getUhrzeit() {
        return uhrzeit;
    }

    public int getUhrzeitStunde() {return this.uhrzeit.getHour();}
    public int getUhrzeitMin(){return this.uhrzeit.getMinute();}

    public void setUhrzeit() { this.uhrzeit = LocalTime.now();};

    public LocalDate getDatum() {
        return datum;
    }


    //public double getPreisTicket() {return preisTicket;}

    public String getArtDesParkplatzes() {
        return artDesParkplatzes;
    }

    public void setEntwertet(boolean ft) {this.entwertet = ft;};
    public boolean getEntwertet() {return this.entwertet;};

    //------------------------------------------------------------------
    //Was mit dem Ticket passiert:
    @Override
    public void entwerten() {
        this.entwertet = true;
        this.setUhrzeit();
        System.out.println("Sie haben um " + getUhrzeitStunde() + ":" + getUhrzeitMin() + " Uhr bezahlt und koennen mit diesem Ticket in der naechsten Viertel Stunde die Schranke oeffnen.");
    }


    //--------------------------------------------------------------------------------------------------------
    //Methode für Zeit

    /**
     * Methode bekommt zwei Parameter, Anfangs- und Endzeit und gibt die verstrichene Zeit zwischen den beiden zurück
     */

    public int zeitDifferenz(LocalTime anfangsZeit) {
        LocalTime now = LocalTime.now();
        int zeitJetzt = now.getMinute() + (now.getHour() * 60);
        int zeitTicket = this.uhrzeit.getMinute() + (this.uhrzeit.getHour() * 60);
        return zeitJetzt - zeitTicket;
    }
}

