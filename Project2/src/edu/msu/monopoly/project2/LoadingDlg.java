package edu.msu.monopoly.project2;

import java.io.IOException;
import java.io.InputStream;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Xml;
import android.widget.Toast;

public class LoadingDlg extends DialogFragment {

    /**
     * Name of catID in bundle
     */
    private final static String ID = "id";
    /**
     * Name of type in bundle
     */
    private final static String TYPE = "type";
    
	/**
     * Id for the image we are loading
     */
    private String catId;
    
    /**
     * Either user or game
     */
    private String type;
    
    public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
     * Set true if we want to cancel
     */
    private boolean cancel = false;
    
    /**
	 * @return the catId
	 */
	public String getCatId() {
		return catId;
	}

	/**
	 * @param catId the catId to set
	 */
	public void setCatId(String catId) {
		this.catId = catId;
	}

	/**
     * Create the dialog box
     */
    @Override
    public Dialog onCreateDialog(Bundle bundle) {
    	
    	if(bundle != null) {
            catId = bundle.getString(ID);
            type = bundle.getString(TYPE);
        }
    	
    	cancel = false;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Set the title
        builder.setTitle("Loading");
		
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                    	cancel = true;
                    }
                });

        
        // Create the dialog box
        final AlertDialog dlg = builder.create();
        
        // Get a reference to the view we are going to load into
        final DrawingView view = (DrawingView)getActivity().findViewById(R.id.drawingView);
        
        /*
         * Create a thread to load the hatting from the cloud
         */
        new Thread(new Runnable() {

            @Override
            public void run() {
            	Cloud cloud = new Cloud();
            	InputStream stream = cloud.loadGame(catId, type);
            	
            	if(cancel) {
                    return;
                }
            	
                // Test for an error
                boolean fail = stream == null;
                if(!fail) {
                    try {
                        XmlPullParser xml = Xml.newPullParser();
                        xml.setInput(stream, "UTF-8");       
                        
                        xml.nextTag();      // Advance to first tag
                        xml.require(XmlPullParser.START_TAG, null, "hatter");
                        String status = xml.getAttributeValue(null, "status");
                        if(status.equals("yes")) {
                        
                            while(xml.nextTag() == XmlPullParser.START_TAG) {
                            	
                                if(xml.getName().equals("hatting")) {
                                	if(cancel) {
                                        return;
                                    }
                                    
                                	// Load the hatting
                                	view.loadXml(xml);
                                    break;
                                }
                                
                                Cloud.skipToEndTag(xml);
                            }
                        } else {
                            fail = true;
                        }
                        
                    } catch(IOException ex) {
                        fail = true;
                    } catch(XmlPullParserException ex) {
                        fail = true;
                    } finally {
                        try {
                            stream.close(); 
                        } catch(IOException ex) {
                        }
                    }
                    
                    final boolean fail1 = fail;
                    view.post(new Runnable() {

                        @Override
                        public void run() {
                            dlg.dismiss();
                            if(fail1) {
                            	Toast.makeText(view.getContext(), "Failed to find user", Toast.LENGTH_SHORT).show();
                            } else {
                                // Success!
                                /*if(getActivity() instanceof HatterActivity) {
                                    ((HatterActivity)getActivity()).updateUI();
                                }*/
                            }
                            
                        }
                    
                    });
                }
            }
            
        }).start();
        
        
        return dlg;
    }
    
    /**
     * Called when the view is destroyed.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cancel = true;
    }
    
    /** 
     * Save the instance state
     */
    @Override
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        
        bundle.putString(ID, catId);
        bundle.putString(ID, catId);
    }

}
