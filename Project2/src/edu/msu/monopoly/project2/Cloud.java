package edu.msu.monopoly.project2;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.os.NetworkOnMainThreadException;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class Cloud {
    private static final String ADDUSER_URL = "https://www.cse.msu.edu/~siaudvyt/monopoly/new-user.php";
    private static final String LOGIN_URL = "https://www.cse.msu.edu/~siaudvyt/monopoly/login.php";
    private static final String LOGOUT_URL = "https://www.cse.msu.edu/~siaudvyt/monopoly/logout.php";
    private static final String CATALOG_URL = "https://www.cse.msu.edu/~siaudvyt/monopoly/load.php";
    private static final String GET_GAME_URL = "https://www.cse.msu.edu/~siaudvyt/monopoly/getGame.php";
    private static final String SAVE_GAME_URL = "https://www.cse.msu.edu/~siaudvyt/monopoly/update.php";
    private static final String UTF8 = "UTF-8";
    
    /**
     * Nested class to store one catalog row
     */
    private static class Item {
        public String name = "";
        public String id = "";
    }
    
    /**
     * Skip the XML parser to the end tag for whatever 
     * tag we are currently within.
     * @param xml the parser
     * @throws IOException
     * @throws XmlPullParserException
     */
    public static void skipToEndTag(XmlPullParser xml) 
        throws IOException, XmlPullParserException {
        int tag;
        do
        {
            tag = xml.next();
            if(tag == XmlPullParser.START_TAG) {
                // Recurse over any start tag
                skipToEndTag(xml);
            }
        } while(tag != XmlPullParser.END_TAG && 
        tag != XmlPullParser.END_DOCUMENT);
    }
    
    /**
     * Create a new user
     * @param name username
     * @param pass password
     * @return
     */
    public boolean addNewUser(String name, String pass) {
    	return createUserXml(ADDUSER_URL, name, pass);
    }
    /**
     * Log a user in
     * @param name username
     * @param pass password
     * @return
     */
    public boolean login(String name, String pass) {
    	return createUserXml(LOGIN_URL, name, pass);
    }
    
    /**
     * Log a user out
     * @param name username
     * @param pass password
     * @return
     */
    public boolean logout(String name, String pass) {
    	return createUserXml(LOGOUT_URL, name, pass);
    }
    
    /**
     * Check if there are any games the user is in
     * @param name username
     * @param pass password
     * @return
     */
    public boolean checkForGames(String name, String pass) {
    	return createUserXml(GET_GAME_URL, name, pass);
    }
    
    /**
     * Sends xml containing a username and password to a specified php script
     * @param dbUrl The url to connect to
     * @param name username
     * @param pass password
     * @return
     */
    private boolean createUserXml(String dbUrl, String name, String pass) {
        name = name.trim();
        if(name.length() == 0) {
            return false;
        }
        
        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "userinfo");

            xml.attribute(null, "user", name);
            xml.attribute(null, "pw", pass);
            
            xml.endTag(null, "userinfo");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }
        
        final String xmlStr = writer.toString();
        
        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        
        InputStream stream = null;
        try {
            URL url = new URL(dbUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } 
            if (dbUrl == GET_GAME_URL) {
	            /*BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
	            String line;
	            while ((line = reader.readLine()) != null) {
	                Log.i("userinfo", line);
	            }*/
            }
        
            stream = conn.getInputStream();
            
            if (!parseUserResult(stream)) {
            	return false;
            }
            
        } catch (MalformedURLException e) {
            return false;
        } catch (IOException ex) {
            return false;
        } catch (SecurityException es) {
            return false;
        } catch (NetworkOnMainThreadException en) {
            return false;
        } finally {
            if(stream != null) {
                try {
                    stream.close();
                } catch(IOException ex) {
                    // Fail silently
                }
            }
        }
        
        return true;
    }

    /**
     * Creates a catalog adapter to create a list of online users
     */
    public static class CatalogAdapter extends BaseAdapter  {
    	
    	/**
    	 * The items to display in the list box. Initially null.
    	 */
    	private ArrayList<Item> items = new ArrayList<Item>();
    	
        /**
         * Constructor
         */
        public CatalogAdapter(final View view, final String username, final String password) {
            // Create a thread to load the catalog
        	new Thread(new Runnable() {

				@Override
				public void run() {
                    ArrayList<Item> newItems = getCatalog(username, password);
                    
                    if(newItems != null) {
                        items = newItems;
                        
                        view.post(new Runnable() {
    
                            @Override
                            public void run() {
                                // Tell the adapter the data set has been changed
                                notifyDataSetChanged();
                            }
                            
                        });
                    } else {
                        // Error condition
                        view.post(new Runnable() {

                            @Override
                            public void run() {
                                Toast.makeText(view.getContext(), "Failed to get catalog", Toast.LENGTH_SHORT).show();
                            }
                            
                        });
                    }
				}
        		
        	}).start();
        }
        
        public ArrayList<Item> getCatalog(String username, String password) {
            ArrayList<Item> newItems = new ArrayList<Item>();
            
            String query = CATALOG_URL;
            // Create an XML packet with the information about the current image
            XmlSerializer xmlOut = Xml.newSerializer();
            StringWriter writer = new StringWriter();
            
            try {
                xmlOut.setOutput(writer);
                
                xmlOut.startDocument("UTF-8", true);
                
                xmlOut.startTag(null, "userinfo");

                xmlOut.attribute(null, "user", username);
                xmlOut.attribute(null, "pw", password);
                
                xmlOut.endTag(null, "userinfo");
                
                xmlOut.endDocument();

            } catch (IOException e) {
                // This won't occur when writing to a string
                return null;
            }
            
            final String xmlStr = writer.toString();
            
            /*
             * Convert the XML into HTTP POST data
             */
            String postDataStr;
            try {
                postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
            } catch (UnsupportedEncodingException e) {
            	return null;
            }
            
            /*
             * Send the data to the server
             */
            byte[] postData = postDataStr.getBytes();
            
            /**
             * Open a connection
             */
            InputStream stream = null;
            try {
            	URL url = new URL(query);
            	
            	HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            	
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
                conn.setUseCaches(false);

                OutputStream out = conn.getOutputStream();
                out.write(postData);
                out.close();
                
                /*BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    Log.i("userinfo", line);
                }*/
                
            	int responseCode = conn.getResponseCode();
            	if (responseCode != HttpURLConnection.HTTP_OK){
            		return null;
            	}
            	
            	stream = conn.getInputStream();
            	
            	try {
            		XmlPullParser xml = Xml.newPullParser();
            		xml.setInput(stream, UTF8);
            		
            		xml.nextTag();
            		xml.require(XmlPullParser.START_TAG, null, "userinfo");
            		
            		String status = xml.getAttributeValue(null, "status");
                    if(status.equals("no")) {
                        return null;
                    }
                    
                    while(xml.nextTag() == XmlPullParser.START_TAG) {
                        if(xml.getName().equals("user")) {
                            Item item = new Item();
                            item.name = xml.getAttributeValue(null, "name");
                            item.id = xml.getAttributeValue(null, "id");
                            newItems.add(item);
                        }
                        skipToEndTag(xml);
                    }                    

                    
            	} catch(XmlPullParserException ex) {
            		return null;
            	} catch (IOException ex) {
            		return null;
            	} finally {
                    try {
                        stream.close();
                    } catch(IOException ex) {
                        
                    }
                }
            	
            	
            } catch (MalformedURLException e) {
            	return null;
            } catch (IOException ex) {
            	return null;
            }
            
            return newItems;
        }
        
		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Item getItem(int position) {
			return items.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			if (view == null) {
				view = LayoutInflater.from(parent.getContext()).inflate(R.layout.catalog_item, parent, false);
			}
			
			TextView tv = (TextView)view.findViewById(R.id.textItem);
			tv.setText(items.get(position).name);
			
			return view;
		}
        
		public String getId(int position) {
			return items.get(position).id;
		}
		
		public String getName(int position) {
			return items.get(position).name;
		}
		
    }
    
    public boolean saveGame(Game game, String drawPhase, DrawingView view) {
        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "game");

            xml.attribute(null, "id", game.getGameId());
            xml.attribute(null, "p1", ((!game.getServerp1().equals("")) ? game.getServerp1() : game.getPlayer1Name()));
            xml.attribute(null, "pw", game.getPassword());
            xml.attribute(null, "p2", ((!game.getServerp2().equals("")) ? game.getServerp2() : game.getPlayer2Name()));
            xml.attribute(null, "p1score", Integer.toString(game.getPlayer1Score()));
            xml.attribute(null, "p2score", Integer.toString(game.getPlayer2Score()));
            xml.attribute(null, "category", game.getCategory());
            xml.attribute(null, "answer", game.getAnswer());
            xml.attribute(null, "hint", game.getTip());
            xml.attribute(null, "drawPhase", drawPhase);
            xml.attribute(null, "p1Drawing", drawPhase);
            /*if (game.getEditingPlayer() == 1) {
            	if (game.getServerp1().equals(game.getPlayer1Name())) {
                    xml.attribute(null, "p1drawing", "1");
            	}else {
                    xml.attribute(null, "p1drawing", "0");
            	}
            } else {
            	if (game.getServerp1().equals(game.getPlayer1Name())) {
                    xml.attribute(null, "p1drawing", "0");
            	}else {
                    xml.attribute(null, "p1drawing", "1");
            	}
            }*/
            
            if (view != null) {
            	view.saveXml(xml);
            } else {
            	xml.attribute (null, "drawing", "");
            }
            
            xml.endTag(null, "game");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return false;
        }
        
        final String xmlStr = writer.toString();
        
        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return false;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        
        InputStream stream = null;
        try {
            URL url = new URL(SAVE_GAME_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return false;
            } 
            
            /*BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i("userinfo", line);
            }*/
            
            stream = conn.getInputStream();
            
            if (!parseUserResult(stream)) {
            	return false;
            }
            
        } catch (MalformedURLException e) {
            // Should never happen
            return false;
        } catch (IOException ex) {
            return false;
        }
        return true;
    }

    
    public InputStream loadGame(String name, String pass) {
        // Create an XML packet with the information about the current image
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        
        try {
            xml.setOutput(writer);
            
            xml.startDocument("UTF-8", true);
            
            xml.startTag(null, "userinfo");

            xml.attribute(null, "user", name);
            xml.attribute(null, "pw", pass);
            
            xml.endTag(null, "userinfo");
            
            xml.endDocument();

        } catch (IOException e) {
            // This won't occur when writing to a string
            return null;
        }
        
        final String xmlStr = writer.toString();
        
        /*
         * Convert the XML into HTTP POST data
         */
        String postDataStr;
        try {
            postDataStr = "xml=" + URLEncoder.encode(xmlStr, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
        
        /*
         * Send the data to the server
         */
        byte[] postData = postDataStr.getBytes();
        
        InputStream stream = null;
        try {
            URL url = new URL(GET_GAME_URL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Length", Integer.toString(postData.length));
            conn.setUseCaches(false);

            OutputStream out = conn.getOutputStream();
            out.write(postData);
            out.close();

            int responseCode = conn.getResponseCode();
            if(responseCode != HttpURLConnection.HTTP_OK) {
                return null;
            } 
            
            /*BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                Log.i("userinfo", line);
            }*/
            
            stream = conn.getInputStream();
            return stream;
        } catch (MalformedURLException e) {
            // Should never happen
            return null;
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Create an XML parser for the result
     */
    public boolean parseUserResult(InputStream stream) {
        try {
            XmlPullParser xmlR = Xml.newPullParser();
            xmlR.setInput(stream, UTF8);
            
            xmlR.nextTag();      // Advance to first tag
            xmlR.require(XmlPullParser.START_TAG, null, "userinfo");
            
            String status = xmlR.getAttributeValue(null, "status");
            if(status.equals("no")) {
                return false;
            }
            
        } catch(XmlPullParserException ex) {
            return false;
        } catch(IOException ex) {
            return false;
        }
        return true;
    }
    
    
}
