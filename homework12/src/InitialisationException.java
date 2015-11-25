import java.lang.Exception;import java.lang.String;import java.lang.Throwable; /**
/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : InitialisationException.java, 2015/11/06
 */
public class InitialisationException extends Exception {
    public InitialisationException() {
    }

    public InitialisationException(String message) {
        super(message);
    }

    public InitialisationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitialisationException(Throwable cause) {
        super(cause);
    }

    public InitialisationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
