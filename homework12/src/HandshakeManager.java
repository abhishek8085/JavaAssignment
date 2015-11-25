import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : HandshakeManager.java, 2015/11/06
 */
public class HandshakeManager {

    public static Player performHandshakeAndGetPlayer(DatagramPacket datagramPacket,DatagramSocket datagramSocket)
    {
        try {

            HandShakeFrame handShakeFrame = new HandShakeFrame(
                    MachineHelper.getLocalHost(), new HandshakeInfo(MachineHelper.getLocalHost()
                    , GlobalConfig.getRandomGamePiece()));


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(handShakeFrame);
            byte[] bytes = bos.toByteArray();

            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(bos1);
            dataOutputStream.writeInt(bytes.length);
            dataOutputStream.write(bos.toByteArray());
            dataOutputStream.flush();

            byte[] udpBytes = bos1.toByteArray();

            DatagramPacket udpHandshakePacket = new DatagramPacket(udpBytes, udpBytes.length, datagramPacket.getAddress()
                    , datagramPacket.getPort());

            datagramSocket.send(udpHandshakePacket);


            byte[] inputBuffer = new byte[udpBytes.length+1024];
            DatagramPacket inputUDPPacket = new DatagramPacket(inputBuffer,inputBuffer.length);
            datagramSocket.receive(inputUDPPacket);

            ByteArrayInputStream byteArrayInputStream  = new ByteArrayInputStream(inputUDPPacket.getData());
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            int inLength = dataInputStream.readInt();
            byte[] inBytes = new byte[inLength];
            dataInputStream.readFully(inBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
            ObjectInputStream in = new ObjectInputStream(bis);


            HandShakeFrame handShakeFrame1 = ( HandShakeFrame )in.readObject();
            HandshakeInfo handshakeInfo = handShakeFrame1.getData();
            Player player = new Player(handshakeInfo.getUserName(),true,handshakeInfo.getGamePiece());

            return player;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Player performHandshakeAndGetPlayer(DataInputStream dataInputStream, DataOutputStream dataOutputStream)
    {


        HandShakeFrame handShakeFrame = new HandShakeFrame(
                MachineHelper.getLocalHost(),new HandshakeInfo(MachineHelper.getLocalHost()
                , GlobalConfig.getRandomGamePiece()));

        try {

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(handShakeFrame);
            byte[] bytes = bos.toByteArray();
            int length = bytes.length;
            dataOutputStream.writeInt(length);
            dataOutputStream.write(bytes);
            dataOutputStream.flush();



            int inLength = dataInputStream.readInt();
            byte[] inBytes = new byte[inLength];
            dataInputStream.readFully(inBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
            ObjectInputStream in = new ObjectInputStream(bis);

            HandShakeFrame handShakeFrame1 = ( HandShakeFrame )in.readObject();
            HandshakeInfo handshakeInfo = handShakeFrame1.getData();
            Player player = new Player(handshakeInfo.getUserName(),true,handshakeInfo.getGamePiece());

            return player;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }


    public static void performClientHandShake(InetAddress inetAddress, int port,DatagramSocket datagramSocket) {

        byte[] dummy = new byte[]{'1','2','3'};
        DatagramPacket datagramPacket = new DatagramPacket(dummy,dummy.length,inetAddress,port);


        try {

            datagramSocket.send(datagramPacket);


            byte[] inputBuffer = new byte[1024 * 1024];
            DatagramPacket inputUDPPacket = new DatagramPacket(inputBuffer, inputBuffer.length);

            datagramSocket.receive(inputUDPPacket);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputUDPPacket.getData());

            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);

            int inLength = dataInputStream.readInt();
            byte[] inBytes = new byte[inLength];
            dataInputStream.readFully(inBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
            ObjectInputStream in = new ObjectInputStream(bis);

            HandShakeFrame handShakeFrame = (HandShakeFrame) in.readObject();
            HandshakeInfo handshakeInfo = handShakeFrame.getData();

            GlobalConfig.setGamePiece(handshakeInfo.getGamePiece());


            HandshakeInfo handshakeInfo1 = new HandshakeInfo(MachineHelper.getLocalHost(), GlobalConfig.getUserName(), handshakeInfo.getGamePiece());
            HandShakeFrame handShakeFrame1 = new HandShakeFrame(MachineHelper.getLocalHost(), handshakeInfo1);


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(handShakeFrame1);
            byte[] bytes = bos.toByteArray();
            int length = bytes.length;
            ByteArrayOutputStream bos1 = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(bos1);
            dataOutputStream.writeInt(length);
            dataOutputStream.write(bytes);
            dataOutputStream.flush();

            byte[] udpBytes = bos1.toByteArray();

            DatagramPacket udpHandshakePacket = new DatagramPacket(udpBytes, udpBytes.length, datagramPacket.getAddress()
                    , datagramPacket.getPort());

            datagramSocket.send(udpHandshakePacket);

        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }



    }

    public static void performClientHandShake(DataInputStream dataInputStream, DataOutputStream dataOutputStream) {





        try {

            int inLength = dataInputStream.readInt();
            byte[] inBytes = new byte[inLength];
            dataInputStream.readFully(inBytes);
            ByteArrayInputStream bis = new ByteArrayInputStream(inBytes);
            ObjectInputStream in = new ObjectInputStream(bis);

            HandShakeFrame handShakeFrame = (HandShakeFrame) in.readObject();
            HandshakeInfo handshakeInfo = handShakeFrame.getData();

            GlobalConfig.setGamePiece(handshakeInfo.getGamePiece());


            HandshakeInfo handshakeInfo1 = new HandshakeInfo(MachineHelper.getLocalHost(), GlobalConfig.getUserName(), handshakeInfo.getGamePiece());
            HandShakeFrame handShakeFrame1 = new HandShakeFrame(MachineHelper.getLocalHost(),handshakeInfo1);



            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(handShakeFrame1);
            byte[] bytes = bos.toByteArray();
            int length = bytes.length;
            dataOutputStream.writeInt(length);
            dataOutputStream.write(bytes);
            dataOutputStream.flush();





        } catch (Exception e) {

        }


    }
}
