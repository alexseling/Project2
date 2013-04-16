package edu.msu.monopoly.project2;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Drawing implements Serializable {
	
	/**
	 * Compiler generated serialID
	 */
	private static final long serialVersionUID = 5453580233120382544L;

	// the paint for the freehand drawing - color and width
	private transient Paint linePaint = new Paint();
	
	PaintParameters params = new PaintParameters();
	
	// the location of a Drawing is a list of x and y coordinates
	// that a touch passes through while drawing.
	// to rotate the drawing, etc, each of these points must be changed
	// according to the rotation, etc, function
	private ArrayList<Point> points = new ArrayList<Point>();
	
	private class PaintParameters implements Serializable {
		/**
		 * Compiler generated serialID
		 */
		private static final long serialVersionUID = -7719719728244930024L;
		int color;
		float width;
	}
	
	private class Point implements Serializable {
		/**
		 * Compiler generated serialID
		 */
		private static final long serialVersionUID = -4709952542099601585L;
		public float x;
		public float y;
		
		Point(float a, float b) {
			x = a;
			y = b;
		}
	}
	
	// should scale and rotation variables be kept here?
	
	/**
	 * Draw a drawing by connecting each coordinates in points
	 * @param canvas where drawing appears
	 */
	public void DrawLine(Canvas canvas) {
		
		linePaint = new Paint();
		linePaint.setColor(params.color);
		linePaint.setStrokeWidth(params.width);
		//}
		for (int i=1; i<this.points.size(); i++) {
			canvas.drawLine(this.points.get(i-1).x, this.points.get(i-1).y, 
					this.points.get(i).x, this.points.get(i).y, linePaint);
			canvas.drawCircle(this.points.get(i-1).x, this.points.get(i-1).y, 
					params.width/2, linePaint);
		}
		canvas.drawCircle(this.points.get(this.points.size()-1).x, this.points.get(this.points.size()-1).y, 
				params.width/2, linePaint);
	}
	
	public void RotateDrawing(final float ca, final float sa, final float x1, final float y1)
	{
		float xp, yp;
		for (int i=0; i<this.points.size(); i++) {
			xp = (this.points.get(i).x - x1) * ca - (this.points.get(i).y - y1) * sa + x1;
	        yp = (this.points.get(i).x - x1) * sa + (this.points.get(i).y - y1) * ca + y1;
			
	        this.points.set(i, new Point(xp, yp));
		}
	}
	
	public void ScaleDrawing(final float scaleFactor, final float centerX, final float centerY) {
		
		for (int i=0; i<this.points.size(); i++) {
			this.points.get(i).x += (this.points.get(i).x - centerX) * (scaleFactor - 1); 
			this.points.get(i).y += (this.points.get(i).y - centerY) * (scaleFactor - 1);
			
		}
	}
	
	public void addPoint(float x, float y) {
		points.add(new Point(x, y));
	}

	public Paint getLinePaint() {
		return linePaint;
	}

	public void setLinePaint(Paint linePaint) {
		this.linePaint = linePaint;
		params.color = linePaint.getColor();
		params.width = linePaint.getStrokeWidth();
	}
	
	public void addXml(XmlSerializer xml) {
        
		try {
	        xml.startTag(null, "drawing");
	
	        xml.attribute(null, "color", Integer.toString(params.color));
	        xml.attribute(null, "width", Float.toString(params.width));
	        
	        for (int i = 0; i < points.size(); i++) {
		        xml.startTag(null, "point");
		        xml.attribute(null, "x", Float.toString(points.get(i).x));
		        xml.attribute(null, "y", Float.toString(points.get(i).y));
		        xml.endTag(null, "point");
	        }
	        xml.endTag(null, "drawing");
		} catch (IOException ex) {
			
		}
	}
	
	public void loadXml(XmlPullParser xml) throws XmlPullParserException, IOException {
		params.color = Integer.parseInt(xml.getAttributeValue(null, "color"));
		params.width = Float.parseFloat(xml.getAttributeValue(null, "width"));
		
		while(xml.nextTag() == XmlPullParser.START_TAG) {
            if(xml.getName().equals("point")) {
                Point p = new Point(Float.parseFloat(xml.getAttributeValue(null, "x")), 
                		Float.parseFloat(xml.getAttributeValue(null, "y")));
                points.add(p);
            }
            Cloud.skipToEndTag(xml);
        }  
	}
	
}
