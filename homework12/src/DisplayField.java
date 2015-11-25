/**
 * @version 1.0 : DisplayField.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
/**
 * Created by Abhishek8085 on 05-11-2015.
 */
public class DisplayField implements Command{
    private char[][] field;

    public DisplayField(char[][] field) {
        this.field = field;
    }

    public char[][] getField() {
        return field;
    }
}
