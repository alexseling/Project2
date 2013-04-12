package edu.msu.monopoly.project2;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

public class Game implements Serializable {
	
	/**
	 * Compiler generated serialID
	 */
	private static final long serialVersionUID = -5154840961469873261L;

	private final int maxScore = 500;
	
	private int editingPlayer = 1;
	
	private int guessingPlayer = 2;
	
	private String player1Name = "";
	
	private String player2Name = "";
	
	private String player1DisplayName = "";
	
	private String player2DisplayName = "";
	
	private int player1Score = 0;
	
	private int player2Score = 0;
	
	private String answer = "";
	
	private String tip = "";
	
	private String category = "";
	
	private String password = "";
	
	private Random randomNumberGenerator = new Random();
	
	private ArrayList<String> categories = new ArrayList<String>();
	
	public Game() {
		categories.add("Animal");
		categories.add("Building");
		categories.add("Object");
		categories.add("Action");
		categories.add("MSU");
		
		randomlySelectCategory();
	}
	
	/**
     * Randomly sets the category
     * @return category randomly selected
     */
	public void randomlySelectCategory() {
		category = categories.get(randomNumberGenerator.nextInt(5) % 5);
	}
	
	/**
     * Compares guess to answer
     * @param guess of what the drawing might be
     * @return true if guess matches the answer
     */
	public Boolean guessAnswer(String guess) {
		if (guess.equalsIgnoreCase(answer))
			return true;
		
		return false;
	}
	
	/**
     * Increment a player's score by a given amount
     * @param playerNumber the chosen player
     * @param amount to increment player's score
     * @return true if player's score is incremented
     */
	public Boolean incrementPlayerScore(int playerNumber, int amount) {
		if (playerNumber == 1)
		{
			player1Score = player1Score + amount;
			return true;
		}
		else if (playerNumber == 2)
		{
			player2Score = player2Score + amount;
			return true;
		}
		else
			return false;
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	/** 
	 * Sets player 1's name to a given string. Defaults to "Player 1"
	 * @param player1Name The name of player 1
	 */
	public void setPlayer1Name(String player1Name) {
		if (!player1Name.equals(null) && !player1Name.equals("")) {
			this.player1Name = player1Name;
		} else {
			this.player1Name = "Player 1";
		}
		if (player1Name.length() > 8) {
			this.player1Name = player1Name.substring(0, 6) + "..";
		}
		player1DisplayName = "-->" + this.player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	/** 
	 * Sets player 2's name to a given string. Defaults to "Player 2"
	 * @param player2Name The name of player 2
	 */
	public void setPlayer2Name(String player2Name) {
		if (!player2Name.equals(null) && !player2Name.equals("")) {
			this.player2Name = player2Name;
		} else {
			this.player2Name = "Player 2";
		}
		if (player2Name.length() > 8) {
			this.player2Name = player2Name.substring(0, 6) + "..";
		}
		player2DisplayName = this.player2Name;
	}

	public String getAnswer() {
		return answer;
	}

	public void setAnswer(String answer) {
		if (!answer.equals(null) && !answer.equals("")) {
			this.answer = answer;
		}
	}
	
	public String getPlayer1DisplayName() {
		return player1DisplayName;
	}
	
	public String getPlayer2DisplayName() {
		return player2DisplayName;
	}

	public int getPlayer1Score() {
		return player1Score;
	}

	public int getPlayer2Score() {
		return player2Score;
	}

	public String getCategory() {
		return category;
	}
	
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		if (!tip.equals(null) && !tip.equals("")) {
			this.tip = tip;
		}
	}
	
	/**
	 * Check the answer and tip to make sure they are not empty
	 * @return True if neither is empty
	 */
	public boolean checkAnswerAndTip() {
		if (tip.equals("") || answer.equals("")) {
			return false;
		}
		return true;
	}
	
	public int getEditingPlayer() {
		return editingPlayer;
	}
	
	public int getGuessingPlayer() {
		return guessingPlayer;
	}

	/**
	 * Swap the guessing and editing players. Also resets the answer and tip, as
	 * this indicates the end of a round
	 */
	public void swapPlayers() {
		int temp = guessingPlayer;
		guessingPlayer = editingPlayer;
		editingPlayer = temp;
		answer = "";
		tip = "";
	}
	
	/**
	 * Check if there is a winner
	 * @return True if a player has won
	 */
	public boolean checkForWinner() {
		if (player1Score >= maxScore || player2Score >= maxScore) {
			return true;
		}
		return false;
	}
	
	/**
	 * Swap between editing and guessing activity. Guessing player gets an arrow
	 * by their name; this carries over when they become the drawer.
	 */
	public void switchRoles() {
		if (player1Name.length() < player1DisplayName.length()) {
			player1DisplayName = player1Name;
			player2DisplayName = "-->" + player2Name;
		} else {
			player1DisplayName = "-->" + player1Name;
			player2DisplayName = player2Name;
		}
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
