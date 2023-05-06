package Parkhaus;

public interface TicketIF {
    public void entwerten();
    //entwertet das Parkhaus.Ticket = setzt die entwertet-Variable des Tickets auf True
    //und aktualisiert die Parkhaus.Ticket-Zeit

    public int zeitDifferenz();
    //vergleicht die Zeit des Tickets mit der aktuellen Zeit und gibt die Parkzeit in Minuten zurueck


}
