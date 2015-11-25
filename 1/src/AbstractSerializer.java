/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public abstract class AbstractSerializer<T extends Frame<U>,U> implements Serializer<T,U> {

    @Override
    public byte[] serialize( T frame) {
        byte[] address = serializeAddress(frame);
        byte[] data = serializeData(frame.getData());
        int length = address.length+data.length+1;
        byte[] appended = new byte[length];
        appended[0]=(byte)((int)length/255);
        appended[1]=(byte)((int)length%255);

        for (int i =0 ; i< address.length;i++) {
            appended[i+2]= address[i];
        }

        for (int i =0 ; i< data.length;i++) {
            appended[i+address.length+2-1]= data[i];
        }
        return appended;
    }

    private byte[] serializeAddress( T frame)
    {
        return frame.getSource().getBytes();
    }

    protected abstract byte[] serializeData( U data );
}
