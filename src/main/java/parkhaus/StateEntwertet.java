package parkhaus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class StateEntwertet extends State{

    //Konstruktor
    public StateEntwertet(Ticket t, State aktiv) {
        super(t);
        this.next = new StateInaktiv(t);
        this.previous = aktiv;
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
     * Die Methode ausfahren prüft, ob die Voraussetzungen zum Verlassen des Parkhauses erfüllt sind. Wenn das Auto
     * ausfahren kann, wird der entsprechende Parkplatz frei gemacht und das Ticket wird inaktiv.
     * @return ein String, der entweder angibt, dass das Ticket nicht entwertet ist, die Zeit zum Ausfahren
     * überschritten ist, oder Auf Wiedersehen wünscht. Der jeweilige String kann auf der Parkhausseite ausgegeben
     * werden.
     * @throws TicketNichtGefundenException falls das Ticket nicht gefunden wurde
     * @author hheyen2s
     */
    public String ausfahren() throws TicketNichtGefundenException{

        //Teste, ob das Ticket gefunden wurde
        if(ticket == null){throw new TicketNichtGefundenException("Ticket nicht gefunden.");
        }else if (ticket.getEntwertet()) {
            LocalTime timeStamp = ticket.getParkhaus().getUhrzeit().minusMinutes(15);
            LocalDate dateStamp = ticket.getParkhaus().getDatum();
            LocalTime uhrzeit = ticket.getUhrzeit();
            LocalDate datum = ticket.getDatum();
            if ((uhrzeit.equals(timeStamp) || uhrzeit.isAfter(timeStamp)) && datum.equals(dateStamp)){
                //Parkplatz freigeben:
                String art = ticket.getArtDesParkplatzes();
                ticket.getParkhaus().setAnzahlFreierParkplaetze(ticket.getParkhaus().getAnzahlFreierParkplaetze()+1);
                //Freigeben der speziellen Parkplätze:
                if (art.equals("Normaler Parkplatz")) {
                    ticket.getParkhaus().setAnzahlFreierNormalerParkplaetze(
                            (ticket.getParkhaus().getAnzahlFreierNormalerParkplaetze() + 1));
                } else if (art.equals("E-Auto-Parkplatz")) {
                    ticket.getParkhaus().setAnzahlFreierEAutoParkplaetze(
                            (ticket.getParkhaus().getAnzahlFreierEAutoParkplaetze() + 1));
                } else if (art.equals("Behinderten-Parkplatz")) {
                    ticket.getParkhaus().setAnzahlFreierBehindertenParkplaetze(
                            (ticket.getParkhaus().getAnzahlFreierBehindertenParkplaetze() + 1));
                } else {
                    ticket.getParkhaus().setAnzahlFreierMotorradParkplaetze(
                            (ticket.getParkhaus().getAnzahlFreierMotorradParkplaetze() + 1));
                }
                //Das Ticket wird zu inaktiven Tickets hinzugefügt
                ticket.getParkhaus().getInaktiveTickets().add(ticket);
                //Das Ticket wird aus aktiven Tickets rausgenommen
                ticket.getParkhaus().getAktiveTickets().remove(ticket);

                //Zustand ändern
                this.ticket.zustand = next;

                return"Auf Wiedersehen!";

            }else {
                ticket.setEntwertet(false);
                //In vorherigen Zustand zurückkehren
                this.ticket.zustand = previous;


                return"Zeit zum Ausfahren überschritten, Zeitstempel zurückgesetzt auf: "
                        + ticket.getUhrzeit().truncatedTo(ChronoUnit.MINUTES)
                        +". Bitte entwerten Sie das Ticket erneut am Automaten.";
            }

        }
        else {return"Ausfahrt nur mit entwertetem Ticket möglich.";}}
    }
