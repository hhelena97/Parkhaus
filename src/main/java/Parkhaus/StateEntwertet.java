package Parkhaus;

public class StateEntwertet extends State{
    public StateEntwertet(Ticket t, State aktiv) {
        super(t);
        this.next = new StateInaktiv(t);
        this.previous = aktiv;
    }


    public double bezahlen() {
        return 0;
    }

    public String ausfahren(Ticket t) {
        return null;
    }
}
