package Parkhaus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Parkhaus implements ParkhausIF {

    private double stundentarif;    //kann man das final machen?
    private LocalTime uhrzeit;  //globale Uhrzeit des Parkhauses
    private LocalDate datum;  //globales Datum

    private int anzahlFreierParkplaetze; //insgesamt inkl. alle arten
    private int anzahlFreierNormalerParkplaetze; //anzahl normaler
    private int anzahlFreierEAutoParkplaetze; //anzahl e auto
    private int anzahlFreierBehindertenParkplaetze; //erklärt sich denke ich
    private int anzahlFreierMotorradParkplaetze; // -----------"--------------
    private int parkplaetzeGesamt; //Anzahl der Parkplätze insgesamt, ob frei oder besetzt

    //Liste mit allen Tickets von Autos, die sich zur Zeit im Parkhaus befinden
    private List<Ticket> aktiveTickets = new ArrayList<>();

    //Liste mit Tickets von Besuchern, die das Parkhaus verlassen haben
    private List<Ticket> inaktiveTickets = new ArrayList<>();
    private LocalTime oeffnungszeit;
    private LocalTime schliessungszeit;


    //brauchen wie beide Konstruktoren? //ja, ist hilfreich zum Testen
    public Parkhaus() {

        this.stundentarif = 0.0;
        oeffnungszeit = setUhrzeitManuell(8, 0);
        schliessungszeit = setUhrzeitManuell(23, 0);

        // Beim Erstellen des Parkhauses wird die Uhrzeit auf 8:00 Uhr gesetzt:
        this.uhrzeit = LocalTime.of(8, 0);
        // Beim Erstellen des Parkhauses wird das Datum auf 01.01.2023 gesetzt:
        this.datum = LocalDate.of(2023, 1, 1);
    }

    public Parkhaus(double stdTarif) {

        this();    // rufe den Konstruktor ohne Parameter auf
        this.stundentarif = stdTarif;
        oeffnungszeit = setUhrzeitManuell(8, 0);
        schliessungszeit = setUhrzeitManuell(23, 0);

        // Beim Erstellen des Parkhauses wird die Uhrzeit auf 8:00 Uhr gesetzt:
        this.uhrzeit = LocalTime.of(8, 0);
        // Beim Erstellen des Parkhauses wird das Datum auf 01.01.2023 gesetzt:
        this.datum = LocalDate.of(2023, 1, 1);
    }

    public Parkhaus(double stundentarif, int normaleParkplaetze, int eAutoParkplaetze, int behindertenParkplaetze, int motoradparkplaetze) {
        this.stundentarif = stundentarif;
        parkplaetzeGesamt = normaleParkplaetze + behindertenParkplaetze + eAutoParkplaetze + motoradparkplaetze;
        anzahlFreierParkplaetze = parkplaetzeGesamt;
        anzahlFreierNormalerParkplaetze = normaleParkplaetze;
        anzahlFreierEAutoParkplaetze = behindertenParkplaetze;
        anzahlFreierBehindertenParkplaetze = behindertenParkplaetze;
        anzahlFreierMotorradParkplaetze = motoradparkplaetze;

        oeffnungszeit = setUhrzeitManuell(8, 0);
        schliessungszeit = setUhrzeitManuell(23, 0);

        // Beim Erstellen des Parkhauses wird die Uhrzeit auf 8:00 Uhr gesetzt:
        this.uhrzeit = LocalTime.of(8, 0);
        // Beim Erstellen des Parkhauses wird das Datum auf 01.01.2023 gesetzt:
        this.datum = LocalDate.of(2023, 1, 1);
    }

    /**
     * Die Methode "neuesTicket" ruft den Konstruktor für ein neues Ticket auf (setzt die Uhrzeit auf die aktuelle
     * Uhrzeit, das Datum auf das aktuelle Datum und die Art auf den mitgegebenen String "art").
     * Danach wird die Anzahl der freien Parkplätze und die Anzahl der jeweiligen freien Plätze der bestimmten
     * Art um 1 verringert.
     *
     * @param art String in welchem steht, welche Art des Parkplatzes der Kunde gewählt hat
     * @return ein neues Ticket mit gesetzten Instanzvariablen
     */
    @Override
    public Ticket neuesTicket(String art) throws ParkplaetzeBelegtException, ParkhausGeschlossenException {

        //falls vor oder nach Öffnungszeit werfe Exception
        if (this.getUhrzeit().isBefore(this.getOeffnungszeit()) || this.getUhrzeit().isAfter(this.getSchliessungszeit()))
        {throw new ParkhausGeschlossenException("Das Parkhaus hat geschlossen.");
        }

        if (this.anzahlFreierParkplaetze == 0) {
            throw new ParkplaetzeBelegtException("Keine freien Parkplaetze verfuegbar!");
        }
        Ticket dasTicket = new Ticket(art, this);
        anzahlFreierParkplaetze--;
        if (art.equals("Normaler Parkplatz") && this.anzahlFreierNormalerParkplaetze == 0){
            throw new ParkplaetzeBelegtException("Keine freien normalen Parkplaetze verfuegbar!");
        } else {anzahlFreierNormalerParkplaetze--;}

        if (art.equals("E-Auto-Parkplatz") && this.anzahlFreierEAutoParkplaetze == 0) {
            throw new ParkplaetzeBelegtException("Keine freien E-Auto-Parkplaetze verfuegbar!");
        } else {anzahlFreierEAutoParkplaetze--;}

        if (art.equals("Behinderten-Parkplatz") && this.anzahlFreierBehindertenParkplaetze == 0) {
            throw new ParkplaetzeBelegtException("Keine freien Behindertenparkplaetze verfuegbar!");
        } else {anzahlFreierBehindertenParkplaetze--;}

        if(art.equals("Motorrad-Parkplatz") && this.anzahlFreierMotorradParkplaetze == 0) {
            throw new ParkplaetzeBelegtException("Keine freien Motorradparkplaetze verfuegbar!");
        } else {anzahlFreierMotorradParkplaetze--;}


        //in aktiveTickets Liste schieben
        aktiveTickets.add(dasTicket);
        return dasTicket;
    }

    /**
     * Die Methode 'parkhauszeitAnpassen()' lässt einen die globale Zeit und das globale Datum des Parkhauses anpassen.
     * Die Methode erfüllt die in Übung 8.1. geforderte Funktionalität.
     * Es ist möglich die Zeit und das Datum des Parkhauses in die Zukunft, nicht jedoch in die Vergangenheit zu verändern.
     * Wird versucht die Zeit in die Vergangenheit zu verändern, wird eine Exception geworfen.
     *
     * @param time - Neue Zeit, die gesetzt werden soll
     * @param date - Neues Datum, das gesetzt werden soll
     * @throws ReiseInVergangenheitException
     * @author jboven2s
     */
    public void parkhauszeitAnpassen(LocalTime time, LocalDate date) throws ReiseInVergangenheitException {

        // Wenn das Datum in der Vergangenheit liegt:
        if (date.isBefore(this.getDatum())) {
            throw new ReiseInVergangenheitException("Reise in die Vergangenheit nicht möglich!");
        }

        // Wenn das Datum das gleiche ist, die Zeit aber in der Vergangenheit liegt:
        if (date.equals(this.getDatum()) && time.isBefore(this.getUhrzeit())) {
            throw new ReiseInVergangenheitException("Reise in die Vergangenheit nicht möglich!");
        }

        // Wenn Datum und/oder Zeit in der Zukunft liegen:
        this.setUhrzeit(time);      // setze Zeit neu
        this.setDatum(date);        // setze Datum neu
    }

    public void oeffnungszeitenAendern(LocalTime oe, LocalTime sc) {
        this.oeffnungszeit = oe;
        this.schliessungszeit = sc;
    }


    // -----------------------------------------------------------------------------------------------------------------
    // String-Methoden:
    public String stringFuerAktiveTicketsAuflistung() {
        String htmlString = "";
        htmlString += "<h2>Zurzeit aktive Tickets: </h2>";
        int index = 0;
        for (Ticket i : this.getAktiveTickets()) {
            htmlString += "<p>Ticket-ID: " + this.getAktiveTickets().get(index).getTicketID() + ", ";
            htmlString += "Datum: " + this.getAktiveTickets().get(index).getDatum() + ", ";
            htmlString += "Ankunftszeit: " + this.getAktiveTickets().get(index).getUhrzeit().getHour();
            if (this.getAktiveTickets().get(index).getUhrzeit().getMinute() < 10) {
                htmlString += ":0" + this.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            } else {
                htmlString += ":" + this.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            }
            htmlString += "Parkplatzart: " + this.getAktiveTickets().get(index).getArtDesParkplatzes() + "</p>";
            index++;
        }
        return htmlString;
    }

    public String stringFuerInaktiveTicketsAuflistung() {
        String htmlString = "";
        int index = 0;
        for (Ticket i : this.getInaktiveTickets()) {
            htmlString += "<p>Ticket-ID: " + this.getInaktiveTickets().get(index).getTicketID() + ", ";
            htmlString += "Datum: " + this.getInaktiveTickets().get(index).getDatum() + ", ";
            htmlString += "Dauer: " + this.getInaktiveTickets().get(index).getParkdauerMin() + ", ";
            htmlString += "Preis: " + this.getInaktiveTickets().get(index).getPreis() + ", ";
            htmlString += "Parkplatzart: " + this.getInaktiveTickets().get(index).getArtDesParkplatzes() + "</p>";
            index++;
        }
        return htmlString;
    }

    public String getUhrzeitStringParkhaus(LocalTime time) {
        if (time.getMinute() < 10) {
            return time.getHour() + ":0" + time.getMinute();
        } else {
            return time.getHour() + ":" + time.getMinute();
        }
    }

    public String ausfahrenNachrichten(String nachricht) {
        return "<p>" + nachricht + "</p>";
    }

    /**
     * StringFuerStats berechnet alle Statistiken über Besucherzahlen, Einnahmen und Parkzeit,
     * die auf der Betreiberseite angezeigt werden und gibt sie als String zurück.
     * @return statsString ist der String zur Ausgabe inkl. Überschrift und Zeitstempel.
     * @author hheyen2s
     */
    public String stringFuerStats() {

        int sizeInaktiv = this.getInaktiveTickets().size();
        int besucherJetzt = this.getAktiveTickets().size();
        int besucherInsgesamt = besucherJetzt + sizeInaktiv;
        int besucherHeute = 0;
        int avParkdauer = 0;
        double avPreis = 0.0;

        LocalDate datumAktuell = this.getDatum();
        double einnahmenTag = 0;
        double einnahmenMonat = 0;
        double einnahmenInsgesamt = 0;

        if (sizeInaktiv != 0) {
            //Berechne Durchschnitt der Preise und Parkdauer
            for (int i = 0; i < sizeInaktiv; i++) {
                avParkdauer += this.getInaktiveTickets().get(i).getParkdauerMin();
                avPreis += this.getInaktiveTickets().get(i).getPreis();
            }
            avParkdauer /= sizeInaktiv;
            avPreis /= sizeInaktiv;

            //Berechne Gesamt- und Monatseinnahmen
            einnahmenInsgesamt = this.getInaktiveTickets().stream()
                    .map(Ticket::getPreis)
                    .reduce(0.0, Double::sum);

            einnahmenMonat = this.getInaktiveTickets().stream()
                    .filter(ticket -> ticket.getDatum().getYear() == datumAktuell.getYear()
                            && ticket.getDatum().getMonthValue() == datumAktuell.getMonthValue())
                    .map(Ticket::getPreis)
                    .reduce(0.0, Double::sum);

            //Berechne heutige Besucher und heutige Tageseinnahmen
            List<Ticket> heuteBezahlt = this.getInaktiveTickets().stream()
                    .filter(ticket -> ticket.getDatum().isEqual(datumAktuell))
                    .collect(Collectors.toList());
            besucherHeute = heuteBezahlt.size() + besucherJetzt;

            einnahmenTag = heuteBezahlt.stream()
                    .map(Ticket::getPreis)
                    .reduce(0.0, Double::sum);

        }

        String statsString = "Stand: " + datum + ", " + uhrzeit.truncatedTo(ChronoUnit.SECONDS) + "<br>";
        statsString += "<p>Besucherzahl aktuell: " + besucherJetzt + "<br>" + "Besucher heute: "
                + besucherHeute + "<br>" + "Besucher insgesamt: " + besucherInsgesamt + "<br>";
        statsString += "Tageseinnahmen: " + einnahmenTag + " Euro"+ "<br>" + "Monatseinnahmen: " + einnahmenMonat +
                " Euro<br>" + "Gesamteinnahmen: " + einnahmenInsgesamt + " Euro<br>";
        statsString += "Durchschnittliche Parkdauer: " + avParkdauer + " min<br>" + "Durchschnittlicher Ticketpreis: "
                + avPreis + " Euro</p>";

        return statsString;
    }


    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:
    // Parkplätze:
    public int getParkplaetzeGesamt() {
        return parkplaetzeGesamt;
    }
    public int getAnzahlFreierParkplaetze() {
        anzahlFreierParkplaetze = anzahlFreierNormalerParkplaetze + anzahlFreierEAutoParkplaetze
                + anzahlFreierBehindertenParkplaetze + anzahlFreierMotorradParkplaetze;
        return anzahlFreierParkplaetze;
    }
    public void setAnzahlFreierParkplaetze(int i) {
        this.anzahlFreierParkplaetze = i;
    }
    public int getAnzahlFreierNormalerParkplaetze() {
        return anzahlFreierNormalerParkplaetze;
    }
    public void setAnzahlFreierNormalerParkplaetze(int i) {
        this.anzahlFreierNormalerParkplaetze = i;
    }
    public void setAnzahlFreierEAutoParkplaetze(int i) {
        anzahlFreierEAutoParkplaetze = i;
    }
    public int getAnzahlFreierEAutoParkplaetze() {
        return anzahlFreierEAutoParkplaetze;
    }
    public int getAnzahlFreierBehindertenParkplaetze() {
        return anzahlFreierBehindertenParkplaetze;
    }
    public void setAnzahlFreierBehindertenParkplaetze(int i) {
        this.anzahlFreierBehindertenParkplaetze = i;
    }
    public int getAnzahlFreierMotorradParkplaetze() {
        return anzahlFreierMotorradParkplaetze;
    }
    public void setAnzahlFreierMotorradParkplaetze(int i) {
        this.anzahlFreierMotorradParkplaetze = i;
    }

    // Zeit und Datum:
    public LocalTime getOeffnungszeit() {
        return oeffnungszeit;
    }
    public void setOeffnungszeit(LocalTime oeffnungszeit) {
        this.oeffnungszeit = oeffnungszeit;
    }
    public LocalTime getSchliessungszeit() {
        return schliessungszeit;
    }
    public void setSchliessungszeit(LocalTime schliessungszeit) {
        this.schliessungszeit = schliessungszeit;
    }
    public LocalTime setUhrzeitManuell(int stunden, int minuten) {
        return LocalTime.of(stunden, minuten);
    }
    public LocalDate setDatumManuell(int tag, int monat, int jahr) {
        return LocalDate.of(jahr, monat, tag);
    }
    public LocalTime getUhrzeit() {
        return uhrzeit;
    }
    public void setUhrzeit(LocalTime uhrzeit) {
        this.uhrzeit = uhrzeit;
    }
    public LocalDate getDatum() {
        return datum;
    }
    public void setDatum(LocalDate datum) {
        this.datum = datum;
    }

    // Tarif:
    public double getStundentarif() {
        return stundentarif;
    }
    public void setStundentarif(double neuerPreis) {
        this.stundentarif = neuerPreis;
    }

    // Ticketlisten:
    public List<Ticket> getAktiveTickets() {
        return aktiveTickets;
    }
    public List<Ticket> getInaktiveTickets() {
        return inaktiveTickets;
    }
    public void resetTicketListen() {
        this.aktiveTickets = new ArrayList<>();
        this.inaktiveTickets = new ArrayList<>();
        Ticket.setIdentifikationsNummer();
    }

}