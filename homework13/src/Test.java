/**
 * Created by Abhishek8085 on 05-11-2015.
 */
public class Test {

    static  int depth=9;
    static int initialSize =26;

    static char[][]  field = new char[9][26];

    public static void main(String args[])
    {
        GameServerSocket gameServerSocket = null;//new GameServerSocket(null,null);
        try {

            gameServerSocket.initialise();
        } catch (InitialisationException e) {
            e.printStackTrace();
        }




       /* //GameClientSocket gameClientSocket = new GameClientSocket();

        gameClientSocket.setObjectArrivalListener(new ObjectArrivalListener() {
            @Override
            public void onObjectArrival(Player player, Object object) {
                System.out.println(((DisplayMessage) ((CommandFrame) object).getData()).getMessage());
            }
        });

        try {
            gameClientSocket.initialise();
        } catch (InitialisationException e) {
            e.printStackTrace();
        }
*/



        initializeField();

        try {
            Thread.sleep(11000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //gameServerSocket.sendFrame(new CommandFrame(MachineHelper.getLocalHost(),new DisplayField(field)));
    }


    public static void initializeField() {
        for (int i = 0; i < depth; i++) {
            for (int k = i; k < initialSize - i; k++) {
                field[i][k] = 'O';
            }
        }
    }
}
