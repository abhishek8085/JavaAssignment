import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Random;
import java.util.Timer;

/**
 * Created by Abhishek8085 on 15-11-2015.
 */
public class ThroughputTester {

    private static Config config;
    private ServerSocket serverSocket;
    private DatagramSocket datagramSocket;
    private Socket remoteSocket;
    private Socket socket;
    private ThroughputMeasure throughputMeasure;
    public static final int INITIATE_THROUGHPUT_MEASURE_SIGNAL = 100;

    public static final int TEST_PACKETS = 10;
    public static final int BYTES_PER_PACKET = 5 * 1024 * 1024;

    public static final int UDP_TEST_PACKETS = 50000;
    public static final int UDP_BYTES_PER_PACKET = 8 * 1024;
    public static final int UDP_TIME_INTERVAL = 10;


    public static void main(String[] args){
        config = CommandLineParser.parseCommandLine(args);

        ThroughputTester throughputTester = new ThroughputTester();
        try {
            throughputTester.initialise();
            throughputTester.doTest();
        } catch (InitializationException e) {
            e.printStackTrace();
        }


    }


    public void initialise() throws InitializationException {
        try {
            if (config.isServer()) {
                if (config.isTCP()) {
                    serverSocket = new ServerSocket(config.getServerPort());
                    System.out.println("Listening on port " + config.getServerPort());

                    remoteSocket = serverSocket.accept();
                    System.out.println("Accepted connection from " + remoteSocket.getRemoteSocketAddress());

                    throughputMeasure = new ThroughputMeasure(config, remoteSocket);
                    throughputMeasure.initialise();
                } else {
                    datagramSocket = new DatagramSocket(config.getServerPort());
                    throughputMeasure = new ThroughputMeasure(config, datagramSocket);
                    System.out.println("Listening on port " + config.getServerPort());


                }
            } else {
                if (config.isTCP()) {
                    socket = new Socket(config.getServerAddress(), config.getServerPort());
                    System.out.println("Connected to " + config.getServerAddress() + " on port " + config.getServerPort());

                    throughputMeasure = new ThroughputMeasure(config, socket);
                    throughputMeasure.initialise();

                } else {
                    datagramSocket = new DatagramSocket();
                    throughputMeasure = new ThroughputMeasure(config, datagramSocket);
                }
            }
        } catch (Exception e) {
            throw new InitializationException(e);
        }
    }


    public void doTest() {

        long bytesPerMillisecond = throughputMeasure.measureThroughputMeasure();

        if (!config.isServer()) {
            System.out.println("Test Complete: Please see output on server side. ");
            return;
        }
        if (!config.isTCP()) {
            return;
        }
        System.out.println("Test Complete.");

        long kilobytesPerSecond = (bytesPerMillisecond * 1000) / 1024;
        System.out.println("Throghput from present computer to " + remoteSocket.getRemoteSocketAddress());
        System.out.println(kilobytesPerSecond + " Kbps");
    }

}

class ThroughputMeasure {

    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    private Socket socket;
    private DatagramSocket datagramSocket;

    private byte[] udpPacket;

    private Config config;

    private boolean isServer;

    private RandomDataGenerator randomDataGenerator = new RandomDataGenerator();

    public ThroughputMeasure(Config config, Socket socket) {
        this.socket = socket;
        this.config = config;
        this.isServer = config.isServer();

    }

    public ThroughputMeasure(Config config, DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
        this.config = config;
        this.isServer = config.isServer();
    }

    public void initialise() throws InitializationException {
        if (config.isTCP()) {
            try {
                dataOutputStream = new DataOutputStream(socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
            } catch (Exception e) {
                throw new InitializationException(e);
            }
        } else
        {
            udpPacket = randomDataGenerator.getRandomData(ThroughputTester.UDP_BYTES_PER_PACKET);
        }

    }

    public long measureThroughputMeasure() {
        try {
            if (config.isTCP()) {
                if (isServer) {
                    dataOutputStream.writeInt(ThroughputTester.INITIATE_THROUGHPUT_MEASURE_SIGNAL);
                    dataOutputStream.flush();
                    byte[] buffer = new byte[ThroughputTester.TEST_PACKETS * ThroughputTester.BYTES_PER_PACKET - 1];
                    dataInputStream.read();
                    System.out.println("Test started.");
                    long time = System.currentTimeMillis();
                    dataInputStream.readFully(buffer);
                    long timeTaken = System.currentTimeMillis() - time;
                    return (ThroughputTester.TEST_PACKETS * ThroughputTester.BYTES_PER_PACKET - 1) / timeTaken;

                } else {
                    while (dataInputStream.readInt() != ThroughputTester.INITIATE_THROUGHPUT_MEASURE_SIGNAL) ;
                    dataOutputStream.write(randomDataGenerator.getRandomData(ThroughputTester.TEST_PACKETS * ThroughputTester.BYTES_PER_PACKET));
                    dataOutputStream.flush();
                    return 0;
                }
            }
            else {

               // Boolean  stopUDP=false;


                class UDPTimerThread extends Thread
                {
                    UDPTimerListener udpTimerListener;
                    InetAddress inetAddress;
                    boolean started = false;
                    public UDPTimerThread(String name, UDPTimerListener udpTimerListener) {
                        super(name);
                        this.udpTimerListener = udpTimerListener;
                    }


                    public synchronized void start(InetAddress inetAddress) {
                        this.inetAddress = inetAddress;
                        if (!started) {
                            System.out.println("Test started: Measuring throughput");
                            super.start();
                        }
                        started = true;
                    }

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(ThroughputTester.UDP_TIME_INTERVAL*1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        udpTimerListener.timeUp(inetAddress);
                    }


                }

                class BytesCounter
                {
                    private long numberOfBytes=0;

                    public synchronized void incrementTheCountBy(long value) {
                        numberOfBytes+=value;
                    }

                    public synchronized long getValue() {
                        return numberOfBytes;
                    }

                }

                class StopBoolean
                {
                    private boolean stop;

                    public synchronized void setValue(boolean value)
                    {
                        stop = value;
                    }

                    public synchronized boolean getValue()
                    {
                        return stop;
                    }
                }

                final BytesCounter numberOfBytesReceived=new BytesCounter();
                final StopBoolean  stopUDP=new StopBoolean();
                InetAddress clientAddress;
                int clientPort;


                UDPTimerThread udpTimerThread = new UDPTimerThread("UDPTIMER", new UDPTimerListener() {
                    @Override
                    public void timeUp( InetAddress inetAddress){
                        long throughPut = numberOfBytesReceived.getValue()/ThroughputTester.UDP_TIME_INTERVAL;
                        stopUDP.setValue(false);

                        if (!config.isServer()) {
                            System.out.println("Test Complete: Please see output on server side. ");
                            System.exit(0);
                        }

                        System.out.println("Test Complete.");
                        System.out.println("Throghput from present computer to "+inetAddress.getHostAddress());
                        System.out.println(throughPut/1024+" Kbps");
                    }
                });

                udpTimerThread.setPriority(Thread.MAX_PRIORITY);

                if (isServer) {

                    byte[] dummy = new byte[1];
                    DatagramPacket dummySData = new DatagramPacket(dummy, dummy.length);
                    datagramSocket.receive(dummySData);
                    clientAddress = dummySData.getAddress();
                    clientPort = dummySData.getPort();


                    byte[] in = new byte[]{ThroughputTester.INITIATE_THROUGHPUT_MEASURE_SIGNAL};
                    DatagramPacket datagramPacket = new DatagramPacket(in,in.length,clientAddress,clientPort);
                    datagramSocket.send(datagramPacket);



                    while (!stopUDP.getValue()) {
                        byte[] packet = new byte[ThroughputTester.UDP_BYTES_PER_PACKET];
                        DatagramPacket throughputPacket = new DatagramPacket(packet, packet.length);
                        datagramSocket.receive(throughputPacket);

                        udpTimerThread.start(clientAddress);
                        numberOfBytesReceived.incrementTheCountBy(throughputPacket.getData().length);
                    }


                }
                else
                {
                    byte[] dummy = new byte[1];
                    DatagramPacket dummyData = new DatagramPacket(dummy, dummy.length,config.getServerAddress(),config.getServerPort());
                    datagramSocket.send(dummyData);

                    byte[] packet = new byte[ThroughputTester.UDP_BYTES_PER_PACKET];
                    DatagramPacket throughputPacket = new DatagramPacket(packet, packet.length);
                    datagramSocket.receive(throughputPacket);
                    int in = throughputPacket.getData()[0];

                    if (in ==ThroughputTester.INITIATE_THROUGHPUT_MEASURE_SIGNAL)
                    {
                        RandomDataGenerator randomDataGenerator = new RandomDataGenerator();
                        byte[] randomBytes = randomDataGenerator.getRandomData(ThroughputTester.UDP_BYTES_PER_PACKET);
                        DatagramPacket testPacket = new DatagramPacket(randomBytes, randomBytes.length,config.getServerAddress(),config.getServerPort());
                        System.out.println("Sending random data in UDP packets.");


                        udpTimerThread.start();
                        while (!stopUDP.getValue())
                        {
                            datagramSocket.send(testPacket);
                        }
                    }

                }




return 0;
            }


        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error");
            return -1;
        }
        finally
        {
            try {
                if (dataInputStream!=null) {
                    dataOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                if(dataInputStream!=null) {
                    dataInputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}



interface UDPTimerListener
{
    void timeUp(InetAddress inetAddress );
}
class RandomDataGenerator {
    private Random random = new Random();

    public byte[] getRandomData(int bytes) {
        byte[] randomData = new byte[bytes];
        random.nextBytes(randomData);
        System.out.println(randomData.length);
        return randomData;
    }
}

class CommandLineParser {
    public static Config parseCommandLine(String[] args) {
        if (args[1].equalsIgnoreCase("-SERVER")) {
            return new Config(args[0].equals("-TCP"), true, "", Integer.parseInt(args[2]));
        }
        else
        {
            return new Config(args[0].equals("-TCP"), false, args[2], Integer.parseInt(args[3]));
        }
    }
}

class Config {
    private boolean isTCP;
    private boolean isServer;
    private String serverAddress;
    private int port;

    public Config(boolean isTCP, boolean isServer, String serverAddress, int port) {
        this.isTCP = isTCP;
        this.isServer = isServer;
        this.serverAddress = serverAddress;
        this.port = port;
    }

    public boolean isServer() {
        return isServer;
    }

    public boolean isTCP() {
        return isTCP;
    }

    public InetAddress getServerAddress() {
        try {
            return InetAddress.getByName(serverAddress);
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public int getServerPort() {
        return port;
    }
}


class InitializationException extends Exception {
    public InitializationException() {
        super();
    }

    public InitializationException(String message) {
        super(message);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public InitializationException(Throwable cause) {
        super(cause);
    }

    protected InitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}