package DynamicIllusion;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

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

    private int port;

    /* Is this Code Running */
    private boolean running = true;

    /**
     * Constructor for the Server Creation
     * @param port Port Number for Illusion
     * @throws IOException Exception Handling for inp==ut and output
     */
    private Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        this.port = port;
    }

    /**
     * Established Connection with both the Clients
     * @throws IOException Exception handling for input and output
     */
    private void connectToClients()throws IOException{
        System.out.println("\t Establishing Connection with User #1");
        client1 = serverSocket.accept();
        printWriter1 = new PrintWriter(client1.getOutputStream());
        print(printWriter1,IllProtocol.INIT + " 1 ");
        in1 = new Scanner(client1.getInputStream());
        System.err.println("\t Connection Established");
            System.out.println("\t Establishing Connection with User #2");
            client2 = serverSocket.accept();
            printWriter2 = new PrintWriter(client2.getOutputStream());
            print(printWriter2,IllProtocol.INIT + " 2 ");
            in2 = new Scanner(client2.getInputStream());
            System.err.println("\t Connection Established");
            System.out.println("\t Initiating Dynamic Encryption Sequence: ");
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
     * (Might Be Used in Later Versions of the Application)
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
     * Starts a Loop where the Users Send messages to Each other one after the other.
     */
    private void talk() {
        Random random = new Random();
        int count = 0;
        int changeAt = random.nextInt(5 - 1 + 1) + 1;
        try {
            while (running){
                count ++;
                /* User #1 Converses */
                print(printWriter1,IllProtocol.UR_TURN);
                String[] user1said = getText(in1);
                System.out.println(" User #1: "+printText(user1said));
                if(user1said[0].equals(IllProtocol.TERMINATE)) running=false;
                print(printWriter2,IllProtocol.SPEECH +" " +printText(user1said));

                /* User #2 Converses */
                print(printWriter2,IllProtocol.UR_TURN);
                String[] user2said = getText(in2);
                System.out.println(" User #2: "+printText(user2said));
                if(user2said[0].equals(IllProtocol.TERMINATE)) running=false;
                print(printWriter1,IllProtocol.SPEECH +" " +printText(user2said));

                /* Announces a Config change at random intervals */
                if(count == changeAt){
                    Encryptor encryptor = new Encryptor();
                    String encrypted = encryptor.AESit(randomConfig(),true,Integer.toString(port));
                    announce(IllProtocol.SERVER_SPEECH +" "+encrypted);
                    count = 0;
                    changeAt = random.nextInt(5 - 1 + 1) + 1;
                }
            }
        } catch (NoSuchElementException ne){
            print(printWriter1, IllProtocol.ERROR);
            print(printWriter2, IllProtocol.ERROR);
            System.err.println("\t Connection Lost");
        }
        try {
            closeGame();
            System.err.println("Closed the Connection.");
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

    public static void main(String[]args)throws IOException{
        System.err.println("\t Establishing Connection ... ");
        try {
            Server server = new Server(1672);

            server.connectToClients();
            server.talk();
        } catch (IOException ie) {
           System.out.println("Na, I dont feel like Working today.");
        }
    }

}
