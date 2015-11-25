/**
 * All controller should extend this
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : AbstractController.java, 2015/10/04
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
