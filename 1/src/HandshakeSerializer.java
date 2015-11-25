import java.util.Vector;

/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public class HandshakeSerializer extends AbstractSerializer<HandShakeFrame, HandshakeInfo>{


    private static HandshakeSerializer handshakeSerializer = new HandshakeSerializer();


    @Override
    protected byte[] serializeData(HandshakeInfo data) {

        Vector<Byte> bytes = new Vector<Byte>();

       for ( byte b:data.getSourceAddress().getBytes())
        {
            bytes.add(b);
        }

        bytes.add((byte)(data.getSourcePort()/255));
        bytes.add((byte)(int)(data.getSourcePort()%255));
        for ( byte b:data.getUserName().getBytes())
        {
            bytes.add(b);
        }

        bytes.add((byte) (data.isFromServer() ? 1 : 0));

        byte[] byteArray = new byte[bytes.size()];

        for ( int i =0; i < bytes.size(); i++)
        {
            byteArray[i] = bytes.get(i);
        }

        return byteArray;
    }

    public static Serializer<HandShakeFrame, HandshakeInfo> getInstance() {
        return handshakeSerializer;
    }

    @Override
    public int getSerializerId() {
        return SerializerIds.HANDSHAKE_SERIALIZER_ID;
    }
}
