/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public class PlayerFrameSerializer extends AbstractSerializer<PlayerFrame, PlayerInfo> {

    private static PlayerFrameSerializer playerFrameSerializer = new PlayerFrameSerializer();

    @Override
    protected byte[] serializeData(PlayerInfo data) {
        return new byte[]{( byte ) data.getMove()};
    }


    public static Serializer<PlayerFrame, PlayerInfo> getInstance() {
        return playerFrameSerializer;
    }

    @Override
    public int getSerializerId() {
        return SerializerIds.PLAYERFRAME_SERIALIZER_ID;
    }
}
