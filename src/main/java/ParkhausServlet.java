import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ParkhausServlet", value = "/parkhaus-servlet")
public class ParkhausServlet extends HttpServlet {
    private String message;
    private Parkhaus p;

    public void init() {
        message = "Das kule Parkhaus";
        p = new Parkhaus(100);
        p.neuesTicket("Normaler Parkplatz");
        p.neuesTicket("Behinderten-Parkplatz");
        System.out.println("Parkhaus erstellt - Parkplätze: " + p.getAnzahlFreierParkplaetze());
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(request.getPathInfo()); //welchen Pfad hinter /parkhaus-servlet
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
            htmlString += "<p>" + p.getAktiveTickets().get(index).getTicketID()+ "</p>";
        }
        htmlString += "</body></html>";
        return htmlString;
    }

    public void destroy() {
    }
}