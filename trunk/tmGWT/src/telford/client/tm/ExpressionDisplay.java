package telford.client.tm;

import java.util.Vector;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.CanvasElement;

import telford.client.view.GraphicsGWT;
import telford.common.Component;
import telford.common.Graphics;

public class ExpressionDisplay implements TMDisplayInterface{
	private  String theExpression = "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 ";//for test
	private  Canvas canvas;
	private final  CssColor colorBlack = CssColor.make("black");
	private final  CssColor colorRed = CssColor.make("red");
	private final  CssColor colorBlue = CssColor.make("blue");

	// Simply copied from TM.ExpressionDisplay.
	private  final int LEFTMARGIN = 4;
	private  final int TOPMARGIN = 20;

	public  final char MARKER1 = '\uffff'; //red
	public  final char MARKER2 = '\ufffe';//non underline
	public  final char MARKER3 = '\ufffd';//underline
	public  final char MARKER4 = '\ufffc';//blue
	public  final char ENDMARKER = '\ufffb';

	public String getExpression() {
		return theExpression;
	}

	public void setExpression(String exp) {
		theExpression = exp;
	}

	public String refresh(EvaluatorInterface evaluator) {
		theExpression = evaluator.getExpression();
		return theExpression;
	}

	public CanvasElement getCanvasEle() {
		return canvas.getCanvasElement();
	}
	public void paintComponent(Component component ) {
		Context2d context = canvas.getContext2d();
		drawArea(context);
		Graphics tg = new GraphicsGWT(context) ;
		component.paintComponent(tg);
	}
	public void drawArea(Context2d context) {
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
					if (temp instanceof String) {
						currColor = temp.equals("red") ? colorRed : colorBlue;
						context.setFillStyle(currColor);
					} 
					else{
						currUnderline = ((Boolean) temp).booleanValue();
					}
					attrStack.removeElementAt(attrStack.size() - 1);
					break;
				default:
					currWidth = (int)(context.measureText(String.valueOf(tempArray[i])).getWidth());
					context.fillText(String.valueOf(tempArray[i]), advance, baseline);
					if (currUnderline) {
						context.setFillStyle(colorBlack);
						context.fillRect( advance, baseline+2, currWidth, +3);
						context.setFillStyle(currColor);
					}
					advance += currWidth;// advances[c];
				}
			}
		}
	}
	
	public Canvas getCanvas(){
		return canvas;
	}
	public ExpressionDisplay() {
		canvas = Canvas.createIfSupported();
		CanvasElement canvasEle = canvas.getCanvasElement();
		canvasEle.setAttribute("class", "tm-canvas");
//		canvasEle.setAttribute("name", "tm-expEngineCanvas");
//		canvasEle.setAttribute("id", "tm-expEngineCanvas");
	}
}