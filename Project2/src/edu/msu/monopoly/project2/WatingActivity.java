package edu.msu.monopoly.project2;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

public class WatingActivity extends Activity {
	
	/**
	 * the game
	 */
	Game game;

	@Override
	protected void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.activity_wating);
		
		Intent intent = getIntent();
		game = (Game)intent.getSerializableExtra("GAME");
		
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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_wating, menu);
		return true;
	}

}
