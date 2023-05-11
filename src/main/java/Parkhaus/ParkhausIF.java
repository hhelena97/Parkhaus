package Parkhaus;

public interface ParkhausIF {
    Ticket neuesTicket(String art);

    double bezahleTicket (Ticket t);

    String ausfahren(Ticket ticket);
}
