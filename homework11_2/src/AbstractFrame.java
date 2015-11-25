/**
 * @version 1.0 : AbstractFrame.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
public abstract class AbstractFrame<T> implements Frame<T> {

    private String sourceAddress;

    public AbstractFrame(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    @Override
    public void setSource( String source) {
        sourceAddress = source;
    }

    @Override
    public String getSource() {
        return sourceAddress;
    }
}
