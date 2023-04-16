import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        System.out.println(LocalTime.now());

        Ticket t1 = new Ticket();

        System.out.println("Stunde:" + t1.getParkzeitStunde());
        System.out.println("Minute:" + t1.getParkzeitMin());
        System.out.println("Entwertet: " + t1.getEntwertet());

        t1.entwerten();

        System.out.println(t1.getEntwertet());

    }
}
