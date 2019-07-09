package Client;

import Common.IllProtocol;
import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Client side application for Illusion
 * @author Mehul Sen
 */
public class IllClient {

    /* Socket Connection to Server */
    private Socket socket;

    /* Connection to the Server for Input and Output */
    private PrintWriter printWriter;
    private Scanner in;

    /* Input from User Console */
    private Scanner user;

    /**
     * Constructor for the Client
     * @param hostname Hostname of the Server
     * @param port Port Number of the Sever
     * @throws IOException Input Output Exception
     */
    private IllClient(String hostname, int port)throws IOException {
        socket = new Socket(hostname,port);
        printWriter = new PrintWriter(socket.getOutputStream());
        in = new Scanner(socket.getInputStream());
        user = new Scanner(System.in);
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
     * Prints to the Server
     * @param text Text to be Printed
     */
    private void print(String text){
        printWriter.println(text);
        printWriter.flush();
    }

    /**
     * Reads from the Server
     * @return String array read
     */
    private String[] read(){
        return in.nextLine().split(" ");
    }

    /**
     * Conducts the communication between the server and the client.
     */
    private void initiate() {
        int usrNo = Integer.parseInt(read()[1]);
        /* Other User's Number */
        int otherNo;
        if(usrNo == 1) otherNo = 2;
        else otherNo = 1;
        System.err.println("\t Connected to Server : #"+usrNo);
        boolean run = true;
        try{
            while(run){
                String[] input = read();
                switch(input[0]){
                    case IllProtocol.SERVER_SPEECH:
                        System.out.println("\t SERVER : "+concatenateArray(input));
                    case IllProtocol.ERROR:
                        System.err.println("\t Something Went Wrong");
                        run = false;
                        break;
                    case IllProtocol.TERMINATE:
                        System.err.println("\t Connection Closed By the Other User");
                        run = false;
                        break;
                    case IllProtocol.SPEECH:
                        System.err.println("\t #"+ otherNo + " : "+concatenateArray(input));
                        break;
                    case IllProtocol.UR_TURN:
                        System.out.print(">");
                        String text = user.nextLine();
                        if(text.equals("TERMINATE")){
                            run = false;
                            System.err.println("\t Closing Connection To Server");
                            print(IllProtocol.TERMINATE);
                        }
                        //TODO Add Encryption To text
                        print(IllProtocol.SPEECH + " " + text);
                        break;
                }
            }
        }catch (NoSuchElementException ne){System.err.println("\t Connection Lost!");}
        in.close();
        printWriter.close();
        user.close();
        try{socket.close();}catch (IOException ie){ie.printStackTrace();}
        System.err.println("\t Connection Closed");
    }

    /**
     * Main Function from the Client Side
     * @param args Arguments that might be passed
     * @throws IOException Exception Handling for Input and Output
     */
    public static void main(String[]args) throws IOException {
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);

        System.out.println("\t Illusion Encryption");
        System.out.println("\t Client Application");
        System.out.println("\t Ver 0.2");
        System.out.println("\t By -M- \n");

        System.out.println("Enter the Host IP Address : ");
        String host = br.readLine();
        System.out.println("Enter the Port Number : ");
        int port = Integer.parseInt(br.readLine());
        boolean scanning = true;
        System.err.println("\t Establishing Connection ... ");
        while(scanning){
            try {
                IllClient server = new IllClient(host,port);
                server.initiate();
                scanning = false;
            }catch (IOException ie){ie.printStackTrace();}
        }
    }
}
