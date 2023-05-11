package Parkhaus;

import javax.servlet.ServletException;
import javax.servlet.WriteListener;
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
            System.out.println("Stundentarif: " + p.getStundentarif());
        } else {
            p = (Parkhaus) getServletContext().getAttribute("parkhaus");
            System.out.println("Parkhaus in init gefunden");
        }
        getServletContext().setAttribute("parkhaus", p);


        //damit in den aktiven Tickets was drin steht (und ich musste da was ausprobieren), kann weg, sobald es den Button zum neuen Ticket erzeugen gibt
        System.out.println("erste ticketID: " +p.neuesTicket("Normaler Parkplatz").getTicketID());
        System.out.println("erste ticketID laut ArrayList: " +p.getAktiveTickets().get(0).getTicketID());
        System.out.println("erste ticketID: " +p.neuesTicket("Behinderten-Parkplatz").getTicketID());
        System.out.println("erste ticketID laut ArrayList: " +p.getAktiveTickets().get(1).getTicketID());
        p.neuesTicket("E-Auto_Parkplatz");
        p.neuesTicket("Normaler Parkplatz");
        p.neuesTicket("Normaler Parkplatz");
        Ticket ticket1 = p.getAktiveTickets().get(0);
        Ticket ticket2 = p.getAktiveTickets().get(1);
        ticket1.setParkdauerMin(30);
        ticket2.setParkdauerMin(60);
        p.bezahleTicket(ticket1);
        p.bezahleTicket(ticket2);
        p.ausfahren(ticket1);
        p.ausfahren(ticket2);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");

        request.setAttribute("parkhaus", p);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        //PrintWriter out = response.getWriter();
        //hole das Parkhaus aus dem Context
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");
        //führe je nach "action" verschiedene Dinge aus
        String action = request.getParameter("action");
        //Button Ticket erstellen
        if("start".equals(action)){
            //this.init();
            //getServletContext().setAttribute("ticketliste", p.getAktiveTickets());

            //p = new Parkhaus(3, 100, 5, 5, 10);


            //getServletContext().setAttribute("parkhaus", p);
            //p.resetTicketListen();
            //getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            //getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());
        }
        else if("ticketErstellen".equals(action)){
            //erstellt ein neues Ticket mit der ausgewählten Parkplatzart
            Ticket t = p.neuesTicket(request.getParameter("ticketArt"));

            //out.println("<p> Es wurde ein neues Ticket mit Parkplatzart: " + t.getArtDesParkplatzes() + " und ID: " + t.getTicketID() + " erstellt! </p><br>");

            System.out.println("Neues Ticket erstellt");
            System.out.println(t.toString());
            //(über)schreibt die Liste aktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());

        } else if ("bezahlen".equals(action)) {
            int len = p.getAktiveTickets().size();
            for (int i = 0; i < len; i++)
            {
                if(p.getAktiveTickets().get(i).getTicketID() == Integer.parseInt(request.getParameter("ticketID")))
                {
                    Ticket t = p.getAktiveTickets().get(i);
                    t.setPreis(p.bezahleTicket(t));
                    double preis = t.getPreis();

                    t.setParkdauerMin(t.zeitDifferenz());
                    int parkzeit = t.getParkdauerMin();

                    // Um diese Elemente anzeigen zu können:
                    request.setAttribute("bezahleTicketX", t);
                    request.setAttribute("preisTicketX", preis);
                    request.setAttribute("zeitTicketX", parkzeit);
                }
            }

            //(über)schreibt die Liste aktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());

        } else if("schrankeOeffnen".equals(action)){
            //t ist das Ticket was ausgewählt wurde
            Ticket ticketAusfahren = null;
            for (Ticket ti: p.getAktiveTickets()) {
                if(ti.getTicketID() == Integer.valueOf(request.getParameter("ticketID"))) {
                    ticketAusfahren = ti;
                }
            }

            String nachricht = p.ausfahren(ticketAusfahren);
            request.setAttribute("NachrichtX", nachricht);
            //out.println("<p>Auf Wiedersehen!</p>");

            //(über)schreibt die Liste aktiver und inaktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());

        }else if ("datenAuswerten".equals(action)){
            //Zeige Datenauswertung
            String stats = p.StringFuerStats();
            request.setAttribute("datenauswertung",stats);

        }else if ("aktiveTickets".equals(action)){
            request.getRequestDispatcher("aktiveTickets.jsp").forward(request, response);
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
