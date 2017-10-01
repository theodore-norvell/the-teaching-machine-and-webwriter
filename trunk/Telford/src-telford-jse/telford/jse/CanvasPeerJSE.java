package telford.jse;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import telford.common.Font ;
import telford.common.FontMetrics ;

public class CanvasPeerJSE extends telford.common.peers.CanvasPeer {

	MyCanvas myCanvas;

	public CanvasPeerJSE(telford.common.Canvas canvas) {
		super(canvas);
		myCanvas = new MyCanvas();
	}

	@Override
	public Object getRepresentative() {
		return myCanvas;
	}

	@Override
	public void addMouseListener(int count) {
		UtilJSE.addMouseListener(myCanvas, count, component);
	}

	@Override
	public int getWidth() {
		return UtilJSE.getWidth(myCanvas);
	}

	@Override
	public int getHeight() {
		return UtilJSE.getHeight(myCanvas);
	}

    @Override
    public void resetSize(int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public FontMetrics getFontMetrics(Font f) {
        FontJSE fontJSE = (FontJSE) f ;
        java.awt.Font javaFont = fontJSE.f ;
        java.awt.FontMetrics javaFM = myCanvas.getGraphics().getFontMetrics( javaFont ) ;
        return new FontMetricsJSE( javaFM ) ;
    }

	@Override
	public void repaint() {
		UtilJSE.repaint(myCanvas);
	}
	
	@Override
	public void setStyleName(String styleName){
		
	}
	
	class MyCanvas extends JPanel{
		@Override public void paintComponent( Graphics g) {
			setBackground(Color.white);
			super.paintComponent(g);
			telford.common.Graphics tg = new GraphicsJSE( (Graphics2D) g) ;
			component.paintComponent(tg) ;
		}
	}
	
}