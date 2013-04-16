package edu.msu.monopoly.project2;

import java.io.InputStream;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.widget.TextView;

public class WaitingToGuessActivity extends Activity {

	/**
	 * the game
	 */
	Game game;
    
    /**
     * handler that posts at regular intervals
     */
    private Handler mHandler = new Handler();
    
    private Activity a;
    
    private int draw;
    
    /**
     * Reference to the dots text (needed to add the extra periods)
     */
    TextView dots = null;
    
    private int iteration = 0;
    
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
             
          // Check if another user has started a game this user
             new Thread(new Runnable() {
                 
                 @Override
                 public void run() {
                	 Cloud c = new Cloud();
                	 // If there's a game for this user
                	 game.getInfoFromInputStream(c.loadGame(game.getPlayer1Name(), game.getPassword()));
                	 
                	 if (game.getEditingPlayer() != draw) {
                		 //mHandler.removeCallbacks(mUpdateTimeTask);
                		 // Load the game
                		 //Sequencer.get().setActivityEdit(game, a);
                	 }
                 }
                 
             }).start();
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
		
		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		a = this;
		draw = game.getEditingPlayer();
		
		
		dots = (TextView)findViewById(R.id.textDots);
		
		// start the timer to pull data from server
		onStartTimer();
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
	
	
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) 
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
 
			// set title
			alertDialogBuilder.setTitle(R.string.quit);

	        alertDialogBuilder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
	               @Override
	               public void onClick(DialogInterface dialog, int id) {

	                   new Thread(new Runnable() {
	                       
	                       @Override
	                       public void run() {
	                   		Cloud c = new Cloud();
	                   		c.logout(game.getPlayer1Name(), game.getPassword());
	                   		mHandler.removeCallbacks(mUpdateTimeTask);
	                       }
	                       
	                   }).start();
	            	   finish();
	               }
	        })
	        
           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   
               }
           });
	        
	        // Create the dialog box and show it
	        AlertDialog alertDialog = alertDialogBuilder.create();
	        alertDialog.show();
		}

        return true;
    }
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStop()
	 */
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
        new Thread(new Runnable() {
            
            @Override
            public void run() {
        		Cloud c = new Cloud();
        		c.logout(game.getPlayer1Name(), game.getPassword());
            }
            
        }).start();
	}
}


