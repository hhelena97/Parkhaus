package Parkhaus;

public class TicketNichtGefundenException extends Exception{

    /**
     * Die TicketNichtGefundenException soll geworfen werden, wenn eine aufrufende Methode ein Ticket bekommt oder
     * referenziert, das inaktiv ist oder nicht existiert.
     * @param message ist die von der aufrufenden Methode hinzugefügte Exceptionnachricht, die auch auf der
     *                Parkhausseite ausgegeben werden kann.
     *@author hheyen2s
     */
    public TicketNichtGefundenException(String message){
        super(message);
    }
}
