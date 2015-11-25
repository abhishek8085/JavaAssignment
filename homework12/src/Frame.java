import java.io.Serializable;

/**
 *
 * @version 1.0 : Frame.java, 2015/11/12
 * @author Abhishek Indukumar
 * 
 *
 */
public interface Frame<T> extends Serializable{

    int getFrameId();

    String getSource();

    void setSource(String source);

    T getData();
}
