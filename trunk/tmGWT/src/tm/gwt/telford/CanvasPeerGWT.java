package tm.gwt.telford;

import telford.common.Font ;
import telford.common.FontMetrics ;
import telford.common.MouseListener;

import java.util.ArrayList;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration ;

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
	    myCanvas.addMouseListener(component);
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
		HandlerRegistration clickHandlerRegistration = null ;
	    
	    public void addMouseListener(telford.common.Component component){
	        int telfordCount = component.mouseListenerCount() ;
	        int platformCount = this.clickHandlerRegistration == null ? 0 : 1 ;
	        if( platformCount == 0 && telfordCount > 0 ) {
	            MouseListenerGWT mouseListener =  new MouseListenerGWT(component) ;
	            this.clickHandlerRegistration = canvas.addClickHandler( mouseListener ) ;
	        }
	        else if( platformCount > 0 && telfordCount == 0 ) {
	            this.clickHandlerRegistration.removeHandler(); 
	            this.clickHandlerRegistration = null ;
	        }
	    }

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
		
	}
}
