import java.rmi.Naming;

/**
 * This is the entry point class where execution starts
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : EntryPoint.java, 2015/11/04
 */
public class EntryPoint
{
    public static void main(String[] args)
    {
        //args=new String[]{"false","127.0.0.1","8090"};

        GlobalConfig.setIsServerMode(args[0].equalsIgnoreCase("true"));





        Model model = new Model();
        Connect4Field controller = new Connect4Field(model,26,9);
        //bind view with model
        View view = new View(controller,model);

        if( GlobalConfig.isServerMode() )
        {
            GlobalConfig.setUserName(args[1]);
            GameServerSocket gameServerSocket =null;

            try {
                 gameServerSocket = new GameServerSocket(controller,controller);
                Naming.rebind("rmi://"+MachineHelper.getLocalHost()+":5000/GameServerConnection", gameServerSocket);
                gameServerSocket.initialise();
            } catch (Exception e) {
                e.printStackTrace();
            }

            ClientsCommunicator clientsCommunicator = new ClientsCommunicator(gameServerSocket);
            model.setClientsCommunicator(clientsCommunicator);
            controller.initialise();

        }
        else
        {
            GlobalConfig.setUserName(args[1]);
            GlobalConfig.setSeverAddress(args[2]);
            GlobalConfig.setServerPort(Integer.parseInt(args[3]));

            try {
                GameClientSocket gameClientSocket = new GameClientSocket(controller);
                Naming.rebind("rmi://"+MachineHelper.getLocalHost()+":5000/GameClientConnection", gameClientSocket);
                gameClientSocket.initialise();
                controller.setGameClientSocket(gameClientSocket);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
