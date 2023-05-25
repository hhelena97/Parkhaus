package Parkhaus;

public interface ParkhausIF {
    Ticket neuesTicket(String art) throws ParkplaetzeBelegtException;
}
