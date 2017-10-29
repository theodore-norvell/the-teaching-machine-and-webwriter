package telford.common.peers;

import telford.common.Canvas;
import telford.common.Font ;
import telford.common.FontMetrics ;

public abstract class CanvasPeer extends ComponentPeer {
	
	public CanvasPeer( Canvas canvas) {
		super(canvas);
	}
	
	abstract public void resetSize(int width, int height) ;

    abstract public FontMetrics getFontMetrics(Font f) ;
}
