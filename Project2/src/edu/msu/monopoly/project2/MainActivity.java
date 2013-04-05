package edu.msu.monopoly.project2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

//	/**
//     * The edit text for entering player 2 name
//     */
//	private EditText editTextPlayer2 = null;
	
	/**
	 * The edit text for entering the password
	 */
	private EditText editTextPass = null;
	
	/**
	 * holds whether or not the user wants to be remembered
	 */
	private boolean rememberMe = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		/*
         * Get some of the views we'll keep around
         */
        //editTextPlayer2 = (EditText)findViewById(R.id.editTextPlayer2);
        editTextPass = (EditText)findViewById(R.id.editPassword);
        
        /*
         * Change the Done button to Start Game
         */
        //editTextPlayer2.setImeActionLabel("Start Game", KeyEvent.KEYCODE_ENTER);
        editTextPass.setImeActionLabel("Start", KeyEvent.KEYCODE_ENTER);
        
        //editTextPlayer2.setOnKeyListener(new TextView.OnKeyListener() {
        editTextPass.setOnKeyListener(new TextView.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                    (keyCode == KeyEvent.KEYCODE_ENTER)) {
                	onStartGame(v);
                	return true;
                }
                return false;
            }
        });
        
	}

	/**
     * Handle a Start Game button
     * @param view
     */
    public void onStartGame(final View view) {
    	Game game = new Game();
    	Cloud cloud = new Cloud();
    	
    	/*
    	 * grab whether or not the rememberMe box is checked
    	 */
    	CheckBox checkbox = (CheckBox)findViewById(R.id.checkBoxRememberMe);
    	rememberMe = checkbox.isChecked();
    	
//    	
//    	// Get player names, set in game
//    	EditText p1Text = (EditText)findViewById(R.id.editTextPlayer1);
//    	EditText p2Text = (EditText)findViewById(R.id.editTextPlayer2);
    	
    	// Added by Justin Fila
    	// Get the username and password
    	EditText userText = (EditText)findViewById(R.id.editUserName);
    	EditText passText = (EditText)findViewById(R.id.editPassword);
    	
    	if(userText.length() != 0 && passText.length() != 0)
    	{
    		//game.setPlayer1Name(userText.getText().toString());
    		Intent intent = new Intent(this, WatingActivity.class);
    		// game.setPlayer1Name...
    		
    		// check username and password...
    		boolean validLogin = cloud.login(userText.getText().toString(), passText.getText().toString());
    		
    		// if username and password is correct...
    		if (!validLogin) {
                // Error condition!
                view.post(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(view.getContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
                    }
                    
                });
    		} else {
	        	game.setPlayer1Name(userText.getText().toString());
	    		
	        	intent.putExtra("GAME", game);
				startActivity(intent);
    		}
    	} else {
    		//game.setPlayer2Name(getString(R.string.hint_password));
    	}
    	//
    	
    	
//    	if(p1Text.length() != 0)
//    		game.setPlayer1Name(p1Text.getText().toString());
//    	else
//    		game.setPlayer1Name(getString(R.string.hint_player1));
//    	if(p2Text.length() != 0)
//    		game.setPlayer2Name(p2Text.getText().toString());
//    	else
//    		game.setPlayer2Name(getString(R.string.hint_player2));
    	
    	
    	// Pick a category
//    	game.randomlySelectCategory();
//    	
//    	Intent intent = new Intent(this, EditActivity.class);
//    	intent.putExtra("GAME", game);
//		startActivity(intent);
	}

    /**
     * Handle a How To Play button
     * @param view
     */
    public void onHowToPlay(View view) {
    	// The drawing is done
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        
        // Parameterize the builder
        builder.setTitle(R.string.how_to_play);
        
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_howto, null))
        // Add action buttons
           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   
               }
           }); 
        
        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    
    /**
     * Handle a new user
     * @param view
     */
    public void onNewUser(View view) {
    	// TODO
    }
}
