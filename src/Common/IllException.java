package Common;

/**
 * Handles any exceptions that occur in this chat conole
 * @author Mehul Sen
 */
public class IllException extends Exception{
    public IllException(String message) {
        super(message);
    }
    public IllException(Throwable cause) {
        super(cause);
    }
    public IllException(String message, Throwable cause) {
        super(message, cause);
    }
}
