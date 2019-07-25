package DynamicIllusion;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Client {
    /* Socket Connection to Server */
    private Socket socket;

    /* Connections to the Server for both the Input and Output */
    private PrintWriter printWriter;
    private Scanner in;

    /* Input from User Console */
    private Scanner user;

    private String secret;

    private String config = "RB6A";

    /**
     * Constructor for the IllClient Class
     * This takes opens the sockets and maintains t
     * @param hostname Hostname of the Server
     * @param port Port Number of the Sever
     * @throws IOException Input Output Exception
     */
    private Client(String hostname, int port, String secret)throws IOException {
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

    private String scramble(String text, boolean enc){
        String output = text;
        Encryptor encryptor = new Encryptor();
        if(enc){
            for(int i = 0; i<config.length();i++){
                char ch = config.charAt(i);
                switch (ch){
                    case 'A':
                        output = encryptor.AESit(output,true,secret);
                        break;
                    case '6':
                        output = encryptor.B64it(output,true);
                        break;
                    case 'B':
                        output = encryptor.BINit(output,true);
                        break;
                    case 'R':
                        output = encryptor.rotIt(output);
                        break;
                }
            }
            return output;
        }
        else{
            for(int i = config.length()-1; i>=0;i--){
                char ch = config.charAt(i);
                switch (ch){
                    case 'A':
                        output = encryptor.AESit(output,false,secret);
                        break;
                    case '6':
                        output = encryptor.B64it(output,false);
                        break;
                    case 'B':
                        output = encryptor.BINit(output,false);
                        break;
                    case 'R':
                        output = encryptor.rotIt(output);
                        break;
                }
                System.out.println(output);
            }
        }
        return output;
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
        String[] init = read();
        int usrNo = Integer.parseInt(init[1]);
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
                        break;
                    case IllProtocol.ERROR:
                        System.err.println("\t Something Went Wrong");
                        run = false;
                        break;
                    case IllProtocol.TERMINATE:
                        System.err.println("\t Connection Closed By the Other User");
                        run = false;
                        break;
                    case IllProtocol.SPEECH:
                        String inputtext = concatenateArray(input).trim();
                        System.err.println("\t #"+ otherNo + " : " + scramble(inputtext,false));
                        break;
                    case IllProtocol.UR_TURN:
                        System.out.print(">");
                        String text = user.nextLine();
                        if(text.equals("TERMINATE")){
                            run = false;
                            System.err.println("\t Closing Connection To Server");
                            print(IllProtocol.TERMINATE);
                        }
                        String outputtext = scramble(text,true);
                        print(IllProtocol.SPEECH + " " + outputtext);
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

    public static void main(String[]args)throws IOException{
        boolean scanning = true;
        System.err.println("\t Establishing Connection ... ");
        while(scanning){
            try {
                Client server = new Client("192.168.206.1",12343,"mehul");
                server.initiate();
                scanning = false;
            }catch (IOException ie){ie.printStackTrace();}
        }
    }
}
