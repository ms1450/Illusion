package Common;

import Common.AESEncryption;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Base64;

/**
 * The first Encryptor that ive created, im not sure if im supposed to label it or not because its an encryption
 * technique but ah well screw it, im going to
 * Its a simple encryption
 * @author Mehul Sen
 */
public class IllusionEncryptor {

    /**
     * This method uses rot 13 if the character is a letter and rot 5 if it is a number
     * @param input string before rotting
     * @return rotted string
     */
    private String rotIt(String input){
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < input.length(); i++){
            char converted;
            char current = input.charAt(i);
            if(Character.isDigit(current)) converted = numROT(current);
            else if (Character.isLetter(current)) converted = charROT(current);
            else {converted = current;}
            output.append(converted);
        }
        return String.valueOf(output);
    }

    /**
     * Helper method to rot the character if the character is a letter.
     * @param input The char input provided
     * @return rotted character
     */
    private char charROT(char input){
        char[] alphabets = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        char converted = input;
        boolean upper = !(converted == (Character.toLowerCase(converted)));

        for (int i = 0; i < 26; i++) {
            if (Character.toLowerCase(input) == alphabets[i]) {
                int posMoved = i + 13;
                if (posMoved >= 26) posMoved -= 26;
                if (upper) converted = Character.toUpperCase(alphabets[posMoved]);
                else converted = alphabets[posMoved];
            }
        }
        return converted;
    }

    /**
     * Helper method to rot if the character is a number
     * @param input the char input provided
     * @return rotted character
     */
    private char numROT(char input){
        char[] numbers = {'1','2','3','4','5','6','7','8','9','0'};
        char converted = 0;
        for(int i = 0; i < 10; i ++){
            if (input == numbers[i]){
                int numMoved = i + 5;
                if(numMoved >= 10) numMoved -= 10;
                converted = numbers[numMoved];
            }
        }
        return converted;
    }

    /**
     * Converts a String to its binary counterpart
     * @param rot String
     * @return String but only consisting of ones and zeroes
     */
    private String ROTtoBIN(String rot){
        byte[] bytes = rot.getBytes();
        StringBuilder binary = new StringBuilder();
        for (byte b : bytes)
        {
            int val = b;
            for (int i = 0; i < 8; i++)
            {
                binary.append((val & 128) == 0 ? 0 : 1);
                val <<= 1;
            }
            binary.append(' ');
        }
        return String.valueOf(binary);
    }

    /**
     * Reverses the binary state, if it is one, then it will have zero, if it is a zero then it will have a one.
     * @param bin Binary String
     * @return reversed binary string
     */
    private String BINtoBINREV(String bin){
        StringBuilder newBin = new StringBuilder();
        for(int i = 0; i < bin.length(); i++){
            if(bin.charAt(i) == '0') newBin.append('1');
            else if(bin.charAt(i) == '1') newBin.append('0');
            else newBin.append(' ');
        }
        return newBin.toString();
    }

    /**
     * Converts a binary string back to a normal string
     * @param bin binary string
     * @return normal string
     */
    private String BINtoSTR(String bin){
                StringBuilder s = new StringBuilder(" ");
        for(int index = 0; index < bin.length(); index+=9) {
            String temp = bin.substring(index, index+8);
            int num = Integer.parseInt(temp,2);
            char letter = (char) num;
            s.append(letter);
        }
        return s.toString().trim();
    }

    /**
     * Adds another layer of encrypton by Encoding and Decoding the String based on the Key decided by the server.
     * @param org Original String
     * @param keylist KeyList array
     * @param enc Value to either encode or decode
     * @return Encoded or Decoded string
     */
    private String keyCrypt(String org, int[] keylist, boolean enc){
        StringBuilder conv = new StringBuilder();
        int count = 0;
        for(int i = 0; i < org.length(); i ++){
            char ch = org.charAt(i);
            if(ch != ' '){
                if(enc) conv.append(Character.getNumericValue(ch) + keylist[count]);
                else conv.append(Character.getNumericValue(ch) - keylist[count]);
                count ++;
            }
            else {
                count = 0;
                conv.append(" ");
            }
        }
        return conv.toString();
    }

    /**
     * Adds the mysterious 6th layer of encryption to the data.
     * @param org Original Data
     * @param enc True or false
     * @return Encoded or Decoded Data
     */
    private String sixthCrypt(String org, boolean enc){
        String output;
        if(enc) output = Base64.getEncoder().encodeToString(org.getBytes());
        else output = new String(Base64.getDecoder().decode(org));
        return output;
    }


    /**
     * The entire encoding method neatly tied with a bow, Kept it for any changes i might make in further developments
     * @param text Text to be encoded
     * @return String with the encoded data
     */
    public static String encoder(String text, int[] array, String secret){
        IllusionEncryptor ie = new IllusionEncryptor();
        return AESEncryption.encrypt(ie.sixthCrypt(ie.keyCrypt(ie.BINtoBINREV(ie.ROTtoBIN(ie.rotIt(text))),array,true),true),secret);
    }

    /**
     * The entire decoding method neatly tied with a bow
     * @param text text to be decoded
     * @return original text
     */
    public static String decoder(String text, int[] array, String secret){
        IllusionEncryptor ie = new IllusionEncryptor();
        return ie.rotIt(ie.BINtoSTR(ie.BINtoBINREV(ie.keyCrypt(ie.sixthCrypt(AESEncryption.decrypt(text.trim(),secret),false),array,false))));
    }

    /**
     * The main program to check if this encoded decoder is working or not.
     * @param args Arguments
     * @throws IOException Exception handling for input output.
     */
    public static void main(String[]args)throws IOException{
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        String secret = "mehul";
        boolean running = true;
        while(running) {
            int[] num = new int[] {1,2,3,4,5,6,7,8};
            System.out.print("ENTER A TEXT : ");
            String text = br.readLine();
            if (text.equals("TERMINATE")) running = false;
            String encoded = encoder(text, num, secret);
            System.err.println(encoded);
            String decoded = decoder(encoded, num, secret);
            System.err.println(decoded);
            if(decoded.equals(text))System.err.println("Success");
            else System.err.println("SOMETHING IS SERIOUSLY WRONG");


        }
    }
}
