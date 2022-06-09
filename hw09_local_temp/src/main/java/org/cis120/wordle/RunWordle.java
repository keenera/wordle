package org.cis120.wordle;

import javax.swing.*;
import java.awt.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 * 
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 * 
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunWordle implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Wordle");
        frame.setLocation(300, 300);

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        // Game board
        final org.cis120.wordle.GameBoard board = new GameBoard(status);
        frame.add(board, BorderLayout.CENTER);

        // JPanel for all control buttons
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Reset/new game button
        final JButton newGame = new JButton("New Game");
        newGame.addActionListener(e -> board.reset());
        control_panel.add(newGame);

        // Undo button
        final JButton undo = new JButton("Undo");
        undo.addActionListener(e -> board.undo());
        control_panel.add(undo);

        // Save game button
        final JButton save = new JButton("Save Game");
        save.addActionListener(e -> board.save());
        control_panel.add(save);

        // Load game button
        final JButton load = new JButton("Load a Saved Game");
        load.addActionListener(e -> board.load());
        control_panel.add(load);

        // Instruction pop-up button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> board.openInstructions());
        control_panel.add(instructions);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game. Happy Wordle-ing :)
        board.reset();
    }
}