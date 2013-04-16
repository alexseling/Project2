package edu.msu.monopoly.project2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
	 * string for saving checkbox state
	 */
	private static final String REMEMBER = "remember";

	/**
	 * string for saving password
	 */
	private static final String PASS = "password";

	/**
	 * The edit text for entering the password
	 */
	private EditText editTextPass = null;
	
	/**
	 * holds whether or not the user wants to be remembered
	 */
	private boolean rememberMe = false;
		
	/**
	 * reference to the sequencer class
	 */
	Sequencer sequencer = Sequencer.get();

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_main);
		
		/*
         * Get some of the views we'll keep around
         */
        //editTextPlayer2 = (EditText)findViewById(R.id.editTextPlayer2);
        editTextPass = (EditText)findViewById(R.id.editPassword);
        
        if (bundle != null) {
        	editTextPass.setText(bundle.getString(PASS));
        	rememberMe = bundle.getBoolean(REMEMBER);
        	
        	CheckBox checkbox = (CheckBox)findViewById(R.id.checkBoxRememberMe);
        	checkbox.setChecked(rememberMe);
        }
        
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

	/* (non-Javadoc)
	 * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
	 */
	@Override
	protected void onSaveInstanceState(Bundle bundle) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(bundle);
		
		bundle.putBoolean(REMEMBER, rememberMe);
		bundle.putString(PASS, editTextPass.getText().toString());
	}

	/**
     * Handle a Start Game button
     * @param view
     */
    public void onStartGame(final View view) {
    	
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
    	final EditText userText = (EditText)findViewById(R.id.editUserName);
    	final EditText passText = (EditText)findViewById(R.id.editPassword);
    	//final Intent intent = new Intent(this, WatingActivity.class);
    	
    	if(userText.length() != 0 && passText.length() != 0)
    	{
    		//game.setPlayer1Name(userText.getText().toString());
    		// game.setPlayer1Name...
    		
    		// check username and password...
    		
            new Thread(new Runnable() {
                
                @Override
                public void run() {
                	Cloud c = new Cloud();
                	boolean validLogin = c.login(userText.getText().toString(), passText.getText().toString());
                    if(!validLogin) {
                        // Create a toast on the view
                        view.post(new Runnable() {
                            @Override
                            public void run() {
                                /*
                                 * If we fail to save, display a toast 
                                 */
                                Toast.makeText(view.getContext(), R.string.login_fail, Toast.LENGTH_SHORT).show();
                            }
                        
                        });
                    } else {
                		// if username and password is correct..
                    	Game game = new Game();
                    	game.setPlayer1Name(userText.getText().toString());
                    	game.setPassword(passText.getText().toString());

                    	setActivityWaiting(game);
                    	//intent.putExtra("GAME", game);
                    	//startActivity(intent);
                    }
                }
                
            }).start();
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
    public void onNewUser(final View view) {
    	// The drawing is done
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        
        // Parameterize the builder
        builder.setTitle(R.string.newUser);
        
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_newuser, null))
        // Add action buttons
           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               	   
               @Override
               public void onClick(DialogInterface dialog, int id) {
            	   
            	   // get references to the edit text fields
	         	   final EditText newUser = (EditText)((AlertDialog) dialog).findViewById(R.id.editNewUser);
	         	   final EditText newPassword = (EditText)((AlertDialog) dialog).findViewById(R.id.editNewPassword);
	         	   final EditText newPasswordConfirm = (EditText)((AlertDialog) dialog).findViewById(R.id.editNewPasswordConfirm);
	         	   
            	   final String newPasswordString = newPassword.getText().toString();
            	   final String newPasswordConfirmString = newPasswordConfirm.getText().toString();
            	   //
                   new Thread(new Runnable() {
                       
                       @Override
                       public void run() {
                    	   // check if the two passwords are equal
                    	   if (newPasswordString.equals(newPasswordConfirmString) ) {
                    		   // create a new user
                    		   Cloud c = new Cloud();
                    		   boolean isValidUser = c.addNewUser(newUser.getText().toString(), newPassword.getText().toString());
                             
                    		   //boolean isValidUser = false;
                    		   if (isValidUser) {
                            	   // Success!
        	            		   view.post(new Runnable() {
        	
        	                           @Override
        	                           public void run() {
        	                               Toast.makeText(view.getContext(), R.string.newuser_success, Toast.LENGTH_SHORT).show();
        	                           }
        	                           
        	                       });
                               } else {
                            	   // Error condition!
        	            		   view.post(new Runnable() {
        	            				
        	                           @Override
        	                           public void run() {
        	                               Toast.makeText(view.getContext(), R.string.newuser_fail, Toast.LENGTH_SHORT).show();
        	                           }
        	                           
        	                       });
                               }
                    	   } else {
                               // Error condition!
                               view.post(new Runnable() {

                                   @Override
                                   public void run() {
                                       Toast.makeText(view.getContext(), R.string.newuser_passfail, Toast.LENGTH_SHORT).show();
                                   }
                                   
                               });
                    	   }
                       }
                       
                   }).start();
                   //
            	   
               }
           }); 
        
        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    
    private void setActivityWaiting(Game game) {
    	sequencer.setActivityWaiting(game, this);
    }
    
}