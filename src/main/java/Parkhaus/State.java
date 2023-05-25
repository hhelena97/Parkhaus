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

    public State(Ticket t) {
        this.ticket = t;

    }

}
