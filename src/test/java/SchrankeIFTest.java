

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SchrankeIFTest {

    @Test
    void ausfahrenTest(){

        Parkhaus testParkhaus = new Parkhaus();
        testParkhaus.setAnzahlFreierParkplaetze(200);
        testParkhaus.setAnzahlFreierNormalerParkplaetze(190);
        testParkhaus.setAnzahlFreierBehindertenParkplaetze(10);
        Schranke testSchranke = new Schranke(testParkhaus);
        Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
        Ticket testTicket2 = testParkhaus.neuesTicket("Behinderten-Parkplatz");

        //Schranke testen mit unbezahltem Ticket, sollte auch Hinweis ausgeben
        testSchranke.ausfahren(testTicket1);
        testSchranke.ausfahren(testTicket2);
        assertEquals(198,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(189,testParkhaus.getAnzahlFreierNormalerParkplaetze());
        assertEquals(9,testParkhaus.getAnzahlFreierBehindertenParkplaetze());

        /*Zeitschranke testen, sollte auch Hinweis über abgelaufene Zeit geben
        testTicket1.setUhrzeitManuell(18,0);
        testTicket1.setEntwertet(true);
        testTicket2.setUhrzeitManuell(18,0);
        testTicket2.setEntwertet(true);
        testSchranke.ausfahren(testTicket1);
        testSchranke.ausfahren(testTicket2);
        assertEquals(198,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(189,testParkhaus.getAnzahlFreierNormalerParkplaetze());
        assertEquals(9,testParkhaus.getAnzahlFreierBehindertenParkplaetze());*/

        //Zeitschranke testen, sollte die Autos nun rauslassen
        testTicket1.setEntwertet(true);
        testTicket1.setUhrzeitManuell(23,0);
        testSchranke.ausfahren(testTicket1);
        assertEquals(199,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(190,testParkhaus.getAnzahlFreierNormalerParkplaetze());
        testTicket2.setEntwertet(true);
        testTicket2.setUhrzeitManuell(23,0);
        testSchranke.ausfahren(testTicket2);
        assertEquals(200,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(10,testParkhaus.getAnzahlFreierBehindertenParkplaetze());
    }
}
