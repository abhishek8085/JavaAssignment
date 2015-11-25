/**
 * Created by Abhishek8085 on 04-11-2015.
 */
public class PlayerDeserializer extends AbstractDeserialize<PlayerFrame,PlayerInfo>{

    private static PlayerDeserializer playerDeserializer = new PlayerDeserializer();


    public static Deserializer<PlayerFrame,PlayerInfo> getInstance() {
        return playerDeserializer;
    }


    @Override
    protected PlayerFrame deSerializeData(byte[] bytes) {
        return new PlayerFrame(super.sourceAddress,new PlayerInfo((int)bytes[0]));
    }
}
