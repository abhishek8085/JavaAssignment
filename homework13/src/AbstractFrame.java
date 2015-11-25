/**
 * @version 1.0 : AbstractFrame.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
public abstract class AbstractFrame<T> implements Frame<T> {

    private String sourceAddress;
    private String playerName;

    public AbstractFrame(String sourceAddress,String playerName ) {
        this.sourceAddress = sourceAddress;
        this.playerName = playerName;
    }

    @Override
    public void setDestination(String source) {
        sourceAddress = source;
    }

    @Override
    public String getDestination() {
        return sourceAddress;
    }

    @Override
    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
