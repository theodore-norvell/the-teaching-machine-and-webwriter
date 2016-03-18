package mun.client.view;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.dom.client.CanvasElement;

public class ExpDisplay {
	private static Canvas canvas;

	public static CanvasElement getCanvasEle(){
		canvas = null;
		if(canvas == null){
			canvas = Canvas.createIfSupported();
		}
		
		CanvasElement canvasEle = canvas.getCanvasElement();
		canvasEle.setAttribute("class", "tm-canvas");
		canvasEle.setAttribute("name", "expCanvas");
		canvasEle.setAttribute("id", "expCanvas");
		return canvasEle;
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
