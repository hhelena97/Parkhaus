package Parkhaus;

public class StateAktiv extends State{

    public StateAktiv(Ticket t) {
        super(t);
        this.next = new StateEntwertet(t, this);
        this.previous = null;
    }

    public double bezahlen() {



        this.ticket.zustand = next;
    }



    public String ausfahren(Ticket t) {
        return null;
    }

}
