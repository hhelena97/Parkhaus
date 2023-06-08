package Parkhaus;

public class ParkplaetzeBelegtException extends Exception{

    /**
     * Die ParkplaetzeBelegtException soll geworfen werden bei dem Versuch einen neuen Parkplatz zu belegen, jedoch
     * die gewünschte Parkplatzart nicht verfügbar ist oder alle Parkplätze bereits belegt sind.
     * @param message ist die von der aufrufenden Methode hinzugefügte Exceptionnachricht, die auch auf der
     *                Parkhausseite ausgegeben werden kann.
     * @author hheyen2s
     */
    public ParkplaetzeBelegtException(String message) {
        super (message);
    }
}
