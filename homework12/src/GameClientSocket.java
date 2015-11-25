import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
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
    private DatagramSocket datagramSocket;

    private boolean isHandShakeDone = false;
    private String connectedPlayerName;
    private ObjectArrivalListener objectArrivalListener;
    private LiteDictionary<String,ConnectionObject> sourceSocketMap = new LiteDictionary<>(10);
    private InetAddress serverInetAddress;
    private int serverPort;

    //private DataInputStream dataInputStream;
    //private DataOutputStream dataOutputStream;


    public GameClientSocket(ObjectArrivalListener objectArrivalListener) {
        this.objectArrivalListener = objectArrivalListener;
    }

    @Override
    public void initialise() throws InitialisationException {
        try {
            datagramSocket = new DatagramSocket();

            //clientSocket = new Socket(GlobalConfig.getSeverAddress(), GlobalConfig.getServerPort());
            serverInetAddress = InetAddress.getByName(GlobalConfig.getSeverAddress());
            serverPort = GlobalConfig.getServerPort();
            //dataInputStream = new DataInputStream(clientSocket.getInputStream());
            //dataOutputStream = new DataOutputStream(clientSocket.getOutputStream());
            HandshakeManager.performClientHandShake(serverInetAddress,serverPort,datagramSocket);
            new ObjectGrabber(null,datagramSocket).start();
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
            if(datagramSocket!=null)
            {
            try {
                ByteArrayOutputStream output = new ByteArrayOutputStream();
                DataOutputStream dataOutputStream = new DataOutputStream(output);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(frame);
                byte[] bytes = bos.toByteArray();
                int length = bytes.length;
                dataOutputStream.writeInt(length);
                dataOutputStream.write(bytes);
                dataOutputStream.flush();


                byte[] outputBuffer = output.toByteArray();
                DatagramPacket datagramPacket = new DatagramPacket(outputBuffer,
                        outputBuffer.length,serverInetAddress,serverPort);

                datagramSocket.send(datagramPacket);


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
        public DatagramSocket datagramSocket;
        public Player player;

        public ObjectGrabber(Player player, DatagramSocket datagramSocket) {
            super("ObjectGrabberThread");
            this.datagramSocket = datagramSocket;
            this.player = player;
        }

        @Override
        public void run() {
            while (true) {
                try {

                    byte[] inputBytes = new byte[1204*1024];
                    DatagramPacket datagramPacket = new DatagramPacket(inputBytes,inputBytes.length);
                    datagramSocket.receive(datagramPacket);

                    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(datagramPacket.getData());
                    DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);


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
