package edu.msu.monopoly.project2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

public class LoadDlg extends DialogFragment {

	private String username;
	private String password;
	
	/**
     * Create the dialog box
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle("Select a user or game");
        
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
        
        AlertDialog dlg = builder.create();
        
        // Find the list view
        ListView list = (ListView)view.findViewById(R.id.listUsers);
        
        // Create an adapter
        final Cloud.CatalogAdapter adapter = new Cloud.CatalogAdapter(list, username, password);
        list.setAdapter(adapter);
        
        return dlg;
    }
    
    public void setUsernameAndPassword(String name, String pass) {
    	username = name;
    	password = pass;
    }
}
