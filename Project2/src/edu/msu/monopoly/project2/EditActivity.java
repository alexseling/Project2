package edu.msu.monopoly.project2;

import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends Activity {
	
	/**
     * Tag used for saving bundle
     */
	private static final String PICTURE = "PICTURE";
	/**
     * Tag used for saving bundle
     */
	private static final String GAME = "GAME";
	
	/**
     * The color select button
     */
    private Button colorButton = null;
    
    /**
     * The pencil select button
     */
    private Button pencilButton = null;
    
    /**
     * The eraser select button
     */
    private Button eraserButton = null;
    
    /**
     * The pencil color
     */
	private int pencilColor;
	
    /**
	 * Player names
	 */
    private TextView p1Name = null;
    private TextView p2Name = null;
    
    /**
	 * Player scores
	 */
	private TextView p1Score = null;
	private TextView p2Score = null;
    
	/**
	 * Category
	 */
    private TextView category = null;
    
    /**
     * The drawing width seekbar, and eraser width seekbar
     */
    SeekBar pencilSeekBar;
    SeekBar eraserSeekBar;
    
    /**
     * Request code when selecting a color
     */
    private static final int SELECT_COLOR = 1;
    
    /**
     * The DrawingView
     */
    private DrawingView drawingView;
    
    /**
     * The game class
     */
    private Game game;
    
    
    private class SeekBarListener implements SeekBar.OnSeekBarChangeListener {
    	@Override
    	public void onStopTrackingTouch(SeekBar arg0) {
    	}
    	
    	@Override
    	public void onStartTrackingTouch(SeekBar arg0)  {		
    	}
    	
    	@Override
    	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
    		drawingView.setPencilPaintWidth((float)progress);
    	}
    }
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);

		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra(GAME);
		
		/*
         * Get some of the views we'll keep around
         */
		drawingView = (DrawingView)findViewById(R.id.drawingView);
		p1Name = (TextView)findViewById(R.id.textViewP1);
		p2Name = (TextView)findViewById(R.id.textViewP2);
		p1Score = (TextView)findViewById(R.id.textViewScoreP1);
		p2Score = (TextView)findViewById(R.id.textViewScoreP2);
		category = (TextView)findViewById(R.id.textViewCategoryType);
		pencilSeekBar = (SeekBar)findViewById(R.id.seekBarPencil);
		eraserSeekBar = (SeekBar)findViewById(R.id.seekBarEraser);
		colorButton = (Button)findViewById(R.id.buttonColor);
		pencilButton = (Button)findViewById(R.id.buttonPencil);
		eraserButton = (Button)findViewById(R.id.buttonEraser);
		
		/*
		 *  Set up text boxes
		 */
		p1Name.setText(game.getPlayer1DisplayName() + ":");
		p2Name.setText(game.getPlayer2DisplayName() + ":");
		p1Score.setText(Integer.toString(game.getPlayer1Score()));
		p2Score.setText(Integer.toString(game.getPlayer2Score()));
		category.setText(game.getCategory());
		
		/*
		 * Listeners for the sliders
		 */
		pencilSeekBar.setOnSeekBarChangeListener(new SeekBarListener() {
			@Override
	    	public void onStopTrackingTouch(SeekBar arg0) {}
			
	    	@Override
	    	public void onStartTrackingTouch(SeekBar arg0) {}
	    	
	    	@Override
	    	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
	    		drawingView.setPencilPaintWidth((float)progress);
	    	}
		});
		

		eraserSeekBar.setOnSeekBarChangeListener(new SeekBarListener() {
			@Override
	    	public void onStopTrackingTouch(SeekBar arg0) {}
			
	    	@Override
	    	public void onStartTrackingTouch(SeekBar arg0) {}
	    	
	    	@Override
	    	public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
	    		drawingView.setEraserWidth((float)progress);
	    	}
		});

		
        /*
         * Restore any state
         */
        if(savedInstanceState != null) {
            loadUi(savedInstanceState);
            drawingView.getFromBundle(PICTURE, savedInstanceState);
        }
        
        eraserButton.getBackground().setColorFilter(0xFFCED3CF, PorterDuff.Mode.MULTIPLY);
        pencilButton.getBackground().setColorFilter(0xFFFFA11C, PorterDuff.Mode.MULTIPLY);
		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
        if(requestCode == SELECT_COLOR && resultCode == Activity.RESULT_OK) {
            // This is a color response
            pencilColor = data.getIntExtra(ColorSelectActivity.COLOR, Color.BLACK);
            drawingView.setPencilPaintColor(pencilColor);
        }
        
	}
	
	/**
     * Handle a Color button press
     */
    public void onColor(View view) {
        // Get a color
        Intent intent = new Intent(this, ColorSelectActivity.class);
        startActivityForResult(intent, SELECT_COLOR);
    }
    
    /**
     * Handle a Pencil button press
     */
    public void onPencil(View view) {
    	drawingView.switchToPencil();
        eraserButton.getBackground().setColorFilter(0xFFCED3CF, PorterDuff.Mode.MULTIPLY);
        pencilButton.getBackground().setColorFilter(0xFFFFA11C, PorterDuff.Mode.MULTIPLY);
    	colorButton.setEnabled(true);
    }
    
    /**
     * Handle an Eraser button press
     */
    public void onEraser(View view) {
    	drawingView.switchToEraser();
        eraserButton.getBackground().setColorFilter(0xFFFFA11C, PorterDuff.Mode.MULTIPLY);
        pencilButton.getBackground().setColorFilter(0xFFCED3CF, PorterDuff.Mode.MULTIPLY);
    	colorButton.setEnabled(false);
    }
    
    /**
     * Handle a Done button press
     * @param view
     */
    public void onDone(View view) {
        
    	// The drawing is done
        // Instantiate a dialog box builder
        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
        
        // Parameterize the builder
        builder.setTitle(R.string.text_done);
        
        // Get the layout inflater
        LayoutInflater inflater = this.getLayoutInflater();

        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.dialog_done, null))
        // Add action buttons
           .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int id) {
	               	EditText answerText = (EditText)((AlertDialog) dialog).findViewById(R.id.editTextAnswer);
	            	EditText tipText = (EditText)((AlertDialog) dialog).findViewById(R.id.editTextTip);
	            	
	            	game.setAnswer(answerText.getText().toString());
	            	game.setTip(tipText.getText().toString());
	            	
	            	onOk();
               }
           })
           .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int id) {
                   
               }
           }); 
        
        // Create the dialog box and show it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    	
    }
    
    /**
     * Handle an Ok button press
     */
    public void onOk() {
    	
    	if (game.checkAnswerAndTip()) {
    		game.switchRoles();
        	Intent intent = new Intent(this, GuessActivity.class);
        	intent.putExtra(GAME, game);
        	drawingView.putDrawings(intent);
    		startActivity(intent);
        	finish();
    	} else {
			Toast.makeText(this,"Answer and tip required", Toast.LENGTH_LONG).show();
		}
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
    
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		saveUi(outState);
		drawingView.putToBundle(PICTURE, outState);
	}
	
    /**
     * Save the view state to a bundle
     * @param bundle bundle to save to
     */
    public void saveUi(Bundle bundle) {
    	bundle.putSerializable(GAME, game);

    }
    
    /**
     * Get the view state from a bundle
     * @param bundle bundle to load from
     */
    public void loadUi(Bundle bundle) {
    	game = (Game)bundle.getSerializable(GAME);
    }
    
}
