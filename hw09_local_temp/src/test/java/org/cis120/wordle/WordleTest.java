package org.cis120.wordle;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

public class WordleTest {
    // some baseline tests for constructor & newGame
    @Test
    public void testAnswerComesFromAnswerSet() {
        Set<String> dict = new TreeSet<>();
        dict.add("please");
        dict.add("hello");
        dict.add("slays");
        Set<String> ans = new TreeSet<>();
        ans.add("please");
        ans.add("hello");
        ans.add("slays");
        Wordle w = new Wordle(dict, ans);
        String a = w.getAnswer();
        assertTrue(a.equals("PLEASE") || a.equals("HELLO") ||
                a.equals("SLAYS"));
    }

    @Test
    public void testConstructorStartsNewGame() {
        Set<String> dict = new TreeSet<>();
        dict.add("please");
        dict.add("hello");
        dict.add("slays");
        Set<String> ans = new TreeSet<>();
        ans.add("please");
        ans.add("hello");
        ans.add("slays");
        Wordle w = new Wordle(dict, ans);
        Cell[][] c = w.getBoard();
        for (int row = 0; row < c.length; row++) {
            for (int col = 0; col < c[row].length; col++) {
                assertEquals(c[row][col], new Cell());
            }
        }
        assertEquals(w.getNumGuessesLeft(), 6);
    }

    // Tests for playWord
    @Test
    public void testPlayWordValidFiveLetters() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertTrue(w.playWord("short"));
        assertEquals(w.getPreview(), "");
        Cell[][] c = w.getBoard();
        assertEquals(c[0][0], new Cell('S', CellGuess.IN_SPOT));
        assertEquals(c[0][1], new Cell('H', CellGuess.NOT_IN_WORD));
        assertEquals(c[0][2], new Cell('O', CellGuess.NOT_IN_WORD));
        assertEquals(c[0][3], new Cell('R', CellGuess.IN_WORD));
        assertEquals(c[0][4], new Cell('T', CellGuess.NOT_IN_WORD));
        for (int i = 1; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], new Cell());
            }
        }
        assertEquals(w.getNumGuessesLeft(), 5);
    }

    @Test
    public void testPlayWordNotInDictionary() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertFalse(w.playWord("shart"));
        assertEquals(w.getPreview(), "");
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], new Cell());
            }
        }
        assertEquals(w.getNumGuessesLeft(), 6);
    }

    @Test
    public void testPlayWordMoreThan6Times() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        dict.add("shear");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertTrue(w.playWord("hello"));
        assertTrue(w.playWord("pleas"));
        assertTrue(w.playWord("pleas"));
        assertTrue(w.playWord("pleas"));
        assertTrue(w.playWord("pleas"));
        assertTrue(w.playWord("pleas"));
        assertFalse(w.playWord("pleas"));
        Cell[][] c = w.getBoard();
        assertEquals(w.getPreview(), "");
        assertEquals(c[0][0], new Cell('H', CellGuess.NOT_IN_WORD));
        assertEquals(c[0][1], new Cell('E', CellGuess.IN_WORD));
        assertEquals(c[0][2], new Cell('L', CellGuess.NOT_IN_WORD));
        assertEquals(c[0][3], new Cell('L', CellGuess.NOT_IN_WORD));
        assertEquals(c[0][4], new Cell('O', CellGuess.NOT_IN_WORD));
        for (int i = 1; i < 5; i++) {
            assertEquals(c[i][0], new Cell('P', CellGuess.IN_WORD));
            assertEquals(c[i][1], new Cell('L', CellGuess.NOT_IN_WORD));
            assertEquals(c[i][2], new Cell('E', CellGuess.IN_SPOT));
            assertEquals(c[i][3], new Cell('A', CellGuess.IN_SPOT));
            assertEquals(c[i][4], new Cell('S', CellGuess.IN_WORD));
        }
        assertEquals(w.getNumGuessesLeft(), 0);
        assertTrue(w.gameOver());
    }

    @Test
    public void testPlayWordIncludingAnswer() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        dict.add("shear");
        Set<String> ans = new TreeSet<>();
        ans.add("pleas");
        Wordle w = new Wordle(dict, ans);
        assertTrue(w.playWord("hello"));
        assertTrue(w.playWord("pleas"));
        assertFalse(w.playWord("hello"));
        Cell[][] c = w.getBoard();
        assertEquals(w.getPreview(), "");
        assertEquals(c[0][0], new Cell('H', CellGuess.NOT_IN_WORD));
        assertEquals(c[0][1], new Cell('E', CellGuess.IN_WORD));
        assertEquals(c[0][2], new Cell('L', CellGuess.IN_WORD));
        assertEquals(c[0][3], new Cell('L', CellGuess.IN_WORD));
        assertEquals(c[0][4], new Cell('O', CellGuess.NOT_IN_WORD));
        assertEquals(c[1][0], new Cell('P', CellGuess.IN_SPOT));
        assertEquals(c[1][1], new Cell('L', CellGuess.IN_SPOT));
        assertEquals(c[1][2], new Cell('E', CellGuess.IN_SPOT));
        assertEquals(c[1][3], new Cell('A', CellGuess.IN_SPOT));
        assertEquals(c[1][4], new Cell('S', CellGuess.IN_SPOT));
        for (int i = 2; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], new Cell());
            }
        }
        assertTrue(w.gameOver());
    }

    @Test
    public void testPlayWordShorterThanFive() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertFalse(w.playWord("shar"));
        assertEquals(w.getPreview(), "");
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], new Cell());
            }
        }
        assertEquals(w.getNumGuessesLeft(), 6);
    }

    @Test
    public void testPlayWordLongerThanFive() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertFalse(w.playWord("sharts"));
        assertEquals(w.getPreview(), "");
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], new Cell());
            }
        }
        assertEquals(w.getNumGuessesLeft(), 6);
    }

    // Tests for addToPreview
    @Test
    public void testAddToPreviewEmptyPreview() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertEquals(w.getPreview(), "");
        w.addToPreview('C');
        assertEquals(w.getPreview(), "C");
    }

    @Test
    public void testAddToPreviewMultipleTimes() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertEquals(w.getPreview(), "");
        w.addToPreview('H');
        w.addToPreview('E');
        w.addToPreview('L');
        w.addToPreview('L');
        assertEquals(w.getPreview(), "HELL");
    }

    @Test
    public void testAddToPreviewGameOver() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertEquals(w.getPreview(), "");
        assertTrue(w.playWord("spear"));
        w.addToPreview('H');
        w.addToPreview('E');
        w.addToPreview('L');
        w.addToPreview('L');
        assertEquals(w.getPreview(), "");
    }

    @Test
    public void testAddToPreviewOver5Letters() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        assertEquals(w.getPreview(), "");
        w.addToPreview('H');
        w.addToPreview('E');
        w.addToPreview('L');
        w.addToPreview('L');
        w.addToPreview('O');
        w.addToPreview('L');
        assertEquals(w.getPreview(), "HELLO");
    }

    // Tests for backspacePreview
    @Test
    public void testBackspacePreviewEmptyPreview() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.backspacePreview();
        assertEquals(w.getPreview(), "");
    }

    @Test
    public void testBackspacePreviewPreviewLength1() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.addToPreview('A');
        assertEquals(w.getPreview(), "A");
        w.backspacePreview();
        assertEquals(w.getPreview(), "");
    }

    @Test
    public void testBackspacePreviewNonemptyPreview() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.addToPreview('A');
        w.addToPreview('A');
        assertEquals(w.getPreview(), "AA");
        w.backspacePreview();
        assertEquals(w.getPreview(), "A");
    }

    // Tests for undo
    @Test
    public void testUndoCalledOnBoardWithNoGuesses() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.undo();
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], new Cell());
            }
        }
        assertEquals(w.getNumGuessesLeft(), 6);
    }

    @Test
    public void testUndoCalledMultipleTimes() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.playWord("hello");
        Cell[][] c1 = w.getBoard();
        w.playWord("short");
        w.playWord("pleas");
        w.undo();
        w.undo();
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], c1[i][j]);
            }
        }
        assertEquals(w.getNumGuessesLeft(), 5);
    }

    @Test
    public void testUndoCalledOnBoardWithOneGuess() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.playWord("short");
        w.undo();
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], new Cell());
            }
        }
        assertEquals(w.getNumGuessesLeft(), 6);
    }

    @Test
    public void testUndoCalledOnBoardWithMultipleGuesses() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.playWord("hello");
        w.playWord("short");
        Cell[][] c1 = w.getBoard();
        w.playWord("pleas");
        w.undo();
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], c1[i][j]);
            }
        }
        assertEquals(w.getNumGuessesLeft(), 4);
    }

    @Test
    public void testUndoCalledWhileGameOver() {
        Set<String> dict = new TreeSet<>();
        dict.add("pleas");
        dict.add("hello");
        dict.add("slays");
        dict.add("spear");
        dict.add("short");
        Set<String> ans = new TreeSet<>();
        ans.add("spear");
        Wordle w = new Wordle(dict, ans);
        w.playWord("hello");
        w.playWord("spear");
        Cell[][] c1 = w.getBoard();
        w.undo();
        Cell[][] c = w.getBoard();
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 5; j++) {
                assertEquals(c[i][j], c1[i][j]);
            }
        }
    }

}
