import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;


/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : GameServerSocket.java, 2015/11/04
 */
public class GameServerSocket implements Initialiseable {


    private ServerSocket serverSocket;

    private boolean isHandShakeDone = false;
    private String connectedPlayerName;
    private ObjectArrivalListener objectArrivalListener;
    private LiteDictionary<String,ConnectionObject> sourceSocketMap = new LiteDictionary<>(10);
    //private LiteDictionary<String,Player> sourceVsPlayers = new LiteDictionary<>(10);
    private Vector<String> allClients = new Vector<String>();
    private PlayerJoinedListener playerJoinedListener;


    public GameServerSocket(ObjectArrivalListener objectArrivalListener, PlayerJoinedListener playerJoinedListener) {
        this.objectArrivalListener = objectArrivalListener;
        this.playerJoinedListener = playerJoinedListener;
    }

    @Override
    public void initialise() throws InitialisationException {
        try {
            serverSocket = new ServerSocket(GlobalConfig.SERVER_PORT_NUMBER);
            System.out.println("Listening on port: "+ GlobalConfig.SERVER_PORT_NUMBER);
            new ConnectionReceiverThread().start();
        } catch (Exception e) {
            throw new InitialisationException(e);
        } finally {

        }
    }

    public void broadCastFrame( Frame frame )
    {
        for (String source:allClients)
        {
            frame.setSource(source);
            sendFrame(frame);
        }
    }



    public void sendFrame( Frame frame )
    {
        ConnectionObject connectionObject = sourceSocketMap.get(frame.getSource());
        if(connectionObject!=null)
        {
            try {
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
            }
        }
        else
        {
            throw new RuntimeException("Handshake not performed");
        }
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