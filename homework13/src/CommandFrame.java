/**
 * @version 1.0 : CommandFrame.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
public class CommandFrame extends AbstractFrame<Command> {

    private Command command;

    public CommandFrame(String sourceAddress,String playerName,Command command) {
        super(sourceAddress,playerName);
        this.command =command;
    }

    @Override
    public int getFrameId() {
        return FrameIds.COMMAND_FRAME;
    }

    @Override
    public Command getData() {
        return command;
    }
}
