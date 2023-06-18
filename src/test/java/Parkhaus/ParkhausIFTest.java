package Parkhaus;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ParkhausIFTest {



    @Test
    void neuesTicketTest() {
        //Testparkhaus erstellen, in dem die Werte geändert werden sollen, wenn ein Auto reinfährt
        // --> überprüfung weiter unten
        Parkhaus testParkhaus = new Parkhaus();
        testParkhaus.setAnzahlFreierParkplaetze(400);
        testParkhaus.setAnzahlFreierEAutoParkplaetze(8);
        testParkhaus.setAnzahlFreierBehindertenParkplaetze(10);
        testParkhaus.setAnzahlFreierNormalerParkplaetze(362);
        testParkhaus.setAnzahlFreierMotorradParkplaetze(20);

        System.out.println("Anzahl Tickets: " + testParkhaus.getAnzahlFreierParkplaetze());

        //dieses Objekt wird mit dem verglichen, was in der zu testenden Methode erstellt wird
        try {
            Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
            System.out.println("Anzahl Tickets: " + testParkhaus.getAnzahlFreierParkplaetze());
            assertEquals(399, testParkhaus.getAnzahlFreierParkplaetze());
            Ticket testTicket2 = testParkhaus.neuesTicket("E-Auto-Parkplatz");
            assertEquals(398, testParkhaus.getAnzahlFreierParkplaetze());
            Ticket testTicket3 = testParkhaus.neuesTicket("Behinderten-Parkplatz");
            assertEquals(397, testParkhaus.getAnzahlFreierParkplaetze());
            Ticket testTicket4 = testParkhaus.neuesTicket("Motorrad-Parkplatz");
            assertEquals(396, testParkhaus.getAnzahlFreierParkplaetze());

            //Erstellungsdatum soll gleich dem heutigen Datum sein
            assertEquals(testTicket1.getDatum(), testParkhaus.getDatum());
            assertEquals(testTicket2.getDatum(), testParkhaus.getDatum());
            assertEquals(testTicket3.getDatum(), testParkhaus.getDatum());
            assertEquals(testTicket4.getDatum(), testParkhaus.getDatum());

            //Erstellungszeit soll vor der jetzigen Zeit sein
            // (da das Ticket 2 Zeilen vorher erstellt wurde und die Sekunden auch gemessen werden)
            assertEquals(testTicket1.getUhrzeit().getHour(), testParkhaus.getUhrzeit().getHour());
            assertEquals(testTicket1.getUhrzeit().getMinute(), testParkhaus.getUhrzeit().getMinute());
            assertEquals(testTicket1.getUhrzeit().getSecond(), testParkhaus.getUhrzeit().getSecond());

            assertEquals(testTicket2.getUhrzeit().getHour(), testParkhaus.getUhrzeit().getHour());
            assertEquals(testTicket2.getUhrzeit().getMinute(), testParkhaus.getUhrzeit().getMinute());
            assertEquals(testTicket2.getUhrzeit().getSecond(), testParkhaus.getUhrzeit().getSecond());

            assertEquals(testTicket3.getUhrzeit().getHour(), testParkhaus.getUhrzeit().getHour());
            assertEquals(testTicket3.getUhrzeit().getMinute(), testParkhaus.getUhrzeit().getMinute());
            assertEquals(testTicket3.getUhrzeit().getSecond(), testParkhaus.getUhrzeit().getSecond());

            assertEquals(testTicket4.getUhrzeit().getHour(), testParkhaus.getUhrzeit().getHour());
            assertEquals(testTicket4.getUhrzeit().getMinute(), testParkhaus.getUhrzeit().getMinute());
            assertEquals(testTicket4.getUhrzeit().getSecond(), testParkhaus.getUhrzeit().getSecond());

            //Art des Parkplatzes soll dann in der Ticket-Instanzvariablen stehen
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
        } catch (ParkplaetzeBelegtException e){System.out.println("Alle Parkplätze belegt");}
        catch (ParkhausGeschlossenException e2){System.out.println("Parkhaus geschlossen");}
    }

    /**
     * Test-Methode zur Funktion Parkhauszeit anpassen.
     * Bei Umstellung der Zeit auf Zukunft soll die Zeit angepasst werden.
     *
     * @author jboven2s
     */
    @Test
    void parkhauszeitAnpassenTestZukunft ()
    {
        try {
            Parkhaus p = new Parkhaus(3,100,5,10,5);
            LocalTime timeVorher = p.getUhrzeit();
            LocalDate dateVorher = p.getDatum();

            LocalTime timeNeu = LocalTime.of(10,20);
            LocalDate dateNeu = LocalDate.of(2024,3,12);

            p.parkhauszeitAnpassen(timeNeu,dateNeu);

            assertEquals(p.getUhrzeit(),timeNeu);
            assertEquals(p.getDatum(),dateNeu);

            assertNotEquals(p.getUhrzeit(),timeVorher);
            assertNotEquals(p.getDatum(),dateVorher);

        } catch (ReiseInVergangenheitException e){System.out.println("Raum-Zeit-Kontinuum verletzt");}
    }

    /**
     * Test-Methode zur Funktion Parkhauszeit anpassen.
     * Bei Umstellung der Zeit auf Vergangenheit soll eine Exception geworfen werden.
     *
     * @author jboven2s
     */
    @Test
    void parkhauszeitAnpassenTestVergangenheit ()
    {
        Parkhaus p = new Parkhaus(3,100,5,10,5);

        LocalTime timeNeu = LocalTime.of(10,20);
        LocalDate dateNeu = LocalDate.of(2022,3,12);

        assertThrows(ReiseInVergangenheitException.class, () -> p.parkhauszeitAnpassen(timeNeu,dateNeu));
    }

    //Testet, ob die Methode neuesTicket die Exception wirft
    @Test
    void ParkplaetzeBelegtTest(){
        Parkhaus testParkhaus = new Parkhaus(0,10,0,0,0);
        assertThrows(ParkplaetzeBelegtException.class, () -> testParkhaus.neuesTicket("E-Auto-Parkplatz"));
        assertThrows(ParkplaetzeBelegtException.class, () -> testParkhaus.neuesTicket("Motorrad-Parkplatz"));
        assertThrows(ParkplaetzeBelegtException.class, () -> testParkhaus.neuesTicket("Behinderten-Parkplatz"));
        testParkhaus.setAnzahlFreierParkplaetze(0);
        assertThrows(ParkplaetzeBelegtException.class, () -> testParkhaus.neuesTicket("Normaler Parkplatz"));
    }

    //Testet, ob die Methode neues Ticket die Exception wirft, wenn das Parkhaus geschlossen ist
    @Test
    void ParkhausGeschlossenTest() throws ReiseInVergangenheitException{
        Parkhaus testParkhaus = new Parkhaus(3,100,5,10,5);
        LocalTime timeNeu = LocalTime.of(23,10);
        testParkhaus.parkhauszeitAnpassen(timeNeu, testParkhaus.getDatum());
        assertThrows(ParkhausGeschlossenException.class, () -> testParkhaus.neuesTicket("E-Auto-Parkplatz"));
    }
    @Test
    void stringFuerStatsTest() {

        try {
            Parkhaus testParkhaus = new Parkhaus(3, 100, 10, 10, 10);
            Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
            Ticket testTicket2 = testParkhaus.neuesTicket("Normaler Parkplatz");
            testParkhaus.parkhauszeitAnpassen(LocalTime.of(9, 0, 0), LocalDate.of(2023, 1, 1));
            testTicket1.bezahlen();
            testTicket1.ausfahren();
            testParkhaus.parkhauszeitAnpassen(LocalTime.of(10, 0, 0), LocalDate.of(2023, 1, 1));
            testTicket2.bezahlen();
            testTicket2.ausfahren();
            testParkhaus.parkhauszeitAnpassen(LocalTime.of(9, 0, 0), LocalDate.of(2023, 1, 2));
            Ticket testTicket3 = testParkhaus.neuesTicket("Normaler Parkplatz");
            testTicket3.bezahlen();
            testTicket3.ausfahren();
            Ticket testTicket4 = testParkhaus.neuesTicket("Normaler Parkplatz");
            String testString = testParkhaus.stringFuerStats();
            System.out.println(testString);

            assertTrue(testString.contains("Besucherzahl aktuell: 1"));
            assertTrue(testString.contains("Besucher insgesamt: 4"));
            assertTrue(testString.contains("Besucher heute: 2"));
            assertTrue(testString.contains("Durchschnittliche Parkdauer: 60"));
            assertTrue(testString.contains("Durchschnittlicher Ticketpreis: 3.0"));
            assertTrue(testString.contains("Tageseinnahmen: 0.0"));
            assertTrue(testString.contains("Monatseinnahmen: 9.0"));
            assertTrue(testString.contains("Gesamteinnahmen: 9.0"));
            testTicket4.ausfahren(); //damit die Variable benutzt wird.

        }catch(ParkhausGeschlossenException e1){ System.out.println("Außerhalb der Öffnungszeiten");}
        catch (ParkplaetzeBelegtException e2){System.out.println("Keine freien Parkplätze");}
        catch (ReiseInVergangenheitException e3){System.out.println("Raum-Zeit-Kontinuum verletzt");}
        catch (TicketNichtGefundenException e4){ System.out.println("Ticket nicht gefunden");}
    }

    @Test
    void resetTicketListen() {
        try {
            Parkhaus testParkhaus = new Parkhaus(3.2, 100, 4, 10, 16);

            //Liste ist nicht leer
            Ticket testTicket = testParkhaus.neuesTicket("Normaler Parkplatz");
            testParkhaus.neuesTicket("Normaler Parkplatz");
            assertNotNull(testParkhaus.getAktiveTickets());
            assertNotEquals(0, Ticket.getIdentifikationsNummer());

            testTicket.bezahlen();
            testTicket.ausfahren();

            assertNotNull(testParkhaus.getInaktiveTickets());

            //Listen resetten
            testParkhaus.resetTicketListen();

            //Listen müssen wieder null sein
            assertTrue(testParkhaus.getAktiveTickets().isEmpty());
            assertTrue(testParkhaus.getInaktiveTickets().isEmpty());


        } catch (ParkplaetzeBelegtException ex1) {System.out.println("Keine freien Parkplaetze");}
        catch (ParkhausGeschlossenException ex2) {System.out.println("Außerhalb der Öffnungszeiten");}
        catch(TicketNichtGefundenException ex3) {System.out.println("Ticket nicht gefunden");}
    }

    //Tests zum Parkhaus
    //Teste Anzahl Parkplätze
    @Test
    void anzahlParkplaetzeTest(){
        Parkhaus p = new Parkhaus(3,10,0,0,0);
        assertEquals(10, p.getParkplaetzeGesamt());
    }

    //Teste setStundentarif
    @Test
    void setStundentarifTest(){
        Parkhaus p = new Parkhaus(3,10,0,0,0);
        p.setStundentarif(5);
        assertEquals(5, p.getStundentarif());

    }

    //Teste setÖffnungszeit
    @Test
    void setOeffungszeitTest(){
        Parkhaus p = new Parkhaus(3,10,0,0,0);
        p.setOeffnungszeit(p.getUhrzeit());
        assertEquals(p.getUhrzeit(), p.getOeffnungszeit());
    }

    //Teste setSchliessungszeit
    @Test
    void setSchliessungszeitTest(){
        Parkhaus p = new Parkhaus(3,10,0,0,0);
        p.setSchliessungszeit(p.getUhrzeit());
        assertEquals(p.getUhrzeit(), p.getSchliessungszeit());
    }

}
