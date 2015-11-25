import java.io.*;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


/**
 *
 * @version 1.0 : GameEngine.java, 2015/11/12
 * @author Abhishek Indukumar
 * 
 *
 */
public class GameClientSocket extends UnicastRemoteObject implements Initialiseable,GameClientConnection {


    private Socket clientSocket;

    private boolean isHandShakeDone = false;
    private String connectedPlayerName;
    private ObjectArrivalListener objectArrivalListener;
    private LiteDictionary<String,ConnectionObject> sourceSocketMap = new LiteDictionary<>(10);

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    private GameServerConnection gameServerConnection;


    public GameClientSocket(ObjectArrivalListener objectArrivalListener) throws RemoteException{
        this.objectArrivalListener = objectArrivalListener;
    }

    @Override
    public void initialise() throws InitialisationException {
        try {
            //clientSocket = new Socket(GlobalConfig.getSeverAddress(), GlobalConfig.getServerPort());
            //dataInputStream = null;
            //dataOutputStream = null
            gameServerConnection = (GameServerConnection)Naming.lookup(GlobalConfig.getServerLookUpurl());
            HandshakeManager.performClientHandShake(null, gameServerConnection);
            //new ObjectGrabber(null,dataInputStream).start();
            System.out.println("Connected to server: "+ GlobalConfig.getSeverAddress()+" on port: "+ GlobalConfig.getServerPort());
        } catch (Exception e) {
            throw new InitialisationException(e);
        } finally {

        }
    }

    public void setObjectArrivalListener(ObjectArrivalListener objectArrivalListener) {
        this.objectArrivalListener = objectArrivalListener;
    }

    public void sendFrame( Frame frame )
    {
            if(gameServerConnection!=null)
            {
            try {

                gameServerConnection.sendToServer(frame);
/*                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(frame);
                byte[] bytes = bos.toByteArray();
                int length = bytes.length;
                dataOutputStream.writeInt(length);
                dataOutputStream.write(bytes);
                dataOutputStream.flush();*/
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            throw new RuntimeException("Handshake not performed");
        }
    }

    @Override
    public void sendToClient(final Frame frame) throws RemoteException {

        new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    if (frame instanceof HandShakeFrame) {
                        HandShakeFrame handShakeFrame = (HandShakeFrame) frame;
                        HandshakeInfo handshakeInfo = handShakeFrame.getData();
                        //GameClientConnection gameClientConnection = (GameClientConnection) Naming.lookup(handshakeInfo.getLookupURL());

                        HandshakeManager.performClientHandShake((HandShakeFrame) frame,gameServerConnection);
                        //allClients.add(player.getName());
                        //playerNameVsPlayerMap.put(player.getName(),player);
                        // sourceSocketMap.put(player.getName(), new ConnectionObject(dataInputStream, dataOutputStream));

                        //sourceVsPlayers.put("127.0.0.1",player);
                        // new ObjectGrabber(player, dataInputStream).start();

                        // playerJoinedListener.onJoined(player);
                    } else {
                        objectArrivalListener.onObjectArrival(null,frame);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }














            }
        }).start();




    }

    private class ObjectGrabber extends Thread {
        public DataInputStream dataInputStream;
        public Player player;

        public ObjectGrabber(Player player, DataInputStream dataInputStream) {
            super("ObjectGrabberThread");
            this.dataInputStream = dataInputStream;
            this.player = player;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    int length = dataInputStream.readInt();
                    byte[] bytes = new byte[length];
                    dataInputStream.readFully(bytes);
                    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                    ObjectInputStream in = new ObjectInputStream(bis);

                    objectArrivalListener.onObjectArrival(player, in.readObject());


                } catch (Exception e) {
                    System.out.println(e);
                    break;
                }
            }
        }
    }
}
