package Parkhaus;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class StateEntwertet extends State implements StateIF{
    public StateEntwertet(Ticket t, State aktiv) {
        super(t);
        this.next = new StateInaktiv(t);
        this.previous = aktiv;
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

        //Zustand nicht ändern
        //this.ticket.zustand = next;

        return ticket.getPreis();
    }

    public String ausfahren() {
        if(ticket == null){throw new TicketNichtGefundenException("Ticket nicht gefunden.");
        }else if (ticket.getEntwertet()) {
            LocalTime timeStamp = LocalTime.now().minusMinutes(15);
            LocalTime uhrzeit = ticket.getUhrzeit();
            if (uhrzeit.equals(timeStamp) || uhrzeit.isAfter(timeStamp)){
                //Parkplatz freigeben:
                String art = ticket.getArtDesParkplatzes();
                ticket.getParkhaus().setAnzahlFreierParkplaetze(ticket.getParkhaus().getAnzahlFreierParkplaetze()+1);
                //Für die speziellen Parkplätze:
                if (art.equals("Normaler Parkplatz")) {
                    ticket.getParkhaus().setAnzahlFreierNormalerParkplaetze((ticket.getParkhaus().getAnzahlFreierNormalerParkplaetze() + 1));
                } else if (art.equals("E-Auto-Parkplatz")) {
                    ticket.getParkhaus().setAnzahlFreierEAutoParkplaetze((ticket.getParkhaus().getAnzahlFreierEAutoParkplaetze() + 1));
                } else if (art.equals("Behinderten-Parkplatz")) {
                    ticket.getParkhaus().setAnzahlFreierBehindertenParkplaetze((ticket.getParkhaus().getAnzahlFreierBehindertenParkplaetze() + 1));
                } else {
                    ticket.getParkhaus().setAnzahlFreierMotorradParkplaetze((ticket.getParkhaus().getAnzahlFreierMotorradParkplaetze() + 1));
                }
                //ticket wird zu inaktiven tickets hinzugefügt
                ticket.getParkhaus().getInaktiveTickets().add(ticket);
                //ticket wird aus aktiven tickets rausgenommen
                ticket.getParkhaus().getAktiveTickets().remove(ticket);

                //Zustand ändern
                this.ticket.zustand = next;

                return"Auf Wiedersehen!";

            }
            else {
                ticket.setEntwertet(false);
                //Zustand zum vorherigen (aktiven) ändern
                this.ticket.zustand = previous;


                return"Zeit zum Ausfahren überschritten, Zeitstempel zurückgesetzt auf: " + ticket.getUhrzeit().truncatedTo(ChronoUnit.MINUTES) +". Bitte entwerten Sie das Ticket erneut am Automaten.";
            }

        }
        else {return"Ausfahrt nur mit entwertetem Ticket möglich.";}    }
    }

