package telford.client.view;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.CanvasElement;

import telford.client.tm.ExpressionDisplay;

public class ComponentPeerGWT extends telford.common.peers.ComponentPeer{

//	MyComponent myComponent;
	ExpressionDisplay myComponent;
	
	public ComponentPeerGWT(telford.common.Component component) {
		super(component);
//		myComponent = new MyComponent();
		myComponent = new ExpressionDisplay();
	}

	@Override
	public Object getRepresentative() {
		return myComponent;
	}

	@Override
	public void addMouseListener(int count) {
//		UtilJSE.addMouseListener(canvas, count, component);
	}

	@Override
	public int getWidth() {
		return UtilGWT.getWidth(myComponent);
	}

	@Override
	public int getHeight() {
		return UtilGWT.getHeight(myComponent);
	}

	@Override
	public void repaint() {
		UtilGWT.repaint(myComponent, component);
	}
	
//	class MyComponent{
//		Canvas canvas;
//		public MyComponent(){
//			canvas = Canvas.createIfSupported();
//			CanvasElement canvasEle = canvas.getCanvasElement();
//			canvasEle.setAttribute("class", "tm-canvas");
//		}
//		
//		public void paintComponent( ) {
//			Context2d c = canvas.getContext2d();
//			telford.common.Graphics tg = new GraphicsGWT(c) ;
//			component.paintComponent(tg) ;
//		}
//		
//		public Canvas getCanvas(){
//			return canvas;
//		}
//	}
}