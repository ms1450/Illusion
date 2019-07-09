package Common;

/**
 * A List of the Protocol Commands that will be Used in this Encryption
 * @author Mehul Sen
 */
public interface IllProtocol {
    //Initialize Command Sent By the Server
    public static final String INIT = "ILLUSION_INIT";

    //Server Requesting the Client for Text
    public static final String UR_TURN = "REQ_TEXT";

    //Client Providing the Server with Text
    public static final String SPEECH = "SAID";

    //Future Applications of Server replying to the Clients on its own
    public static final String SERVER_SPEECH = "SERVER_SAID";

    //Sent when ending a client ends a conversation
    public static final String TERMINATE = "EXIT";

    //Sent if an error occurs
    public static final String ERROR = "OHNO";
}
