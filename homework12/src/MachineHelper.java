import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : MachineHelper.java, 2015/11/06
 */
public class MachineHelper
{
    private static String localHost;
    static
    {
        try {
            localHost = "127.0.0.1";InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }


    public static String getLocalHost() {
        return localHost;
    }
}
