import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class ParkhausInterfaceTest {

    @Test
    void neuesTicketTest() {
        //Testparkhaus erstellen in dem die Werte geändert werden sollen wenn ein Auto reinfährt --> überprüfung weiter unten
        Parkhaus testParkhaus = new Parkhaus();
        testParkhaus.setAnzahlFreierParkplaetze(400);
        testParkhaus.setAnzahlFreieEAutoParkplaetze(8);
        testParkhaus.setAnzahlFreierBehindertenParkplaetze(10);
        testParkhaus.setAnzahlFreierNormalerParkplaetze(362);
        testParkhaus.setAnzahlFreierMotorradParkplaetze(20);

        //dieses Objekt wird mit dem verglichen was in der zu testenden Methode erstellt wird
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

        //TODO: Zeit überprüfen

        /**ErstellungsZeit soll vor der jetzigen Zeit sein (da das Ticket 2 Zeilen vorher erstellt wurde und die Sekunden auch gemessen werden)
        assertTrue(testTicket.getUhrzeit() < LocalTime.now());
        //ErstellungsZeit soll aber auch nicht irgendwie in der Zukunft liegen
        assertTrue(testTicket.getUhrzeit() > LocalTime.now().plusMinutes(1));**/

        //Art des Parkplatzes soll dann in der Ticket-Instanzvariablen stehen
        assertEquals("Normaler Parkplatz", testTicket1.getArtDesParkplatzes());
        assertEquals("E-Auto-Parkplatz", testTicket2.getArtDesParkplatzes());
        assertEquals("Behinderten-Parkplatz", testTicket3.getArtDesParkplatzes());
        assertEquals("Motorrad-Parkplatz", testTicket4.getArtDesParkplatzes());

        //Anzahl der besonderen Parkplätze muss sich jeweils auch verändern
        assertTrue(testParkhaus.getAnzahlFreierNormalerParkplaetze() == 361);
        assertTrue(testParkhaus.getAnzahlFreierEAutoParkplaetze() == 7);
        assertTrue(testParkhaus.getAnzahlFreierBehindertenParkplaetze() == 9);
        assertTrue(testParkhaus.getAnzahlFreierMotorradParkplaetze() == 19);
    }

    @Test
    void bezahleTicketTest() {

        Parkhaus p = new Parkhaus(2.1);
        Ticket t = new Ticket("Normaler Parkplatz");
        t.setUhrzeitManuell(16, 30);
        p.bezahleTicket(t);

        // Teste ob 'preis' richtig berechnet wurde
        double erwarteterPreis = p.getStundentarif()*t.zeitDifferenz();
        assertEquals(erwarteterPreis, t.getPreis());


        // Teste ob 'preis' auf 'einnahmenTag' gerechnet wurde
        //Kommentar Sarah: Kommt erst später. Wir lernen in der nächsten Vorlesung, wie man so Zeug speichern kann
        /* double erwarteteEinnahmenTag = p.getEinnahmenTag() + erwarteterPreis;
        assertEquals(erwarteteEinnahmenTag, p.getEinnahmenTag());

         */

    }

}
