package Parkhaus;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
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
            Ticket t1 = p.neuesTicket("Normaler Parkplatz");
            LocalTime vergleichszeitStart = LocalTime.of(16, 30);
            LocalTime vergleichszeitSchluss = LocalTime.now();
            int stundenSum;
            int minSum;
            if (vergleichszeitSchluss.getMinute() >= vergleichszeitStart.getMinute()) {
                stundenSum = vergleichszeitSchluss.getHour() - vergleichszeitStart.getHour();
                minSum = (stundenSum * 60) + vergleichszeitSchluss.getMinute() - vergleichszeitStart.getMinute();
            } else {
                stundenSum = vergleichszeitSchluss.getHour() - vergleichszeitStart.getHour() - 1;
                minSum = (stundenSum * 60) + ((vergleichszeitSchluss.getMinute() + 60) - vergleichszeitStart.getMinute());
            }
            p.setUhrzeitManuell(16, 30);
            int dauer = t1.zeitDifferenz();
            assertEquals(minSum, dauer);
        }catch (ParkplaetzeBelegtException e) {
            System.out.println("Keine freien Parkplaetze");
        }catch (ParkhausGeschlossenException e) {
            System.out.println("Parkhaus geschlossen");}
    }

    @Test
    void bezahleTicketTest() {
        try {
            Parkhaus p = new Parkhaus(2.1);
            Ticket t = new Ticket("Normaler Parkplatz", p);
            p.setUhrzeitManuell(16, 30);
            t.bezahlen();

            // Teste ob 'preis' richtig berechnet wurde
            double erwarteterPreis = p.getStundentarif() * t.zeitDifferenz();
            assertEquals(erwarteterPreis, t.getPreis());

        } catch (TicketNichtGefundenException e1) {
            System.out.println("Ticket nicht gefunden.");
        } catch (ParkhausGeschlossenException e2) {
            System.out.println("Außerhalb der Öffnungszeiten");
        }
    }

    @Test
    void ausfahrenTest(){

        try {
            Parkhaus testParkhaus = new Parkhaus(2.0, 190, 0, 10, 0);
            Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
            Ticket testTicket2 = testParkhaus.neuesTicket("Behinderten-Parkplatz");

            //Ausfahren testen mit unbezahltem Ticket, sollte auch Hinweis ausgeben
            testTicket1.ausfahren();
            testTicket2.ausfahren();
            assertEquals(198, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(189, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierBehindertenParkplaetze());

            //Zeitschranke testen, sollte auch Hinweis über abgelaufene Zeit geben
            LocalTime testUhrzeit = LocalTime.now().minusMinutes(20);
            testParkhaus.setUhrzeitManuell(testUhrzeit.getHour(), testUhrzeit.getMinute());
            testTicket1.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(testUhrzeit.getHour(), testUhrzeit.getMinute());
            testTicket2.setEntwertet(true);
            testTicket1.ausfahren();
            testTicket2.ausfahren();
            assertEquals(198, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(189, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierBehindertenParkplaetze());

            //Zeitschranke testen, sollte die Autos nun rauslassen
            testTicket1.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testTicket1.ausfahren();
            assertEquals(199, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(190, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            testTicket2.setEntwertet(true);
            testParkhaus.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testTicket2.ausfahren();
            System.out.println(testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(200, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(10, testParkhaus.getAnzahlFreierBehindertenParkplaetze());
        }catch (ParkplaetzeBelegtException e1){
            System.out.println("Keine freien Parkplaetze");
        }catch (TicketNichtGefundenException e2){
            System.out.println("Ticket nicht gefunden");
        } catch (ParkhausGeschlossenException e3) {
            System.out.println("Außerhalb der Öffnungszeiten");
    }
    }

}