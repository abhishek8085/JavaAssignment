import java.io.*;
import java.net.*;
import java.util.Vector;


/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : GameServerSocket.java, 2015/11/04
 */
public class GameServerSocket implements Initialiseable {


    //private ServerSocket serverSocket;
    private DatagramSocket datagramSocket;

    private boolean isHandShakeDone = false;
    private String connectedPlayerName;
    private ObjectArrivalListener objectArrivalListener;
    private LiteDictionary<String,ConnectionObject> sourceSocketMap = new LiteDictionary<>(10);

    private LiteDictionary<String,UDPParam> playerVsUDPParam = new LiteDictionary<>(10);
    private LiteDictionary<UDPParam,ObjectWorker> uDPParamVsObjectWorker = new LiteDictionary<>(10);

    //private LiteDictionary<String,Player> sourceVsPlayers = new LiteDictionary<>(10);
    private Vector<String> allClients = new Vector<String>();
    private Vector<InetAddress> allClientIPs = new Vector<InetAddress>();
    private PlayerJoinedListener playerJoinedListener;



    public GameServerSocket(ObjectArrivalListener objectArrivalListener, PlayerJoinedListener playerJoinedListener) {
        this.objectArrivalListener = objectArrivalListener;
        this.playerJoinedListener = playerJoinedListener;
    }

    @Override
    public void initialise() throws InitialisationException {
        try {
            //serverSocket = new ServerSocket(GlobalConfig.SERVER_PORT_NUMBER);
            datagramSocket = new DatagramSocket(GlobalConfig.SERVER_PORT_NUMBER);
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
       // ConnectionObject connectionObject = sourceSocketMap.get(frame.getSource());
        UDPParam udpParam = playerVsUDPParam.get(frame.getSource());
        if(udpParam!=null)
        {
            try {
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos);
                out.writeObject(frame);
                byte[] bytes = bos.toByteArray();
                int length = bytes.length;
                DataOutputStream dataOutputStream = new DataOutputStream(outputStream);
                dataOutputStream.writeInt(length);
                dataOutputStream.write(bytes);
                dataOutputStream.flush();

                byte[] buffer = outputStream.toByteArray();
                DatagramPacket datagramPacket = new DatagramPacket(buffer,buffer.length,udpParam.getAddress(),udpParam.getPort());
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


    private class ConnectionReceiverThread extends Thread
    {
        private int connectionCount=0;



        @Override
        public void run() {
            while (true) {
                Socket socket = null;
/*                if(connectionCount++== GlobalConfig.getMaxConnection())
                {
                    break;
                }*/
                try {
                    //System.out.println("Waiting for connection.");
                    byte[] udpbuffer = new byte[1024*1024];
                    DatagramPacket datagramPacket = new DatagramPacket(udpbuffer,udpbuffer.length);
                    datagramSocket.receive(datagramPacket);
                    UDPParam udpParam = new UDPParam(datagramPacket.getAddress(),datagramPacket.getPort());

                    if (!(uDPParamVsObjectWorker.get(udpParam)!=null) ||allClientIPs.size() == GlobalConfig.getMaxConnection() ) {

                        allClientIPs.add(datagramPacket.getAddress());

                        byte[] udpFrame = datagramPacket.getData();


                        //socket = serverSocket.accept();

                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(udpFrame);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                       // DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
                       // DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);

                        Player player = HandshakeManager.performHandshakeAndGetPlayer(datagramPacket,datagramSocket);



                        //Player player = HandshakeManager.performHandshakeAndGetPlayer(dataInputStream,
                          //      dataOutputStream);



                        allClients.add(player.getName());

                        playerVsUDPParam.put(player.getName(),udpParam);

                        //sourceSocketMap.put(player.getName(), new ConnectionObject(dataInputStream, dataOutputStream));

                        //sourceVsPlayers.put("127.0.0.1",player);
                        ObjectWorker objectWorker = new ObjectWorker(player);
                        objectWorker.start();

                        uDPParamVsObjectWorker.put(udpParam,objectWorker);


                        playerJoinedListener.onJoined(player);
                    }
                    else
                    {

                        uDPParamVsObjectWorker.get(udpParam).setDatagramPacket(datagramPacket);

                    }




                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
               // System.out.println("Accepted connection from "+socket.getRemoteSocketAddress());
            }
        }
    }


    private class ObjectWorker extends Thread
    {
        public DatagramPacket datagramPacket=null;
        public Player player;
        private Object mutex = new Object();

        public ObjectWorker(Player player) {
            super("ObjectGrabberThread");
            this.player = player;
        }

        public void setDatagramPacket(DatagramPacket datagramPacket) {
            synchronized (mutex) {
                this.datagramPacket = datagramPacket;
                mutex.notifyAll();
            }
        }

        @Override
        public void run() {
            while (true) {
                synchronized (mutex) {
                    while (datagramPacket == null) {
                        try {
                            mutex.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        byte[] input = datagramPacket.getData();
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(input);
                        DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
                        int length = dataInputStream.readInt();
                        byte[] bytes = new byte[length];
                        dataInputStream.readFully(bytes);
                        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                        ObjectInputStream in = new ObjectInputStream(bis);
                        objectArrivalListener.onObjectArrival(player, in.readObject());
                        datagramPacket =null;
                    } catch (IndexOutOfBoundsException e) {
                        //

                    } catch (Exception e) {
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }
    }





}

class UDPPacketArrivalListener
{

}


class UDPParam
{
    private InetAddress address;
    private int port;

    public UDPParam(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public InetAddress getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    @Override
    public boolean equals(Object obj) {
 if (! (obj instanceof UDPParam))
        {
            return false;
        }

        UDPParam udpParam2 = (UDPParam) obj;

        if ((udpParam2.getAddress().equals(this.getAddress()))&&(udpParam2.getPort()==udpParam2.getPort()))
        {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int result = address.hashCode();
        result = 31 * result + port;
        return result;
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