package Parkhaus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "ParkhausServlet", value = "/parkhaus-servlet")
public class ParkhausServlet extends HttpServlet {
    public void init() {
        //existiert bereits ein Parkhaus im Context, dann wird das verwendet - sonst wird ein neues erstellt
        Parkhaus p;
        if (getServletContext().getAttribute("parkhaus") == null) {
            p = new Parkhaus(3, 100, 5, 5, 10);
            System.out.println("Neues Parkhaus erstellt");
        } else {
            p = (Parkhaus) getServletContext().getAttribute("parkhaus");
            System.out.println("Parkhaus in init gefunden");
        }
        getServletContext().setAttribute("parkhaus", p);


        //damit in den aktiven Tickets was drin steht (und ich musste da was ausprobieren), kann weg sobald es den Button zum neuen Ticket erzeugen gibt
        System.out.println("erste ticketID: " +p.neuesTicket("Normaler Parkplatz").getTicketID());
        System.out.println("erste ticketID laut ArrayList: " +p.getAktiveTickets().get(0).getTicketID());
        System.out.println("erste ticketID: " +p.neuesTicket("Behinderten-Parkplatz").getTicketID());
        System.out.println("erste ticketID laut ArrayList: " +p.getAktiveTickets().get(1).getTicketID());
        p.neuesTicket("E-Auto_Parkplatz");
        p.neuesTicket("Normaler Parkplatz");
        p.neuesTicket("Normaler Parkplatz");

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");

        request.setAttribute("parkhaus", p);
        request.getRequestDispatcher("index.jsp").forward(request, response);

        //System.out.println(request.getPathInfo()); //welchen Pfad hinter /parkhaus-servlet

        /*Sarah: die Anzeige der freien Parkplätze hab ich in die index verschoben. Sonst brauchen wir das bei doGet und doPost
        einverstanden?

        PrintWriter out = response.getWriter();
        if(request.getPathInfo() != null && request.getPathInfo().equals("/aktiveTickets")) {
            out.println(StringFuerAktiveTicketsAuflistung(response)); //hier methode, die die Tickets zurückgibt
        }
        else if (request.getPathInfo() != null && request.getPathInfo().equals("/neuesTicket"))
        {
            out.println(StringFuerNeuesTicketAuswahl());
        }
        else if(request.getPathInfo() != null && request.getPathInfo().equals("/inaktiveTickets")) {
            out.println(StringFuerInaktiveTicketsAuflistung(response));
        }
        else {
            out.println("<html><body>");
            out.println("<h1>" + message + "</h1>");
            out.println("<p> Parkplätze gesamt: " + p.getParkplaetzeGesamt() + "</p><br>");
            out.println("<p> aktuell freie Parkplätze gesamt: " + p.getAnzahlFreierParkplaetze() + "</p><br>");
            out.println("<p> aktuell freie normale Parkplätze: " + p.getAnzahlFreierNormalerParkplaetze() + "</p><br>");
            out.println("<p> aktuell freie Parkplätze für E-Autos: " + p.getAnzahlFreierEAutoParkplaetze() + "</p><br>");
            out.println("<p> aktuell freie Behinderten-Parkplätze: " + p.getAnzahlFreierBehindertenParkplaetze() + "</p><br>");
            out.println("<p> aktuell freie Motorrad-Parkplätze: " + p.getAnzahlFreierMotorradParkplaetze() + "</p><br>");
            out.println("</body></html>");

         */
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        PrintWriter out = response.getWriter();
        //hole das Parkhaus aus dem Context
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");
        //führe je nach "action" verschiedene Dinge aus
        String action = request.getParameter("action");
        //Button Ticket erstellen
        if("ticketErstellen".equals(action)){
            //erstellt ein neues Ticket mit der ausgewählten Parkplatzart
            Ticket t = p.neuesTicket(request.getParameter("ticketArt"));

            out.println("<p> Es wurde ein neues Ticket mit Parkplatzart: " + t.getArtDesParkplatzes() + " und ID: " + t.getTicketID() + " erstellt! </p><br>");

            System.out.println("Neues Ticket erstellt");
            System.out.println(t.toString());
            //(über)schreibt die Liste aktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            //request.setAttribute("Ticket-ID", t.getTicketID());

        } else if ("bezahlen".equals("action")){
            //Ticket t = p.Methode zum Suchen des Tickets in der Liste(request.getParameter("ticketID")
            //double preis = p.bezahleTicket(t);
            out.println("<p>Ticket entwertet</p>");
            //out.println("Preis: " + preis);

            //(über)schreibt die Liste aktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());

        } else if("schrankeOeffnen".equals("action")){
            //Ticket t = Methode zum Suchen des Tickets in der Liste(request.getParameter("ticketID")
            //Schranke erstellen
            //schranke.ausfahren(t)
            out.println("<p>Schranke geht auf</p>");

            //(über)schreibt die Liste aktiver und inaktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());
        }
        request.setAttribute("parkhaus", p);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    private String StringFuerAktiveTicketsAuflistung(HttpServletResponse response) throws IOException{
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");
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


    public String StringFuerInaktiveTicketsAuflistung(HttpServletResponse response) throws IOException{
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");
        String htmlString = "";
        htmlString += "<html><body><h2>Alle inaktiven Tickets: </h2>";
        int index = 0;
        for (Ticket i : p.getInaktiveTickets()) {
            htmlString += "<p>Ticket" + p.getInaktiveTickets().get(index).getTicketID()+ ", ";
            htmlString += "Datum: " + p.getInaktiveTickets().get(index).getDatum()+", ";
            htmlString += "Ausfahrzeit: " + p.getInaktiveTickets().get(index).getUhrzeit().getHour();
            if(p.getInaktiveTickets().get(index).getUhrzeit().getMinute() <10) {
                htmlString += ":0" + p.getInaktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            } else {
                htmlString += ":" + p.getInaktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            }
            htmlString += "Parkplatzart: " + p.getInaktiveTickets().get(index).getArtDesParkplatzes()+ "</p>";
            index++;
        }
        htmlString += "</body></html>";
        return htmlString;
    }

    private String StringFuerNeuesTicketAuswahl()            // Ticket-Auswahl mit Knopf
    {
        String s = "";
        s += "<form method = \"POST\" target = \"_blank\">";
        s += "<input type = \"checkbox\" name = \"parkplatzArt\" value=\"Normaler Parkplatz\" /> Normaler Parkplatz</br>";
        s += "<input type = \"checkbox\" name = \"parkplatzArt\" value=\"Behinderten-Parkplatz\"  /> Behinderten Parkplatz</br>";
        s += "<input type = \"checkbox\" name = \"parkplatzArt\" value=\"E-Auto-Parkplatz\" /> E-Auto Parkplatz</br>";
        s += "<input type = \"checkbox\" name = \"parkplatzArt\" value=\"Motorrad-Parkplatz\" /> Motorrad Parkplatz</br>";
        s += "<input type = \"submit\" name = \"buttonNeuesTicketErstellen\" value = \"Erstelle Ticket\" />";
        s += "</form>";
        return s;
    }




    public void destroy() {
    }
}
