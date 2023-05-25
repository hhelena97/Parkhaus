package Parkhaus;

public class StateInaktiv extends State{
    public StateInaktiv(Ticket t) {
        super(t);
        this.next = null;
        this.previous = null;
    }

    public double bezahlen() {
        return 0;
    }

    public String ausfahren() {
        return null;
    }
}
