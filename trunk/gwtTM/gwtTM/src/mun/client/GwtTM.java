package mun.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.ElementBuilderFactory;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GwtTM implements EntryPoint {
	static Canvas canvas;
	static Context2d context;
	final static String htmlTagId = "gwtTM";

	public void onModuleLoad() {
		updateStateValue();
		makeButton();
		makeDivElement();
		getCanvasElement();
		
		getExpressionDisplay();
		refresh();
	}

	public static Element makeDiv() {
		DivBuilder divBuilder = ElementBuilderFactory.get().createDivBuilder();

		// Add attributes to the div.
		divBuilder.id("tm-expDisp");
		divBuilder.attribute("class", "tm-expression-display");
		divBuilder.endDiv();

		// Get the element out of the builder.
		Element div = divBuilder.finish();
		return div;
	}

	public static Element makeButton(String name) {
		Button button = new Button(name);
		Element ele = button.getElement();
		return ele;
	}

	public static void setExpByCState() {
		// Call JS function to get current state
		int cState = getCState();
		String exp = "";
		String tempC = "";
		String tempF = "";
		switch (cState) {
		case 0:
			exp = "";
			tempC = "";
			tempF = "";
			break;
		case 1:
			exp = "tempF = (tempC * 5 / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 2:
			exp = "tempF = (tempC * 5 / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 3:
			exp = "tempF = (tempC * 5 / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 4:
			exp = "tempF= (10 * 5 / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 5:
			exp = "tempF = (10 *  9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 6:
			exp = "tempF = 50.0";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 7:
			exp = "50.0";
			tempC = "10.0";
			tempF = "0.0";
			break;
		}

		// Call JS function to update expression value
		updateExp(exp, tempC, tempF);
	}

	private static ExpressionDisplay createExpressionDisplay() {
		ExpressionDisplay expressionDisplay = new ExpressionDisplay();
		return expressionDisplay;
	}

	private static String refreshExpression(ExpressionDisplay warpper) {
		warpper.setExpression(getExpFromJsState());
		return warpper.getExpression();
	}
	
	private static String refreshExpDisplay(ExpressionDisplay expDisplay, EvaluatorInterface evaluator){
		return expDisplay.refresh(evaluator);
	}

	// ======================segment line==================================
	// ======Following methods defined for call JS code in GWT=============
	// ====================================================================

	/**
	 * JSNI Java call JS Get current status from SateEvaluator defined on JS
	 * code
	 * 
	 * @param id
	 */

	public static native int getCState() /*-{
		var cState = $wnd.getCurrentState();
		return cState
	}-*/;

	public static native boolean getNativeVariableType(String jsVar)/*-{
		return eval('$wnd.' + jsVar);
	}-*/;

	public static native void updateExp(String exp, String tempC, String tempF) /*-{
		$wnd.updateExp(exp, tempC, tempF);
	}-*/;

	/*
	 * Access JSState and get expression
	 * */
	public static native String getExpFromJsState() /*-{
		var expJS = $wnd.getJsState().getExpression();;
		return expJS
	}-*/;

	// ======================segment line==================================
	// ======Following methods defined for access by JS code===============
	// ======================segment line==================================

	public static native void updateStateValue() /*-{
		$wnd.updateStateValue = function() {
			@mun.client.GwtTM::setExpByCState()();
		}
	}-*/;

	private static native void makeButton() /*-{
		$wnd.makeButton = function(str) {
			return @mun.client.GwtTM::makeButton(Ljava/lang/String;)(str);
		};
	}-*/;

	private static native void makeDivElement() /*-{
		$wnd.makeDivElement = function() {
			return @mun.client.GwtTM::makeDiv()();
		};
	}-*/;

	private static native void getCanvasElement() /*-{
		$wnd.getCanvasElement = function() {
			return @mun.client.ExpressionDisplay::getCanvasEle()();
//			return @mun.client.view.ExpDisplay::getCanvasEle()();
		};
	}-*/;

	private static native void getExpressionDisplay() /*-{
		$wnd.getExpressionDisplay = function() {
			return @mun.client.GwtTM::createExpressionDisplay()();
		}
	}-*/;

	private static native void refresh() /*-{
		$wnd.refresh = function(obj1,obj2) {
			var exp = @mun.client.GwtTM::refreshExpDisplay(Lmun/client/ExpressionDisplay;Lmun/client/EvaluatorInterface;)(obj1,obj2);
//			alert("exp is:" + exp);
			return exp;
		}
	}-*/;
}
