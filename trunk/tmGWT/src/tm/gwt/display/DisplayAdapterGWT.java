package tm.gwt.display;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.event.dom.client.ClickEvent;

import telford.common.MouseEvent;
import tm.gwt.telford.MouseListenerGWT;
import tm.portableDisplays.PortableDisplayer;

public class DisplayAdapterGWT extends WorkAreaGWT {
	public PortableDisplayer displayer ;
	
	private int verticalScale, horizontalScale;
	
	public DisplayAdapterGWT(PortableDisplayer displayer, String rootName, String title, int canvasWidth, int canvasHeight) {
	    super(title, rootName);
	    this.displayer = displayer ;
	    displayer.resetSize(canvasWidth, canvasHeight);
	    verticalScale = 1;
	    horizontalScale = 1;
	}
	
	public void refresh() {
//		displayer.refresh();
        displayer.repaint();
    }
	
	public void setScale(int hScale, int vScale){
	    horizontalScale = hScale;
	    verticalScale = vScale;
	}
	
	public int getHScale(){ return horizontalScale;}
	
	public int getVScale(){ return verticalScale;}
}
