package parkhaus;

public class StateAktiv extends State{

    //Konstruktor
    public StateAktiv(Ticket t) {
        super(t);
        this.next = new StateEntwertet(t, this);
        this.previous = null;
    }

    /**
     * Die Methode 'bezahlen()' berechnet aus der Parkdauer des Tickets und dem Stundentarif des Parkhauses
     * den Preis des Tickets. Hierfür werden zuerst die Stunden ausgerechnet und damit der Preis bestimmt.
     * Danach wird ein eventuell vorliegender Rabatt in den Preis miteingerechnet und der Preis somit gesetzt.
     * Außerdem wird noch die Ticket-Zeit auf die aktuelle Parkhausszeit gesetzt, um so die 15 Minuten Zeitspanne
     * nach dem Bezahlen zum Ausfahren überprüfen zu können.
     *
     * @return den bezahlten Preis
     */
    public double bezahlen() {

        int dauer = ticket.zeitDifferenz();
        int stunden = dauer/60;

        if (dauer%60 != 0) {stunden++;}     // angefangene Stunden berücksichtigen


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

    /**
     * Die Methode 'ausfahren()' wirft eine Exception, da ausfahren in diesem State nicht erlaubt ist.
     *
     * @return Exceptionnachricht, dass ausfahren nicht möglich ist
     * @throws TicketNichtGefundenException
     */
    public String ausfahren() throws TicketNichtGefundenException{
        throw new TicketNichtGefundenException("Ausfahrt nur mit entwertetem Ticket möglich.");
    }
}
