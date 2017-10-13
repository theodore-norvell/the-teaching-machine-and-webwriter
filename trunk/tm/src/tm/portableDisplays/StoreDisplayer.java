package tm.portableDisplays;

import telford.common.Graphics;
import tm.interfaces.StateInterface;

public class StoreDisplayer extends PortableDisplayer {
	private int width;
	private int height;
	private StoreDisplayerInfo displayInfo = new StoreDisplayerInfo();
	
	public StoreDisplayer(StateInterface model, PortableContextInterface context) {
		super(model, context);
	}

	public StoreDisplayerInfo getDisplayInfo() {
		return displayInfo;
	}

	public void setDisplayInfo(StoreDisplayerInfo displayInfo) {
		this.displayInfo = displayInfo;
	}

	@Override
	public void paintComponent(Graphics g) {
		drawArea(g);
	}

	public void drawArea(Graphics screen) {
	    if (screen == null || displayInfo.getRegion() == null) return;
//	    Debug.getInstance().msg(Debug.DISPLAY, "drawing " + this);
	    
	    if (width != getWidth() || height != getHeight()) {
//	    	displayInfo.getLayoutManager().layoutDisplay();
	        width = getWidth();
	        height = getHeight();
	    }
	    
	    for (int i = 0; i < displayInfo.getListDD().size(); i++) {
	    	DatumDisplay dd = displayInfo.getListDD().get(i);
	    	dd.draw(screen);
		}
	}
	
	
	
	
}
