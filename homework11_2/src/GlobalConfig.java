import java.util.UUID;
import java.util.Vector;

/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : GlobalConfig.java, 2015/11/04
 */
 public class GlobalConfig
{
    private static Vector<Character> gamePieces = new Vector<>();

    static
    {
        gamePieces.add('+');
        gamePieces.add('-');
        gamePieces.add('^');
    }



    private static int maxConnection=5;
    private static int serverPort;
    private static String severAddress;

private static String  userName ="";

    private GlobalConfig(){}
    private static boolean isServerModeVariable=false;
    public static final int SERVER_PORT_NUMBER=8090;

    public static boolean isServerMode() {
        return isServerModeVariable;
    }

    public static void setIsServerMode(boolean isServerMode)
    {
        isServerModeVariable = isServerMode;
    }

    public static int getMaxConnection() {
        return maxConnection;
    }

    public static char getRandomGamePiece() {
        return gamePieces.remove(0);
    }




    public static void setUserName(String userName) {
        GlobalConfig.userName = userName;
    }

    public static String getUserName() {
    return userName;


}

    public static char getGamePiece() {
        return '+';
    }

    public static void setGamePiece(char gamepiece) {
        System.out.println(gamepiece);
    }


    //For Client
    //-----------------------------------------------------------------------------------------

    public static void setSeverAddress(String severAddress) {
        GlobalConfig.severAddress = severAddress;
    }

    public static String getSeverAddress() {
        return severAddress;
    }

    public static void setServerPort(int serverPort) {
        GlobalConfig.serverPort = serverPort;
    }

    public static int getServerPort() {
        return serverPort;
    }
    //-------------------------------------------------------------------------------------------------


}
