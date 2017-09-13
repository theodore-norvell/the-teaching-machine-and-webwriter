package tm.gwt.telford;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style.Unit;

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
		return myCanvas.getRepresentative().getCoordinateSpaceWidth();
	}

	@Override
	public int getHeight() {
		return myCanvas.getRepresentative().getCoordinateSpaceHeight();
	}

	@Override
	public Object getRepresentative() {
		return myCanvas.getRepresentative();
	}

	@Override
	public void addMouseListener(int count) {
	}

	@Override
	public void setStyleName(String styleName) {
		myCanvas.getRepresentative().setStyleName(styleName);
	}
	
	@Override
	public void resetSize(int width, int height) {
		myCanvas.getRepresentative().setWidth(width + Unit.PX.getType());
		myCanvas.getRepresentative().setHeight(height + Unit.PX.getType());
		myCanvas.getRepresentative().setCoordinateSpaceWidth(width);
		myCanvas.getRepresentative().setCoordinateSpaceHeight(height);
	}

	class MyCanvas {
		Canvas canvas;

		public MyCanvas() {
			if (Canvas.isSupported()) {
				canvas = Canvas.createIfSupported();
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

		public Canvas getRepresentative() {
			return canvas;
		}
	}
}
