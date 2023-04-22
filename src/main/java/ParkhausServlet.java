import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ParkhausServlet", value = "/parkhaus-servlet")
public class ParkhausServlet extends HttpServlet {
    private String message;         //hi :)
    private Parkhaus p;

    public void init() {
        message = "Das kule Parkhaus";
        p = new Parkhaus(100);

        //damit in den aktiven Tickets was drin steht (und ich musste da was ausprobieren), kann weg sobald es den Button zum neuen Ticket erzeugen gibt
        System.out.println("erste ticketID: " +p.neuesTicket("Normaler Parkplatz").getTicketID());
        System.out.println("erste ticketID laut ArrayList: " +p.getAktiveTickets().get(0).getTicketID());
        System.out.println("erste ticketID: " +p.neuesTicket("Behinderten-Parkplatz").getTicketID());
        System.out.println("erste ticketID laut ArrayList: " +p.getAktiveTickets().get(1).getTicketID());
        p.neuesTicket("E-Auto_Parkplatz");
        p.neuesTicket("Normaler Parkplatz");
        p.neuesTicket("Normaler Parkplatz");

        System.out.println("Parkhaus erstellt - Parkplätze: " + p.getAnzahlFreierParkplaetze());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //System.out.println(request.getPathInfo()); //welchen Pfad hinter /parkhaus-servlet
        response.setContentType("text/html");

        // Hello
        PrintWriter out = response.getWriter();
        if(request.getPathInfo() != null && request.getPathInfo().equals("/aktiveTickets")) {
            out.println(StringFuerAktiveTicketsAuflistung(response)); //hier methode, die die Tickets zurückgibt
        } else {
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("</body></html>");
        }
    }
    
    private String StringFuerAktiveTicketsAuflistung(HttpServletResponse response) throws IOException{
        String htmlString = "";
        htmlString += "<html><body><h2>Zurzeit aktive Tickets: </h2>";
        int index = 0;
        for (Ticket i : p.getAktiveTickets()) {
            htmlString += "<p>Ticket" + p.getAktiveTickets().get(index).getTicketID()+ ", ";
            htmlString += "Datum: " + p.getAktiveTickets().get(index).getDatum()+", ";
            htmlString += "Ankunftszeit: " + p.getAktiveTickets().get(index).getUhrzeit().getHour();
            if(p.getAktiveTickets().get(index).getUhrzeit().getMinute() <10) {
                htmlString += ":0" + p.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            } else {
                htmlString += ":" + p.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            }
            htmlString += "Parkplatzart: " + p.getAktiveTickets().get(index).getArtDesParkplatzes()+ "</p>";
            index++;
        }
        htmlString += "</body></html>";
        return htmlString;
    }

    public void destroy() {
    }
}