package mun.client.view;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.CanvasElement;

public class ExpDisplay {
	private static Canvas canvas;
	final static CssColor colorBlack = CssColor.make("black");
	public static CanvasElement getCanvasEle(){
		canvas = null;
		if(canvas == null){
			canvas = Canvas.createIfSupported();
		}
		//leave drawing at JS-side
		doFillText(canvas);
		CanvasElement canvasEle = canvas.getCanvasElement();
		canvasEle.setAttribute("class", "tm-canvas");
		canvasEle.setAttribute("name", "tm-expEngineCanvas");
		canvasEle.setAttribute("id", "tm-expEngineCanvas");
		return canvasEle;
	}
	
	private static void doFillText(Canvas canvas){
		Context2d context = canvas.getContext2d();
		context.setFont("12pt times");
        context.setFillStyle(colorBlack);
        context.fillText("<span>&#8594;</span>", 25, 25);
	}
	
	public static String getExpression(int state) {
		String[] expList = { "Expression_1", "Expression_2", "Expression_3" };
		int len = expList.length;
		if (0 <= state && state < len)
			return expList[state];
		else
			return "NULL";
	}

}
