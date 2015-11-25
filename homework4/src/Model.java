import java.util.Observable;

/**
 * This is the model
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Model.java, 2015/10/04
 */
public class Model extends Observable{

    private String message;
    private char[][] field;
    private PlayerInterface playerInterface;

    /**
     * called to display whole field
     * @param field connect4 field
     */
    public void setField( char[][] field )
    {
        this.field = field;
        setChanged();
        notifyObservers(field);
    }

    /**
     * called to display Input message
     * @param message message
     */
    public void setInputExpectedMessage( String message )
    {
        this.message = message;
        setChanged();
        notifyObservers("IO:"+message);
    }

    /**
     * called to request players move
     * @param playerInterface player
     */
    public void setPlayerMoveRequestMessage( PlayerInterface playerInterface )
    {
        this.playerInterface = playerInterface;
        setChanged();
        notifyObservers(playerInterface);
    }

    /**
     * called to set output message
     * @param message message
     */
    public void setMessage(String message) {
        this.message = message;
        setChanged();
        notifyObservers(message);
    }

    public String getMessage() {
        return message;
    }

    public char[][] getField() {
        return field;
    }
}
