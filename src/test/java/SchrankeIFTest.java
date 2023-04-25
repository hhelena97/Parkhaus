

import org.junit.jupiter.api.Test;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class SchrankeIFTest {

    @Test
    void ausfahrenTest(){

        Parkhaus testParkhaus = new Parkhaus();
        testParkhaus.setAnzahlFreierParkplaetze(200);
        testParkhaus.setAnzahlFreierNormalerParkplaetze(190);
        testParkhaus.setAnzahlFreierBehindertenParkplaetze(10);
        Schranke testSchranke = new Schranke();
        Ticket testTicket1 = testParkhaus.neuesTicket("Normaler Parkplatz");
        Ticket testTicket2 = testParkhaus.neuesTicket("Behinderten-Parkplatz");

        //Schranke testen mit unbezahltem Ticket, sollte auch Hinweis ausgeben
        testSchranke.ausfahren(testTicket1, testParkhaus);
        testSchranke.ausfahren(testTicket2, testParkhaus);
        assertEquals(198,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(189,testParkhaus.getAnzahlFreierNormalerParkplaetze());
        assertEquals(9,testParkhaus.getAnzahlFreierBehindertenParkplaetze());

        //Zeitschranke testen, sollte auch Hinweis über abgelaufene Zeit geben
        LocalTime testUhrzeit = LocalTime.now().minusMinutes(20);
        testTicket1.setUhrzeitManuell(testUhrzeit.getHour(),testUhrzeit.getMinute());
        testTicket1.setEntwertet(true);
        testTicket2.setUhrzeitManuell(testUhrzeit.getHour(),testUhrzeit.getMinute());
        testTicket2.setEntwertet(true);
        testSchranke.ausfahren(testTicket1, testParkhaus);
        testSchranke.ausfahren(testTicket2, testParkhaus);
        assertEquals(198,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(189,testParkhaus.getAnzahlFreierNormalerParkplaetze());
        assertEquals(9,testParkhaus.getAnzahlFreierBehindertenParkplaetze());

        //Zeitschranke testen, sollte die Autos nun rauslassen
        testTicket1.setEntwertet(true);
        testTicket1.setUhrzeitManuell(LocalTime.now().getHour(),LocalTime.now().getMinute());
        testSchranke.ausfahren(testTicket1, testParkhaus);
        assertEquals(199,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(190,testParkhaus.getAnzahlFreierNormalerParkplaetze());
        testTicket2.setEntwertet(true);
        testTicket2.setUhrzeitManuell(LocalTime.now().getHour(),LocalTime.now().getMinute());
        testSchranke.ausfahren(testTicket2, testParkhaus);
        System.out.println(testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(200,testParkhaus.getAnzahlFreierParkplaetze());
        assertEquals(10,testParkhaus.getAnzahlFreierBehindertenParkplaetze());
    }
}
