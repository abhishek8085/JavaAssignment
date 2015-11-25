/**
 * Created by Abhishek8085 on 04-11-2015.
 */
interface Serializer<T extends Frame<U>,U> {


    int getSerializerId();

    byte[] serialize( T frame );
}
