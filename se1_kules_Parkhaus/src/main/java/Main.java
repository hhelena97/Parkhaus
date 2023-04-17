import java.time.LocalDate;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        System.out.println(LocalTime.now());

        Ticket t1 = new Ticket();

        System.out.println("Stunde:" + t1.getUhrzeitStunde());
        System.out.println("Minute:" + t1.getUhrzeitMin());
        System.out.println("Entwertet: " + t1.getEntwertet());

        t1.entwerten();

        System.out.println(t1.getEntwertet());

        Parkhaus p = new Parkhaus(2.1);
        Ticket t = new Ticket("Normaler Parkplatz");
        t.setUhrzeitManuell(16, 30);


    }
}
