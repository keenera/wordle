package org.cis120.wordle;
import java.awt.*;
import java.io.*;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Set;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Wordle {
    private Cell[][] board; // 2D Arrays

    // collections
    private LinkedList<Cell[][]> boardHistory;
    private final Set<String> dictionary;
    private final Set<String> allAnswers;
    private LinkedList<String> guesses;

    private String answer;
    private String preview;

    /**
     * Creates a Wordle object (which models a Wordle game).
     *
     * @param dictFilepath - A path to the file containing the words the user
     * is allowed to guess.
     * @param dictCol - The index of the column in the dictionary CSV file
     * that contains the words.
     * @param ansFilepath - A path to the file containing the words that can
     * possibly be the Wordle answer.
     * @param ansCol - The index of the column in the CSV file of answers
     * that contains the answers.
     */
    public Wordle(String dictFilepath, int dictCol, String ansFilepath,
                  int ansCol) {
        dictionary = WordListParser.csvDataToWords(
                FileLineIterator.fileToReader(dictFilepath), dictCol);
        allAnswers = WordListParser.csvDataToWords(
                FileLineIterator.fileToReader(ansFilepath), ansCol);
        newGame();
    }

    /**
     * Creates a Wordle object (which models a Wordle game).
     *
     * @param dict - A set of Strings representing the full dictionary of
     * words a user is allowed to guess.
     * @param ans - A set of Strings representing possible answers for
     * a Wordle game.
     */
    public Wordle(Set<String> dict, Set<String> ans) {
        dictionary = WordListParser.cleanWordSet(dict);
        allAnswers = WordListParser.cleanWordSet(ans);
        newGame();
    }

    /**
     * Resets the Wordle object by clearing the board, the guess list, and
     * the board history. Selects an answer for the Wordle at random from the
     * list of possible answers.
     */
    public void newGame() {
        board = new Cell[][]{
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()}};
        guesses = new LinkedList<>();
        answer = (String) allAnswers.toArray()[(int)
                (Math.random() * allAnswers.size())];
        boardHistory = new LinkedList<>();
        boardHistory.add(new Cell[][]{
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()}});
        preview = "";
    }

    /**
     * Plays a turn by checking a guess against the answer and updating the
     * board accordingly. Returns true if the turn was successfully executed.
     * If the guess is a word not included in the dictionary set or the game
     * is over, returns false.
     *
     * @param g - String representing the guess
     * @return a boolean representing whether the attempt to play the word was
     * successful.
     */
    public boolean playWord(String g) {
        String word = g.toUpperCase();
        if (dictionary.contains(word) && !gameOver()) {
            guesses.add(word);
            Cell[][] b = new Cell[6][5];
            for (int row = 0; row < b.length; row++) {
                for (int col = 0; col < b[row].length; col++) {
                    Cell c = board[row][col];
                    if (c.getAccuracy() != CellGuess.NOT_GUESSED) {
                        b[row][col] = new Cell(c.getContents(),
                                c.getAccuracy());
                    } else {
                        b[row][col] = new Cell();
                    }
                }
            }
            boardHistory.add(b);

            for (int i = 0; i < 5; i++) {
                String w = word.charAt(i) + "";
                Cell cell = board[guesses.size() - 1][i];
                CellGuess a;
                if (word.charAt(i) == answer.charAt(i)) {
                    a = CellGuess.IN_SPOT;
                } else if (answer.contains(w)) {
                    a = CellGuess.IN_WORD;
                } else {
                    a = CellGuess.NOT_IN_WORD;
                }
                cell.guess(a, w);
            }

            preview = "";
            return true;
        }
        return false;
    }

    /* getter method for the preview String (the guess the user is
    in the process of making) */
    public String getPreview() {
        return preview;
    }

    // getter method for the answer
    public String getAnswer() {
        return answer;
    }

    // getter method for the board 2D array (for testing)
    public Cell[][] getBoard() {
        Cell[][] b = new Cell[6][5];
        for (int row = 0; row < b.length; row++) {
            for (int col = 0; col < b[row].length; col++) {
                Cell c = board[row][col];
                if (c.getAccuracy() != CellGuess.NOT_GUESSED) {
                    b[row][col] = new Cell(c.getContents(),
                            c.getAccuracy());
                } else {
                    b[row][col] = new Cell();
                }
            }
        }
        return b;
    }

    // getter method for the number of guesses left
    public int getNumGuessesLeft() {
        return 6 - guesses.size();
    }

    /**
     * Deletes from the preview and updates the board accordingly.
     */
    public void backspacePreview() {
        if (preview.length() >= 1) {
            preview = preview.substring(0, preview.length() - 1);
            board[guesses.size()][preview.length()].preview(" ");
        }
    }

    /**
     * Adds the given character to the preview and updates the
     * board accordingly.
     *
     * @param c - char to be added to preview
     */
    public void addToPreview(char c) {
        if (!gameOver()) {
            if (preview.length() < 5) {
                preview += c + "";
            }
            for (int i = 0; i < preview.length(); i++) {
                board[guesses.size()][i].preview(preview.charAt(i) + "");
            }
        }
    }

    /**
     * Returns whether the game is over by checking that the user
     * has either won or used all their guesses.
     *
     * @return a boolean representing whether the game is over.
     */
    public boolean gameOver() {
        return hasWon() || (guesses.size() == 6);
    }

    /**
     * Returns whether the player has won by checking if the
     * player has guessed the answer.
     *
     * @return a boolean representing whether the game has been won.
     */
    public boolean hasWon() {
        return guesses.contains(answer);
    }

    /**
     * Undoes the most recent turn.
     */
    public void undo() {
        preview = "";
        if (boardHistory.size() > 1 && !gameOver()) {
            board = boardHistory.removeLast();
            guesses.removeLast();
        }
    }

    // File I/O

    /**
     * Returns a String representation of the board.
     *
     * @return a String containing the contents of the board.
     */
    public String boardString() {
        String state = "";
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                state += board[row][col].getContents();
            }
            state += "\n";
        }
        return state;
    }

    /**
     * Writes the String representing board state to a file.
     */
    public void writeToFile() {
        BufferedWriter writer = null;
        try {
            String fileName = "files/" +
                    new SimpleDateFormat("YYYY-MM-dd-HH-mm-ss").format(new Date());
            writer = new BufferedWriter(
                    new OutputStreamWriter(
                    new FileOutputStream(fileName+".txt"), "utf-8"));
            writer.write(answer + "\n");
            writer.write(boardString());
            writer.flush();
            writer.close();
        }
        catch (IOException e) {
            System.out.println("Unable to write game state to file.");
        }
    }

    /**
     * Uploads a file containing information about the board and
     * sets up the game with its information.
     *
     * @return boolean representing whether the upload was successful.
     */
    public boolean loadGame(String filepath) {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(filepath));
            board = new Cell[][]{
                    {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                    {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                    {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                    {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                    {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()},
                    {new Cell(), new Cell(), new Cell(), new Cell(), new Cell()}};
            answer = reader.readLine();
            preview = "";
            guesses.clear();
            for (int row = 0; row < 6; row++) {
                String guess = reader.readLine();
                if (guess != null) {
                    playWord(guess);
                } else {
                    for (int col = 0; col < 5; col++) {
                        board[row][col] = new Cell();
                    }
                }
            }

            return true;
        }
        catch (FileNotFoundException e) {
            System.out.println("File wasn't found.");
        }
        catch (IOException e) {
            System.out.println("Couldn't read from the file.");
        }
        catch (IllegalArgumentException e) {
            System.out.println("You tried to load an invalid file.");
        }
        return false;
    }

    /**
     * Specifies how the model is drawn.
     */
    // for drawing the model in GameBoard.java...
    public void draw(Graphics g, int width, int height) {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                board[row][col].draw(g, col * (width - 5) / 5,
                        row * (height - 5) / 6);

            }
        }
    }
}
