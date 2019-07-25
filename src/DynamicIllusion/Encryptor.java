package DynamicIllusion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;
import java.util.Base64;

public class Encryptor {
    /**
     * This method uses rot 13 if the character is a letter and rot 5 if it is a number
     * @param input string before rotting
     * @return rotted string
     */
    String rotIt(String input){
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

    String BINit(String text, boolean encrypt){
        if(encrypt){
            String temporary = STRtoBIN(text);
            return BINtoBINREV(temporary);
        }
        else{
            String temporary = BINtoBINREV(text);
            return BINtoSTR(temporary);
        }
    }

    /**
     * Converts a String to its binary counterpart
     * @param text String
     * @return String but only consisting of ones and zeroes
     */
    private String STRtoBIN(String text){
        byte[] bytes = text.getBytes();
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
     * Adds the mysterious 6th layer of encryption to the data.
     * @param org Original Data
     * @param enc True or false
     * @return Encoded or Decoded Data
     */
    String B64it(String org, boolean enc){
        if(enc) return Base64.getEncoder().encodeToString(org.getBytes());
        else return new String(Base64.getDecoder().decode(org));
    }

    String AESit(String text, boolean enc, String secret){
        if(enc) return AESEncryption.encrypt(text,secret);
        else return AESEncryption.decrypt(text,secret);
    }

    public static void main(String[]args)throws IOException{
        InputStreamReader isr = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(isr);
        Encryptor enc = new Encryptor();
        System.out.println("Enter Text : ");
        String text = br.readLine();
        String ROTencrypt = enc.rotIt(text);
        System.out.println("ROT Encrypt : " +ROTencrypt);
        System.out.println("ROT Decrypt : " + enc.rotIt(ROTencrypt));
        String BINencrypt = enc.BINit(text,true);
        System.out.println("BIN Encrypt : "+BINencrypt);
        System.out.println("BIN Decrypt : " +enc.BINit(BINencrypt,false));
        String Base64encrypt = enc.B64it(text,true);
        System.out.println("B64 Encrypt : " +Base64encrypt);
        System.out.println("B64 Decrypt : " +enc.B64it(Base64encrypt,false));
        System.out.println("AES Secret : mehul");
        String AESencrypt = enc.AESit(text,true,"mehul");
        System.out.println("AES Encrypt : "+AESencrypt);
        System.out.println("AES Decrypt : "+enc.AESit(AESencrypt,false,"mehul"));

        System.out.println(" ");
        String SampleText = "This is the Sample Text I want to Encrypt";
        String encrypted = enc.AESit(SampleText,true,"192.168.206.1");
        System.out.println(encrypted);
        System.out.println(enc.AESit(encrypted,false,"192.168.206.1"));


        String systemipaddress = "";
        try
        {
            URL url_name = new URL("http://bot.whatismyipaddress.com");

            BufferedReader sc =
                    new BufferedReader(new InputStreamReader(url_name.openStream()));

            // reads system IPAddress
            systemipaddress = sc.readLine().trim();
        }
        catch (Exception e)
        {
            systemipaddress = "Cannot Execute Properly";
        }
        System.out.println("Public IP Address: " + systemipaddress +"\n");
    }
}



