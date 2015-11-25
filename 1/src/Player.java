import java.util.Scanner;

/**
 * This is player class
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : EntryPoint.java, 2015/09/07
 */
class Player implements PlayerInterface {

    private Connect4FieldInterface connect4FieldInterface;
    private String name;
    private char gamePiece;
    private Scanner scanner = new Scanner(System.in);
    private boolean isHuman;
    private boolean isRemote = false;

    Player(Connect4FieldInterface theField, String name, boolean isHuman, char gamePiece) {
        this.connect4FieldInterface = theField;
        this.name = name;
        this.isHuman = isHuman;
        this.gamePiece = gamePiece;
    }


    @Override
    public boolean isHuman() {
        return isHuman;
    }

    public void setIsRemote(boolean isRemote) {
        this.isRemote = isRemote;
    }

    @Override
    public boolean isRemote() {
        return isRemote;
    }

    @Override
    public char getGamePiece() {
        return gamePiece;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int nextMove() {
        System.out.print(name + " your move : ");
        if (scanner.hasNextLine()) {
            return scanner.nextInt();
        }
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        scanner.close();
    }
}