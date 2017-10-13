package tm.gwt.telford;

import telford.common.Font ;
import telford.common.FontMetrics ;
import telford.common.MouseListener;
import java.util.ArrayList;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;

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
	public void addMouseListener() {
        UtilGWT.addMouseListener(myCanvas, component);
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

    @Override
    public FontMetrics getFontMetrics(Font f) {
        Context2d context2d = myCanvas.canvas.getContext2d() ;
        return new FontMetricsGWT(context2d, f ) ;
    }

	class MyCanvas {
		Canvas canvas;
		ArrayList<MouseListener> mouseListeners = new ArrayList<MouseListener>() ;

		public MyCanvas() {
			if (Canvas.isSupported()) {
				canvas = Canvas.createIfSupported();
			} else {
				UtilGWT.error(UtilGWT.ERROR_NOT_SUPPORT_CANVAS, "MyCanvas constructor");
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
		
		public void addMouseListener(MouseListener mouseListener) {
			 mouseListeners.add(mouseListener);
			 canvas.addClickHandler( (ClickHandler) mouseListener ) ;
		}
		
		public int mouseListenerCount() {
		    return mouseListeners.size() ; }
		
		public MouseListener getMouseListeners(){
			return mouseListeners.get(0);
		}
		
		public void removeMouseListener(MouseListener mouseListener){
			mouseListeners.remove(mouseListener);
		}
		
	}
}
