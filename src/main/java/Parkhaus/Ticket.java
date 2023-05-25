package Parkhaus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class Ticket implements TicketIF {

    State zustand = null;

    private LocalDate datum;
    private LocalTime uhrzeit;
    private final String artDesParkplatzes;
    private double preis;
    private double rabatt;
    private boolean entwertet;
    private int parkdauerMin;
    private int ticketID;
    private static int identifikationsNummer = 0;
    public Parkhaus getParkhaus() {
        return parkhaus;
    }

    private Parkhaus parkhaus;

    //Test-Konstruktor damit man sich nicht immer ein Parkhaus.Ticket mit "Parkhaus.Ticket-Art" erstellen muss zum Testen
    public Ticket(){
        this.datum = LocalDate.now();
        this.uhrzeit = LocalTime.now();
        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = "normaler Parkplatz";
        this.parkdauerMin = 0;
        this.entwertet = false;
    }

    public Ticket(String art, Parkhaus parkhaus) {
        this.datum = LocalDate.now();
        this.uhrzeit = LocalTime.now();
        this.ticketID = identifikationsNummer++;
        this.artDesParkplatzes = art;
        this.parkdauerMin = 0;
        this.entwertet = false;
        this.parkhaus = parkhaus;
    }



    // -----------------------------------------------------------------------------------------------------------------
    // Getter und Setter:

    public LocalTime getUhrzeit() {return uhrzeit;}
    public int getUhrzeitStunde() {return this.uhrzeit.getHour();}
    public int getUhrzeitMin(){return this.uhrzeit.getMinute();}

    public static void setIdentifikationsNummer(){
        identifikationsNummer = 0;
    }

    public String getUhrzeitString() {
        int h = this.getUhrzeitStunde();
        int m = this.getUhrzeitMin();
        return h + ":" + m;
    }

    public int getTicketID() {
        return ticketID;
    }
    public void setUhrzeit() { this.uhrzeit = LocalTime.now();};
    public void setUhrzeitManuell(int stunden, int minuten){this.uhrzeit = LocalTime.of(stunden, minuten);};
    public LocalDate getDatum() {
        return datum;
    }
    public String getArtDesParkplatzes() {
        return artDesParkplatzes;
    }
    public void setPreis(double preis){this.preis = preis;}
    public double getPreis(){return this.preis;}
    public double getRabatt() {return rabatt;}
    public void setRabatt(double rabatt) {this.rabatt = rabatt;}
    public void setEntwertet(boolean ft) {this.entwertet = ft;}
    public boolean getEntwertet() {return this.entwertet;}
    public void setParkdauerMin(int dauer) {this.parkdauerMin += dauer;}
    public int getParkdauerMin() {return this.parkdauerMin;}
    //public double getPreisTicket() {return preisTicket;}

    @Override
    public String toString() {
        return "{" +
                "TicketID: " + ticketID +
                ", " + artDesParkplatzes + '\'' +
                ", " + this.getUhrzeitString() +
                ", entwertet: " + entwertet +
                '}';
    }


    //--------------------------------------------------------------------------------------------------------------------------------------
    //Was mit dem Parkhaus.Ticket passiert:
    /**
     * Die Methode 'entwerten' setzt die entwerten-Variable auf true
     * und aktualisiert die gespeicherte Zeit
     */
    @Override
    public void entwerten() {
        //this.zustand.entwerten();

        this.setEntwertet(true);
        this.setUhrzeit();
        System.out.println("Sie haben um " + getUhrzeitStunde() + ":" + getUhrzeitMin() + " Uhr bezahlt und koennen mit diesem Parkhaus.Ticket in der naechsten Viertel Stunde die Parkhaus.Schranke oeffnen.");

    }



    /**
     * Die Methode 'zeitDifferenz' vergleicht die Uhrzeit des Tickets mit der aktuellen Zeit und berechnet die Differenz zwischen beiden
     *
     * @return die Differenz zwischen der im Parkhaus.Ticket gespeicherten Uhrzeit und der aktuellen Zeit in Minuten
     */
    public int zeitDifferenz() {
        LocalTime now = LocalTime.now();
        int zeitJetzt = now.getMinute() + (now.getHour() * 60);
        int zeitTicket = this.uhrzeit.getMinute() + (this.uhrzeit.getHour() * 60);
        return zeitJetzt - zeitTicket;
    }

    /**
     * "ausfahren" prüft, ob das Ticket entwertet wurde und die Zeit zum ausfahren noch reicht. Wenn die Bedingungen nicht
     * erfüllt sind, wird ein entsprechender Hinweis ausgegeben. Ist das Ticket entwertet und die Viertelstunde noch nicht um,
     * wird das Ticket inaktiv und die Anzahl der freien Parkplätze wird um eins erhöht, entsprechend dem Parkplatz, der belegt war.
     * @return Nachricht, die dem Besucher angezeigt werden soll
     */
    public String ausfahren() throws TicketNichtGefundenException{

        if(this == null){throw new TicketNichtGefundenException("Ticket nicht gefunden.");
        }else if (this.getEntwertet()) {
            LocalTime timeStamp = LocalTime.now().minusMinutes(15);
            LocalTime uhrzeit = this.getUhrzeit();
            if (uhrzeit.equals(timeStamp) || uhrzeit.isAfter(timeStamp)){
                //Parkplatz freigeben:
                String art = this.getArtDesParkplatzes();
                parkhaus.setAnzahlFreierParkplaetze(parkhaus.getAnzahlFreierParkplaetze()+1);
                //Für die speziellen Parkplätze:
                if (art.equals("Normaler Parkplatz")) {
                    parkhaus.setAnzahlFreierNormalerParkplaetze((parkhaus.getAnzahlFreierNormalerParkplaetze() + 1));
                } else if (art.equals("E-Auto-Parkplatz")) {
                    parkhaus.setAnzahlFreierEAutoParkplaetze((parkhaus.getAnzahlFreierEAutoParkplaetze() + 1));
                } else if (art.equals("Behinderten-Parkplatz")) {
                    parkhaus.setAnzahlFreierBehindertenParkplaetze((parkhaus.getAnzahlFreierBehindertenParkplaetze() + 1));
                } else {
                    parkhaus.setAnzahlFreierMotorradParkplaetze((parkhaus.getAnzahlFreierMotorradParkplaetze() + 1));
                }
                //ticket wird zu inaktiven tickets hinzugefügt
                parkhaus.getInaktiveTickets().add(this);
                //ticket wird aus aktiven tickets rausgenommen
                parkhaus.getAktiveTickets().remove(this);

                return"Auf Wiedersehen!";

            }
            else {
                this.setEntwertet(false);
                return"Zeit zum Ausfahren überschritten, Zeitstempel zurückgesetzt auf: " + this.getUhrzeit().truncatedTo(ChronoUnit.MINUTES) +". Bitte entwerten Sie das Ticket erneut am Automaten.";
            }

        }
        else {return"Ausfahrt nur mit entwertetem Ticket möglich.";}
    }

    /**
     * Die Methode 'bezahleTicket' ...
     * @return den zu bezahlenden Preis als double
     */
    public double bezahlen() {


        int dauer = this.zeitDifferenz();
        int stunden = dauer/60;

        if (dauer%60 != 0) {stunden++;}
        // -> angefangene Stunden berücksichtigen

        double preis = (parkhaus.getStundentarif() * stunden);
        double rabatt = preis * this.getRabatt();
        preis = preis - rabatt;
        this.setPreis(preis);

        //'preis' auf 'einnahmenTag' rechnen
        parkhaus.setEinnahmenTag(parkhaus.getEinnahmenTag() + preis);

        //set parkdauer zur späteren Auswertung
        this.setParkdauerMin(dauer);

        //in Real erst nach dem Bezahlen
        this.entwerten();
        return this.getPreis();
    }
}

