package Client;

/* Packages required for this Program */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * This class handles the Client Side Communication of the Illusion Connection.
 * It Encrypts the Data and sends it to the server by establishing a connection, however the server has no knowledge about
 * the contents of the data packets, only another server connected the the same client can decrypt the dynamically encrypted
 * packets to extract the text messages sent by the client.
 * @author Mehul Sen
 */
public class Illusion_Client {
    /* Socket Connection to Server */
    private Socket socket;

    /* Connections to the Server for both the Input and Output */
    private PrintWriter printWriter;
    private Scanner in;

    /* Input from User Console */
    private Scanner user;

    /* Secret Text */
    private String secret;

    /* Illusion_Encryptors Instance */
    private Illusion_Encryption illusionEncrypt;

    /* Port Number on which the Connection takes place */
    private int port;

    /* Initial Configuration of the Text, however, this randomly changes during runtime. */
    private String config;

    /**
     * Constructor for the Client Class
     * This takes opens the sockets and maintains encryption from start till the end of the program.
     * @param hostname Hostname of the Server
     * @param port Port Number of the Sever
     * @throws IOException Input Output Exception
     */
    private Illusion_Client(String hostname, int port, String secret)throws IOException {
        /* Initial Values for different Variables. */
        config = "ARB6";
        illusionEncrypt = new Illusion_Encryption();
        this.port = port;
        this.secret = secret;

        /* Connections to the Server */
        socket = new Socket(hostname,port);
        printWriter = new PrintWriter(socket.getOutputStream());
        in = new Scanner(socket.getInputStream());
        user = new Scanner(System.in);
    }

    /**
     * Function to print the application banner.
     */
    private void banner(){
        System.out.println(" _____ _             _____ _ _           _             ");
        System.out.println("/__   \\ |__   ___    \\_   \\ | |_   _ ___(_) ___  _ __");
        System.out.println("  / /\\/ '_ \\ / _ \\    / /\\/ | | | | / __| |/ _ \\| '_ \\ ");
        System.out.println(" / /  | | | |  __/ /\\/ /_ | | | |_| \\__ \\ | (_) | | | |");
        System.out.println(" \\/   |_| |_|\\___| \\____/ |_|_|\\__,_|___/_|\\___/|_| |_|");
        System.out.println(" ");
        System.out.println("Client Application");
        System.out.println("Created by -M-");
    }

    /**
     * Concatenates the String Array into one Long String
     * @param array Array to be concatenated
     * @return One Long String
     */
    private String concatenateArray(String[] array){
        StringBuilder temp = new StringBuilder();
        for(int i = 1;i< array.length; i++){
            temp.append(" ").append(array[i]);
        }
        return temp.toString();
    }

    /**
     * Uses four different encryption's on the text based on the configuration that is set during runtime.
     * This is dynamic and thus it is guaranteed to have a different method of encryption after every 10 exchanges of text.
     * @param text Text to be encrypted
     * @param enc True for encoding and False for decoding the text
     * @return Scrambled text
     */
    private String scramble(String text, boolean enc){
        String output = text;
        /* If the Text is to be Encrypted */
        if(enc){
            for(int i = 0; i<config.length();i++){
                char ch = config.charAt(i);
                switch (ch){
                    case 'A':
                        output = illusionEncrypt.AESit(output,true,secret);
                        break;
                    case '6':
                        output = illusionEncrypt.B64it(output,true);
                        break;
                    case 'B':
                        output = illusionEncrypt.BINit(output,true);
                        break;
                    case 'R':
                        output = illusionEncrypt.rotIt(output);
                        break;
                }
            }
            return output;
        }
        /* If the Text is to be Decrypted */
        else{
            for(int i = config.length()-1; i>=0;i--){
                char ch = config.charAt(i);
                switch (ch){
                    case 'A':
                        output = illusionEncrypt.AESit(output,false,secret);
                        break;
                    case '6':
                        output = illusionEncrypt.B64it(output,false);
                        break;
                    case 'B':
                        output = illusionEncrypt.BINit(output,false);
                        break;
                    case 'R':
                        output = illusionEncrypt.rotIt(output);
                        break;
                }
            }
        }
        return output;
    }

    /**
     * Sends the Text to the Server
     * @param text Text to be Printed
     */
    private void print(String text){
        printWriter.println(text);
        printWriter.flush();
    }

    /**
     * Reads text from the Server
     * @return String array read
     */
    private String[] read(){
        return in.nextLine().split(" ");
    }

    /**
     * The Encryption and the Communication occur here, it reads what the server says and responds accordingly.
     */
    private void initiate() {

        /* The Connections get Established in this part. */
        String[] init = read();
        int usrNo = Integer.parseInt(init[1]);
        int otherNo;
        if(usrNo == 1) otherNo = 2;
        else otherNo = 1;
        System.err.println("\t Connected to Server : #"+usrNo);
        System.out.println("\t Type \"TERMINATE\" to Close the Connection.");

        /* The Server and Client Communicate inside the While loop where the Client handles each input from the server
        based on the specific protocol. */
        boolean run = true;
        try{
            while(run){
                String[] input = read();
                switch(input[0]){
                    /* The Dynamic aspect comes into play as the Server constantly changes the configuration for encryption */
                    case Illusion_Protocol.SERVER_SPEECH:
                        String configuration = illusionEncrypt.AESit(input[1],false,Integer.toString(port));
                        System.out.println("\t SERVER : Changed Configuration to "+configuration);
                        config = configuration;
                        break;
                    case Illusion_Protocol.ERROR:
                        System.err.println("\t Something Went Wrong");
                        run = false;
                        break;
                    case Illusion_Protocol.TERMINATE:
                        System.err.println("\t Connection Closed By the Other User");
                        run = false;
                        break;
                    case Illusion_Protocol.SPEECH:
                        String input_text = concatenateArray(input).trim();
                        System.err.println("\t #"+ otherNo + " : " + scramble(input_text,false));
                        break;
                    case Illusion_Protocol.UR_TURN:
                        System.out.print(">");
                        String text = user.nextLine();
                        /* Closes Connection if the User types "TERMINATE" */
                        if(text.equals("TERMINATE")){
                            run = false;
                            System.err.println("\t Closing Connection To Server");
                            print(Illusion_Protocol.TERMINATE);
                        }
                        String output_text = scramble(text,true);
                        print(Illusion_Protocol.SPEECH + " " + output_text);
                        break;
                }
            }
        } catch (NoSuchElementException ne){System.err.println("User #"+otherNo+ " terminated the connection.");}

        /* Closes all Connections to the Server */
        in.close();
        printWriter.close();
        user.close();
        try{socket.close();}catch (IOException ie){ie.printStackTrace();}
        System.err.println("\t Connection Successfully Closed");
        System.out.println("Goodbye :)");
    }

    /**
     * This is the main class where the Client gets instantiated. This method takes the input from the User and creates
     * connections based on that input.
     * @param args Arguments are not Implemented.
     * @throws IOException Input Output error that might occur.
     */
    public static void main(String[]args)throws IOException{

        /* Initial Values of Variables. */
        int port = 0;
        String ip = "";
        boolean incorrect = true;
        boolean scanning = true;
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        /* Loops till the User inputs valid IP Address */
        while(scanning) {
            System.out.print("Enter the IP Address you want to Connect to : ");
            while (incorrect) {
                ip = br.readLine();
                if (VariableCheck.ipChecker(ip)) incorrect = false;
                else System.out.print(" Wrong Input, Try Again : ");
            }
            incorrect = true;
            /* Loops till the User inputs valid Port Number */
            System.out.print("Enter the Port Number : ");
            while (incorrect) {
                try {
                    port = Integer.parseInt(br.readLine());
                    if (VariableCheck.portChecker(port)) incorrect = false;
                    else System.out.print("Wrong Input, Try Again : ");
                } catch (NumberFormatException nfe) {
                    System.out.print("That Is Not A Number, Try Again : ");
                }
            }
            /* Takes the Secret Key from the User */
            System.out.print("Enter the Secret Key : ");
            String key = br.readLine();

            /* Established Communication once all the Variables are present */
            System.err.println("\t Establishing Connection ... ");
            Illusion_Client server = new Illusion_Client(ip, port, key);
            server.banner();
            server.initiate();
            scanning = false;
        }
    }
}
