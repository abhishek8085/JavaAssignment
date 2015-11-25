import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Abhishek8085 on 21-11-2015.
 */
public interface GameServerConnection extends Remote {

    public void sendToServer( Frame frame )throws RemoteException;
}
