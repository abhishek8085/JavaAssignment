/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public class HandshakeFrameDeserializer extends AbstractDeserialize<HandShakeFrame, HandshakeInfo> {

    private static HandshakeFrameDeserializer handshakeFrameDeserializer = new HandshakeFrameDeserializer();


    public static Deserializer<HandShakeFrame, HandshakeInfo> getInstance() {
        return handshakeFrameDeserializer;
    }


    @Override
    protected HandShakeFrame deSerializeData(byte[] bytes) {
        String sourceAddress=super.sourceAddress;
        int port= (int)(bytes[11]&0xff)*255 + (int)(bytes[12]&0xff);
        boolean isFromServer = bytes[bytes.length-1]==1;
        String userName ="";
        for (int i =13; i< bytes.length-1;i++)
        {
           userName+=((char)bytes[i])+"";
        }
        return new HandShakeFrame(sourceAddress,new HandshakeInfo(sourceAddress,port,userName,isFromServer));
    }


    public static void main(String[] args)
    {
        HandshakeInfo handshakeInfo = new HandshakeInfo("192.168.1.1",8090,"Abhishek",true);
        HandShakeFrame handShakeFrame = new HandShakeFrame("192.168.1.1",handshakeInfo);

        HandShakeFrame handShakeFrame1 =HandshakeFrameDeserializer.getInstance().deSerialize(handShakeFrame.serialise());


        PlayerInfo playerInfo = new PlayerInfo(5);
        PlayerFrame playerFrame = new PlayerFrame("192.168.1.1",playerInfo);
        PlayerFrame playerFrame1 = PlayerDeserializer.getInstance().deSerialize(playerFrame.serialise());





    }



}
