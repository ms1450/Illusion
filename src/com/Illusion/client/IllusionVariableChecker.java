package com.Illusion.client;

/**
 * This Class is used to make sure that the variable entered into the program by the user are accurate.
 */
public class IllusionVariableChecker {
    /**
     * Checks if the IP Entered By the USer is Accurate
     * @param ip IP entered
     * @return True or false
     */
    static boolean ipChecker(String ip){
        if(ip.length() < 5 || ip.length() > 15) return false;
        int dotCount = 0;
        for(int i= 0; i<ip.length(); i ++){
            if(ip.charAt(i) == '.' && i != ip.length()-1){
                dotCount ++;
                if(ip.charAt(i + 1) == '.') return false;
            }
            if(ip.charAt(i) == '.' && i == ip.length()-1) return false;
            if(!Character.isDigit(ip.charAt(i)) && ip.charAt(i)!='.') return false;
        }
        return dotCount == 3;
    }

    /**
     * Checks if the Key Entered by the User is Accurate
     * @param key The string of the Key
     * @return True or False
     */
    public static boolean keyChecker(String key){
        if(key.length() != 8) return false;
        for(int i =0; i < key.length(); i ++){
            if(!Character.isDigit(key.charAt(i))) return false;
        }
        return true;
    }

    static void greet(boolean server){
        System.out.println("\t Illusion Encryption");
        if(server)System.out.println("\t Server Application");
        else System.out.println("\t Client Application");
        System.out.println("\t Ver 0.7");
        System.out.println("\t By -M- \n");
    }
}
