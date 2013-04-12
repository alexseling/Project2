package edu.msu.monopoly.project2;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;

public class WaitingToGuessActivity extends Activity {

	/**
	 * the game
	 */
	//Game game;
    
    /**
     * handler that posts at regular intervals
     */
    private Handler mHandler = new Handler();
    
    /**
     * Reference to the dots text (needed to add the extra periods)
     */
    TextView dots = null;
    
    private int iteration = 0;
    
    //private long mStartTime = 0;
    
    //tells handler to send a message
    private Runnable mUpdateTimeTask = new Runnable() {

    	// will run every second. Check if other player is finished here.
         public void run() {
             //final long start = mStartTime;
        	 
        	 if (iteration == 1)
        		 dots.setText("");
        	 else if (iteration == 2)
        		 dots.setText(".");
        	 else if (iteration == 3)
        		 dots.setText("..");
        	 else {
        		 dots.setText("...");
        		 iteration = 0;
        	 }
        	 iteration++;
        	 
             mHandler.postDelayed(this, 1000);
         }
    };
    
    /**
     * Start the timer
     */
    public void onStartTimer() {
        //mStartTime = System.currentTimeMillis();
        mHandler.removeCallbacks(mUpdateTimeTask);
        mHandler.postDelayed(mUpdateTimeTask, 0);
    }

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_waiting_to_guess);
		
		//Intent intent = getIntent();
		//game = (Game)intent.getSerializableExtra("GAME");
		
		dots = (TextView)findViewById(R.id.textDots);
		
		// start the timer to pull data from server
		onStartTimer();
		
		// get the second player...
//		game.setPlayer2Name(p2Text.getText().toString());
		
    	// Pick a category
//    	game.randomlySelectCategory();
//    	
//		// start the game
//    	Intent intent = new Intent(this, EditActivity.class);
//    	intent.putExtra("GAME", game);
//		startActivity(intent);
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		mHandler.removeCallbacks(mUpdateTimeTask);
	}


	/* (non-Javadoc)
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		onStartTimer();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onBackPressed()
	 */
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		
		mHandler.removeCallbacks(mUpdateTimeTask);
	}
}
