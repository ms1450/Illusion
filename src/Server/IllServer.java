package Server;

import Common.IllException;
import Common.IllProtocol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;


public class IllServer implements IllProtocol {
    /* Server Socket Created */
    private ServerSocket serverSocket;

    /* Client 1 Connection Created */
    private Socket client1;
    private PrintWriter printWriter1;
    private Scanner in1;

    /* Client 2 Connection Created */
    private Socket client2;
    private PrintWriter printWriter2;
    private Scanner in2;

    private boolean running = true;

    private IllServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

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

    private String printText(String[] array){
        String temp = "";
        for(int i = 1;i< array.length; i++){
            temp = temp + " " + array[i];
        }
        return temp;
    }

    /**
     * Sends a String to a Client
     *
     * @param printer PrintWriter of a Client
     * @param text    Text to be Sent
     */
    private void print(PrintWriter printer, String text) {
        printer.println(text);
        printer.flush();
    }

    /**
     * Sends text to both the Clients
     *
     * @param printWriter1 PrintWriter of Client 1
     * @param printWriter2 PrintWriter of Client 2
     * @param text1        Text to be Sent to Client 1
     * @param text2        Text to be Sent to Client 2
     */
    private void announce(PrintWriter printWriter1, PrintWriter printWriter2, String text1, String text2) {
        print(printWriter1, text1);
        print(printWriter2, text2);
    }

    private void talk()throws IllException{
        try {
            while (running){
                print(printWriter1,IllProtocol.UR_TURN);
                String[] user1said = getText(in1);
                System.out.println("\t User #1: "+printText((user1said)));
                if(user1said[0] == IllProtocol.TERMINATE)running=false;
                print(printWriter2,IllProtocol.SPEECH +" " +printText(user1said));

                print(printWriter2,IllProtocol.UR_TURN);
                String[] user2said = getText(in2);
                System.out.println("\t User #2: "+printText((user2said)));
                if(user2said[0] == IllProtocol.TERMINATE)running=false;
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

    private String[] getText(Scanner in) {
        String[] token = in.nextLine().split(" ");
        return token;
    }

    /**
     * Closes every Connection.
     *
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
     *
     * @param args Used to specify the port on which the server should listen
     *             for incoming client connections.
     * @throws IllException If there is an error starting the server.
     */
    public static void main(String[] args) throws IllException, IOException {
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