package telford.gwt;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.user.client.ui.Widget;

public class CanvasPeerGWT extends telford.common.peers.CanvasPeer {

	MyCanvas myCanvas;

	CanvasPeerGWT(telford.common.Canvas canvas) {
		super(canvas);
		myCanvas = new MyCanvas();
	}

	@Override
	public void repaint() {
		UtilGWT.repaint(myCanvas);
	}

	@Override
	public int getWidth() {
		return UtilGWT.getWidth(myCanvas);
	}

	@Override
	public int getHeight() {
		return UtilGWT.getHeight(myCanvas);
	}

	@Override
	public Object getRepresentative() {
		return myCanvas;
	}

	@Override
	public void addMouseListener(int count) {
	}

	class MyCanvas extends Widget {
		Canvas canvas;

		public MyCanvas() {
			if (Canvas.isSupported()) {
				canvas = Canvas.createIfSupported();
				canvas.getCanvasElement().setAttribute("class", "tm-canvas");
			} else {
				GWTUtil.error(GWTUtil.ERROR_NOT_SUPPORT_CANVAS, "MyCanvas constructor");
			}
		}

		public void paintComponent() {
			Context2d context = canvas.getContext2d();
			telford.common.Graphics tg = new GraphicsGWT(context);
			component.paintComponent(tg);
		}
		
		public Canvas getCanvas(){
			return canvas;
		}
	}
}
