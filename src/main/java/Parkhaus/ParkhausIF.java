package Parkhaus;

import java.time.LocalDate;
import java.time.LocalTime;

public interface ParkhausIF {
    Ticket neuesTicket(String art) throws ParkplaetzeBelegtException, ParkhausGeschlossenException;
    public void parkhauszeitAnpassen(LocalTime time, LocalDate date) throws ReiseInVergangenheitException;

}
