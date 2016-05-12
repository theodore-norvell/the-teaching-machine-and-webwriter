package telford.client.view;

import com.google.gwt.canvas.client.Canvas;

import telford.client.tm.ExpressionDisplay;
import telford.common.Component;

public class UtilGWT {

	public static void addMouseListener(Canvas canvas, int count, telford.common.Component component){}

//	public static int getWidth(MyComponent component) {
//		return component.getCanvas().getCanvasElement().getWidth();
//	}
//
//	public static int getHeight(MyComponent component) {
//		return component.getCanvas().getCanvasElement().getHeight();
//	}
//
//	public static void repaint(MyComponent component) {
//		component.paintComponent();
//		//TODO repaint process code here
//		//....
//	}
//	
//	public static void repaint(MyComponent mycomponent, Component component) {
//		mycomponent.paintComponent();
//		//TODO repaint process code here
//		//....
//	}
	
	public static int getWidth(ExpressionDisplay component) {
		return component.getCanvas().getCanvasElement().getWidth();
	}

	public static int getHeight(ExpressionDisplay component) {
		return component.getCanvas().getCanvasElement().getHeight();
	}

	public static void repaint(ExpressionDisplay mycomponent, Component component) {
		mycomponent.paintComponent(component);
	}
}
