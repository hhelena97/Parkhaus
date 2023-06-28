package parkhaus;

import javax.servlet.ServletException;

public class ReiseInVergangenheitException extends ServletException {

    /**
     * Die ReiseInVergangenheitException soll geworfen werden bei dem Versuch eine neue Parkhauszeit
     * zu setzen, in die Vergangenheit gereist werden soll. Ist dies der Fall, wird eine Exception geworfen.
     *
     * @param message wird beim Aufrufen der Exception festgelegt
     * @author jboven2s
     */
    public ReiseInVergangenheitException(String message) {
        super (message);
    }
}
