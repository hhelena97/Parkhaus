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

    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");

        request.setAttribute("parkhaus", p);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        //braucht man, damit der nachdem 1 Knopf in der Betreiberansicht gedrückt wurde nicht wieder auf die Startseite springt
        String naechsteSeite = "index.jsp";

        //hole das Parkhaus aus dem Context
        Parkhaus p = (Parkhaus) getServletContext().getAttribute("parkhaus");

        //führe je nach "action" verschiedene Dinge aus
        String action = request.getParameter("action");

        if ("start".equals(action)) {
            p.resetTicketListen();
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());
            p = new Parkhaus(3, 100, 5, 5, 10);

            LocalTime time = LocalTime.of(8,0);                         // resetten der Uhrzeit
            LocalDate date = LocalDate.of(2023,1,1);          // resetten des Datums
            p.setUhrzeit(time);                                                     // setzen der neuen Zeit
            p.setDatum(date);                                                       // setzen des neuen Datums

            getServletContext().setAttribute("parkhaus", p);
            //Exception-Nachrichten ausblenden
            NachrichtenAusblenden();

        } else if("ParkhauszeitenAnpassen".equals(action)) {        // Anpassen der Parkhauszeit

            LocalTime t = LocalTime.parse(request.getParameter("Zeit"));    // auf Webseite eingegebene Zeit
            LocalDate d = LocalDate.parse(request.getParameter("Datum"));   // auf Webseite eingegebenes Datum

            p.parkhauszeitAnpassen(t,d);        // passe Parkhauszeit an

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

            //Exception-Nachrichten ausblenden
            NachrichtenAusblenden();

            //erstellt ein neues Ticket mit der ausgewählten Parkplatzart
            try {
                Ticket t = p.neuesTicket(request.getParameter("ticketArt"));

                getServletContext().setAttribute("TicketErstellt",
                                "<p> Es wurde ein neues Ticket mit Parkplatzart: " + t.getArtDesParkplatzes()
                                + " und ID: " + t.getTicketID() + " erstellt! </p><br>");

                System.out.println("Neues Ticket erstellt");
                System.out.println(t);
                //(über)schreibt die Liste aktiver Tickets im Context
                getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            } catch (ParkplaetzeBelegtException e) {
                getServletContext().setAttribute("TicketErstellenException", e.getMessage());
            } catch (ParkhausGeschlossenException e2) {
                getServletContext().setAttribute("ParkhausGeschlossenException", e2.getMessage());
            }


        } else if ("bezahlen".equals(action)) {         // Bezahlen eines Tickets

            //Exception-Nachrichten ausblenden
            NachrichtenAusblenden();

            int len = p.getAktiveTickets().size();
            try {
                for (int i = 0; i < len; i++)           // suche Ticket mit eingegebener TicketID in den aktiven Tickets
                {
                    if (p.getAktiveTickets().get(i).getTicketID() == Integer.parseInt(request.getParameter(
                            "ticketID"))) {

                        Ticket t = p.getAktiveTickets().get(i);     // speichere das Ticket mit der ID
                        t.setPreis(t.bezahlen());                   // bestimme den Preis des Tickets
                        double preis = t.getPreis();                // speichere den Preis des Tickets
                        double rabattEuro = t.getRabattEuro();      // speichere den Rabatt des Tickets
                        int parkzeit = t.getParkdauerMin();         // speichere die Parkzeit des Tickets

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
                    Exception e1 = new TicketNichtGefundenException
                            ("Ticket nicht gefunden. Zur Zahlung bereite Tickets unter 'aktive Tickets'.");
                    getServletContext().setAttribute("BezahlenException", e1.getMessage());
                }
            } catch (TicketNichtGefundenException e11){
                getServletContext().setAttribute("TicketNichtGefundenException", e11.getMessage());
            } catch (NumberFormatException e2) {
                //Do nothing
            } catch (ParkhausGeschlossenException e3) {
                getServletContext().setAttribute("ParkhausGeschlossenException", e3.getMessage());
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
                    if (ti.getTicketID() == Integer.parseInt(request.getParameter("ticketID"))) {
                        ticketAusfahren = ti;
                    }
                }

                String nachricht = ticketAusfahren.ausfahren();
                request.setAttribute("NachrichtX", nachricht);
            } catch (TicketNichtGefundenException e1) {
                getServletContext().setAttribute("AusfahrenException", e1.getMessage());
            } catch (NullPointerException nullPointerException){
                getServletContext().setAttribute("AusfahrenException", "Ausfahren fehlgeschlagen!");
            }catch (NumberFormatException e2) {
                //Do nothing
            } catch (ParkhausGeschlossenException e3) {
                getServletContext().setAttribute("ParkhausGeschlossenException", e3.getMessage());
            }

            //(über)schreibt die Liste aktiver und inaktiver Tickets im Context
            getServletContext().setAttribute("ticketliste", p.getAktiveTickets());
            getServletContext().setAttribute("inaktiveTicketliste", p.getInaktiveTickets());

        } else if ("rabattGeben".equals(action)) {              // Ticket einen Rabatt geben
            int len = p.getAktiveTickets().size();
            double rabatt = 0;
            for (int i = 0; i < len; i++)               // suche Ticket mit eingegebener TicketID in den aktiven Tickets
            {
                if (p.getAktiveTickets().get(i).getTicketID() == Integer.parseInt(request.getParameter("ticketID"))){

                    Ticket t = p.getAktiveTickets().get(i);         // speichere das Ticket mit der ID

                    if (request.getParameter("rabatt").equals("Personalrabatt (10 %)"))
                    {
                        rabatt = 0.1;       // gebe Rabatt wie ausgewählt (10 %)
                    }
                    else if (request.getParameter("rabatt").equals("Besucher EKZ (20 %)"))
                    {
                        rabatt = 0.2;       // gebe Rabatt wie ausgewählt (20 %)

                    }
                    else if (request.getParameter("rabatt").equals("Treuerabatt (25 %)"))
                    {
                        rabatt = 0.25;      // gebe Rabatt wie ausgewählt (25 %)
                    }

                    t.setRabattProzent(rabatt);     // setze den Rabatt vom Ticket auf den ausgewählten Rabatt

                    // Um diese Elemente anzeigen zu können:
                    request.setAttribute("rabattTicketX", t);
                    request.setAttribute("rabattX", (rabatt * 100));
                }
            }
            //damit der nach dem Ändern in der Betreiberansicht bleibt
            naechsteSeite = "Betreiberansicht.jsp";

        } else if ("aktiveTickets".equals(action)) {
            //öffnet Seite, auf der alle aktiven Tickets aufgelistet werden
            request.getRequestDispatcher("aktiveTickets.jsp").forward(request, response);

        } else if ("inaktiveTickets".equals(action)){
            //öffnet Seite, auf der alle inaktiven Tickets aufgelistet werden
            request.getRequestDispatcher("inaktiveTickets.jsp").forward(request, response);

        } else if ("Betreiberansicht".equals(action)) {
            //öffnet die Betreiberansicht
            request.getRequestDispatcher("Betreiberansicht.jsp").forward(request, response);

        } else if ("OeffnungszeitenAendern".equals(action)) {
            //globale Öffnungszeiten des Parkhauses werden hier verändert
            LocalTime oeffnen = LocalTime.parse(request.getParameter("Oeffnen"));
            LocalTime schliessen = LocalTime.parse(request.getParameter("Schliessen"));

            //neue Öffnungszeiten im Context ändern
            getServletContext().setAttribute("Oeffnen", oeffnen);
            getServletContext().setAttribute("Schliessen", schliessen);

            //Öffnungszeiten im Parkhaus aktualisieren
            p.setOeffnungszeit(oeffnen);
            p.setSchliessungszeit(schliessen);

            //damit der nach dem Ändern in der Betreiberansicht bleibt
            naechsteSeite = "Betreiberansicht.jsp";

        } else if ("StundentarifAendern".equals(action)) {
            double neuerPreis = Double.parseDouble(request.getParameter("Preis"));
            getServletContext().setAttribute("Stundentarif", neuerPreis);
            p.setStundentarif(neuerPreis);
            System.out.println("Preis ändern in " + neuerPreis);

            //damit der nach dem Ändern in der Betreiberansicht bleibt
            naechsteSeite = "Betreiberansicht.jsp";
        }
        request.setAttribute("parkhaus", p);
        request.getRequestDispatcher(naechsteSeite).forward(request, response);
    }


    //Eine Methode um alle Nachrichten auszublenden
    private void NachrichtenAusblenden (){
        if (getServletContext().getAttribute("TicketErstellt") != null) {
            getServletContext().removeAttribute("TicketErstellt");
        }
        if (getServletContext().getAttribute("TicketErstellenException") != null) {
            getServletContext().removeAttribute("TicketErstellenException");
        }
        if (getServletContext().getAttribute("BezahlenException") != null) {
            getServletContext().removeAttribute("BezahlenException");
        }
        if (getServletContext().getAttribute("AusfahrenException") != null) {
            getServletContext().removeAttribute("AusfahrenException");
        }
        if (getServletContext().getAttribute("ZeitException") != null) {
            getServletContext().removeAttribute("ZeitException");
        }
        if (getServletContext().getAttribute("ParkhausGeschlossenException") != null) {
            getServletContext().removeAttribute("ParkhausGeschlossenException");
        }
    }

    public void destroy() {
    }
}
