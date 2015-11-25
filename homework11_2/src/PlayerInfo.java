import java.io.Serializable;

/**
 * @version 1.0 : PlayerInfo.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
public class PlayerInfo implements Serializable{

    public PlayerInfo(int move) {
        this.move = move;
    }
    private int move;

    public int getMove()
    {
        return move;
    }
}
