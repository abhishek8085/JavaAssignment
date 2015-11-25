/**
 * @version 1.0 : AbstractView.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
import java.util.Observable;
import java.util.Observer;

/**
 * All view should extend this
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : AbstractView.java, 2015/10/04
 */
public abstract class AbstractView implements Observer {

    private Controller controller;
    private Model model;

    public AbstractView(Controller controller, Model model) {
        this.controller = controller;
        this.model = model;
        bindWithModel(model);
    }

    /**
     *
     * @param o
     * @param arg
     */
    @Override
    public void update(Observable o, Object arg) {
        if ( o instanceof Model )
        {
            if ( arg instanceof String ) {
                String message = (String) arg;
                if(message.startsWith("IO:"))
                {
                    controller.userInput(displayIOMessage(((String) arg).substring(3)));
                }
                else
                {
                    displayMessage((String) arg);
                }
            }
            else if ( arg instanceof PlayerInterface )
            {
                controller.onUserMove((PlayerInterface)arg, getPlayerInput((PlayerInterface)arg));
            }
            else if( arg instanceof char[][] )
            {
                showField( (char[][]) arg );
            }
        }
    }

    /**
     * binds with the view
     * @param model mymodel
     */
    private void bindWithModel( Model model )
    {
        model.addObserver(this);
    }

    /**
     * Called to display input excepted message
     * @param message request input message
     * @return input
     */
    protected abstract int displayIOMessage( String message );

    /**
     * Called to display user output
     * @param message message
     */
    protected abstract void displayMessage( String message );

    /**
     * Called to display , requesting for user move
     * @param playerInterface player
     * @return player move
     */
    protected abstract int getPlayerInput( PlayerInterface playerInterface );

    /**
     * called to display whole field
     * @param field connect 4 field
     */
    protected abstract void showField( char[][] field );

    protected Controller getController() {
        return controller;
    }

    protected Model getModel() {
        return model;
    }

}
