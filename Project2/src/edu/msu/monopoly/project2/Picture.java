package edu.msu.monopoly.project2;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.util.Xml;

public class Picture implements Serializable {

	/**
	 * Compiler generated serialID
	 */
	private static final long serialVersionUID = -7698564561406540923L;
	
	/**
	 *  An array of the Drawings that make up the Picture
	 */
	private ArrayList<Drawing> drawings = new ArrayList<Drawing>();
	
	private float angle = 0;
    private float scale = 1;
    private float offsetX = 0;
    private float offsetY = 0;
	
	public Picture() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Add drawing to array of drawings
	 * @param drawing to be added to array
	 */
	public void AddDrawing(Drawing drawing) {
		drawings.add(drawing);
	}
	
	/**
	 * Get drawing from the drawings array
	 * @param index of were the drawing is in the array
	 * @return the drawing at the given index
	 */
	public Drawing GetDrawing(int index) {
		return drawings.get(index);
	}

	/**
	 * Get array containing drawing
	 * @return drawings the array containing the drawings
	 */
	public ArrayList<Drawing> getDrawings() {
		return drawings;
	}

	/**
	 * Set the array of drawings
	 * @param drawings the array of drawings
	 */
	public void setDrawings(ArrayList<Drawing> drawings) {
		this.drawings = drawings;
	}

	private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
    }

	private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
		//in.registerValidation(this, 0);
		in.defaultReadObject();
	}

	public void setOffsetX(float offsetX) {
		this.offsetX = offsetX;
	}

	public float getOffsetY() {
		return offsetY;
	}

	public void setOffsetY(float offsetY) {
		this.offsetY = offsetY;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getScale() {
		return scale;
	}

	public void setScale(float scale) {
		this.scale = scale;
	}

	public float getOffsetX() {
		return offsetX;
	}
	
	public String getXml() {
        XmlSerializer xml = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try {
            xml.setOutput(writer);
            
            xml.startTag(null, "picture");

            xml.attribute(null, "angle", Float.toString(angle));
            xml.attribute(null, "scale", Float.toString(scale));
            xml.attribute(null, "offsetX", Float.toString(offsetX));
            xml.attribute(null, "offsetY", Float.toString(offsetY));
            
            for (int i = 0; i < drawings.size(); i++) {
            	drawings.get(i).addXml(xml);
            }
            xml.endTag(null, "picture");
            
		} catch (IOException ex){
			
		}
        return writer.toString();
        
	}
	
	public void loadXml(String xmlStr) {
		try {
			XmlPullParser xml = Xml.newPullParser();
			xml.setInput(new StringReader(xmlStr));
			
			xml.require(XmlPullParser.START_TAG, null, "picture");
			
			angle = Float.parseFloat(xml.getAttributeValue(null, "angle"));
			scale = Float.parseFloat(xml.getAttributeValue(null, "scale"));
			offsetX = Float.parseFloat(xml.getAttributeValue(null, "offsetX"));
			offsetY = Float.parseFloat(xml.getAttributeValue(null, "offsetY"));
            
            while(xml.nextTag() == XmlPullParser.START_TAG) {
                if(xml.getName().equals("drawing")) {
                    Drawing d = new Drawing();
                    d.loadXml(xml);
                    drawings.add(d);
                }
                Cloud.skipToEndTag(xml);
            }  
			
		} catch (XmlPullParserException e) {
			
		} catch (IOException e) {
			
		}
	}
	
}
