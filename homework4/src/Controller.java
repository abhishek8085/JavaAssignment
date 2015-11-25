

/**
 * All controller should implement this
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Controller.java, 2015/10/04
 */
public interface Controller{

    /**
     * Initialises controller
     */
     void initialise();

    /**
     * This method is invoked when user
     * enters a choice between computer vs
     * human or human vs computer.
     * @param input user choice
     */
    void userInput( int input );

    /**
     * Invoked on every user move.
     * @param column user entry
     */
    void onUserMove( PlayerInterface playerInterface, int column );

}
