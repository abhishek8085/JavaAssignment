/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public abstract class AbstractFrame<T> implements Frame <T>{

    private String sourceAddress;

    public AbstractFrame(String sourceAddress) {
        this.sourceAddress = sourceAddress;
    }

    @Override
    public String getSource() {
        return sourceAddress;
    }
}
