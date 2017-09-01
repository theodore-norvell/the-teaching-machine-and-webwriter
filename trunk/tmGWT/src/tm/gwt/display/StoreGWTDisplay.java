package tm.gwt.display;

import java.util.Vector;

import com.google.gwt.user.client.ui.Button;

import telford.common.MouseEvent;
import telford.common.Point;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;
import tm.interfaces.StateInterface;
import tm.portableDisplays.DatumDisplay;
import tm.portableDisplays.PortableContextInterface;
import tm.portableDisplays.StoreDatumDisplay;
import tm.portableDisplays.StoreDisplayer;

public class StoreGWTDisplay extends DisplayAdapterGWT implements DataDisplayView{

	public static final int LOGICAL = 0; // each variable gets a similar box
	public static final int SCALED = 1; // box depth reflects variable's size
	public static final int BINARY = 2; // bit patterns for each byte


	private int view; // Current view being used (LOGICAL, SCALED, etc.)
	private StoreLayoutManagerGWT layoutManager;
	private RegionInterface region; // Reference to client
	private PortableContextInterface context = new GWTContext();
	private StoreDisplayer storeDisplayer;
	public StoreGWTDisplay(StateInterface e, PortableContextInterface context, String configId) {
		super(new StoreDisplayer(e, context), configId, null, 250, 75);
		if (this.displayer instanceof StoreDisplayer) {
			this.storeDisplayer = (StoreDisplayer) this.displayer;
		}
		this.context = context;
		if (configId.equalsIgnoreCase("Heap"))
			region = e.getHeapRegion();
		else if (configId.equalsIgnoreCase("Stack"))
			region = e.getStackRegion();
		else if (configId.equalsIgnoreCase("Static"))
			region = e.getStaticRegion();
		else if (configId.equalsIgnoreCase("Scratch"))
			region = e.getScratchRegion();
		else
			context.getAsserter().check(false,"No store display for region " + configId);
		
		this.storeDisplayer.getDisplayInfo().setRegion(region);
		view = LOGICAL;
		Button bLogical = new Button("<img src='/images/Logical.gif'/>");
		Button bScaled = new Button("<img src='/images/Scaled.gif'/>");
		Button bBinary = new Button("<img src='/images/Binary.gif'/>");
		toolBar.add(bLogical);
		toolBar.add(bScaled);
		toolBar.add(bBinary);
		myWorkPane.setStyleName("tm-smallScrollPanel");
		layoutManager = new StoreLayoutManagerGWT(this);
	}

	/**
	 * what kind of store view is currently being used?
	 * 
	 * @return LOGICAl SCALED or BINARY
	 */
	public int getDatumView() {
		return view;
	}

	/*
	 * In Store view, address width is fixed, while the other two are elastic.
	 * Name and value widths take for example expanding to fill a window unless
	 * that results in too small a size. Thus field widths are a function of the
	 * particular view.
	 */
	public int getNameW() {
		return 0;
	}

	public int getValueW() {
		return 0;
	}

	public int getAddressW() {
		return 0;
	}

	public RegionInterface getRegion() {
		return region;
	}

	public int getFirstY() {
		return myWorkPane.getHorizontalScrollPosition();
	}

	public int getLastY() {
		return myWorkPane.getHorizontalScrollPosition() + myWorkPane.getElement().getClientHeight() - 1;
	}
	
	public void refresh() {
//		if (displayer instanceof StoreDisplayer) {
//			((StoreDisplayer) displayer).setDisplayInfo(((GWTContext) context).getStoreDisplayerInfo());
//		}
		if (region == null)
			return;
		int frameBoundary = region.getFrameBoundary();
		DatumDisplay.updateFonts(context.getDisplayFont(), 12);
		setScale(1, DatumDisplay.baseHeight); // scrolling increment
		storeDisplayer.getDisplayInfo().getListDD().clear();
		for (int i = 0; i < region.getNumChildren(); i++) {
			Datum kid = region.getChildAt(i);
			DatumDisplay dd = DatumDisplay.getAssociated(kid, this);
			if (dd == null) {
				dd = new StoreDatumDisplay(kid, this, false);
			}
			dd.setGrayOut(i < frameBoundary);
			storeDisplayer.getDisplayInfo().getListDD().add(dd);
		}
		layoutManager.layoutDisplay();
		super.refresh();
	}

	public void buttonPushed(int i) {
//		Assert.check(i >= 0 && i < 3);
//		// Reset other buttons up
//		for (int j = 0; j < 3; j++) {
//			if (j != i)
//				buttons[j].setButton(false);
//			else
//				buttons[j].setButton(true);
//		}
		view = i;
		refresh();
	}

	protected void mouseJustClicked(MouseEvent evt) {
		if (evt == null)
			return;
		if (region == null)
			return;

		Point location = new Point(evt.getX(), evt.getY());
		for (int i = 0; i < region.getNumChildren(); i++) {
			Datum kid = region.getChildAt(i);
			DatumDisplay dd = DatumDisplay.getAssociated(kid, this);
			context.getAsserter().check(dd != null);
			if (dd.contains(location)) {
				dd.mouseClicked(location);
				break;
			}
		}
	}
	
	public String getDisplayString() {
		return "storeNode";
	}

	/**
	 * At the moment only top level datums are included in the selection
	 */
	public Vector<Datum> getSelected() {
		Vector<Datum> selection = new Vector<Datum>();
		for (int i = 0; i < region.getNumChildren(); i++) {
			Datum datum = region.getChildAt(i);
			DatumDisplay dd = DatumDisplay.getAssociated(datum, this);
			if (dd != null && dd.isSelected())
				selection.add(datum);
		}
		return selection;
	}
}
