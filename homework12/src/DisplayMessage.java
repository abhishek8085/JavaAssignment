/**
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : DisplayMessage.java, 2015/11/04
 */
public class DisplayMessage implements Command{
    private String message;

    public DisplayMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
