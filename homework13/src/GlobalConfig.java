import java.net.InetAddress;
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
    private static String lookUpurl;
    private static String serverLookUpurl;
    private static char aGamePiece;

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
        return GlobalConfig.aGamePiece;
    }

    public static void setGamePiece(char gamepiece) {
        GlobalConfig.aGamePiece = gamepiece;
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

    public static String getLocalLookUpurl() {
        return "rmi://"+ MachineHelper.getLocalHost()+":5000/GameClientConnection";
    }

    public static String getServerLookUpurl() {
        return "rmi://"+ getSeverAddress()+":5000/GameServerConnection";
    }

    public static void setServerLookUpurl(String serverLookUpurl) {
        GlobalConfig.serverLookUpurl = serverLookUpurl;
    }
    //-------------------------------------------------------------------------------------------------


}
