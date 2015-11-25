/**
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : PlayerFrame.java, 2015/11/06
 */
public class PlayerFrame extends AbstractFrame<PlayerInfo> {

    private PlayerInfo playerInfo;

    public PlayerFrame(String sourceAddress,String playerName, PlayerInfo playerInfo) {
        super(sourceAddress,playerName);
        this.playerInfo = playerInfo;
    }

    @Override
    public int getFrameId() {
        return FrameIds.PLAYER_FRAME;
    }

    @Override
    public PlayerInfo getData() {
        return playerInfo;
    }


}
