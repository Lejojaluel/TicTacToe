/**
 * The main class for Tic Tac Toe game that houses all the functions. This is a one off class
 * that only requires the main driver.
 *
 * @author Leroy Valencia
 * @version 2.0
 */
import java.util.ArrayList;
import java.util.Scanner;
/** The main Class TicTacToe. */
public class TicTacToe {
	//INSTANCE VARIABLES
	private boolean verbose = true;
	private int lastX = 0, lastY = 0;
	private int unwinnableCount = 0 , turnCount = 0, minTurnToWin=0;
	private int nRows=0, nCols=0, state=0;
	private int minTurnToDraw, minUnwinnableCount;
	private int	option = 10;
	private String currentTurn="";
	private BlockType[][] gameArr;

  /**
   *  enum Block type that has the X, O and EMPTY for each cell.
   */
  public enum BlockType {
    /** X block type. */
    X,
    /** O block type. */
    O,
    /** Empty block type. */
    EMPTY;

    /**
     * This method returns the string equivalency of the BlocktypeB.
     *
     * @param b the blocktype that is requested to be a string.
     * @return the string that is associated with the param b.
     */
    public static String toString(BlockType b) {
			//Switch case is here to give the appropriate string. Better then ifelseifs.
			switch(b){
				case O:
					return " O ";
				case X:
					return " X ";
				case EMPTY:
					return "   ";
				default:
					//Impossible Block Type but throws error if it ever comes across it.
					System.err.println("Impossible Block Type");
					System.exit(1);
			}
			return "Impossible";
		}
	}

  /**
   * The enum Status type that holds the game statuses WIN, DRAW, CONTINUE.
   */
  public enum StatusType {
    /** Win status type. */
    WIN,
    /** Draw status type. */
    DRAW,
    /** Continue status type. */
    CONTINUE;

    /**
     * This method returns the string value of the StatusType s that is given.
     *
     * @param s the statusType that needs to be converted to a string.
     * @return the string that is associated with StatusType s.
     */
    public static String toString(StatusType s) {
    		//Switch case is here to give the appropriate string. Better then ifelseifs.
			switch(s){
				case WIN:
					return "Win";
				case DRAW:
					return "Draw";
				case CONTINUE:
					return "Continue";
				default:
					//Impossible StatusType but throws error if it ever comes across it.
					System.err.println("Impossible status value in Status.toString() " + s);
					System.exit(1);
			}
			return "Impossible";
		}
	}

  /*
  The Block type. This is a constructor to use for BlockTypes
  The Game status. This is a constructor to use for StatusType. It it initiated as StatusType.CONTINUE
  because by default we want the game to continue.
  */
  BlockType blockType;
  StatusType gameStatus = StatusType.CONTINUE;

  /**
   * Main Constructor for TicTacToe.
   *
   * This starts the main menu gets user input for board size and populates the gameArr to empty.
   */
  public TicTacToe() {
		System.out.println("\n======================================");
		System.out.println("——————— Welcome to Tic-Tac-Toe ———————");
		System.out.println("======================================\n");

		Scanner sc = new Scanner(System.in); // Input scanner for menu options
		state = 10; // I had to set state = to something other then 0 because I didn't want the default to be 3x3.
	  	/*
	  	* There is a while loop here with a state to be able to keep asking for a menu option instead of just closing
	  	* the program which would make playing the game frustrating if the menu closed everytime you made a wrong choice
	    */
		while (state == 10) {
			/*This try catch validates the initial input for the menu. A number in the range of menu items, and catches
			the inputmismatch if you enter a letter or string.*/
			try{
				System.out.printf("\nPlease select an option of length for the playing field.\n0 for 3x3\n1 for 5x5\n2 for 7x7\n");
				option = sc.nextInt();
			}catch(Exception e){
				sc.next();
				System.err.println("Please enter a number option only!\n");
			}
			state = validateOption(option); // Depends on validateOption will continue in while or end while.
			//option = 0; //bypass for testing
		}

		verbose(nCols +" , "+nRows); //see verbose method at bottom.
		//Update Instance array with inputted variables. Better to have the array at instance level to have universal access
		gameArr = new BlockType[nCols][nRows];

		//Fill the initial array full of Block objects that have the enumerated status type EMPTY
		for(int i = 0; i < nCols; i ++){
			for (int j = 0; j < nRows; j++) {
				gameArr[i][j] = BlockType.EMPTY;
				verbose(gameArr[i][j].toString()); //Prints each block while its being filled.
			}
		}
	}

  /**
   * When called this method prints the board to the screen. It has a combination of loops and if statements
   * to make sure that the board can be universal size to nRows.
   */
  public void printBoard() {
		int rowsCounter = nRows - 1; //To label access of rows.
	    //Loop goes for as many rows. Main board loop. Most of these loops I tried to keep i as nCols and j as nRows
		for (int j = 0; j < nRows; j++){
			//Prints the middle values and the array values
			for (int i = 0; i < nCols; i++) {
				if(i == 0){ //For beginning case of the board
					System.out.print("\n"+ rowsCounter +" "+blockType.toString(gameArr[i][rowsCounter]));
				}else {
					System.out.print("|"+blockType.toString(gameArr[i][rowsCounter])); //Print the string value of gameArr
				}
			}

			System.out.println(""); //The next line to print the bottom line

			//Prints the bottom lines all except lowest line
			for (int i = 0; i < nCols; i++) {
				if(i == 0 && rowsCounter != 0) {
					System.out.print("  ———|");
				}else{
					if(rowsCounter == 0){ //ignores lower line.
					}else{
						if(i == nCols -1){ //ignores the | at the right side
							System.out.print("————");
						}else{
						System.out.print("———|");
						}
					}
				}
			}
			rowsCounter--;
		}

		//Prints lower (x) coordinate numbers
		for (int k = 0; k < nCols; k++) {
			if(k == 0){
				System.out.print("   "+k);
			}else{
				System.out.print("   "+k);
			}
		}
	}

  /**
   * This is the main game loop that is ran to play the game. It is ended by changing the
   * gameStatus.setGameStatus(StatusType). It will continue to run as log as gameStatus == StatusType.continue
   */
  public void play() {
		String whoIsNextTurn = "player1"; // Necessary to see who goes first and for switching players.
		while(gameStatus == StatusType.CONTINUE){
			printBoard();
			/*
			This is the flip flop switch for the players. Default is player1 who is X
			who also goes first. playerTurn happens and then whoIsNextTurn is changed to player2
			so when the loops comes back it then goes to player 2 instead of player1. This just keeps
			repeating for the entirety of the while loop. It also sets who has the current turn for checkWinning()
			later
			*/
			if(whoIsNextTurn == "player1"){
				System.out.println("\nPlayer 1's Turn");
				playerTurn("player1");
				currentTurn = "player1";
				whoIsNextTurn = "player2";
			}else if(whoIsNextTurn == "player2"){
				System.out.println("\nPlayer 2! Your Turn");
				playerTurn("player2");
				currentTurn = "player2";
				whoIsNextTurn = "player1";
			}

			/*
			No one can win tic tac toe if the games hasn't been played for 3 turns.
			I made this to be dynamic to the grid size. In our case the number of turns minimum for player1
			to get 3 X's in a row is 5 total turns. (counting 1 turn as player1 and 2 turns as player2)
			see validateOption to see what these fixed values are.
			 */
			turnCount++; //Keeps track of how many turns we've been through
			verbose(turnCount);
			if(turnCount < minTurnToWin){}else if(turnCount >= minTurnToWin){
				/*
				This is after 5 turns, then finally start checking for winning combinations. This isn't necessary
				but I felt it was easy enough to integrate to save some refreshing time.
				I used a switch case here which was prettier then using a bunch of if(){}elseif's
				*/
				switch (checkWinning(currentTurn)){
					case "player1":
						printBoard(); //I wanted to print the board everytime before it ended the game
						System.out.println("\n\nCongrats Player 1 has won!"); // Congrats message
						gameStatus = StatusType.WIN; // Sets the gameStatus to WIN to end the while loop
						System.out.println("\nGame Status= " + getGameStatus());
						break;
					case "player2":
						printBoard();
						System.out.println("\n\nCongrats Player 2 has won");
						gameStatus = StatusType.WIN;
						System.out.println("\nGame Status= " + getGameStatus());
						break;
					case "draw":
						printBoard();
						System.out.println("\n\nIt is a TIE. Sorry better luck next time!");
						gameStatus = StatusType.DRAW;
						System.out.println("\nGame Status= " + getGameStatus());
						break;
					case "cont":
						gameStatus = StatusType.CONTINUE; //This is most common which will just continue the game
						//This is for EXTRA CREDIT to check if the game is unwinnable.
						System.out.println("\nGame Status= " + getGameStatus());
						if(turnCount > 1){
							checkWinnable();
						}
						break;
					default:
						//This is the impossible fail catch jic
						System.err.println("Impossible outcome of gameStatus.");
						System.exit(1);
				}
			}
		}
	}

  /**
   * This is the method that is called for each player. Both players do the same exact thing so no
   * need for separate methods.
   *
   * @param player this is the player that has the current turn
   */
  public void playerTurn(String player) {
		Scanner ls = new Scanner(System.in); //Scanner to introduce the input of coords
		state = 0;
		/*
		I put the coordinates in a while loop because I didn't want the whole program to end
		just because we enter one wrong number. So the while loop doesn't proceed until it gets good
		coordinates which are validated by their methods validMoveCoordinate and validateMove
		 */
		while(state == 0) {
			System.out.println("Enter an x coordinate: ");
			lastX = ls.nextInt();
			if(validMoveCoordinate(lastX)) { //I nested the if statements to allow for one coordinate to be validated at a time
				System.out.println("Enter a y coordinate: ");
				lastY = ls.nextInt();
				if (validMoveCoordinate(lastY)) {
					if (validateMove(lastX, lastY)) { //validates if there is already a marking there.
						state = 1; //Finally breaks the loop after all criteria has been met.
					}
				}
			}
		}
		/*
		After this the validated coords and player get handed off to updateGameBoard
		I had to call this in here because the coords are already here instead of returning them and calling it
		elsewhere. Also each turn then the board is updated which is nice.
		*/
		updateGameBoard(player, lastX, lastY);
	}

  /**
   * This is a method that updates the game board to be printed again. It populates the desired x
   * and y location with the appropriate player's marking: X or O.
   *
   * @param player The player that is currently on it's turn
   * @param x The x value of the board given by player
   * @param y The y value of the board given by player
   */
  public void updateGameBoard(String player, int x, int y) {
		if(player.equals("player1")){
			gameArr[x][y] = BlockType.X;
		}else if(player.equals("player2")){
			gameArr[x][y] = BlockType.O;
		}
	}

  /**
   * This is the method that checks for the winning condition. They are separated into 4 other methods,
   * checkRows, checkCol, checkDiag, checkForBlackOut. They each check their own designated area of the game for wins.
   *
   * @param lastPlayer the last player to have made a mark in updateGameBoard This keeps track of
   *     who wins.
   * @return lastPlayer who made a mark to dictate who is the winner.
   *  "draw" for a tie situation.
   *  "cont" for the continuing "no one won" situation.
   */
  public String checkWinning(String lastPlayer) {
  		//It just uses if statements to return the last player.
		if(checkRows()){return lastPlayer;}
		if(checkCol()){return lastPlayer;}
		if(checkDiag()){return lastPlayer;}
		if(checkForBlackOut()){return "draw";}
		return "cont";
	}

  /**
   * Check row at the lastY and see if the other values in that row are the same. If so then return true which is won
   * or return false which is continue. It just loops through each row and compares the base origin but lastY to the
   * other values.
   *
   * @return false if there are differences in the same row as the lastY that was placed.
   */
  public boolean checkRows() {
		for (int i = 0; i < nRows; i++) {
			if(gameArr[0][lastY] != gameArr[i][lastY]){
				verbose("No Rows. Searching Columns...");
				return false;
			}
		}
		return true;
	}

  /**
   * Check columns for cells that are the same. It loops through the column that was at lastX and compares each cell.
   * If they are the same then return true, which is a win. If they are different then return false.
   *
   * @return the boolean which is either true or false. True for win and False for nowin.
   */
  public boolean checkCol() {
		for (int i = 0; i < nCols; i++) {
			if(gameArr[lastX][0] != gameArr[lastX][i]){
				verbose("No Columns. Searching Diags...");
				return false;
			}
		}
		return true;
	}

  /**
   * Check diagonals for the same values. It loops through each diagonal separately. There are only 2 diagonals in tic
   * tac to which is bottom left to top right and top left to bottom right. Only these two have to be tested for the
   * same value. return true if they are the same which is a win and return false if they are different.
   *
   * @return the boolean which is either true or false. True for win and False for nowin.
   */
  public boolean checkDiag() {
		int counter = nRows-1;
		//Bottom left to top right check. This was simple, this is at [0,0][1,1][2,2]
		for(int i = 0; i < nCols; i++){
			if(gameArr[0][0] != gameArr[i][i]){
				/*Top left to bottom right check. This was harder because I had to have a counter that counted backwards
				because this diagnol is [0,2][1,1][2,0]*/
				verbose("nRows="+nRows);
				for (int j = 0; j < nRows; j++) { //increased the row.
						System.out.println(gameArr[j][counter].toString() + " "+j+","+counter);
						if (gameArr[lastX][lastY] != gameArr[j][counter]) {
							verbose(gameArr[j][counter].toString() + " "+j+","+counter);
							verbose(gameArr[lastX][lastY].toString() +","+ gameArr[j][counter].toString());
							verbose("No Diagonals checking Black Out");
							return false;
						}
					counter--; //decreased the counter
				}
			}
		}
		return true;
	}

  /**
   * Check for black out which is a draw. This was necessary because a draw has to be all spots are filled which is
   * a black out of the board game. This just looped through all the board spots and made sure none of them equalled
   * BlockType.EMPTY
   *
   * @return the boolean which is either true or false. True for draw and False for no draw.
   */
  public boolean checkForBlackOut() {
		//for draw
		for(int i = 0; i < nCols; i ++){
			for (int j = 0; j < nRows; j++) {
				if(gameArr[i][j] == BlockType.EMPTY){
					return false;
				}
			}
		}
		return true;
	}

  /**
   * Check winnable is a method for extra credit. I made it its own thing and own methods because I wanted to be able
   * to turn off the check if I needed to. This is extra searches which make speed slow down. But we are talking
   * small numbers now just a future habit check. There are seperate methods for each type of win.
   *
   * The logic behind this is that you can't win tic tac toe if there are 2 different markings in either row,col, or diag.
   * So this checks all the rows for any that are unwinnable(have X and O). This then counts up an instance variable
   * that sums all the unwinnable events. Comes to find out when you have a combination of unwinnables and a min
   * amount of turns then the game is going to be unwinnable. I set these values in the options menu based on what size
   * we are playing.
   */
  public void checkWinnable() {
  		//Checks.
		checkUnwinnableRows();
		checkUnwinnableCol();
		checkUnwinnableDiag();
		verbose("UnwinnableCount= " + unwinnableCount);

		//If statement that determines if the game is unwinnable.
		if ((unwinnableCount >= minUnwinnableCount) && (turnCount >= minTurnToDraw)){
			System.out.println("Game is Unwinnable");
		}else{
			System.out.println("Game is Winnable");
		}
		unwinnableCount = 0; // had to reset it to 0 so it doesn't get update cumulatively.
	}

  /**
   * Check unwinnable rows by looping each row, adding it to an array, searching the array if there is an X and O,
   * sets unwinabble variable to true to induce the count to increment and then clears the array and loops again.
   * This was the easiest method to check that wasn't comparing results with results.
   *
   * I used an ArrayList here to make it easier on the bigger boards having a dynamic size list to add
   * the row values to. Because I don't know how many X and O are going to be in a row of 7. It just matters
   * that there is 1 X and 1 O that makes the row unwinnable.
   */
  public void checkUnwinnableRows() {
		ArrayList<BlockType> temp = new ArrayList<BlockType>();
		boolean unwinnable = false;
		for (int j = 0; j < nRows; j++) {
			for (int i = 0; i < nCols; i++) {
				temp.add(gameArr[i][j]);
			}
			if(temp.contains(BlockType.X) && temp.contains(BlockType.O)){
				verbose("Row "+j+" is unwinnable");
				unwinnable = true;
			}else{
				verbose("Row "+j+" is winnable");
			}
			if(unwinnable){unwinnableCount++;}
			temp.clear();
			unwinnable = false;
		}
	}

  /**
   * Check unwinnable columns by doing the same as checkUnwinnableRows, but the problem was they were subtly different
   * enough that I couldn't make it one method. This would have cleaned it up but o`whale.
   */
  public void checkUnwinnableCol() {
		boolean unwinnable = false;
		ArrayList<BlockType> temp = new ArrayList<BlockType>();
		for (int i = 0; i < nCols; i++) {
			for (int j = 0; j < nRows; j++) {
				temp.add(gameArr[i][j]);
			}
			if(temp.contains(BlockType.X) && temp.contains(BlockType.O)){
				verbose("Column "+i+" is unwinnable");
				unwinnable = true;
			}else{
				verbose("Column "+i+" is winnable");
			}
			if(unwinnable){unwinnableCount++;}
			temp.clear();
			unwinnable = false;
		}
	}

  /**
   * Check unwinnable diagonals by doing the same concept. Loops through 1 diagonal and adds the value to an
   * arraylist then checks if there is an X and O.
   */
  public void checkUnwinnableDiag() {
		ArrayList<BlockType> temp = new ArrayList<BlockType>();
		int counter = nRows-1;
		boolean unwinnable = false;

		//Bottom left to top right check
		for(int i = 0; i < nCols; i++) {
			temp.add(gameArr[i][i]);
		}
		if(temp.contains(BlockType.X) && temp.contains(BlockType.O)){
			verbose("BackSlash Diagonal is unwinnable");
			unwinnable = true;
		}else{
			verbose("BackSlash Diagonal is winnable");
		}
		//Counts the unwinnable for the backslash diagonal.
		if(unwinnable){unwinnableCount++;}
		temp.clear();
		unwinnable = false;

		//Top left to bottom right check
		for (int j = 0; j < nRows; j++) {
			temp.add(gameArr[j][counter]);
			counter--;
		}
		if(temp.contains(BlockType.X) && temp.contains(BlockType.O)){
			verbose("ForwardSlash Diagonal is unwinnable");
			unwinnable = true;
		}else{
			verbose("ForwardSlash Diagonal is winnable");
		}
		if(unwinnable){unwinnableCount++;}
		temp.clear();
		unwinnable = false;
  }

  /**
   * A method that returns the game status instaed of calling gameStatus.toString();
   *
   * @return toString value of gameStatus which is the enumerated type for the
   *         game status
   */
  public String getGameStatus() {
		return gameStatus.toString();
	}

  /**
   * This method is for the menu, validates the input for the options and sets size based variables that are fixed
   * to that size only.
   *
   * @param opt option inputted by the player
   * @return a 0 if its a bad option or 1 if its okay.
   */
  public int validateOption(int opt) {
		if (opt > 2 || opt < 0) {
			System.err.println(" ! Choose a valid option!");
			return 10;
		}
		if(option == 0){nRows = 3; nCols = 3; minTurnToWin = 5; minTurnToDraw = 6; minUnwinnableCount = 7;}
		if(option == 1){nRows = 5; nCols = 5; minTurnToWin = 9; minTurnToDraw = 10; minUnwinnableCount = 16;}
		if(option == 2){nRows = 7; nCols = 7; minTurnToWin = 13; minTurnToDraw = 24; minUnwinnableCount = 16;}
		return 1;
	}

  /**
   * This method validates the move coordinates that are inputted by the player. This allows for the
   * player to stay in the bounds of the playing field. No wandering here :) This was easy because
   * tik tac toe has to be played in square so the same x and y value can be tested within the
   * bounds of the number of Rows.
   *
   * @param x the value that needs to be tested
   * @return returns true if coords are good and false if they are not good.
   */
  public boolean validMoveCoordinate(int x) {
		if(x < 0 || x > nRows -1){
			System.err.println(" ! This coordinate is out of bounds");
			return false;
		}
		return true;
	}

  /**
   * This method validates move choice by making sure that the spot that the player wants to move to
   * is already EMPTY and not taken already
   *
   * @param x the x coordinate of move choice
   * @param y the y coordinate of move choice
   * @return true if the move choice is empty or false if there is a mark there.
   */
  public boolean validateMove(int x, int y) {
		/*
		This was a simple method that just got the array block at location x and y and tested
		if it was equal to EMPTY. If so then return true so the spot can be filled
		return false if no. There is a System.err message to tell you what you did wrong.
		Better because a retry is necessary not a whole program exit
		 */
		if(gameArr[x][y] == BlockType.EMPTY){
			return true;
		}
		System.err.println("There is already a mark there!");
		return false;
	}

  /**
   * This method helps me with debugging by being able to leave in printlns that I used for testing but just making
   * them toggle with the verbose instance variable. true for the verbose and false for none.
   *
   * @param stuff inputs either a string to print on screen.
   */
  public void verbose(String stuff) {
		if(verbose) {
			System.out.println(stuff);
		}
	}
  /**
   * Verbose method for integers. Needs both otherwise it doesn't know how to print.
   *
   * @param stuff input for the integer you want to print.
   */
  public void verbose(int stuff) {
	  if(verbose) {
		  System.out.println(stuff);
	  }
  }
}



