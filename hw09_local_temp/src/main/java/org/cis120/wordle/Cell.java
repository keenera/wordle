package org.cis120.wordle;
import java.awt.*;

public class Cell {
    private char contents;
    private CellGuess accuracy;
    public static final Color LETTER_IN_SPOT = new Color(108,172,100);
    public static final Color LETTER_IN_WORD = new Color(204,180,84);
    public static final Color LETTER_NOT_IN_WORD = new Color(124,124,124);
    public static final Color LETTER_NOT_GUESSED = Color.BLACK;
    public static final int CELL_WIDTH = GameBoard.BOARD_WIDTH / 5 - 4;
    public static final int CELL_HEIGHT = GameBoard.BOARD_WIDTH / 5 - 4;


    public Cell() {
        contents = ' ';
        accuracy = CellGuess.NOT_GUESSED;
    }

    public Cell(char contents, CellGuess accuracy) {
        this.contents = contents;
        this.accuracy = accuracy;
    }

    // getter method for cell contents
    public char getContents() {
        return contents;
    }

    // getter method for accuracy of cell
    public CellGuess getAccuracy() {
        return accuracy;
    }

    /** changes cell contents and accuracy based on a
     * guess.
     * */
    public void guess(CellGuess c, String letter) {
        String l = letter.toUpperCase();
        contents = l.charAt(0);
        accuracy = c;
    }

    /** changes cell contents, but not accuracy, based
     * on the preview on the board.
     * */
    public void preview(String letter) {
        String l = letter.toUpperCase();
        contents = l.charAt(0);
    }

    /** specifies how to draw the board.
     * */
    public void draw(Graphics g, int x, int y) {
        int xCorner = x + 2;
        int yCorner = y + 2;
        if (accuracy == CellGuess.IN_SPOT) {
            g.setColor(LETTER_IN_SPOT);
        } else if (accuracy == CellGuess.IN_WORD) {
            g.setColor(LETTER_IN_WORD);
        } else if (accuracy == CellGuess.NOT_IN_WORD) {
            g.setColor(LETTER_NOT_IN_WORD);
        } else {
            g.setColor(LETTER_NOT_GUESSED);
        }
        g.fillRect(xCorner, yCorner, CELL_WIDTH, CELL_HEIGHT);
        g.setColor(Color.WHITE);
        g.setFont(new Font("Helvetica", Font.BOLD, 50));
        g.drawString(contents + "", x + 25,y + 60);
    }

    @Override
    public boolean equals(Object c) {
        if (c == null) {
            return false;
        }
        if (c instanceof Cell ce) {
            return contents == ce.getContents()
                    && accuracy == ce.getAccuracy();
        }
        return false;
    }
}
