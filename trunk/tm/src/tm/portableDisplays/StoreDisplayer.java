package tm.portableDisplays;

import telford.common.Graphics;
import telford.common.MouseEvent;
import telford.common.MouseListener;
import telford.common.Point;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;
import tm.interfaces.StateInterface;
import tm.utilities.Assert;

public class StoreDisplayer extends PortableDisplayer implements DataDisplayView {
	
	// There are 3 separate views. The values correspond to the button order
	public static final int LOGICAL = 0; // each variable gets a similar box
	public static final int SCALED = 1; // box depth reflects variable's size
	public static final int BINARY = 2; // bit patterns for each byte
	
	private int width;
	private int height;
	private RegionInterface region; // Reference to client
	private int view; // Current view being used (LOGICAL, SCALED, etc.)
	private StoreDisplayerInfo displayInfo = new StoreDisplayerInfo();
	
	public StoreDisplayer(StateInterface model, PortableContextInterface context) {
		super(model, context);
		view = LOGICAL;
		
		addMouseListener( new MouseListener() {
			@Override
			public void mouseMoved(telford.common.MouseEvent e) {

			}
			@Override
			public void mouseClick(telford.common.MouseEvent e) {
				mouseJustClicked(e);
			} } );
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
	
	public void mouseJustClicked(MouseEvent evt) {
		if (evt == null)
			return;
		if (region == null)
			return;

		Point location = new Point(evt.getX(), evt.getY());
		for (int i = 0; i < region.getNumChildren(); i++) {
			Datum kid = region.getChildAt(i);
			tm.portableDisplays.DatumDisplay dd = tm.portableDisplays.DatumDisplay.getAssociated(kid, this);
			if (dd.contains(location)) {
				dd.mouseClicked(location);
				break;
			}
		}
	}

	@Override
	public int getNameW() {
		return 0;
	}

	@Override
	public int getValueW() {
		return 0;
	}

	@Override
	public int getAddressW() {
		return 0;
	}

	@Override
	public int getDatumView() {
		return view;
	}

	@Override
	public String getDisplayString() {
		return "storeNode";
	}
	
	
	
	
}
