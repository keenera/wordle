package org.cis120.wordle;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * This class instantiates a Wordle object, which models the game.
 * As the user types and makes guesses, the model is updated. Whenever the
 * model is updated, the game board is repainted and the status JLabel is
 * updated to reflect the number of guesses left. If the user tries to guess
 * an invalid word (i.e. one that's not five letters long or not in the
 * game's dictionary), they'll be notified via that status JLabel. When the
 * game is over - either because the user won or they made it to 6 guesses -
 * the status JLabel reveals the answer.
 * 
 * This game adheres to a Model-View-Controller design framework.
 * GameBoard stores the model as a field and acts as both the controller
 * (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private Wordle wordle; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 405;
    public static final int BOARD_HEIGHT = 485;

    private static final String AUTO_PATH =
            "/Users/audreykeener/Downloads/hw09_local_temp/files";
    private static String path = "/Users/audreykeener/Downloads/hw09_local_temp";

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        wordle = new Wordle("files/dictionarywords.csv",0,
                "files/wordlewords.csv",5); // initializes model for the game
        status = statusInit; // initializes the status JLabel

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);

                char l = e.getKeyChar();
                if (!wordle.gameOver()) {
                    status.setText("");

                    if (l == KeyEvent.VK_ENTER || l == '\r') { // if enter pressed
                        boolean successfulTurn =
                                wordle.playWord(wordle.getPreview());
                        if (successfulTurn) {
                            updateStatus();
                        } else {
                            status.setText("Not a valid word. " +
                                    "You have " + wordle.getNumGuessesLeft() +
                                    " guesses left.");
                        }
                    } else if (l == KeyEvent.VK_DELETE ||
                            l == KeyEvent.VK_BACK_SPACE) { // if delete pressed
                        wordle.backspacePreview();
                        updateStatus();
                    } else if ((l >= 'a' && l <= 'z') ||
                            (l >= 'A' && l <= 'Z')) {
                        wordle.addToPreview(l);
                        updateStatus();
                    } else {
                        updateStatus();
                    }

                    repaint();
                }
            }
        });
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        wordle.newGame();
        updateStatus();
        repaint();

        // Makes sure this component has keyboard/mouse focus
        requestFocusInWindow();
    }

    public void undo() {
        wordle.undo();
        if (!wordle.gameOver()) {
            updateStatus();
        }
        repaint();

        requestFocusInWindow();
    }

    public void save() {
        JOptionPane.showMessageDialog(null,
                "Your game file was saved to the Files folder.\n" +
                        "Any unentered guesses were submitted.");
        wordle.writeToFile();
        repaint();
        requestFocusInWindow();
    }

    public String selectFile() {
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new java.io.File(AUTO_PATH));
        fc.setDialogTitle("Select a Game File");
        int value = fc.showOpenDialog(null);
        if (value == JFileChooser.APPROVE_OPTION) {
            path = fc.getSelectedFile().getAbsolutePath();
        }
        requestFocusInWindow();
        return path;
    }

    public void load() {
        wordle.loadGame(selectFile());
        updateStatus();
        repaint();
        requestFocusInWindow();
    }

    public void openInstructions() {
        String instruct = "Welcome to Wordle, 2022's favorite word game!" +
                "\n\nInstructions:\n\nYou have six tries to guess" +
                " the Wordle. Each guess must be a valid five-letter word." +
                "\nUse your keyboard to type out a guess. When you're ready " +
                "to submit the guess, hit enter.\n\nAfter a guess, the color" +
                " of the cells will change to show how close your guess is" +
                " to the Wordle:" +
                "\n     Green means the letter in that cell is in the Wordle" +
                " and in the right spot." +
                "\n     Yellow means the letter in that cell is in the" +
                " Wordle, but it's not in the right spot." +
                "\n     Gray means the letter in that cell isn't in the " +
                "Wordle in any spot." +
                "\n\nHit the Undo button to remove a guess " +
                "(okay, maybe that's cheating in the real game) and the New" +
                " Game button to restart with a new Wordle." +
                "\nYou can save a certain game with the Save Game button," +
                " or load one onto the board with the Load Saved Game button." +
                "\n\nClick OK when you're ready to play!";
        JOptionPane.showMessageDialog(null, instruct, "Welcome to Wordle!",
                JOptionPane.PLAIN_MESSAGE);
        requestFocusInWindow();
    }

    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        if (wordle.gameOver()) {
            String s = "Answer: " + wordle.getAnswer();
            if (wordle.hasWon()) {
                s += ". You win!";
            }
            status.setText(s);
        } else {
            status.setText("You have " + wordle.getNumGuessesLeft() + " guesses left.");
        }
    }

    /**
     * Draws the game board.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        wordle.draw(g, BOARD_WIDTH, BOARD_HEIGHT);
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
