package Parkhaus;

public interface ParkhausIF {
    Ticket neuesTicket(String art) throws ParkplaetzeBelegtException;
    public double bezahleTicket(Ticket t);
    public String ausfahren(Ticket ticket) throws TicketNichtGefundenException;
}
