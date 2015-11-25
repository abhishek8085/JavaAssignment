/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public interface Deserializer<T extends Frame<U>,U> {



    T deSerialize( byte[] bytes );
}
