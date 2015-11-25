/**
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : DisplayUserMoveRequested.java, 2015/11/04
 */
public class DisplayUserMoveRequested implements Command{
    private String message;
    private Player player;

    public DisplayUserMoveRequested(Player player,String message) {
        this.player =player;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Player getPlayer() {
        return player;
    }
}
