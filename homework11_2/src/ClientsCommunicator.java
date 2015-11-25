/**
 * @version 1.0 : ClientsCommunicator.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
public class ClientsCommunicator
{
    GameServerSocket gameServerSocket;


    public ClientsCommunicator(GameServerSocket gameServerSocket) {
        this.gameServerSocket = gameServerSocket;
    }

    public void displayField(char[][] field)
    {
        gameServerSocket.broadCastFrame(new CommandFrame("",new DisplayField(field)));
    }

    public void displayMessage( String string )
    {
        gameServerSocket.broadCastFrame(new CommandFrame("",new DisplayMessage(string)));
    }

    public void displayUserMoveRequested( Player player,String message )
    {
        gameServerSocket.broadCastFrame(new CommandFrame("",new DisplayUserMoveRequested(player,message)));
    }
}
