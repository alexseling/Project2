package edu.msu.monopoly.project2;

import android.app.Activity;
import android.content.Intent;

public class Sequencer {
	
	public static Sequencer sequencer = new Sequencer();
	
	// private constructor ensures it remains one object
	private Sequencer() {
	}
	
	// get a reference to the object
	public static Sequencer get() {
		return sequencer;
	}
	
	/**
	 * navigate user to the editing activity
	 * @param activity the activity that will start the guessing activity
	 * @param game the game
	 */
	public void setActivityEdit(Game game, Activity activity) {
		Intent intent = new Intent(activity, EditActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		intent.putExtra("GAME", game);
		activity.startActivity(intent);
		activity.finish();
	}
	
	/**
	 * navigate user to the guessing activity
	 * @param game a reference to the game object
	 * @param activity used to call the new activity
	 * @param drawingView the view for the drawing
	 */
	public void setActivityGuess(Game game, Activity activity, Picture pictureFromServer) {
		Intent intent = new Intent(activity, GuessActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//		drawingView.putDrawings(intent);
		intent.putExtra("PICTURE", pictureFromServer);
		intent.putExtra("GAME", game);
		activity.startActivity(intent);
		activity.finish();
	}
	
	/**
	 * navigate user to the guessing activity
	 * @param game a reference to the game object
	 * @param actvity the activity that will start the guessing activity
	 */
	public void setActivityWaiting(Game game, Activity activity) {
		Intent intent = new Intent(activity, WatingActivity.class);
		intent.putExtra("GAME", game);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		activity.startActivity(intent);
	}
	
	/**
	 * navigate user to the guessing activity
	 * @param game a reference to the game object
	 * @param actvity the activity that will start the guessing activity
	 */
	public void setActivityWaitingForGuess(Game game, Activity activity) {
		Intent intent = new Intent(activity, WaitingToGuessActivity.class);
		intent.putExtra("GAME", game);
		intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		activity.startActivity(intent);
		activity.finish();
	}
	
	/**
	 * navigate user to the guessing activity
	 * @param game a reference to the game object
	 * @param activity used to call the new activity
	 */
	public void setActivityEnd(Game game, Activity activity) {
    	Intent intent = new Intent(activity, FinalActivity.class);
    	intent.putExtra("GAME", game);
		activity.startActivity(intent);
		activity.finish();
	}
	
	/**
	 * navigate to the main activity
	 * @param activity used to call the new activity
	 */
	public void setActivityMain(Activity activity) {
    	Intent intent = new Intent(activity, MainActivity.class);
    	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
		activity.finish();
	}
	
	/**
	 * push the drawing to the server
	 * @param drawingView reference to the drawing view
	 */
	public void pushDrawing(Picture pictureFromServer) {
		// push the drawing here
	}
}
