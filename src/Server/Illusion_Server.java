package Server;

/* Imports All the Necessary Packages*/
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * This is the Server class which establishes a TCP connection on a specific port with two clients, it then sends out commands
 * on how to dynamically encrypt the clients conversation however, it itself cannot intercept their conversations, only the packets
 * they are sending out.
 * @author Mehul Sen
 */
public class Illusion_Server {

    /* Server Socket */
    private ServerSocket serverSocket;

    /* All the Client 1 Connections */
    private Socket client1;
    private PrintWriter printWriter1;
    private Scanner in1;

    /* All the Client 2 Connections */
    private Socket client2;
    private PrintWriter printWriter2;
    private Scanner in2;

    /* Port Number for the Connection */
    private int port;

    /* Boolean value for the State of the CPde */
    private boolean running = true;

    /**
     * Constructor for Server Creation
     * @param port Port Number for Illusion
     * @throws IOException Exception Handling for inp==ut and output
     */
    private Illusion_Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.port = port;
    }

    /**
     * Establishes Connection with both the Clients and starts the Encryption.
     * @throws IOException Exception handling for input and output
     */
    private void connectToClients()throws IOException{
        /* User #1 Connections */
        System.out.println("\t Establishing Connection with User #1");
        client1 = serverSocket.accept();
        printWriter1 = new PrintWriter(client1.getOutputStream());
        print(printWriter1, Illusion_Protocol.INIT + " 1 ");
        in1 = new Scanner(client1.getInputStream());
        System.err.println("\t Connection Established");

        /* User #2 Connections */
        System.out.println("\t Establishing Connection with User #2");
        client2 = serverSocket.accept();
        printWriter2 = new PrintWriter(client2.getOutputStream());
        print(printWriter2, Illusion_Protocol.INIT + " 2 ");
        in2 = new Scanner(client2.getInputStream());

        /* Passes on to the Next Stage */
        System.err.println("\t Connection Established");
        System.out.println("\t Initiating Dynamic Encryption Sequence: ");
    }

    /**
     * Converts any array into a continuous string
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
     * Sends a String output to the Client
     * @param printer PrintWriter of a Client
     * @param text    Text to be Sent
     */
    private void print(PrintWriter printer, String text) {
        printer.println(text);
        printer.flush();
    }

    /**
     * Sends a String output to both the Clients
     * Used to Send Commands coming from the Server
     * @param text        Text to be Sent
     */
    private void announce(String text) {
        print(printWriter1, text);
        print(printWriter2, text);
    }

    /**
     * Randomly rearranges the letters 'R','B','6' and 'A"
     * @return string with ARB6 rearranged.
     */
    private String randomConfig(){
        List<Character> characters = new ArrayList<>();
        for(char c:"ARB6".toCharArray()){
            characters.add(c);
        }
        StringBuilder output = new StringBuilder(4);
        while(characters.size()!=0){
            int randPicker = (int)(Math.random()*characters.size());
            output.append(characters.remove(randPicker));
        }
        return output.toString();
    }

    /**
     * Starts a loop in which the Encryptions as well as the Conversations take place.
     */
    private void talk() {
        /* Variables that will be required */
        int count = 0;
        Random random = new Random();
        int changeAt = random.nextInt(5 - 1 + 1) + 1;

        /* The Conversations occur here */
        try {
            while (running){

                /* Counts the Number of Loops occuring */
                count ++;

                /* User #1 Converses */
                print(printWriter1, Illusion_Protocol.UR_TURN);
                String[] user1said = getText(in1);
                System.out.println(" User #1: "+printText(user1said));
                if(user1said[0].equals(Illusion_Protocol.TERMINATE)) running=false;
                print(printWriter2, Illusion_Protocol.SPEECH +" " +printText(user1said));

                /* User #2 Converses */
                print(printWriter2, Illusion_Protocol.UR_TURN);
                String[] user2said = getText(in2);
                System.out.println(" User #2: "+printText(user2said));
                if(user2said[0].equals(Illusion_Protocol.TERMINATE)) running=false;
                print(printWriter1, Illusion_Protocol.SPEECH +" " +printText(user2said));

                /* Announces a Config change at Random Intervals */
                if(count == changeAt){
                    String encrypted = AESEncryption.encrypt(randomConfig(),Integer.toString(port));
                    announce(Illusion_Protocol.SERVER_SPEECH +" "+encrypted);
                    count = 0;
                    changeAt = random.nextInt(5 - 1 + 1) + 1;
                }
            }
        } catch (NoSuchElementException ne){
            /* Catches Exceptions and Tells the Clients that an error has occurred */
            print(printWriter1, Illusion_Protocol.ERROR);
            print(printWriter2, Illusion_Protocol.ERROR);
            System.err.println("\t Connection Lost");
        }
        try {
            /* Closes All the Connections */
            closeConnections();
            System.out.println("Goodbye :)");
        } catch (IOException ie) {ie.printStackTrace();}
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
    private void closeConnections() throws IOException {
        /* User #1 Connections */
        printWriter1.flush();
        in1.close();
        printWriter1.close();
        client1.close();

        /* User #2 Connections */
        printWriter2.flush();
        in2.close();
        printWriter2.close();
        client2.close();

        /* Closes the Socket */
        serverSocket.close();
        System.err.println("\t Successfully closed the Connection.");
    }

    /**
     * Main Class that runs actually runs the Server
     * @param args Arguments not present
     * @throws IOException In Case of an Input Output Error
     */
    public static void main(String[]args) throws IOException {
        /* String Handling from User */
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        /* Gathers the Port Number */
        System.out.print("Enter the Port Number : ");
        int port = 0;
        boolean incorrect = true;
        while(incorrect){
            try {
                port = Integer.parseInt(br.readLine());
                if (VariableCheck.portChecker(port)) incorrect = false;
                else System.out.print("Wrong Input, Try Again : ");
            } catch (NumberFormatException nfe){
                System.out.print("That Is Not A Number, Try Again : ");
            }
        }

        /* Establishes Communication */
        System.err.println("\t Establishing Connection ... ");
        try {
            Illusion_Server illusionServer = new Illusion_Server(port);
            illusionServer.connectToClients();
            illusionServer.talk();
        } catch (IOException ie) {
           System.out.println("Try Changing the Port Number");
        }
    }
}
