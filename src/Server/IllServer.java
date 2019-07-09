package Server;

import Common.IllProtocol;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The Server Side Application for Illusion
 * @author Mehul Sen
 */
public class IllServer implements IllProtocol {

    /* Server Socket Created */
    private ServerSocket serverSocket;

    /* Client 1 Connections */
    private Socket client1;
    private PrintWriter printWriter1;
    private Scanner in1;

    /* Client 2 Connections */
    private Socket client2;
    private PrintWriter printWriter2;
    private Scanner in2;

    /* State of the Application*/
    private boolean running = true;

    /**
     * Constructor for the Server Creation
     * @param port Port Number for Illusion
     * @throws IOException Exception Handling for inp==ut and output
     */
    private IllServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    /**
     * Connects to both the Clients and starts the exchange
     * @throws IOException Exception handling for input and output
     */
    private void connectToClients()throws IOException{
        System.err.println("\t Connecting to User #1 : ");
        client1 = serverSocket.accept();
        printWriter1 = new PrintWriter(client1.getOutputStream());
        print(printWriter1,IllProtocol.INIT + " 1");
        in1 = new Scanner(client1.getInputStream());
        System.err.println("\t Successfully Connected to User #1");

        System.err.println("\t Connecting to User #2 : ");
        client2 = serverSocket.accept();
        printWriter2 = new PrintWriter(client2.getOutputStream());
        print(printWriter2,IllProtocol.INIT + " 2");
        in2 = new Scanner(client2.getInputStream());
        System.err.println("\t Successfully Connected to User #2");

        System.err.println("\t Initiating Conversation ...");
    }

    /**
     * Converts any array into a Continous string
     * @param array Array to be converted
     * @return Continuous string
     */
    private String printText(String[] array){
        StringBuilder temp = new StringBuilder();
        for(int i = 1;i< array.length; i++){
            temp.append(" ").append(array[i]);
        }
        return temp.toString();
    }

    /**
     * Sends a String to a Client
     * @param printer PrintWriter of a Client
     * @param text    Text to be Sent
     */
    private void print(PrintWriter printer, String text) {
        printer.println(text);
        printer.flush();
    }

//    /**
//     * Sends text to both the Clients
//     * (Might Be Used in Later Versions of the Application)
//     * @param printWriter1 PrintWriter of Client 1
//     * @param printWriter2 PrintWriter of Client 2
//     * @param text        Text to be Sent
//     */
//    private void announce(PrintWriter printWriter1, PrintWriter printWriter2, String text) {
//        print(printWriter1, text);
//        print(printWriter2, text);
//    }

    /**
     * Conducts the exchange of words between the Clients
     */
    private void talk() {
        try {
            while (running){
                print(printWriter1,IllProtocol.UR_TURN);
                String[] user1said = getText(in1);
                System.out.println(" User #1: "+printText((user1said)));
                if(user1said[0].equals(IllProtocol.TERMINATE))running=false;
                print(printWriter2,IllProtocol.SPEECH +" " +printText(user1said));

                print(printWriter2,IllProtocol.UR_TURN);
                String[] user2said = getText(in2);
                System.out.println(" User #2: "+printText((user2said)));
                if(user2said[0].equals(IllProtocol.TERMINATE))running=false;
                print(printWriter1,IllProtocol.SPEECH +" " +printText(user2said));
            }
        }catch (NoSuchElementException ne){
            print(printWriter1, IllProtocol.ERROR);
            print(printWriter2, IllProtocol.ERROR);
            System.err.println("\t Connection Lost");
        }
        try {closeGame();
        }catch (IOException ie){ie.printStackTrace();
        }
    }

    /**
     * Gets the Text from a client.
     * @param in Scanner for a Client
     * @return Array including the Text
     */
    private String[] getText(Scanner in) {
        return in.nextLine().split(" ");
    }

    /**
     * Closes every Connection.
     * @throws IOException Input Output Exception
     */
    private void closeGame() throws IOException {
        printWriter1.flush();
        in1.close();
        printWriter1.close();
        client1.close();

        printWriter2.flush();
        in2.close();
        printWriter2.close();
        client2.close();

        serverSocket.close();
    }

    /**
     * Starts a new sever.
     * @param args Used to specify the port on which the server should listen
     *             for incoming client connections.
     */
    public static void main(String[] args) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        System.out.println("\t Illusion Encryption");
        System.out.println("\t Server Application");
        System.out.println("\t Ver 0.2");
        System.out.println("\t By -M- \n");

        System.out.println(" Enter the Port Number : ");
        int port = Integer.parseInt(br.readLine());
        System.err.println("\t Establishing Connection ... ");
        try {
            IllServer server = new IllServer(port);
            server.connectToClients();
            server.talk();
        } catch (IOException ie) {
            System.out.println();
        }


    }
}