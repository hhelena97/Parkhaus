import static org.junit.jupiter.api.Assertions.*;

class TicketIFTest {


    Ticket t1 = new Ticket();
    Ticket t2 = new Ticket();


    @Test
    void entwertenTest(){
        t1.entwerten();
        assertTrue(t1.getEntwertet());
        assertFalse(t2.getEntwertet());
    }

}