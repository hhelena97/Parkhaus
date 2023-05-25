package Parkhaus;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class StateAktiv extends State{

    public StateAktiv(Ticket t) {
        super(t);
        this.next = new StateEntwertet(t, this);
        this.previous = null;
    }

    public double bezahlen() {
        int dauer = ticket.zeitDifferenz();
        int stunden = dauer/60;

        if (dauer%60 != 0) {stunden++;}
        // -> angefangene Stunden berücksichtigen

        double preis = (ticket.getParkhaus().getStundentarif() * stunden);
        double rabatt = preis * ticket.getRabatt();
        preis = preis - rabatt;
        ticket.setPreis(preis);

        //'preis' auf 'einnahmenTag' rechnen
        ticket.getParkhaus().setEinnahmenTag(ticket.getParkhaus().getEinnahmenTag() + preis);

        //set parkdauer zur späteren Auswertung
        ticket.setParkdauerMin(dauer);

        //in Real erst nach dem Bezahlen
        ticket.entwerten();

        //Zustand ändern
        this.ticket.zustand = next;

        return ticket.getPreis();
    }



    public String ausfahren() throws TicketNichtGefundenException{
        throw new TicketNichtGefundenException("Ausfahrt nur mit entwertetem Ticket möglich.");
    }
}