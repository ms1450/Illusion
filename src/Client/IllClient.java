package Client;

import Common.IllusionVariableChecker;
import Common.IllusionEncryptor;
import Common.IllProtocol;
import java.io.*;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Client Application for Illusion
 * This class connects to the Server and encrypts any messages entered by the user before sending it to the server.
 * It also decrypts any messages received and relays them to the user.
 * @author Mehul Sen
 */
public class IllClient {

    /* Socket Connection to Server */
    private Socket socket;

    /* Connections to the Server for both the Input and Output */
    private PrintWriter printWriter;
    private Scanner in;

    /* The Secret Code to Be entered by the User on Runtime */
    private String secret;

    /* Input from User Console */
    private Scanner user;

    /**
     * Constructor for the IllClient Class
     * This takes opens the sockets and maintains t
     * @param hostname Hostname of the Server
     * @param port Port Number of the Sever
     * @throws IOException Input Output Exception
     */
    private IllClient(String hostname, int port, String secret)throws IOException {
        socket = new Socket(hostname,port);
        printWriter = new PrintWriter(socket.getOutputStream());
        in = new Scanner(socket.getInputStream());
        user = new Scanner(System.in);
        this.secret = secret;
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
     * Converts a number to an array consisting of that number
     * @param num Number to be converted
     * @return array containing those numbers
     */
    private int[] IntToArray(int num){
        int[] array = new int[8];
        for(int i = 0; i < 8; i++){
            array[i] = num%10;
            num = num/10;
        }
        return array;
    }

    /**
     * Conducts the communication between the server and the client.
     */
    private void initiate() {
        String[] init = read();
        int usrNo = Integer.parseInt(init[1]);
        int[] keyArr = IntToArray(Integer.parseInt(init[2]));
        /* Other User's Number */
        int otherNo;
        if(usrNo == 1) otherNo = 2;
        else otherNo = 1;
        System.err.println("\t Connected to Server : #"+usrNo);
        System.out.println("\t Type \"TERMINATE\" to Close the Connection.");
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
                        //System.err.println("\t #"+ otherNo + " : "+concatenateArray(input));
                        System.err.println("\t #"+ otherNo + " : " + IllusionEncryptor.decoder(concatenateArray(input).trim(), keyArr,secret));
                        break;
                    case IllProtocol.UR_TURN:
                        System.out.print(">");
                        String text = user.nextLine();
                        if(text.equals("TERMINATE")){
                            run = false;
                            System.err.println("\t Closing Connection To Server");
                            print(IllProtocol.TERMINATE);
                        }
                        //print(IllProtocol.SPEECH + " " + text);
                        print(IllProtocol.SPEECH + " " + IllusionEncryptor.encoder(text, keyArr, secret));
                        break;
                }
            }
        }catch (NoSuchElementException ne){System.err.println("User #"+otherNo+ " terminated the connection.");}
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

        IllusionVariableChecker.greet(false);
        boolean incorrect = true;
        String host = "";
        while(incorrect){
            System.out.println("Enter the Host IP Address : ");
            host = br.readLine().trim();
            if(IllusionVariableChecker.ipChecker(host)) incorrect = false;
            else System.out.println("Incorrect IP Syntax, Please Try Again.");
        }
        System.out.println("Enter the Port Number : ");
        int port = Integer.parseInt(br.readLine());
        System.err.println("Enter the Secret Key : ");
        String secret = br.readLine();
        boolean scanning = true;
        System.err.println("\t Establishing Connection ... ");
        while(scanning){
            try {
                IllClient server = new IllClient(host,port,secret);
                server.initiate();
                scanning = false;
            }catch (IOException ie){ie.printStackTrace();}
        }
    }
}
