/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public class PlayerFrame extends AbstractFrame<PlayerInfo>{

    private PlayerInfo playerInfo;

    public PlayerFrame(String sourceAddress, PlayerInfo playerInfo) {
        super(sourceAddress);
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

    @Override
    public byte[] serialise() {
        return PlayerFrameSerializer.getInstance().serialize(this);
    }
}
