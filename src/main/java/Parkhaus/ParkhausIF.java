package Parkhaus;

public interface ParkhausIF {
    Ticket neuesTicket(String art) throws ParkplaetzeBelegtException;

    double bezahleTicket (Ticket t);

    String ausfahren(Ticket ticket);
}
