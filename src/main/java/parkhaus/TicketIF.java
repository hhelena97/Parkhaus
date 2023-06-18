package parkhaus;

public interface TicketIF {
    public void entwerten();
    //entwertet das Parkhaus.Ticket = setzt die entwertet-Variable des Tickets auf True
    //und aktualisiert die Ticket-Zeit

    public int zeitDifferenz();
    //vergleicht die Zeit des Tickets mit der aktuellen Zeit und gibt die Parkzeit in Minuten zurueck

    public double bezahlen() throws TicketNichtGefundenException, ParkhausGeschlossenException;

    public String ausfahren() throws TicketNichtGefundenException, ParkhausGeschlossenException;
}
