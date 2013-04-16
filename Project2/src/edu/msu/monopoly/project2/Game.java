package edu.msu.monopoly.project2;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.util.Xml;

public class Game implements Serializable {
	
	/**
	 * Compiler generated serialID
	 */
	private static final long serialVersionUID = -5154840961469873261L;

	private final int maxScore = 500;

	private String gameId = "";
	
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
	
	private String serverp1 = "";
	
	private String serverp2 = "";
	
	private String p1Drawing = "";
	
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
	
	public boolean getInfoFromInputStream(InputStream stream) {
		try {
            XmlPullParser xml = Xml.newPullParser();
            xml.setInput(stream, "UTF-8");       
            
            xml.nextTag();      // Advance to first tag
            xml.require(XmlPullParser.START_TAG, null, "userinfo");
            String status = xml.getAttributeValue(null, "status");
            if(status.equals("yes")) {
            
                while(xml.nextTag() == XmlPullParser.START_TAG) {
                	
                	// When we find a game tag, load the attributes
                    if(xml.getName().equals("game")) {
                    	gameId = xml.getAttributeValue(null, "id");
                    	serverp1 = xml.getAttributeValue(null, "p1");
                    	serverp2 = xml.getAttributeValue(null, "p2");
                    	String p1DrawingTemp = xml.getAttributeValue(null, "p1drawing");
                    	if (!p1DrawingTemp.equals(p1Drawing)) {
                    		swapPlayers();
                    	}
                    	
                    	
             
                        category = xml.getAttributeValue(null, "category");
                        answer = xml.getAttributeValue(null, "answer");
                        tip = xml.getAttributeValue(null, "hint");
                        player1Score = Integer.parseInt(xml.getAttributeValue(null, "p1score"));
                        player2Score = Integer.parseInt(xml.getAttributeValue(null, "p2score"));
                        break;
                    }
                    
                    Cloud.skipToEndTag(xml);
                }
            } else {
            	return false;
            }
            
        } catch(IOException ex) {
        	return false;
        } catch(XmlPullParserException ex) {
        	return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
		return true;
	}

	/**
	 * @return the serverp1
	 */
	public String getServerp1() {
		return serverp1;
	}

	/**
	 * @param serverp1 the serverp1 to set
	 */
	public void setServerp1(String serverp1) {
		this.serverp1 = serverp1;
	}

	/**
	 * @return the serverp2
	 */
	public String getServerp2() {
		return serverp2;
	}

	/**
	 * @param serverp2 the serverp2 to set
	 */
	public void setServerp2(String serverp2) {
		this.serverp2 = serverp2;
	}
	
	/**
	 * @return the gameId
	 */
	public String getGameId() {
		return gameId;
	}

	/**
	 * @param gameId the gameId to set
	 */
	public void setGameId(String gameId) {
		this.gameId = gameId;
	}
}
