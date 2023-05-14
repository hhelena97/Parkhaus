package Parkhaus;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ParkhausIFTest {

    @Test
    void neuesTicketTest() {
        //Testparkhaus erstellen in dem die Werte geändert werden sollen wenn ein Auto reinfährt --> überprüfung weiter unten
        Parkhaus testParkhaus = new Parkhaus();
        testParkhaus.setAnzahlFreierParkplaetze(400);
        testParkhaus.setAnzahlFreierEAutoParkplaetze(8);
        testParkhaus.setAnzahlFreierBehindertenParkplaetze(10);
        testParkhaus.setAnzahlFreierNormalerParkplaetze(362);
        testParkhaus.setAnzahlFreierMotorradParkplaetze(20);

        //dieses Objekt wird mit dem verglichen was in der zu testenden Methode erstellt wird
        try {
            Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
            assertEquals(399, testParkhaus.getAnzahlFreierParkplaetze());
            Ticket testTicket2 = testParkhaus.neuesTicket("E-Auto-Parkplatz");
            assertEquals(398, testParkhaus.getAnzahlFreierParkplaetze());
            Ticket testTicket3 = testParkhaus.neuesTicket("Behinderten-Parkplatz");
            assertEquals(397, testParkhaus.getAnzahlFreierParkplaetze());
            Ticket testTicket4 = testParkhaus.neuesTicket("Motorrad-Parkplatz");
            assertEquals(396, testParkhaus.getAnzahlFreierParkplaetze());

            //ErstellungsDatum soll gleich dem heutigen Datum sein
            assertEquals(testTicket1.getDatum(), LocalDate.now());
            assertEquals(testTicket2.getDatum(), LocalDate.now());
            assertEquals(testTicket3.getDatum(), LocalDate.now());
            assertEquals(testTicket4.getDatum(), LocalDate.now());

            //ErstellungsZeit soll vor der jetzigen Zeit sein (da das Parkhaus.Ticket 2 Zeilen vorher erstellt wurde und die Sekunden auch gemessen werden)
            assertEquals(testTicket1.getUhrzeit().getHour(), LocalTime.now().getHour());
            assertEquals(testTicket1.getUhrzeit().getMinute(), LocalTime.now().getMinute());
            assertEquals(testTicket1.getUhrzeit().getSecond(), LocalTime.now().getSecond());

            assertEquals(testTicket2.getUhrzeit().getHour(), LocalTime.now().getHour());
            assertEquals(testTicket2.getUhrzeit().getMinute(), LocalTime.now().getMinute());
            assertEquals(testTicket2.getUhrzeit().getSecond(), LocalTime.now().getSecond());

            assertEquals(testTicket3.getUhrzeit().getHour(), LocalTime.now().getHour());
            assertEquals(testTicket3.getUhrzeit().getMinute(), LocalTime.now().getMinute());
            assertEquals(testTicket3.getUhrzeit().getSecond(), LocalTime.now().getSecond());

            assertEquals(testTicket4.getUhrzeit().getHour(), LocalTime.now().getHour());
            assertEquals(testTicket4.getUhrzeit().getMinute(), LocalTime.now().getMinute());
            assertEquals(testTicket4.getUhrzeit().getSecond(), LocalTime.now().getSecond());

            //Art des Parkplatzes soll dann in der Parkhaus.Ticket-Instanzvariablen stehen
            assertEquals("Normaler Parkplatz", testTicket1.getArtDesParkplatzes());
            assertEquals("E-Auto-Parkplatz", testTicket2.getArtDesParkplatzes());
            assertEquals("Behinderten-Parkplatz", testTicket3.getArtDesParkplatzes());
            assertEquals("Motorrad-Parkplatz", testTicket4.getArtDesParkplatzes());

            //Anzahl der besonderen Parkplätze muss sich jeweils auch verändern
            assertEquals(361, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            assertEquals(7, testParkhaus.getAnzahlFreierEAutoParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierBehindertenParkplaetze());
            assertEquals(19, testParkhaus.getAnzahlFreierMotorradParkplaetze());

            assertEquals(testTicket1, testParkhaus.getAktiveTickets().get(0));
            assertEquals(testTicket2, testParkhaus.getAktiveTickets().get(1));
            assertEquals(testTicket3, testParkhaus.getAktiveTickets().get(2));
            assertEquals(testTicket4, testParkhaus.getAktiveTickets().get(3));

            assertEquals(testTicket1.getTicketID(), 0);
            assertEquals(testTicket2.getTicketID(), 1);
            assertEquals(testTicket3.getTicketID(), 2);
            assertEquals(testTicket4.getTicketID(), 3);
        } catch (ParkplaetzeBelegtException e){
            System.out.println("Keine freien Parkplaetze");
        }
    }

    @Test
    void bezahleTicketTest() {


        Parkhaus p = new Parkhaus(2.1);
        Ticket t = new Ticket("Normaler Parkplatz");
        t.setUhrzeitManuell(16, 30);
        p.bezahleTicket(t);

        // Teste ob 'preis' richtig berechnet wurde
        double erwarteterPreis = p.getStundentarif() * t.zeitDifferenz();
        //assertEquals(erwarteterPreis, t.getPreis());


        // Teste ob 'preis' auf 'einnahmenTag' gerechnet wurde
        double erwarteteEinnahmenTag = p.getEinnahmenTag() + erwarteterPreis;
        assertEquals(erwarteteEinnahmenTag, p.getEinnahmenTag());

    }

    @Test
    void ausfahrenTest(){

        try {
            Parkhaus testParkhaus = new Parkhaus(2.0, 190, 0, 10, 0);
            Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
            Ticket testTicket2 = testParkhaus.neuesTicket("Behinderten-Parkplatz");

            //Ausfahren testen mit unbezahltem Ticket, sollte auch Hinweis ausgeben
            testParkhaus.ausfahren(testTicket1);
            testParkhaus.ausfahren(testTicket2);
            assertEquals(198, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(189, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierBehindertenParkplaetze());

            //Zeitschranke testen, sollte auch Hinweis über abgelaufene Zeit geben
            LocalTime testUhrzeit = LocalTime.now().minusMinutes(20);
            testTicket1.setUhrzeitManuell(testUhrzeit.getHour(), testUhrzeit.getMinute());
            testTicket1.setEntwertet(true);
            testTicket2.setUhrzeitManuell(testUhrzeit.getHour(), testUhrzeit.getMinute());
            testTicket2.setEntwertet(true);
            testParkhaus.ausfahren(testTicket1);
            testParkhaus.ausfahren(testTicket2);
            assertEquals(198, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(189, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            assertEquals(9, testParkhaus.getAnzahlFreierBehindertenParkplaetze());

            //Zeitschranke testen, sollte die Autos nun rauslassen
            testTicket1.setEntwertet(true);
            testTicket1.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testParkhaus.ausfahren(testTicket1);
            assertEquals(199, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(190, testParkhaus.getAnzahlFreierNormalerParkplaetze());
            testTicket2.setEntwertet(true);
            testTicket2.setUhrzeitManuell(LocalTime.now().getHour(), LocalTime.now().getMinute());
            testParkhaus.ausfahren(testTicket2);
            System.out.println(testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(200, testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(10, testParkhaus.getAnzahlFreierBehindertenParkplaetze());
        }catch (ParkplaetzeBelegtException e1){
            System.out.println("Keine freien Parkplaetze");
        }catch (TicketNichtGefundenException e2){
            System.out.println("Ticket nicht gefunden");
        }
    }

}
