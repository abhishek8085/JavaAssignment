/**
 *
 * This rule based game engine serves as computer
 * player
 *
 * @version 1.0 : GameEngine.java, 2015/11/12
 * @author Abhishek Indukumar
 * 
 *
 */
public class GameEngine implements PlayerInterface {
    private Connect4Field theField;
    private char gamePiece;
    private MovePredictor movePredictor;
    private char[][] charField;

    GameEngine(Connect4Field theField, char gamePiece) {
        this.theField = theField;
        this.gamePiece = gamePiece;
        this.charField = theField.getField();
        this.movePredictor = new MovePredictor(theField);
    }

    @Override
    public boolean isHuman() {
        return false;
    }

    @Override
    public char getGamePiece() {
        return gamePiece;
    }

    @Override
    public String getName() {
        return "HAL 9000";
    }

    @Override
    public int nextMove() {

        MinimumMove hCMMoves = movePredictor.scanHorizontalForWinMove(gamePiece);
        MinimumMove hMMMoves = movePredictor.scanHorizontalForWinMove(theField.getHumanUserGamePeice());

        MinimumMove vCMMoves = movePredictor.scanVerticalForWinMove(gamePiece);
        MinimumMove vMMMoves = movePredictor.scanVerticalForWinMove(theField.getHumanUserGamePeice());

        MinimumMove computerMMove = hCMMoves.getMinimumMove() > vCMMoves.getMinimumMove() ? vCMMoves : hCMMoves;
        MinimumMove humanMMove = hMMMoves.getMinimumMove() > vMMMoves.getMinimumMove() ? vMMMoves : hMMMoves;

        if (humanMMove.getMinimumMove() <= computerMMove.getMinimumMove()) {
            //defensive
            if (humanMMove.isHorizontal()) {
                return getHorizontalPiece(humanMMove.getXPosition(), humanMMove.getYPosition());
            } else {
                return humanMMove.getXPosition();
            }

        } else {
            //offsive
            if (computerMMove.isHorizontal()) {
                return getHorizontalPiece(computerMMove.getXPosition(), computerMMove.getYPosition());
            } else {
                return computerMMove.getXPosition();
            }
        }

    }

    /**
     * Free location to drop horizontal piece
     * @param column
     * @param row
     * @return column
     */
    public int getHorizontalPiece(int column, int row) {

        int position = -1;
        boolean fastReturn = false;

        for (int i = column; i < column + 4; i++) {

            if (charField[row][i] == theField.getHumanUserGamePeice()) {
                if (position == -1) {
                    fastReturn = true;
                    continue;
                } else {
                    return position;
                }
            } else if (charField[row][i] == Connect4Field.ACCESSIBLE_POINTS) {
                if (fastReturn) {
                    return i;
                } else {
                    position = i;
                }
            }
        }
        return position;
    }

    /**
     * Free location to drop horizontal piece
     * @param column
     * @param currentGamePeice column
     */
    public void dropVerticalPiece(int column, char currentGamePeice) {
        theField.dropPieces(column, currentGamePeice);
    }

}

/**
 * The class predicts computer move based on human move
 */
class MovePredictor {
    private char[][] field;
    private int initialSize;
    private int depth;

    public MovePredictor(Connect4Field theField) {
        this.field = theField.getField();
        this.initialSize = theField.getInitialSize();
        this.depth = theField.getDepth();
    }

    /**
     * scan horizontally for minimum moves to win
     * @param gamePiece
     * @return MinimumMove
     */
    public MinimumMove scanHorizontalForWinMove(char gamePiece) {
        MinimumMove horizontalMinimumMovesRequired = new MinimumMove(Integer.MAX_VALUE, 0, 0, true);
        MinimumMove minimumMovesRequired = new MinimumMove(Integer.MAX_VALUE, 0, 0, true);
        for (int i = 0; i <= depth; i++) {
            for (int k = 0; k <= initialSize - 4; k++) {
                MinimumMove minimumMovesInHorizontalWindow = new MinimumMove(minimumMovesInHorizontalWindow(field[i], gamePiece, k), k, i, true);
                minimumMovesRequired = minimumMovesInHorizontalWindow.getMinimumMove() < minimumMovesRequired.getMinimumMove() ? minimumMovesInHorizontalWindow : minimumMovesRequired;
            }
            horizontalMinimumMovesRequired = minimumMovesRequired.getMinimumMove() < horizontalMinimumMovesRequired.getMinimumMove() ? minimumMovesRequired : horizontalMinimumMovesRequired;
        }
        return horizontalMinimumMovesRequired;
    }

    /**
     *Method return minimum moves in the window
     *
     * @param field
     * @param gamePiece
     * @param offset
     * @return minimum moves
     */
    public int minimumMovesInHorizontalWindow(char field[], char gamePiece, int offset) {
        int minimumMoves = 0;

        for (int i = offset; i < offset + 4; i++) {

            if (field[i] == 0) {
                return Integer.MAX_VALUE;
            }

            if (field[i] == Connect4Field.ACCESSIBLE_POINTS) {
                minimumMoves++;
            } else if (field[i] != gamePiece) {
                return Integer.MAX_VALUE;
            }
        }
        return minimumMoves;
    }

    /**
     * Method scans vertically for minimum moves to win.
     *
     * @param gamePiece
     * @return minimum moves
     */
    public MinimumMove scanVerticalForWinMove(char gamePiece) {
        MinimumMove verticalMinimumMovesRequired = new MinimumMove(Integer.MAX_VALUE, 0, 0, false);
        MinimumMove minimumMovesRequired = new MinimumMove(Integer.MAX_VALUE, 0, 0, false);

        for (int i = 0; i <= depth - 4; i++) {
            for (int k = 0; k < initialSize; k++) {
                MinimumMove minimumMovesInVerticalWindow = new MinimumMove(minimumMovesInVerticalWindow(field, gamePiece, k, i), k, i, false);
                minimumMovesRequired = minimumMovesInVerticalWindow.getMinimumMove() < minimumMovesRequired.getMinimumMove() ? minimumMovesInVerticalWindow : minimumMovesRequired;
            }
            verticalMinimumMovesRequired = minimumMovesRequired.getMinimumMove() < verticalMinimumMovesRequired.getMinimumMove() ? minimumMovesRequired : verticalMinimumMovesRequired;
        }
        return verticalMinimumMovesRequired;
    }


    /**
     * Method return minimum moves in the window
     * @param field
     * @param gamePiece
     * @param xOffset
     * @param yOffset
     * @return minimum moves in the window
     */
    public int minimumMovesInVerticalWindow(char field[][], char gamePiece, int xOffset, int yOffset) {
        int minimumMoves = 0;

        for (int i = yOffset; i < 4 + yOffset; i++) {

            if (field[i][xOffset] == 0) {
                return Integer.MAX_VALUE;
            }

            if (field[i][xOffset] == Connect4Field.ACCESSIBLE_POINTS) {
                minimumMoves++;
            } else if (field[i][xOffset] != gamePiece) {
                return Integer.MAX_VALUE;
            }
        }
        return minimumMoves;
    }
}

/**
 * Place holder for minimum moves object
 * contains minimum move x and y position
 */
class MinimumMove {
    private int minimumMove;
    private int xPosition;
    private int yPosition;
    private boolean isHorizontal;

    //constructor
    public MinimumMove(int minimumMove, int xPosition, int yPosition, boolean isHorizontal) {
        this.minimumMove = minimumMove;
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.isHorizontal = isHorizontal;
    }

    //Getters and Setters//

    public int getMinimumMove() {
        return minimumMove;
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

}