package mun.client;

import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.CanvasElement;

public class ExpressionDisplay {
	public static Logger logger = Logger.getLogger("gwtLog");
	 private static String theExpression = null;
	private static Canvas canvas;
	private final static CssColor colorBlack = CssColor.make("black");
	private final static CssColor colorRed = CssColor.make("red");
	private final static CssColor colorBlue = CssColor.make("blue");

	// Simply copied from TM.ExpressionDisplay.
	private static final int LEFTMARGIN = 4;
	private static final int TOPMARGIN = 20;

	public static final char MARKER1 = '\uffff'; //red
	public static final char MARKER2 = '\ufffe';//non underline
	public static final char MARKER3 = '\ufffd';//underline
	public static final char MARKER4 = '\ufffc';//blue
	public static final char ENDMARKER = '\ufffb';

	public String getExpression() {
		return theExpression;
	}

	public void setExpression(String exp) {
		theExpression = exp;
	}

	public String refresh(EvaluatorInterface evaluator) {
//		logger.log(Level.SEVERE, "evaluator.getExpression()");
		theExpression = evaluator.getExpression();
//		logger.log(Level.SEVERE, "evaluator.getExpression() : result is "+ theExpression);
		return theExpression;
	}

	public static CanvasElement getCanvasEle() {
		canvas = null;
		if (canvas == null) {
			canvas = Canvas.createIfSupported();
		}
		Context2d context = canvas.getContext2d();
		drawArea(context);
		CanvasElement canvasEle = canvas.getCanvasElement();
		canvasEle.setAttribute("class", "tm-canvas");
		canvasEle.setAttribute("name", "tm-expEngineCanvas");
		canvasEle.setAttribute("id", "tm-expEngineCanvas");
		return canvasEle;
	}

	public static void drawArea(Context2d context) {
		int advance = LEFTMARGIN;
		context.setFont("normal 16px serif");

		if (theExpression.length() > 0) {
			char tempArray[] = theExpression.toCharArray();
			
			context.setFillStyle(colorBlack);
			CssColor currColor = colorBlack;
			boolean currUnderline = false;
			int currWidth = 0;
			int baseline = TOPMARGIN + 12;
			Vector<Object> attrStack = new Vector<>();
			for (int i = 0, sz = theExpression.length(); i < sz; ++i) {
				char c = tempArray[i]; // Next character
				switch (c) {
				case MARKER1:
					attrStack.addElement("red");
					 currColor = colorRed ;
					context.setFillStyle(currColor);
					break;
				case MARKER2:
					attrStack.addElement(new Boolean(currUnderline));
					currUnderline = false;
					break;
				case MARKER3:
					attrStack.addElement(new Boolean(currUnderline));
					currUnderline = true;
					break;
				case MARKER4:
					attrStack.addElement("blue");
					currColor = colorBlue ;
					context.setFillStyle(currColor);
					break;
				case ENDMARKER:
					Object temp = attrStack.elementAt(attrStack.size() - 1);
					logger.log(Level.SEVERE, temp.toString());
					if (temp instanceof String) {
						logger.log(Level.SEVERE, "temp is CssColor");
						currColor = temp.equals("red") ? colorRed : colorBlue;
						context.setFillStyle(currColor);
					} 
					else{
//						logger.log(Level.SEVERE, "temp is Boolean");
						currUnderline = ((Boolean) temp).booleanValue();
					}
					attrStack.removeElementAt(attrStack.size() - 1);
					break;
				default:
//					logger.log(Level.SEVERE, String.valueOf(tempArray[i]));
					currWidth = (int)(context.measureText(String.valueOf(tempArray[i])).getWidth());
					context.fillText(String.valueOf(tempArray[i]), advance, baseline);
					if (currUnderline) {
						context.setFillStyle(colorBlack);
						context.fillRect( advance, baseline+2, currWidth, +3);
						context.setFillStyle(currColor);
					}
					advance += currWidth;// advances[c];
				} // Switch
			} // For loop
		} // End if we have an expression

	}
	public ExpressionDisplay() {
	}
}