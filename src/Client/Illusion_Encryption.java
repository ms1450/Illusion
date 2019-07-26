package Client;

/* Import's all the necessary packages */
import java.util.Base64;

/**
 * This Class contains four different types of encryptions. It acts as a Helper class to the Illusion_Client
 * @author Mehul Sen
 */
class Illusion_Encryption {

    /**
     * This method uses ROT13 if the character is a letter and ROT5 if it is a number to encrypt each individual
     * characters in a string.
     * @param input String to be Encrypted or Decrypted.
     * @return Rotted String
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

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int)(Math.random()*ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    /**
     * Helper method to rotIt, used when the character is a letter.
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
     * Helper method to rotIt, used when the character is a number
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
     * This method first converts the String to Binary, then reverses the Binary Values (1 becomes 0 and 0 becomes 1)
     * @param text Text to be Encrypted or Decrypted
     * @param encrypt True for Encryption and False for Decryption
     * @return Returns the Modified Text
     */
    String BINit(String text, boolean encrypt){
        /* Runs if the Text needs to be Encrypted */
        if(encrypt){
            String temporary = STRtoBIN(text);
            return BINtoBINrev(temporary);
        }
        /* Runs if the Text needs to be Decrypted */
        else{
            String temporary = BINtoBINrev(text);
            return BINtoSTR(temporary);
        }
    }

    /**
     * Helper method to BINit, used to convert a String to its Binary counterpart
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
     * Helper method to BINit, reverses the binary state, (1 becomes 0 and 0 becomes 1)
     * @param bin Binary String
     * @return reversed binary string
     */
    private String BINtoBINrev(String bin){
        StringBuilder newBin = new StringBuilder();
        for(int i = 0; i < bin.length(); i++){
            if(bin.charAt(i) == '0') newBin.append('1');
            else if(bin.charAt(i) == '1') newBin.append('0');
            else newBin.append(' ');
        }
        return newBin.toString();
    }

    /**
     * Helper method to BINit, converts a binary string back to a text.
     * @param bin binary string
     * @return text
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
     * Encrypts or Decrypts the Text using Base64 Encryption
     * @param org Original Data
     * @param enc True for Encryption and False for Decryption
     * @return Encoded or Decoded Data
     */
    String B64it(String org, boolean enc){
        if(enc) return Base64.getEncoder().encodeToString(org.getBytes());
        else return new String(Base64.getDecoder().decode(org));
    }

    /**
     * Encrypts or Decrypts a text using the helper class AESEncryption to implement AES on the given text.
     * @param text Text to be Encrypted or Decrypted
     * @param enc True for Encryption and False for Decryption
     * @param secret Secret Keyword used for Encryption and Decryption
     * @return Encrypted or Decrypted Data.
     */
    String AESit(String text, boolean enc, String secret){
        if(enc) return AESEncryption.encrypt(text,secret);
        else return AESEncryption.decrypt(text,secret);
    }
}



