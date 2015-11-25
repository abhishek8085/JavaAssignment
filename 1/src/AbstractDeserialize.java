import java.util.Arrays;

/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public abstract class AbstractDeserialize<T extends Frame<U>,U> implements Deserializer <T,U>
{
    protected String sourceAddress="";

    @Override
    public T deSerialize(byte[] bytes) {

        for (int i=2; i< 13; i++)
        {
            sourceAddress+=(char)bytes[i]+"";
        }

        return deSerializeData(Arrays.copyOfRange(bytes,12,bytes.length));
    }

    protected abstract T deSerializeData( byte[] bytes );


    public static void main(String args[])
    {
        System.out.println(new String("192.138.1.1").getBytes().length);
    }


}
