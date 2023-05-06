package Parkhaus;

public interface ParkhausIF {
    public Ticket neuesTicket(String art);

    public double bezahleTicket (Ticket t);
}
