import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by Abhishek8085 on 21-11-2015.
 */
public interface GameClientConnection extends Remote {
    public void sendToClient( Frame frame )throws RemoteException;
}
