package com.Illusion.client;

/**
 * A List of the Protocol Commands that will be Used in this Encryption
 * @author Mehul Sen
 */
public interface IllProtocol {
    //Initialize Command Sent By the Server
    String INIT = "ILLUSION_INIT";

    //Server Requesting the Client for Text
    String UR_TURN = "REQ_TEXT";

    //Client Providing the Server with Text
    String SPEECH = "SAID";

    //Future Applications of Server replying to the Clients on its own
    String SERVER_SPEECH = "SERVER_SAID";

    //Sent when ending a client ends a conversation
    String TERMINATE = "EXIT";

    //Sent if an error occurs
    String ERROR = "OHNO";
}
