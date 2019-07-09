package Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Text to ROT  then to BIN then to REV BIN
 */
public class IllusionEncryptor {


    private String rotIt(String input){
        StringBuilder output = new StringBuilder();
        for(int i = 0; i < input.length(); i++){
            char converted = ' ';
            char current = input.charAt(i);
            if(Character.isDigit(current)) converted = numROT(current);
            else if (Character.isLetter(current)) converted = charROT(current);
            else {converted = current;}
            output.append(converted);
        }
        return String.valueOf(output);
    }

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

    private String BINtoBINREV(String bin){
        StringBuilder newBin = new StringBuilder();
        for(int i = 0; i < bin.length(); i++){
            if(bin.charAt(i) == '0') newBin.append('1');
            else if(bin.charAt(i) == '1') newBin.append('0');
            else newBin.append(' ');
        }
        return newBin.toString();
    }

    private String BINtoSTR(String bin){
                String s = " ";
        for(int index = 0; index < bin.length(); index+=9) {
            String temp = bin.substring(index, index+8);
            int num = Integer.parseInt(temp,2);
            char letter = (char) num;
            s = s+letter;
        }
        return s.trim();
    }

    public static String encoder(String text){
        IllusionEncryptor ie = new IllusionEncryptor();
        return ie.BINtoBINREV(ie.ROTtoBIN(ie.rotIt(text)));
    }

    public static String decoder(String text){
        IllusionEncryptor ie = new IllusionEncryptor();
        return ie.rotIt(ie.BINtoSTR(ie.BINtoBINREV(text)));
    }

    public static void main(String[]args)throws IOException{
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        boolean running = true;
        while(running) {
            System.out.print("ENTER A TEXT : ");
            String text = br.readLine();
            if (text.equals("TERMINATE")) running = false;
            String encoded = encoder(text);
            System.err.println(encoded);
            String decoded = decoder(encoded);
            System.err.println(decoded);

            if(decoded.equals(text))System.err.println("SUCCESS ENCRYPTOR IS WORKING");
            else System.err.println("SOMETHING IS SERIOUSLY WRONG");


        }
    }
}
