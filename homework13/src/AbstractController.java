/**
 * @version 1.0 : AbstractController.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */

public abstract class AbstractController implements Controller{

    private Model model;

    public AbstractController(Model model) {
        this.model = model;
    }

    protected Model getModel() {
        return model;
    }
}
