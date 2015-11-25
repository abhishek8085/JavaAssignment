import java.io.*;
import java.net.Socket;


/**
 *
 * @version 1.0 : GameEngine.java, 2015/11/12
 * @author Abhishek Indukumar
 * 
 *
 */
public class GameClientSocket implements Initialiseable {


    private Socket clientSocket;

    private boolean isHandShakeDone = false;
    private String connectedPlayerName;
    private ObjectArrivalListener objectArrivalListener;
    private LiteDictionary<String,ConnectionObject> sourceSocketMap = new LiteDictionary<>(10);

    private DataInputStream dataInputStream;
    private DataOutputStream dataOutputStream;


    public GameClientSocket(ObjectArrivalListener objectArrivalListener) {
        this.objectArrivalListener = objectArrivalListener;
    }

    @Override
    public void initialise() throws InitialisationException {
        try {
            clientSocket = new Socket(GlobalConfig.getSeverAddress(), GlobalConfig.getServerPort());
            dataInputStream = new DataInputStream(clientSocket.getInputStream());
            dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            HandshakeManager.performClientHandShake(dataInputStream, dataOutputStream);
            new ObjectGrabber(null,dataInputStream).start();
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
            if(dataOutputStream!=null)
            {
            try {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(frame);
                byte[] bytes = bos.toByteArray();
                int length = bytes.length;
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
