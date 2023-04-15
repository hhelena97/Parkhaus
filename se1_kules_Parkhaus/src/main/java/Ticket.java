import java.time.LocalDate;
import java.time.LocalTime;

public class Ticket {

    private double parkzeitTicket;
    private double preisTicket;
    private LocalDate datum;
    private LocalTime uhrzeit;
    private String artDesParkplatzes;

    public Ticket(String art) {
        datum = LocalDate.now();
        uhrzeit = LocalTime.now();
        artDesParkplatzes = art;
    }

    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    public double getParkzeitTicket() {
        return parkzeitTicket;
    }

    public double getPreisTicket() {
        return preisTicket;
    }

    public LocalDate getDatum() {
        return datum;
    }

    public LocalTime getUhrzeit() {
        return uhrzeit;
    }

    public String getArtDesParkplatzes() {
        return artDesParkplatzes;
    }
}
