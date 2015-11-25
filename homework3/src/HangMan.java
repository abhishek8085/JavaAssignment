import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

/**
 * This Program is Multi player hangman game.
 *
 * @author Abhishek Indukumar
 * 
 * @version 1.0 : Calculator.java, 2015/09/07
 */
public class HangMan {

    private static final int GUESS_LIMIT=8;
    private static final int TOP_PLAYERS=10;

    public static void main(String[] args) {
        CommandLineHelper.parseCommandLine(new String[]{"c:/words/words","player1","player2","player3","player4","player5","player6"});
        //creates players
        Players players = new Players(CommandLineHelper.getUserNames());
        //Initialising word selector
        WordSelector wordSelector = new WordSelector(new File(CommandLineHelper.getDictionaryLocation()));
        try {
            wordSelector.initialize();
        } catch (FileNotFoundException e) {
            throw new RuntimeException (e);
        }
        //Create UI Manager
        UIManager uiManager = new UIManager();
        //Get rounds info
        int rounds = Integer.parseInt(uiManager.requestRounds());

        for (int i = 0; i < rounds; i++) {
            uiManager.printRounds(i + 1);
            for (Player player : players.getPlayers()) {
                uiManager.requestNextPlayer(player);
                Word word = wordSelector.getWord();
                word.setGuessLimit(GUESS_LIMIT);
                String guessStatus = "";
                do {
                    guessStatus = word.evaluateGuess(uiManager.requestGuess(word));
                    uiManager.printGuessStatus(word, guessStatus);
                    player.setGuessStatus(guessStatus);
                }
                while (!(guessStatus.equals(Word.WHOLE_WORD_GUESSED) || guessStatus.equals(Word.EXCEED_GUESS_LIMIT)));
            }
        }
        uiManager.printScores(players.getPlayers(), TOP_PLAYERS);
    }
}

/**
 * This class extracts Dictionary file location
 * as well as player info from command
 * line
 * @author Abhishek Indukumar
 * 
 */
class CommandLineHelper {
    private static String dictionaryLocation;
    private static String[] userNames;

    /**
     * extracts dictionary file location
     * as well as players info.
     *
     * @param args command line args
     */
    public static void parseCommandLine(String[] args) {
        dictionaryLocation = args[0];
        userNames = new String[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            userNames[i - 1] = args[i];
        }
    }

    /**
     * Return dictionary file location
     *@return dictionary file location
     */
    public static String getDictionaryLocation() {
        return dictionaryLocation;
    }

    /**
     * Returns all player names
     * @return return all player names
     */
    public static String[] getUserNames() {
        return userNames;
    }
}

/**
 * This class responsible of User Interaction
 * It takes input from user as well displays
 * output to user.
 *
 * @author Abhishek Indukumar
 * 
 */
class UIManager {

    /**
     * Requests players to enter number of rounds
     * they wish to play and returns the input
     * from them.
     *
     * @return number of rounds
     */
    public String requestRounds() {
        System.out.print("Please enter a number of rounds: ");
        return getLineFromScanner();
    }

    /**
     * Gets user input
     * @return String user input.
     */
    private String getLineFromScanner()
    {
        Scanner scanner=null;
        try {
            scanner= new Scanner(System.in);
            return scanner.nextLine();
        }finally {
            if(scanner!=null) {
                //scanner.close();
            }
        }
    }

    /**
     * Requests players to enter a letter
     * and returns the input from them.
     * @return letter
     */
    private String requestLetter() {
        System.out.print("Please enter a letter: ");
        return getLineFromScanner();
    }

    /**
     * Displays the incomplete word
     *
     * Requests players to enter a letter
     * and returns the input from them.
     * @return letter
     * @param word incomplete word
     */
    public String requestGuess(Word word) {
        System.out.println(word);
        System.out.println();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println(word.getLettersWhichAreGuessed());
        System.out.println("-----------------------------------------------------------------------");
        System.out.println();
        System.out.println("You have " + (word.getGuessLimit() - word.getGuesses()) + " guesses left");
        return requestLetter();
    }

    /**
     * Displays a Players's turn and wait for keypress.
     *
     * @param player Player
     */
    public void requestNextPlayer(Player player) {
        System.out.print('"' + player.getName() + '"' + " Please enter to begin");
        getLineFromScanner();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("                      " + player.getName() + "'s" + " turn                   ");
        System.out.println("-----------------------------------------------------------------------");
    }

    /**
     * Display or prints rounds info.
     *
     * @param round round
     */
    public void printRounds(int round) {
        System.out.println();
        System.out.println("-----------------------------------------------------------------------");
        System.out.println("                            ROUND: " + round + "                           ");
        System.out.println("-----------------------------------------------------------------------");
        System.out.println();
    }

    /**
     *
     * Displays top players with order of the higest scores
     *
     * @param players all players
     * @param topPlayers  number of top players to display
     */
    public void printScores(Vector<Player> players, int topPlayers) {
        players = sortPlayers(players);

        System.out.println("**************************************************");
        System.out.println("**************************************************");
        System.out.println("Top " + (players.size()>=topPlayers?topPlayers:players.size()) + " players are...");
        for (int i = 0; i< players.size() && i < topPlayers; i++) {
            System.out.println("\t" + players.get(i).getName() + ": \t" + players.get(i).getPoints());
        }
        System.out.println("**************************************************");
        System.out.println("**************************************************");
    }

    /**
     *
     * Display output to user based on guessed status.
     *
     * @param word word
     * @param guessStatus
     */
    public void printGuessStatus(Word word, String guessStatus) {
        if (guessStatus.equals(Word.RIGHT_GUESS)) {
            displayRightGuess();
        } else if (guessStatus.equals(Word.WRONG_GUESS)) {
            displayWrongGuess();
        } else if (guessStatus.equals(Word.EXCEED_GUESS_LIMIT)) {
            displayExceededGuessLimit(word);
        } else if (guessStatus.equals(Word.WHOLE_WORD_GUESSED) ) {
            displayWholeWordGuessed(word);
        }
    }


    /**
     * Display for right guess of letter
     */
    public void displayRightGuess() {
        System.out.println("***** Guess is Right *****");
        System.out.println();
    }

    /**
     * Display for wrong guess
     */
    public void displayWrongGuess() {
        System.out.println("***** Guess is Wrong *****");
        System.out.println();
    }

    /**
     * Display for Exceeding guess limit
     *
     * @param word
     */
    public void displayExceededGuessLimit(Word word) {
        System.out.println("******************************");
        System.out.println("     Exceeded guess limit     ");
        System.out.println("******************************");
        System.out.println("Answer: ");
        System.out.println(word.getHiddenWord());
        System.out.println();
    }

    /**
     * Display for guessing whole word right.
     *
     * @param word word
     */
    public void displayWholeWordGuessed(Word word) {
        System.out.println();
        System.out.println(word.getHiddenWord());
        System.out.println();
        System.out.println("******************************");
        System.out.println("       !!!!Congrates!!!!      ");
        System.out.println("******************************");
        System.out.println();
    }

    /**
     * Sort players based of highest score first order.
     * @param players all players
     * @return sorted players
     */
    private Vector<Player> sortPlayers(Vector<Player> players) {
        for (int i = 0; i < players.size(); i++) {
            for (int k = 0; k < players.size() - 1; k++) {
                if (players.get(k).getPoints() < players.get(k + 1).getPoints()) {
                    Player tempPlayer = players.get(k + 1);
                    players.set(k + 1, players.get(k));
                    players.set(k, tempPlayer);
                }
            }
        }
        return players;
    }
}

/**
 * This Class is responsible to read the word from
 * File and provide a random word when asked.
 *
 * @author Abhishek Indukumar
 * 
 */
class WordSelector {
    private File file;
    private Randomizer randomizer;
    private Vector<Word> words;

    public WordSelector(File file) {
        this.file = file;
    }

    /**
     * Reads word from file
     * and instantiates Randomizer
     * @throws FileNotFoundException if file not found
     */
    public void initialize() throws FileNotFoundException {
        words = getWords(file);
        randomizer = new Randomizer(words.size());
    }

    /**
     * Provides random Word from Dictionary(file)
     * @return Word
     */
    public Word getWord() {
        return words.get(randomizer.getRandomInt());
    }

    /**
     * This Class for generating random number
     * with in limits
     * @author Abhishek Indukumar
     * 
     */
    private class Randomizer {
        int maxLimit;
        Random random = new Random();

        /**
         * Max limit for random number generation
         * @param maxLimit max limit
         */
        public Randomizer(int maxLimit) {
            this.maxLimit = maxLimit;
        }

        /**
         * Returns random Int
         * @return Random int
         */
        public int getRandomInt() {
            return random.nextInt(maxLimit - 1);
        }
    }


    /**
     * Reads the words from the file tokenises it
     * and puts it in the vector.
     * @param file dictionary file
     * @return Vector of Words
     * @throws FileNotFoundException id the dictionary file is not found
     */
    private Vector<Word> getWords(File file) throws FileNotFoundException {
        Vector<Word> words = new Vector<Word>();
        Scanner scanner = null;
        try {
            scanner = new Scanner(file);
            while (scanner.hasNext()) {
                words.add(new Word(scanner.next().trim()));
            }
        } finally {
            if (scanner != null)
                scanner.close();
        }
        return words;
    }

}

/**
 * This Class is responsible the following:
 * 1) Keeps track of guesses.
 * 2) Keeps original word.
 * 3) Keeps incomplete guessed word
 * 4) Evaluates player input
 *
 * @author Abhishek Indukumar
 * 
 */
class Word {
    private static final String DUMMY = "^";
    public String word;
    private String copyWord;
    private int guessLimit;
    private int guesses = 0;
    private String[] printArray;
    private Vector<String> lettersWhichAreGuessed = new Vector<String>();

    public static final String RIGHT_GUESS = "R";
    public static final String WHOLE_WORD_GUESSED = "WWG";
    public static final String WRONG_GUESS = "W";
    public static final String EXCEED_GUESS_LIMIT = "EGL";
    public static final String EMPTY_GUESS = "EG";

    public Word(String word) {
        this.word = word.toUpperCase();
        this.copyWord = new String(this.word);
        printArray = new String[word.length()];
        fillPrintArrayWithBlanks();
    }

    /**
     * Return guess limit set to the word
     * @return limit
     */
    public int getGuessLimit() {
        return guessLimit;
    }

    /**
     * Sets max guess limit
     * @param guessLimit guess limit
     */
    public void setGuessLimit(int guessLimit) {
        this.guessLimit = guessLimit;
    }

    /**
     * Returns number of guess on the word
     * @return number of guesses
     */
    public int getGuesses() {
        return guesses;
    }

    /**
     * This method is invoked when a letter is guessed
     * by the player, This method evaluates the the
     * input letter and returns following status:
     *
     * RIGHT_GUESS: If guessed letter belong to word.
     * WRONG_GUESS: If guessed letter doesn't belong
     *              to word.
     * WHOLE_WORD_GUESSED: If the all the letter in the
     *                      word are guessed.
     * EXCEED_GUESS_LIMIT: If the number of guesses has gone
     *                     past the maximum guess allowed.
     *
     * @param letter input letter from player
     * @return Evaluation status as describe above.
     */
    public String evaluateGuess(String letter) {
        letter = letter.toUpperCase();

         if ( letter.isEmpty() || letter.equals("") ||lettersWhichAreGuessed.contains(letter) ) {
             return EMPTY_GUESS;
         }
        else {
             lettersWhichAreGuessed.add(letter);
         }

        if (!(letter.trim().isEmpty()) && (copyWord.indexOf(letter)) != -1) {
            for (int i = 0; i < copyWord.length(); i++) {
                if (copyWord.toUpperCase().charAt(i) == letter.charAt(0)) {
                    printArray[i] = letter;
                }
            }

            copyWord = copyWord.toUpperCase().replaceAll(letter, DUMMY);

            if (isWholeWordGuessed()) {
                return WHOLE_WORD_GUESSED;
            }
            return RIGHT_GUESS;
        } else {
            if (++guesses >= guessLimit) {
                return EXCEED_GUESS_LIMIT;
            }
            return WRONG_GUESS;
        }


    }

    /**
     * Return true if the all the letters in the word
     * are guessed.
     * @return true if all the letter are guessed
     *         false otherwise.
     */
    private boolean isWholeWordGuessed() {
        for (int i = 0; i < copyWord.length(); i++) {
            if (!(copyWord.charAt(i) + "").equals(DUMMY)) {
                return false;
            }
        }
        return true;
    }

    /**
     *Fills the print array with "_".
     */
    private void fillPrintArrayWithBlanks() {
        for (int i = 0; i < printArray.length; i++) {
            printArray[i] = "_";
        }
    }

    /**
     * This returns "word" with blanks for
     * not yet guessed letter
     *
     * @return incomplete word
     */
    @Override
    public String toString() {
        String print = "";
        for (String s : printArray) {
            print += s + " ";
        }
        return print;
    }

    /**
     *Return the word which nedded to be guessed
     *
     * @return complete word
     */
    public String getHiddenWord() {
        String print = "";
        for (char c : word.toCharArray()) {
            print += c + " ";
        }
        return print;
    }

    /**
     * Returns letters that are already gussed
     * @return String letters guessed
     */
    public String getLettersWhichAreGuessed()
    {
        String print = "";
        for (String string : lettersWhichAreGuessed) {
            print += string + " ";
        }
        return print;
    }
}

/**
 * This class is responsible to maintain
 * points of a player based on right and
 * wrong guess
 *
 * @author Abhishek Indukumar
 * 
 */
class Points {
    private static final int RIGHT_GUESS_POINTS = 10;
    private static final int WRONG_GUESS_DEDUCTION = 5;

    private int points = 0;

    /**
     * Invoked on right guess of a word
     * adds 'RIGHT_GUESS_POINTS' points
     */
    public void rightGuess() {
        points += RIGHT_GUESS_POINTS;
    }

    /**
     * Invoked on wrong guess of a word
     * subtracts 'WRONG_GUESS_DEDUCTION' points
     */
    public void wrongGuess() {
        if (points > 0)
            points -= WRONG_GUESS_DEDUCTION;
    }

    /**
     * Returns points
     * @return points
     */
    public int getPoints() {
        return points;
    }
}

/**
 * This Class contains collection of players
 *
 *
 * @author Abhishek Indukumar
 * 
 */
class Players {
    private Vector<Player> players = new Vector<Player>();

    /**
     * Creates player objects from player
     * name array
     * @param playersNames Array of player names.
     */
    public Players(String[] playersNames) {
        for (String player : playersNames) {
            players.add(new Player(player));
        }
    }

    /**
     * Gets vector of all players
     * @return All players
     */
    public Vector<Player> getPlayers() {
        return players;
    }
}

/**
 * This class has all properties required for
 * player
 *
 * @author Abhishek Indukumar
 * 
 */
class Player {
    private String name;
    private Points points = new Points();

    public Player(String name) {
        this.name = name;
    }

    /**
     * Returns player name
     * @return player name
     */
    public String getName() {
        return name;
    }


    /**
     * Invoked on right guess of
     * words
     *
     * Adds points
     */
    private void rightGuess() {
        points.rightGuess();
    }

    /**
     * Invoked on wrong guess of
     * word
     *
     * Deducts points
     */
    private void wrongGuess() {
        points.wrongGuess();
    }

    /**
     * Invokes rightGuess if guessStatus is WHOLE_WORD_GUESSED
     * or invokes wrongGuess if guessStatus is EXCEED_GUESS_LIMIT
     * @param guessStatus
     */
    public void setGuessStatus(String guessStatus) {
        if (guessStatus.equals(Word.WHOLE_WORD_GUESSED)) {
            rightGuess();
        } else if (guessStatus.equals(Word.EXCEED_GUESS_LIMIT)) {
            wrongGuess();
        }
    }

    /**
     * Get points of the player
     * @return points
     */
    public int getPoints() {
        return points.getPoints();
    }
}