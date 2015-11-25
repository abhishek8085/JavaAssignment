import java.io.Serializable;

/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public interface Frame<T> {

    int getFrameId();

    String getSource();

    T getData();

    byte[] serialise();
}
