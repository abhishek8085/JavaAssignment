/**
 * @version 1.0 : Connect4FieldInterface.java, 2015/11/06
 * @author Abhishek Indukumar
 * 
 *
 */
public interface Connect4FieldInterface {
	public boolean checkIfPiecedCanBeDroppedIn(int column);

	public void dropPieces(int column, char gamePiece);

	boolean didLastMoveWin();

	public boolean isItaDraw();

	public void init(PlayerInterface playerA, PlayerInterface playerB);

	public String toString();

	public void playTheGame();
}
