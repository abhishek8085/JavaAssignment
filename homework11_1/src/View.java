import java.util.Scanner;

/**
 * This is the view
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : View.java, 2015/10/04
 */
public class View extends AbstractView {

    private Scanner scanner = new Scanner(System.in);

    public View(Controller controller, Model model) {
        super(controller, model);
    }

    @Override
    protected int displayIOMessage(String message) {
        System.out.println(message);
        if (scanner.hasNextLine()) {
            return scanner.nextInt();
        }
        return 0;
    }

    @Override
    protected void displayMessage(String message) {
        System.out.println(message);
    }

    @Override
    protected int getPlayerInput(PlayerInterface playerInterface) {
        System.out.print(playerInterface.getName() + " your move : ");

        if (scanner.hasNextLine()) {
            return scanner.nextInt();
        }
        return 0;
    }

    @Override
    protected void showField(char[][] field) {
        for (int i = 0; i < field[0].length; i++) {
            System.out.print("_");
        }
        System.out.println("");
        for (int i = 0; i < 9; i++) {
            for (int k = 0; k < field[0].length; k++) {
                System.out.print(field[i][k]==0?" ":field[i][k]);
            }
            System.out.println("");
        }

        for (int i = 0; i < field[0].length; i++) {
            System.out.print("-");
        }
        System.out.println("");



    }
}

