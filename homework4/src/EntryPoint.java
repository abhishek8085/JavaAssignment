/**
 * This is the entry point class where execution starts
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : EntryPoint.java, 2015/10/04
 */
public class EntryPoint
{
    public static void main(String[] args)
    {
        Model model = new Model();
        Controller controller = new Connect4Field(model,26,9);
        //bind view with model
        View view = new View(controller,model);
        controller.initialise();
    }
}
