package Parkhaus;

public class TicketNichtGefundenException extends Exception{
    public TicketNichtGefundenException(String message){
        super(message);
    }
}
