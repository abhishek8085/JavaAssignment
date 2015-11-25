
/**
 * @version 1.0 : Connect4Field.java, 2015/09/12
 * @author Abhishek Indukumar
 * 
 */

import java.util.Vector;

/**
 * This program is Multiplayer connect4 game.
 *
 */

public class Connect4Field extends AbstractController implements Connect4FieldInterface, UserMoveListener {

    public static char ACCESSIBLE_POINTS = 'O';
    public static char INACCESSIBLE_POINTS = 'I';
    private static char GAME_WIN = 'W';
    private static char GAME_DRAW = 'D';
    private UsersHandler usersHandler = new UsersHandler(this);
    private boolean endGame = false;
    private char[][] field;
    private int initialSize;
    private int depth;
    private boolean isPlayerVsPlayer = true;

    /**
     * starting point of the program
     *
     * @param args
     */
    public static void main(String[] args) {
    /*	Connect4Field connect4Field = new Connect4Field(26, 9);
		connect4Field.initialize();
		connect4Field.playTheGame();*/
    }

    /**
     * Constructor
     *
     * @param initialSize
     * @param depth
     */
    public Connect4Field(Model model, int initialSize, int depth) {
        super(model);
        this.field = new char[initialSize][initialSize];
        this.initialSize = initialSize;
        this.depth = depth;
        initializeField();
    }

    public void setIsPlayerVsPlayer(boolean isPlayerVsPlayer) {
        this.isPlayerVsPlayer = isPlayerVsPlayer;
    }

    public char[][] getField() {
        return field;
    }

    public int getInitialSize() {
        return initialSize;
    }

    public int getDepth() {
        return depth;
    }

    /**
     * This method initializes players, connect managers, connect4 field and
     * other required parameters.
     */
    public void initialise() {
        displayWelcomeMessage();
        isPlayerVsPlayer();
    }

    /**
     * Take the input from user if the game is user vs user or user vs computer
     *
     * @return
     */
    private void isPlayerVsPlayer() {
        getModel().setMessage("Please enter a choice:");
        getModel().setMessage(" 1) User Vs User ");
        getModel().setMessage(" 2) User Vs Computer ");
        getModel().setInputExpectedMessage("Choice: ");
    }

    /**
     * Initializes char field
     */
    public void initializeField() {
        for (int i = 0; i < depth; i++) {
            for (int k = i; k < initialSize - i; k++) {
                field[i][k] = ACCESSIBLE_POINTS;
            }
        }
    }

    /**
     * Checking if the piece can be dropped
     */
    @Override
    public boolean checkIfPiecedCanBeDroppedIn(int column) {
        if (column < 0 || column > initialSize - 1)
            return false;

        return field[0][column] == ACCESSIBLE_POINTS;
    }

    /**
     * Drop the piece
     */
    @Override
    public void dropPieces(int column, char gamePiece) {
        int i = 0;
        while (true) {
            if (field[i][column] != ACCESSIBLE_POINTS) {
                if (i == 0) {
                    break;
                }
                field[i - 1][column] = gamePiece;
                break;
            }
            i++;
        }
    }

    /**
     * Scans the entire connect4 field and checks if the last move is the
     * winning one.
     */
    @Override
    public boolean didLastMoveWin() {
        return scanHorizontalForWinMove() || scanVerticalForWinMove()
                || scanLeftDiagonalForWinMove()
                || scanRightDiagonalForWinMove();
    }

    /**
     * Check if its draw
     */
    @Override
    public boolean isItaDraw() {
        for (int i = 0; i < depth; i++) {
            for (int k = 0; k < initialSize; k++) {
                if (field[i][k] == ACCESSIBLE_POINTS) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Initialize users
     */
    @Override
    public void init(PlayerInterface playerA, PlayerInterface playerB) {
        usersHandler.addPlayers(playerA);
        usersHandler.addPlayers(playerB);
    }

    /**
     * Start the game
     */
    @Override
    public void playTheGame() {
        getModel().setPlayerMoveRequestMessage(usersHandler.nextUser());
    }

    /**
     * Displays the welcome message
     */
    private void displayWelcomeMessage() {
        getModel().setMessage(
                "************************************************************************** ");
        getModel().setMessage(
                "                         Welcome To Connect4                               ");
        getModel().setMessage(
                "************************************************************************** ");
        getModel().setMessage("");
    }


    /**
     * Use "*" for human user
     *
     * @return
     */
    public char getHumanUserGamePeice() {
        return '*';
    }

    /**
     * displays the complete connect4 field
     */
    public void showField() {
        for (int i = 0; i < initialSize; i++) {
            getModel().setMessage("_");
        }
        getModel().setMessage("");
        for (int i = 0; i < depth; i++) {
            for (int k = 0; k < initialSize; k++) {
                getModel().setMessage(field[i][k] + "");
            }
            getModel().setMessage("");
        }

        for (int i = 0; i < initialSize; i++) {
            getModel().setMessage("-");
        }
        getModel().setMessage("");

    }

    @Override
    public void userInput(int input) {

        isPlayerVsPlayer = input == 1;

        PlayerInterface playerInterface1;
        PlayerInterface playerInterface2;

        if (isPlayerVsPlayer) {
            playerInterface1 = new Player(this, "A", true, '*');
            playerInterface2 = new Player(this, "B", true, '+');
            getModel().setMessage(
                    "There are two players, Each player will be asked a column of choice ");
            getModel().setMessage("to drop the piece ");
        } else {
            playerInterface1 = new Player(this, "A", true, '*');
            playerInterface2 = new GameEngine(this, '+');
            getModel().setMessage(
                    "There are two players, One of them is HAL 9000(computer) another player is You, Enter column of choice ");
            getModel().setMessage("to drop the piece ");
        }

        init(playerInterface1, playerInterface2);

        PlayerInterface p1 = usersHandler.getPlayers().get(0);
        PlayerInterface p2 = usersHandler.getPlayers().get(1);
        getModel().setMessage(
                p1.getName() + "'s game peice is '" + p1.getGamePiece() + "'");
        getModel().setMessage(
                p2.getName() + "'s game peice is '" + p2.getGamePiece() + "'");

        playTheGame();
    }


    /**
     * Scans the conncet4 field horizontally to check if any one won.
     *
     * @return
     */
    public boolean scanHorizontalForWinMove() {
        for (int i = 0; i < depth; i++) {
            int counter = 1;
            char previousChar = 'd';
            for (int k = 0; k < initialSize; k++) {
                char presentChar = field[i][k];

                if (((int) presentChar) == 0) {
                    continue;
                }

                if (presentChar != ACCESSIBLE_POINTS
                        && presentChar == previousChar) {
                    if (++counter == 4) {
                        return true;
                    }
                    previousChar = presentChar;
                } else {
                    previousChar = presentChar;
                    counter = 1;
                }
            }
        }
        return false;
    }

    /**
     * Scans the conncet4 field vertically to check if any one won.
     *
     * @return
     */
    public boolean scanVerticalForWinMove() {
        for (int i = 0; i < depth; i++) {
            int counter = 1;
            char previousChar = 'd';
            for (int k = 0; k < initialSize; k++) {
                char presentChar = field[k][i];

                if (((int) presentChar) == 0) {
                    continue;
                }

                if (presentChar != ACCESSIBLE_POINTS
                        && presentChar == previousChar) {
                    if (++counter == 4) {
                        return true;
                    }
                    previousChar = presentChar;
                } else {
                    previousChar = presentChar;
                    counter = 1;
                }
            }
        }
        return false;
    }

    /**
     * Scans the conncet4 field left diagonally to check if any one won.
     *
     * @return
     */
    public boolean scanLeftDiagonalForWinMove() {
        int counter = 1;
        char previousChar = 'd';
        for (int i = 0; i < initialSize; i++) {
            for (int k = 0; k < depth; k++) {

                if (i + k >= initialSize)
                {
                    return false;
                }

                char presentChar = field[k][i + k >= initialSize
                        ? initialSize - 1 : i + k];
                if (((int) presentChar) == 0) {
                    continue;
                }
                if (presentChar != ACCESSIBLE_POINTS
                        && presentChar == previousChar) {
                    if (++counter == 4) {
                        return true;
                    }
                    previousChar = presentChar;
                } else {
                    previousChar = presentChar;
                    counter = 1;
                }
            }
        }
        return false;
    }

    /**
     * Scans the conncet4 field right diagonally to check if any one won.
     *
     * @return
     */
    public boolean scanRightDiagonalForWinMove() {
        int counter = 1;
        char previousChar = 'd';
        for (int i = initialSize - 1; i >= 0; i--) {
            for (int k = 0; k < depth; k++) {

                if (i - k <= 0)
                {
                    return false;
                }

                char presentChar = field[k][i - k <= 0 ? 0 : i - k];
                if (((int) presentChar) == 0) {
                    continue;
                }
                if (presentChar != ACCESSIBLE_POINTS
                        && presentChar == previousChar) {
                    if (++counter == 4) {
                        return true;
                    }
                    previousChar = presentChar;
                } else {
                    previousChar = presentChar;
                    counter = 1;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the game has ended or not and lets the user to make the next
     * move.
     */
    @Override
    public boolean canUserMove() {
        return !endGame;
    }

    /**
     * After user gives the input this will drop the piece to the specified
     * column or gives an message if the column if filled up.
     */
    @Override
    public void onUserMove(PlayerInterface playerInterface, int move) {
        if (checkIfPiecedCanBeDroppedIn(move)) {
            dropPieces(move, playerInterface.getGamePiece());
            getModel().setField(field);
            char gameCondition = checkForWindOrDraw();
            if (gameCondition == GAME_WIN) {
                gameWin(playerInterface);
                return;
            } else if (gameCondition == GAME_DRAW) {
                gameDraw(playerInterface);
                return;
            }

            PlayerInterface nexPlayer = usersHandler.nextUser();

            if (nexPlayer.isHuman()) {
                getModel().setPlayerMoveRequestMessage(nexPlayer);
            } else {
                onUserMove(nexPlayer, nexPlayer.nextMove());
            }
        } else {

            getModel().setMessage("Can't drop in column: " + move);
            getModel().setPlayerMoveRequestMessage(playerInterface);
        }


    }

    /**
     * checks if game has won or its a draw.
     *
     * @return
     */
    private char checkForWindOrDraw() {
        if (didLastMoveWin()) {
            return GAME_WIN;
        } else if (isItaDraw()) {
            return GAME_DRAW;
        }
        return 0;
    }

    /**
     * prints the message and returns the value
     *
     * @param playerInterface
     */
    private void gameDraw(PlayerInterface playerInterface) {
        getModel().setMessage("*************************************");
        getModel().setMessage("           Game Draw                 ");
        getModel().setMessage("*************************************");
        endGame = true;
    }

    /**
     * prints the message and returns the value
     *
     * @param playerInterface
     */
    private void gameWin(PlayerInterface playerInterface) {
        getModel().setMessage("*************************************");
        getModel().setMessage(playerInterface.getName() + " won the game");
        getModel().setMessage("*************************************");
        endGame = true;
    }

    /**
     * Complete connect4 field is displayed here.
     */
    @Override
    public String toString() {

        String fieldInString = "";

        for (int i = 0; i < initialSize; i++) {
            fieldInString += "_";
        }
        fieldInString += "\n";
        for (int i = 0; i < depth; i++) {
            for (int k = 0; k < initialSize; k++) {
                fieldInString += field[i][k];
            }
            fieldInString += "\n";
        }

        for (int i = 0; i < initialSize; i++) {
            fieldInString += "-";
        }
        fieldInString += "\n";
		/*
		 * for (int i = 0; i < initialSize; i++) { getModel().setMessage(i); }
		 * getModel().setMessage();
		 */

        return fieldInString;
    }
}

/**
 * This method keeps all the user records and manages all the user moves and
 * turns that's required.
 */
class UsersHandler {
    private UserMoveListener userMoveListener;
    private Vector<PlayerInterface> playerVector = new Vector<PlayerInterface>();
    private PlayerRotater playerRotater = new PlayerRotater(playerVector);

    public UsersHandler(UserMoveListener userMoveListener) {
        this.userMoveListener = userMoveListener;
    }

    public void addPlayers(PlayerInterface playerInterface) {
        playerVector.add(playerInterface);
    }

    public Vector<PlayerInterface> getPlayers() {
        return playerVector;
    }

    private class PlayerRotater {
        private Vector<PlayerInterface> playerVector;
        private int counter;

        public PlayerRotater(Vector<PlayerInterface> playerVector) {
            this.playerVector = playerVector;
        }

        public PlayerInterface getNextPlayer() {
            if (counter >= playerVector.size()) {
                counter = 0;
            }
            return playerVector.get(counter++);
        }
    }

    public PlayerInterface nextUser() {
        return playerRotater.getNextPlayer();
        //return playerRotater.getNextPlayer();


        //while (!userMoveListener.onUserMove(playerInterface,
        //playerInterface.nextMove()));
        //}
    }
}


