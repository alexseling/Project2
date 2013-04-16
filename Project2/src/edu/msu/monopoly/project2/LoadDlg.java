package edu.msu.monopoly.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class LoadDlg extends DialogFragment {

	private static final String USER = "USER";
	private static final String PASS = "PASS";
	private static final String GAME = "GAME";
	private String username;
	private String password;
	private Game game;
	
	
	/**
	 * @return the game
	 */
	public Game getGame() {
		return game;
	}

	/**
	 * @param game the game to set
	 */
	public void setGame(Game game) {
		this.game = game;
	}

	/**
     * Create the dialog box
     */
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
    	
    	if(bundle != null) {
    		username = bundle.getString(USER);
    		password = bundle.getString(PASS);
    		game = (Game)bundle.getSerializable(GAME);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle("Select a partner");
        
        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
       
        // Pass null as the parent view because its going in the dialog layout
        View view = inflater.inflate(R.layout.usercatalog_dlg, null);
        builder.setView(view);
        
        // Add a cancel button
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
             @Override
             public void onClick(DialogInterface dialog, int id) {
                 // Cancel just closes the dialog box
             }
          });
        
        final AlertDialog dlg = builder.create();
        
        // Find the list view
        ListView list = (ListView)view.findViewById(R.id.listUsers);
        
        // Create an adapter
        final Cloud.CatalogAdapter adapter = new Cloud.CatalogAdapter(list, username, password);
        list.setAdapter(adapter);
        
        list.setOnItemClickListener(new ListView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                String player = adapter.getName(position);
                
                // Set up the game
                game.setPlayer2Name(player);
                game.randomlySelectCategory();
                // Dismiss the dialog box
                dlg.dismiss();
                
                new Thread(new Runnable() {
                    
                    @Override
                    public void run() {
                		Cloud c = new Cloud();
                		c.saveGame(game, "1", null);
                    }
                    
                }).start();
                
        		Sequencer.get().setActivityEdit(game, getActivity());
            }
            
        });
        
        return dlg;
    }
    
    public void setUsernameAndPassword(String name, String pass) {
    	username = name;
    	password = pass;
    }
    
    /** 
     * Save the instance state
     */
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        
        bundle.putString(USER, username);
        bundle.putString(PASS, password);
        bundle.putSerializable(GAME, game);
    }
}
