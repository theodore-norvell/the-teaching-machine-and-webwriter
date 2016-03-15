package mun.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.builder.shared.DivBuilder;
import com.google.gwt.dom.builder.shared.ElementBuilderFactory;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Button;

public class TestCall implements EntryPoint {
	static Canvas canvas;
	static Context2d context;
	final static String htmlTagId = "testCall";

	public void onModuleLoad() {
		updateStateValue();
		makeButton();
		makeDivElement();
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
		//Call JS function to get current state
		int cState = getCState();
		String exp = "";
		String tempC = "";
		String tempF="";
		switch (cState) {
		case 0:
			exp = "";
			tempC = "";
			tempF = "";
			break;
		case 1:
			exp = "\ufffetempF\ufffb = (tempC * 5 / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 2:
			exp = "\ufffctempF\ufffb = (\ufffetempC\ufffb * 5 / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 3:
			exp = "\ufffctempF\ufffb = (\ufffe\ufffctempC\ufffb\ufffb * 5 / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 4:
			exp = "\ufffctempF\ufffb = (\uffff10\ufffb * \ufffe5\ufffb / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 5:
			exp = "\ufffctempF\ufffb = (\ufffe\uffff10\ufffb * \uffff5.0\ufffb\ufffb / 9) + 32 ";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 6:
			exp = "\ufffe\ufffctempF\ufffb = \uffff50.0\ufffb\ufffb";
			tempC = "10.0";
			tempF = "0.0";
			break;
		case 7:
			exp = "\uffff50.0\ufffb";
			tempC = "10.0";
			tempF = "0.0";
			break;
		}
		
		//Call JS function to update expression value
		updateExp(exp, tempC, tempF);
	}

	// ======================segment line==================================
	// ======Following methods defined for call JS code in GWT=============
	// ======================segment line==================================
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

	public static native void updateExp(String exp,String tempC, String tempF ) /*-{
		$wnd.updateExp(exp, tempC, tempF);
	}-*/;

	// ======================segment line==================================
	// ======Following methods defined for access by JS code===============
	// ======================segment line==================================
	public static native void updateStateValue() /*-{
		$wnd.updateStateValue = function() {
			@mun.client.TestCall::setExpByCState()();
		}
	}-*/;

	private static native void makeButton() /*-{
		$wnd.makeButton = function(str) {
			return @mun.client.TestCall::makeButton(Ljava/lang/String;)(str);
		};
	}-*/;

	private static native void makeDivElement() /*-{
		$wnd.makeDivElement = function() {
			return @mun.client.TestCall::makeDiv()();
		};
	}-*/;
}
