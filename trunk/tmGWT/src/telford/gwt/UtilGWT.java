package telford.gwt;

import com.google.gwt.user.client.ui.Widget;

import telford.gwt.CanvasPeerGWT.MyCanvas;

public class UtilGWT {

	public static void addMouseListener(Widget myComponent, int count, telford.common.Component component){
	}

	public static int getWidth(Widget component) {
		return component.getOffsetWidth();
	}

	public static int getHeight(Widget component) {
		return component.getOffsetHeight();
	}

	public static void repaint(MyCanvas component) {
		component.paintComponent();
	}
}
