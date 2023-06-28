package parkhaus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

class TicketIFTest {


    @Test
    void entwertenTest(){
        try{
            Parkhaus p = new Parkhaus(3, 100, 5, 5, 10);
            Ticket t1 = p.neuesTicket("Normaler Parkplatz");
            Ticket t2 = p.neuesTicket("Normaler Parkplatz");
            t1.entwerten();
            assertTrue(t1.getEntwertet());
            assertFalse(t2.getEntwertet());
        }catch (ParkplaetzeBelegtException e) {
            System.out.println("Keine freien Parkplaetze");
        }catch (ParkhausGeschlossenException e) {
            System.out.println("Parkhaus geschlossen");}
    }


    @Test
    void zeitDifferenzTest(){

        try {
            Parkhaus p = new Parkhaus(3, 100, 5, 5, 10);
            Ticket t1 = p.neuesTicket("Normaler Parkplatz"); //Ticket zur Uhrzeit 8:00 uhr erstellen
            p.setUhrzeit(LocalTime.of(13,0));
            LocalTime vergleichszeitStart = LocalTime.of(8, 0);
            LocalTime vergleichszeitSchluss = p.getUhrzeit();
            int stundenSum;
            int minSum;
            if (vergleichszeitSchluss.getMinute() >= vergleichszeitStart.getMinute()) {
                stundenSum = vergleichszeitSchluss.getHour() - vergleichszeitStart.getHour();
                minSum = (stundenSum * 60) + vergleichszeitSchluss.getMinute() - vergleichszeitStart.getMinute();
            } else {
                stundenSum = vergleichszeitSchluss.getHour() - vergleichszeitStart.getHour() - 1;
                minSum = (stundenSum * 60) + ((vergleichszeitSchluss.getMinute() + 60) - vergleichszeitStart.getMinute());
            }

            int dauer = t1.zeitDifferenz();
            assertEquals(minSum, dauer);


            //Test mit Datum
            //ParkhausZeit wieder auf 8:00 Uhr zurück
            p.setUhrzeit(LocalTime.of(8,0));
            Ticket t2 = p.neuesTicket("Normaler-Parkplatz");

            //Parkhaus Datum ändern
            p.parkhauszeitAnpassen(LocalTime.of(9,0), LocalDate.of(2023, 1, 2));

            assertEquals(25*p.getStundentarif(), (int)t2.bezahlen());


        }catch (ParkplaetzeBelegtException e) {
            System.out.println("Keine freien Parkplaetze");
        }catch (ParkhausGeschlossenException e) {
            System.out.println("Parkhaus geschlossen");
        } catch (ReiseInVergangenheitException e) {
            System.out.println("Raum-Zeit-Kontinuum verletzt");
        } catch (TicketNichtGefundenException e) {
            System.out.println("Das Ticket existiert nicht");
        }
    }


    /**
     * Test-Methode zur Funktion Parkhauszeit anpassen.
     * Bei Umstellung der Zeit auf Vergangenheit soll eine Exception geworfen werden.
     *
     * @author jboven2s
     */
    @Test
    void bezahleTicketTestStateAktiv() {
        try {
            Parkhaus p = new Parkhaus(2.1);
            LocalTime t1 = LocalTime.of(12,00);
            LocalTime t2 = LocalTime.of(16,30);
            p.setUhrzeit(t1);
            Ticket t = new Ticket("Normaler Parkplatz", p);
            p.setUhrzeit(t2);
            t.bezahlen();

            // Teste ob 'preis' richtig berechnet wurde
            int h = t2.getHour() - t1.getHour();
            if (t2.getMinute() - t1.getMinute() % 60 != 0)
            {
                h++;
            }

            double erwarteterPreis = p.getStundentarif() * h;
            assertEquals(erwarteterPreis, t.getPreis());

        } catch (TicketNichtGefundenException e1) {
            System.out.println("Ticket nicht gefunden.");
        } catch (ParkhausGeschlossenException e2) {
            System.out.println("Außerhalb der Öffnungszeiten");
        }
    }

    /**
     *
     *
     */
    @Test
    void bezahleTicketTestStateEntwertet() {

        Parkhaus p = new Parkhaus(2.1);
        Ticket t = new Ticket("Normaler Parkplatz", p);
        StateAktiv a = new StateAktiv(t);
        t.zustand = new StateEntwertet(t, a);

        assertThrows(TicketNichtGefundenException.class, () -> t.bezahlen());
    }

    /**
     *
     *
     */
    @Test
    void bezahleTicketTestStateInaktiv() {

        Parkhaus p = new Parkhaus(2.1);
        Ticket t = new Ticket("Normaler Parkplatz", p);
        t.zustand = new StateInaktiv(t);

        assertThrows(TicketNichtGefundenException.class, () -> t.bezahlen());
    }


    @Test
    void ausfahrenTest(){

        try {
            Parkhaus testParkhaus = new Parkhaus(2.0, 10, 10, 10, 10);
            Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
            Ticket testTicket2 = testParkhaus.neuesTicket("Behinderten-Parkplatz");
            Ticket testTicket3 = testParkhaus.neuesTicket("E-Auto-Parkplatz");
            Ticket testTicket4 = testParkhaus.neuesTicket("Motorrad Parkplatz");

            //Ausfahren testen mit unbezahltem Ticket, sollte auch Hinweis ausgeben
            testTicket1.ausfahren();
            testTicket2.ausfahren();
            testTicket3.ausfahren();
            testTicket4.ausfahren();
            assertEquals(36, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierBehindertenParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierEAutoParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierMotorradParkplaetze());

            //Zeitschranke testen, sollte auch Hinweis über abgelaufene Zeit geben
            LocalTime testUhrzeit = LocalTime.now().minusMinutes(20);
            testParkhaus.setUhrzeitManuell(testUhrzeit.getHour(), testUhrzeit.getMinute());
            testTicket1.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(testUhrzeit.getHour(), testUhrzeit.getMinute());
            testTicket2.setEntwertet(true);
            testTicket1.ausfahren();
            testTicket2.ausfahren();
            assertEquals(36, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierBehindertenParkplaetze());

            //Zeitschranke testen, sollte die Autos nun ausfahren lassen
            testTicket1.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testTicket1.ausfahren();
            assertEquals(37, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(10, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            testTicket2.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testTicket2.ausfahren();
            System.out.println(testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(38, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(10, testParkhaus.getAnzahlFreierBehindertenParkplaetze());
            testTicket3.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testTicket3.ausfahren();
            assertEquals(39, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(10, testParkhaus.getAnzahlFreierEAutoParkplaetze());
            testTicket1.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testTicket1.ausfahren();
            assertEquals(40, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(10, testParkhaus.getAnzahlFreierMotorradParkplaetze());
        }catch (ParkplaetzeBelegtException e1){
            System.out.println("Keine freien Parkplaetze");
        }catch (TicketNichtGefundenException e2){
            System.out.println("Ticket nicht gefunden");
        } catch (ParkhausGeschlossenException e3) {
            System.out.println("Außerhalb der Öffnungszeiten");
        }
    }
    //Testet, ob die Methoden bezahlen und ausfahren die Exception werfen
    @Test
    void TicketNichtGefundenTest() throws ParkhausGeschlossenException, ParkplaetzeBelegtException {

        Parkhaus testParkhaus = new Parkhaus(3.0,10,0,0,0);
        Ticket testticket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
        assertThrows(TicketNichtGefundenException.class, () -> testticket1.ausfahren());
        testticket1.zustand = testticket1.zustand.getNext();
        assertThrows(TicketNichtGefundenException.class, () -> testticket1.bezahlen());
        testticket1.zustand = testticket1.zustand.getNext();
        assertThrows(TicketNichtGefundenException.class, () -> testticket1.ausfahren());
    }
}