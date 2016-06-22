package tm.gwt.telford;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;

public class CanvasPeerGWT extends telford.common.peers.CanvasPeer {

	MyCanvas myCanvas;

	CanvasPeerGWT(telford.common.Canvas canvas) {
		super(canvas);
		myCanvas = new MyCanvas();
	}

	@Override
	public void repaint() {
		myCanvas.paintComponent();
	}

	@Override
	public int getWidth() {
		return myCanvas.getCanvas().getCoordinateSpaceWidth();
	}

	@Override
	public int getHeight() {
		return myCanvas.getCanvas().getCoordinateSpaceHeight();
	}

	@Override
	public Object getRepresentative() {
		return myCanvas.getCanvas();
	}

	@Override
	public void addMouseListener(int count) {
	}

	@Override
	public void setStyleName(String styleName) {
		myCanvas.getCanvas().setStyleName(styleName);
	}
	@Override
	public void resetSize(int width, int height) {
		myCanvas.getCanvas().setCoordinateSpaceWidth(width);
		myCanvas.getCanvas().setCoordinateSpaceHeight(height);
	}

	class MyCanvas {
		Canvas canvas;

		public MyCanvas() {
			if (Canvas.isSupported()) {
				canvas = Canvas.createIfSupported();
				canvas.setStyleName("tm-canvas");// default canvas style
			} else {
				GWTUtil.error(GWTUtil.ERROR_NOT_SUPPORT_CANVAS, "MyCanvas constructor");
			}
		}

		public void paintComponent() {
			Context2d context = canvas.getContext2d();
			context.clearRect(0, 0, canvas.getOffsetWidth(), canvas.getOffsetHeight());
			telford.common.Graphics tg = new GraphicsGWT(context);
			component.paintComponent(tg);
		}

		public Canvas getCanvas() {
			return canvas;
		}
	}
}
