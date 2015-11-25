import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : GameServerSocket.java, 2015/11/04
 */
public class GameServerSocket extends  UnicastRemoteObject implements Initialiseable,GameServerConnection {


    private ServerSocket serverSocket;

    private boolean isHandShakeDone = false;
    private String connectedPlayerName;
    private ObjectArrivalListener objectArrivalListener;
    private LiteDictionary<String,ConnectionObject> sourceSocketMap = new LiteDictionary<>(10);
    //private LiteDictionary<String,Player> sourceVsPlayers = new LiteDictionary<>(10);
    private Vector<String> allClients = new Vector<String>();
    private PlayerJoinedListener playerJoinedListener;

    private Map<String,GameClientConnection> playerNameVsGameClientConnection = new HashMap<>();
    private Map<String,Player> playerNameVsPlayerMap = new HashMap<>();


    public GameServerSocket(ObjectArrivalListener objectArrivalListener, PlayerJoinedListener playerJoinedListener) throws RemoteException{
        this.objectArrivalListener = objectArrivalListener;
        this.playerJoinedListener = playerJoinedListener;
    }

    @Override
    public void initialise() throws InitialisationException {
        try {
            //serverSocket = new ServerSocket(GlobalConfig.SERVER_PORT_NUMBER);

            System.out.println("Listening on port: "+ GlobalConfig.SERVER_PORT_NUMBER);
          // new ConnectionReceiverThread().start();
        } catch (Exception e) {
            throw new InitialisationException(e);
        } finally {

        }
    }

    public void broadCastFrame( Frame frame )
    {
        for (String source:allClients)
        {
            frame.setDestination(source);
            sendFrame(frame);
        }
    }


    public void sendFrame( Frame frame )
    {
        GameClientConnection gameClientConnection = playerNameVsGameClientConnection.get(frame.getDestination());
        if(gameClientConnection!=null)
        {

            try {
                gameClientConnection.sendToClient(frame);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
/*            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(frame);
                byte[] bytes = bos.toByteArray();
                int length = bytes.length;
                DataOutputStream dataOutputStream =connectionObject.getDataOutputStream();
                dataOutputStream.writeInt(length);
                dataOutputStream.write(bytes);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        else
        {
            throw new RuntimeException("Handshake not performed");
        }
    }

    @Override
    public synchronized void sendToServer(final Frame frame) throws RemoteException {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (frame instanceof HandShakeFrame) {
                        HandShakeFrame handShakeFrame = (HandShakeFrame) frame;
                        HandshakeInfo handshakeInfo = handShakeFrame.getData();

                        if (!playerNameVsGameClientConnection.containsKey(handshakeInfo.getUserName())) {
                            GameClientConnection gameClientConnection = (GameClientConnection) Naming.lookup(handshakeInfo.getLookupURL());
                            playerNameVsGameClientConnection.put(handshakeInfo.getUserName(),gameClientConnection);
                            HandshakeManager.performHandshakeAndGetPlayer((HandShakeFrame) frame,gameClientConnection,true);
                        }
                        else
                        {
                            Player player = HandshakeManager.performHandshakeAndGetPlayer((HandShakeFrame) frame,playerNameVsGameClientConnection.get(handshakeInfo.getUserName()),false);



                            allClients.add(player.getName());
                            playerNameVsPlayerMap.put(player.getName(),player);
                            playerJoinedListener.onJoined(player);
                        }


                        // sourceSocketMap.put(player.getName(), new ConnectionObject(dataInputStream, dataOutputStream));

                        //sourceVsPlayers.put("127.0.0.1",player);
                        // new ObjectGrabber(player, dataInputStream).start();


                    } else {
                        objectArrivalListener.onObjectArrival(playerNameVsPlayerMap.get(frame.getPlayerName()),frame);
                    }
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }






            }
        }).start();




    }


    private class ConnectionReceiverThread extends Thread
    {
        private int connectionCount=0;



        @Override
        public void run() {
            while (true) {
                Socket socket = null;
                if(connectionCount++== GlobalConfig.getMaxConnection())
                {
                    break;
                }
                try {
                    System.out.println("Waiting for connection.");
                    socket = serverSocket.accept();
                    DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                    DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

                    Player player = HandshakeManager.performHandshakeAndGetPlayer(dataInputStream,
                            dataOutputStream);




                    allClients.add(player.getName());
                    sourceSocketMap.put(player.getName(),new ConnectionObject(dataInputStream,dataOutputStream));

                    //sourceVsPlayers.put("127.0.0.1",player);
                    new ObjectGrabber(player,dataInputStream).start();

                    playerJoinedListener.onJoined(player);

                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Accepted connection from "+socket.getRemoteSocketAddress());
            }
        }
    }


    private class ObjectGrabber extends Thread
    {
        public DataInputStream dataInputStream;
        public Player player;

        public ObjectGrabber( Player player,DataInputStream dataInputStream) {
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
                    objectArrivalListener.onObjectArrival(player,in.readObject());
                } catch (IndexOutOfBoundsException e) {
                    //

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    break;
                }
            }
        }
    }





}


class ConnectionObject
{
    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;

    public ConnectionObject(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {
        this.dataInputStream = dataInputStream;
        this.dataOutputStream = dataOutputStream;
    }

    public DataInputStream getDataInputStream() {
        return dataInputStream;
    }

    public DataOutputStream getDataOutputStream() {
        return dataOutputStream;
    }
}
