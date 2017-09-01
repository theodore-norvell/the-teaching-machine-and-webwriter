package tm.gwt.display;

import com.google.gwt.canvas.client.Canvas;

import tm.portableDisplays.PortableDisplayer;

public class DisplayAdapterGWT extends WorkAreaGWT {
	public PortableDisplayer displayer ;
	
	private int verticalScale, horizontalScale;
	
	public DisplayAdapterGWT(PortableDisplayer displayer, String rootName, String title, int canvasWidth, int canvasHeight) {
		super(title, rootName);
        this.displayer = displayer ;
        displayer.resetSize(canvasWidth, canvasHeight);
        
        myWorkPane.add((Canvas)displayer.getPeer().getRepresentative());
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
