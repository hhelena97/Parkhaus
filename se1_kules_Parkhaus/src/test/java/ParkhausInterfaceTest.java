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
    void bezahleTicketTest() {

        Parkhaus p = new Parkhaus(2.1);
        Ticket t = new Ticket("Normaler Parkplatz");

        // Teste ob 'parkzeit' richtig berechnet wurde
        double erwarteteParkzeit = LocalTime.now() - t.uhrzeit;
        assertEquals(erwarteteParkzeit, t.getParkzeitTicket());


        // Teste ob 'preis' richtig berechnet wurde
        double erwarteterPreis = p.getStundentarif()*t.getParkzeitTicket();
        assertEquals(erwarteterPreis, t.getPreisTicket());


        // Teste ob 'preis' auf 'einnahmenTag' gerechnet wurde
        double erwarteteEinnahmenTag = p.getEinnahmenTag() + erwarteterPreis;
        assertEquals(erwarteteEinnahmenTag, p.getEinnahmenTag());

    }

}
