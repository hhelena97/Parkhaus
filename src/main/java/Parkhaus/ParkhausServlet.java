package Parkhaus;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

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


        //damit in den aktiven Tickets was drin steht (und ich musste da was ausprobieren), kann weg, sobald es den Button zum neuen Ticket erzeugen gibt
        // verschoben in den Button Testtickets
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");

        request.setAttribute("parkhaus", p);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String naechsteSeite = "index.jsp";
        //PrintWriter out = response.getWriter();
        //hole das Parkhaus aus dem Context
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");
        //führe je nach "action" verschiedene Dinge aus
        String action = request.getParameter("action");
        //Button Ticket erstellen
        if ("start".equals(action)) {
            p.resetTicketListen();
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());
            p = new Parkhaus(3, 100, 5, 5, 10);

            LocalTime time = LocalTime.of(8,0);                         // resetten der Uhrzeit...
            LocalDate date = LocalDate.of(2023,1,1);          // ...und des Datums
            p.setUhrzeit(time);                                                     //
            p.setDatum(date);                                                       //

            getServletContext().setAttribute("parkhaus", p);
            //Exception-Nachrichten ausblenden
            if (getServletContext().getAttribute("TicketErstellenException") != null) {
                getServletContext().removeAttribute("TicketErstellenException");
            }
            if (getServletContext().getAttribute("AusfahrenException") != null) {
                getServletContext().removeAttribute("AusfahrenException");
            }
            if (getServletContext().getAttribute("BezahlenException") != null) {
                getServletContext().removeAttribute("BezahlenException");
            }
            if (getServletContext().getAttribute("ZeitException") != null) {
                getServletContext().removeAttribute("ZeitException");
            }
            if (getServletContext().getAttribute("ParkhausGeschlossenException") != null) {
                getServletContext().removeAttribute("ParkhausGeschlossenException");
            }
        } else if("ParkhauszeitenAnpassen".equals(action)) {        // TODO: Exception reparieren, bitte. :c

            //Exception-Nachrichten ausblenden
            NachrichtenAusblenden();

            try {
                LocalTime t = LocalTime.parse(request.getParameter("Zeit"));
                LocalDate d = LocalDate.parse(request.getParameter("Datum"));

                System.out.println("Zeit: " + request.getParameter("Zeit"));
                System.out.println("Datum: " + request.getParameter("Datum"));

                p.parkhauszeitAnpassen(t,d);

            } catch (ReiseInVergangenheitException e) {
                getServletContext().setAttribute("ZeitException", e.getMessage());
            }

        } else if ("Testtickets".equals(action)) {
            try {
                System.out.println("erste ticketID: " + p.neuesTicket("Normaler Parkplatz").getTicketID());
                System.out.println("erste ticketID laut ArrayList: " + p.getAktiveTickets().get(0).getTicketID());
                System.out.println("erste ticketID: " + p.neuesTicket("Behinderten-Parkplatz").getTicketID());
                System.out.println("erste ticketID laut ArrayList: " + p.getAktiveTickets().get(1).getTicketID());
                p.neuesTicket("E-Auto-Parkplatz");
                p.neuesTicket("Normaler Parkplatz");
                p.neuesTicket("Normaler Parkplatz");
                Ticket ticket1 = p.getAktiveTickets().get(0);
                Ticket ticket2 = p.getAktiveTickets().get(1);
                ticket1.setParkdauerMin(30);
                ticket2.setParkdauerMin(60);
                ticket1.bezahlen();
                ticket2.bezahlen();
                ticket1.ausfahren();
                ticket2.ausfahren();
            } catch (ParkplaetzeBelegtException e1) {
                getServletContext().setAttribute("TicketErstellenException", e1.getMessage());
            } catch (TicketNichtGefundenException e2) {
                getServletContext().setAttribute("AusfahrenException", e2.getMessage());
            } catch (ParkhausGeschlossenException e3) {
            getServletContext().setAttribute("ParkhausGeschlossenException", e3.getMessage());
            }
        } else if ("ticketErstellen".equals(action)) {
            //erstellt ein neues Ticket mit der ausgewählten Parkplatzart
            try {
                Ticket t = p.neuesTicket(request.getParameter("ticketArt"));

                //out.println("<p> Es wurde ein neues Ticket mit Parkplatzart: " + t.getArtDesParkplatzes() + " und ID: " + t.getTicketID() + " erstellt! </p><br>");

                System.out.println("Neues Ticket erstellt");
                System.out.println(t.toString());
                //(über)schreibt die Liste aktiver Tickets im Context
                getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            } catch (ParkplaetzeBelegtException e) {
                getServletContext().setAttribute("TicketErstellenException", e.getMessage());
            } catch (ParkhausGeschlossenException e2) {
                getServletContext().setAttribute("ParkhausGeschlossenException", e2.getMessage());
            }


        } else if ("bezahlen".equals(action)) {

            //Exception-Nachrichten ausblenden
            NachrichtenAusblenden();

            int len = p.getAktiveTickets().size();
            try {
                for (int i = 0; i < len; i++) {
                    if (p.getAktiveTickets().get(i).getTicketID() == Integer.parseInt(request.getParameter("ticketID"))) {

                        Ticket t = p.getAktiveTickets().get(i);
                        t.setPreis(t.bezahlen());
                        double preis = t.getPreis();
                        double rabattEuro = t.getRabattEuro();
                        int parkzeit = t.getParkdauerMin();

                        // Um diese Elemente anzeigen zu können:
                        request.setAttribute("bezahleTicketX", t);
                        request.setAttribute("preisTicketX", preis);
                        request.setAttribute("rabattBezahlenX", rabattEuro);
                        request.setAttribute("zeitTicketX", parkzeit);

                        if (getServletContext().getAttribute("BezahlenException") != null) {
                            getServletContext().removeAttribute("BezahlenException");
                        }
                    }
                }
                if (getServletContext().getAttribute("bezahleTicketX") == null) {
                    Exception e1 = new TicketNichtGefundenException("Ticket nicht gefunden. Zur Zahlung bereite Tickets unter 'aktive Tickets'.");
                    getServletContext().setAttribute("BezahlenException", e1.getMessage());
                }
            } catch (TicketNichtGefundenException e11){
                getServletContext().setAttribute("TicketNichtGefundenException", e11.getMessage());
            } catch (NumberFormatException e2) {
                //Do nothing
            } catch (ParkhausGeschlossenException e3) {
                getServletContext().setAttribute("ParkhausGeschlossenException", e3.getMessage());;
            }

            //(über)schreibt die Liste aktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());

        } else if ("schrankeOeffnen".equals(action)) {

            //Exception-Nachrichten ausblenden
            NachrichtenAusblenden();

            //t ist das Ticket was ausgewählt wurde
            try {

                Ticket ticketAusfahren = null;
                for (Ticket ti : p.getAktiveTickets()) {
                    if (ti.getTicketID() == Integer.valueOf(request.getParameter("ticketID"))) {
                        ticketAusfahren = ti;
                    }
                }

                String nachricht = ticketAusfahren.ausfahren();
                request.setAttribute("NachrichtX", nachricht);
            } catch (TicketNichtGefundenException e1) {
                getServletContext().setAttribute("AusfahrenException", e1.getMessage());
            } catch (NumberFormatException e2) {
                //Do nothing
            } catch (ParkhausGeschlossenException e3) {
                getServletContext().setAttribute("ParkhausGeschlossenException", e3.getMessage());
            }

            //(über)schreibt die Liste aktiver und inaktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());

        } else if ("rabattGeben".equals(action)) {
            // Rabatt geben (später auf Betreiberseite)
            int len = p.getAktiveTickets().size();
            double rabatt = 0;
            for (int i = 0; i < len; i++) {
                if (p.getAktiveTickets().get(i).getTicketID() == Integer.parseInt(request.getParameter("ticketID"))) {

                    Ticket t = p.getAktiveTickets().get(i);
                    if (request.getParameter("rabatt").equals("Personalrabatt (10 %)")) {
                        rabatt = 0.1;
                    } else if (request.getParameter("rabatt").equals("Besucher EKZ (20 %)")) {
                        rabatt = 0.2;

                    } else if (request.getParameter("rabatt").equals("Treuerabatt (25 %)")) {
                        rabatt = 0.25;
                    }
                    t.setRabattProzent(rabatt);
                    //System.out.println("Rabatt: " + rabatt);

                    // Um diese Elemente anzeigen zu können:
                    request.setAttribute("rabattTicketX", t);
                    request.setAttribute("rabattX", (rabatt * 100));
                }
            }
            naechsteSeite = "Betreiberansicht.jsp";

        } else if ("aktiveTickets".equals(action)) {
            request.getRequestDispatcher("aktiveTickets.jsp").forward(request, response);
        } else if ("inaktiveTickets".equals(action)){
            request.getRequestDispatcher("inaktiveTickets.jsp").forward(request, response);

        } else if ("Betreiberansicht".equals(action)) {
            request.getRequestDispatcher("Betreiberansicht.jsp").forward(request, response);
        } else if ("OeffnungszeitenAendern".equals(action)) {
            System.out.println("Ändern");
            LocalTime oeffnen = LocalTime.parse(request.getParameter("Oeffnen"));
            LocalTime schliessen = LocalTime.parse(request.getParameter("Schliessen"));

            //Offnungszeiten im Context ändern
            getServletContext().setAttribute("Oeffnen", oeffnen);
            getServletContext().setAttribute("Schliessen", schliessen);

            //Öffnungszeiten im Parkhaus ändern
            p.setOeffnungszeit(oeffnen);
            p.setSchliessungszeit(schliessen);

            naechsteSeite = "Betreiberansicht.jsp";

        } else if ("StundentarifAendern".equals(action)) {
            double neuerPreis = Double.parseDouble(request.getParameter("Preis"));
            getServletContext().setAttribute("Stundentarif", neuerPreis);
            p.setStundentarif(neuerPreis);
            System.out.println("Preis ändern in " + neuerPreis);
            naechsteSeite = "Betreiberansicht.jsp";
        }
        request.setAttribute("parkhaus", p);
        request.getRequestDispatcher(naechsteSeite).forward(request, response);
    }

    private String StringFuerAktiveTicketsAuflistung(HttpServletResponse response) throws IOException {
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");
        String htmlString = "";
        htmlString += "<html><body><h2>Zurzeit aktive Tickets: </h2>";
        int index = 0;
        for (Ticket i : p.getAktiveTickets()) {
            htmlString += "<p>Ticket" + p.getAktiveTickets().get(index).getTicketID() + ", ";
            htmlString += "Datum: " + p.getAktiveTickets().get(index).getDatum() + ", ";
            htmlString += "Ankunftszeit: " + p.getAktiveTickets().get(index).getUhrzeit().getHour();
            if (p.getAktiveTickets().get(index).getUhrzeit().getMinute() < 10) {
                htmlString += ":0" + p.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            } else {
                htmlString += ":" + p.getAktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            }
            htmlString += "Parkplatzart: " + p.getAktiveTickets().get(index).getArtDesParkplatzes() + "</p>";
            index++;
        }
        htmlString += "</body></html>";
        return htmlString;
    }



    public String StringFuerInaktiveTicketsAuflistung(HttpServletResponse response) throws IOException {
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");
        String htmlString = "";
        htmlString += "<html><body><h2>Alle inaktiven Tickets: </h2>";
        int index = 0;
        for (Ticket i : p.getInaktiveTickets()) {
            htmlString += "<p>Ticket" + p.getInaktiveTickets().get(index).getTicketID() + ", ";
            htmlString += "Datum: " + p.getInaktiveTickets().get(index).getDatum() + ", ";
            htmlString += "Ausfahrzeit: " + p.getInaktiveTickets().get(index).getUhrzeit().getHour();
            if (p.getInaktiveTickets().get(index).getUhrzeit().getMinute() < 10) {
                htmlString += ":0" + p.getInaktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            } else {
                htmlString += ":" + p.getInaktiveTickets().get(index).getUhrzeit().getMinute() + ", ";
            }
            htmlString += "Parkplatzart: " + p.getInaktiveTickets().get(index).getArtDesParkplatzes() + "</p>";
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

    private void NachrichtenAusblenden (){
        if (getServletContext().getAttribute("TicketErstellenException") != null) {
            getServletContext().removeAttribute("TicketErstellenException");
        }
        if (getServletContext().getAttribute("BezahlenException") != null) {
            getServletContext().removeAttribute("BezahlenException");
        }
        if (getServletContext().getAttribute("AusfahrenException") != null) {
            getServletContext().removeAttribute("AusfahrenException");
        }
    }

    public void destroy() {
    }
}
