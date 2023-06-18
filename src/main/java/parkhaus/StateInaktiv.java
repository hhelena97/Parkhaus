package parkhaus;

public class StateInaktiv extends State{

    //Konstruktor
    public StateInaktiv(Ticket t) {
        super(t);
        this.next = null;
        this.previous = null;
    }

    public double bezahlen() throws TicketNichtGefundenException{
        throw new TicketNichtGefundenException("Ticket bereits bezahlt");
    }

    public String ausfahren() throws TicketNichtGefundenException{
       throw new TicketNichtGefundenException("Auto bereits ausgefahren");
    }
}
