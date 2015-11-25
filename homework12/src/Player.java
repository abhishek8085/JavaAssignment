import java.io.Serializable;
import java.util.Scanner;

/**
 * This is player class
 *
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : EntryPoint.java, 2015/11/07
 */
class Player implements PlayerInterface,Serializable{

    private Connect4FieldInterface connect4FieldInterface;
    private String name;
    private char gamePiece;
    //private Scanner scanner = new Scanner(System.in);
    private boolean isHuman;

    Player( String name, boolean isHuman, char gamePiece) {
        this.name = name;
        this.isHuman = isHuman;
        this.gamePiece = gamePiece;
    }


    @Override
    public boolean isHuman() {
        return isHuman;
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
 /*       if (scanner.hasNextLine()) {
            return scanner.nextInt();
        }*/
        return 0;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
       // scanner.close();
    }
}