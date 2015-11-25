/**
 * @version 1.0 : PlayerInterface.java, 2015/09/12
 * @author Abhishek Indukumar
 * 
 *
 */
public interface PlayerInterface {

	// this is how your constructor has to be
	// Player(Connect4FieldInterface theField, String name, char gamePiece)

	public boolean isHuman();

	public boolean isRemote();

	public char getGamePiece();

	public String getName();

	public int nextMove();
}
