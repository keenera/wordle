=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: _______
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. File I/O - Saving the game state to a file and uploading it to use later
  (which uses BufferedWriters, properly handles exceptions, parses and uploads
  correctly, and stores a sufficient amount of data that is persistent)

  2. Collections - Model class (Wordle.java) has a LinkedList, which is
  a collection, containing the history of the board's contents. This is used
  to implement undo. I got full approval for this proposal.

  3. 2D Arrays - the board in the model class (Wordle.java) is represented
  by a 2D Array of cell objects, which is appropriate because they're laid out in
  rows and columns. I access and iterate over elements properly.
  I got full approval for this proposal.

  4. JUnit testing - I have exhaustive and unique test cases for basically all
  methods in my Wordle.java class and their side effects. This is what got
  approved in my proposal.

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.

RunWordle.java - Swing layout for game, adds handlers for buttons, etc.
GameBoard.java - Handles events using a Wordle model
Wordle.java - Contains the model for the game (which has fields including
the board, the board and guess history, etc.) and the methods that can
be applied to it.
Cell.java - Contains information for a given cell in the game (its guess
accuracy and the letter it contains)

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?

It was very difficult to implement file I/O because I ended up realizing that
my proposed implementation of this concept wouldn't be sufficient to earn
points on the rubric. So, in addition to the proposal I made (reading in from
files of words to make the dictionary and answer sets), I decided to implement
saving and loading game state - which was hard because I didn't know how to
program file-saving until I looked at the official Java documentation.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?

I think have a good separation of functionality - i.e. the implementation of
drawing the Wordle model is separated into different classes (Wordle.java and
Cell.java). So is the implementation of guessing the word and updating the
board accordingly - which is done in response to keyboard events detected
and handled in GameBoard.java.

I also think the private state is encapsulated well - for instance, I was
careful  to make copies of object types/collections before returning them in
getter methods, in order to protect from changes being made to the returned
data that would alter the internal state of the model.

If given the time, I would want to refactor my playWord() function - it seems
inefficient to copy the current board and *then* make the new guess (lots of
loops); perhaps there's a way to change the logic to have only one or two loops.

========================
=: External Resources :=
========================

- Cite any external resources (images, tutorials, etc.) that you may have used 
  while implementing your game.

Oracle's online Java documentation.