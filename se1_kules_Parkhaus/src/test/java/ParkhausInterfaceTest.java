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
        testParkhaus.setAnzahlFreieEAutoParkplaetze(6);

        //dieses Objekt wird mit dem verglichen was in der zu testenden Methode erstellt wird
        Ticket testTicket = testParkhaus.neuesTicket("E-Auto Parkplatz");

        //ErstellungsDatum soll gleich dem heutigen Datum sein
        assertEquals(testTicket.datum, LocalDate.now());

        //ErstellungsZeit soll vor der jetzigen Zeit sein (da das Ticket 2 Zeilen vorher erstellt wurde und die Sekunden auch gemessen werden)
        assertTrue(testTicket.uhrzeit < LocalTime.now());
        //ErstellungsZeit soll aber auch nicht irgendwie in der Zukunft liegen
        assertTrue(testTicket.uhrzeit > LocalTime.now().plusMinutes(1));

        //Art des Parkplatzes soll dann in der Ticket-Instanzvariablen stehen
        assertEquals("Normaler Parkplatz", testTicket.artDesParkplatzes);

        //gesamtanzahl an freien Parkplätzen soll um 1 verringert werden
        assertSame(399,Parkhaus.anzahlFreierParkplaetze);

        //Anzahl der besonderen Parkplätze muss sich jeweils auch verändern
        assertTrue(testParkhaus.eAutoParkplatz == 5);
    }

    @Test
    void bezahleTicket() {

        // Teste ob 'parkzeit' richtig berechnet wurde

        // Teste ob 'preis' richitg berechnet wurde

        // Teste ob 'preis' auf 'tageseinnahmen' gerechnet wurde
    }

}
