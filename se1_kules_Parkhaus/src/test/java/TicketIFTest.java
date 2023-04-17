import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalTime;

class TicketIFTest {


    Ticket t1 = new Ticket();
    Ticket t2 = new Ticket();


    @Test
    void entwertenTest(){
        t1.entwerten();
        assertTrue(t1.getEntwertet());
        assertFalse(t2.getEntwertet());
    }


    @Test
    void zeitDifferenzTest(){
        LocalTime vergleichszeitStart = LocalTime.of(16, 30);
        LocalTime vergleichszeitSchluss = LocalTime.now();
        int stundenSum;
        int minSum;
        if (vergleichszeitSchluss.getMinute() >= vergleichszeitStart.getMinute()){
            stundenSum = vergleichszeitSchluss.getHour() - vergleichszeitStart.getHour();
            minSum = (stundenSum * 60) + vergleichszeitSchluss.getMinute() - vergleichszeitStart.getMinute();
        } else {
            stundenSum = vergleichszeitSchluss.getHour() - vergleichszeitStart.getHour() -1;
            minSum = (stundenSum * 60) + ((vergleichszeitSchluss.getMinute() + 60) - vergleichszeitStart.getMinute());
        }
        t1.setUhrzeitManuell(16, 30);
        int dauer = t1.zeitDifferenz();
        assertEquals(minSum, dauer);

    }

}