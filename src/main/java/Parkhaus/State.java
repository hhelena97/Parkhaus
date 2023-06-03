package Parkhaus;

public class State {
    public State getNext() {
        return next;
    }

    public void setNext(State next) {
        this.next = next;
    }

    public State getPrevious() {
        return previous;
    }

    public void setPrevious(State previous) {
        this.previous = previous;
    }

    State next = null;
    State previous = null;
    Ticket ticket = null;

    //Konstruktor
    public State(Ticket t) {
        this.ticket = t;

    }

    public double bezahlen() throws TicketNichtGefundenException{
        return 0.0;
    }

    public String ausfahren() throws TicketNichtGefundenException {
        return "";
    }
}
