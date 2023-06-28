package parkhaus;

public class StateInaktiv extends State{

    //Konstruktor
    public StateInaktiv(Ticket t) {
        super(t);
        this.next = null;
        this.previous = null;
    }

    /**
     * Die Methode 'bezahlen()' wirft eine Exception, da bezahlen in diesem State nicht erlaubt ist.
     *
     * @return Exceptionnachricht, dass das Ticket schon bezahlt ist
     * @throws TicketNichtGefundenException
     */
    public double bezahlen() throws TicketNichtGefundenException{
        throw new TicketNichtGefundenException("Ticket bereits bezahlt");
    }

    /**
     * Die Methode 'ausfahren()' wirft eine Exception, da ausfahren in diesem State nicht erlaubt ist.
     *
     * @return Exceptionnachricht, dass ausfahren nicht möglich ist
     * @throws TicketNichtGefundenException
     */
    public String ausfahren() throws TicketNichtGefundenException{
       throw new TicketNichtGefundenException("Auto bereits ausgefahren");
    }
}
