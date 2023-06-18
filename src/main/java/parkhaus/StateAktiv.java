package parkhaus;

public class StateAktiv extends State{

    //Konstruktor
    public StateAktiv(Ticket t) {
        super(t);
        this.next = new StateEntwertet(t, this);
        this.previous = null;
    }

    //Methode bezahlen mit Zustandsänderung ergänzt
    public double bezahlen() {

        if (ticket.zeitDifferenz() >= (24*60))       // Wenn jemand über Nacht parkt
        {
            return 100.0;        // Fixbetrag, fürs Über-Nacht-Parken
        }

        int dauer = ticket.zeitDifferenz();
        int stunden = dauer/60;

        if (dauer%60 != 0) {stunden++;}
        // -> angefangene Stunden berücksichtigen

        double preis = (ticket.getParkhaus().getStundentarif() * stunden);
        ticket.setRabattEuro(preis * ticket.getRabattProzent());
        preis = preis - ticket.getRabattEuro();
        ticket.setPreis(preis);

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
