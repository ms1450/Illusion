package Client;

/**
 * This class is used to make sure the input given by the user is valid. It handles any errors or exceptions on its own.
 * @author Mehul Sen
 */
class VariableCheck {
    /**
     * Checks if the IP Entered By the User is Accurate
     * @param ip IP entered
     * @return True or False
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
     * Checks if the Port Entered by the User is Accurate.
     * @param port Port Number entered
     * @return True or False
     */
    static boolean portChecker(int port){
        if(port <= 1023) return false;
        return port <= 65534;
    }
}
