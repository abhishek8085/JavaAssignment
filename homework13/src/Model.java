import java.util.Observable;

/**
 * This is the model
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Model.java, 2015/11/04
 */
public class Model extends Observable{

    private String message;
    private char[][] field;
    private PlayerInterface playerInterface;
    private ClientsCommunicator clientsCommunicator;


    public void setClientsCommunicator(ClientsCommunicator clientsCommunicator) {
        this.clientsCommunicator = clientsCommunicator;
    }

    /**
     * called to display whole field
     * @param field connect4 field
     */
    public void setField( char[][] field )
    {
        this.field = field;
        setChanged();
        notifyObservers(field);
        if (GlobalConfig.isServerMode()) {
            clientsCommunicator.displayField(field);
        }
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
        if (GlobalConfig.isServerMode()) {
            clientsCommunicator.displayUserMoveRequested((Player) playerInterface, playerInterface.getName() + "'s move: ");
        }
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
        if (GlobalConfig.isServerMode()) {
            clientsCommunicator.displayMessage(message);
        }
    }

    public String getMessage() {
        return message;
    }

    public char[][] getField() {
        return field;
    }
}
