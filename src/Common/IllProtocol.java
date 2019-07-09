package Common;

public interface IllProtocol {
    //Start Up
    public static final String INIT = "ILLUSION_INIT";
    //Sent from Server to Client Asking it for Text
    public static final String UR_TURN = "REQ_TEXT";
    //Sent from Client to server along with the text
    public static final String SPEECH = "SAID";
    public static final String SERVER_SPEECH = "SERVER_SAID";
    //Sent when ending a chat
    public static final String TERMINATE = "EXIT";
    //Sent if an error occurs
    public static final String ERROR = "OHNO";
}
