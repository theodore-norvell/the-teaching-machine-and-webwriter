package tm.displayEngine;

import java.awt.event.MouseEvent;
import java.util.Vector;

import telford.common.Point;
import tm.configuration.Configuration;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;
import tm.interfaces.ImageSourceInterface;
import tm.interfaces.RegionInterface;
import tm.portableDisplays.StoreDisplayer;
import tm.subWindowPkg.SmallToggleButton;
import tm.subWindowPkg.ToolBar;
import tm.utilities.Assert;

/**
 * New class to display the contents of a memory store. Introduced in Fall, 1999
 * to replace older memory store display technique.
 *
 * This class is built on top of the notion of a {@link DatumDisplay}, a
 * lightweight class of self-drawing objects which represent the display of
 * datums on the screen. Since Datums are hierarchial, DatumDisplay objects may
 * be expanded or contracted, which affects the store display inasmuch as it
 * changes the total number of DatumDisplay objects available for display.
 */
public class StoreDisplay1 extends SwingDisplay implements DataDisplayView {

	private static final long serialVersionUID = 9057830260011019321L;

	// There are 3 separate views. The values correspond to the button order
	public static final int LOGICAL = 0; // each variable gets a similar box
	public static final int SCALED = 1; // box depth reflects variable's size
	public static final int BINARY = 2; // bit patterns for each byte

	// under program control
	private SmallToggleButton buttons[] = new SmallToggleButton[3];

	private int view; // Current view being used (LOGICAL, SCALED, etc.)

	private RegionInterface region; // Reference to client
	private tm.displayEngine.StoreLayoutManager1 layoutManager;
	private StoreDisplayer storeDisplayer;
	// =================================================================
	// Constructor
	// =================================================================

	/**
	 * Constructor
	 * 
	 * @param dc
	 *            The displayManager which owns this display
	 * @param r
	 *            the Region of memory represented by this display
	 * @param w
	 *            the subWindow which holds the display
	 * @param b
	 *            the array of buttons for the display
	 */
	public StoreDisplay1(DisplayManager dm, String configId) {
		super(dm, configId, new StoreDisplayer(dm.getCommandProcessor(), dm.getPortableContext()));
		if (this.displayer instanceof StoreDisplayer) {
			this.storeDisplayer = (StoreDisplayer) this.displayer;
		}

		// System.out.println("storeDisplay configId is " + configId);
		if (configId.equalsIgnoreCase("Heap"))
			region = commandProcessor.getHeapRegion();
		else if (configId.equalsIgnoreCase("Stack"))
			region = commandProcessor.getStackRegion();
		else if (configId.equalsIgnoreCase("Static"))
			region = commandProcessor.getStaticRegion();
		else if (configId.equalsIgnoreCase("Scratch"))
			region = commandProcessor.getScratchRegion();
		else
			Assert.error("No store display for region " + configId);
		this.storeDisplayer.getDisplayInfo().setRegion(region);
		view = LOGICAL;
		ImageSourceInterface imageSource = context.getImageSource();
		buttons[0] = new SmallToggleButton("Logical", imageSource);
		buttons[0].setToolTipText("Logical view");
		buttons[1] = new SmallToggleButton("Scaled", imageSource);
		buttons[1].setToolTipText("Scaled view");
		buttons[2] = new SmallToggleButton("Binary", imageSource);
		buttons[2].setToolTipText("Binary view");
		buttons[view].setButton(true);
		toolBar = new ToolBar(buttons);
		mySubWindow.addToolBar(toolBar);
		layoutManager = new tm.displayEngine.StoreLayoutManager1(this);
//		this.storeDisplayer.getDisplayInfo().setLayoutManager(layoutManager);
		// mySize = new Dimension(getSize());
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
		return getScrollPosition().y;
	}

	public int getLastY() {
		return getFirstY() + getViewportSize().height - 1;
	}

	public void setOnScreen(int first, int last) {
//		firstDatum = first;
//		lastDatum = last;
	}

	// =================================================================
	// Over-ride on parent refresh. Refresh is the only signal raised when
	// the state changes, thus all code that is state dependent must be called
	// from here
	// =================================================================
	public void refresh() {
		if (region == null)
			return;
		int frameBoundary = region.getFrameBoundary();
		DatumDisplay.updateFonts(context.getDisplayFont(),
				getToolkit().getFontMetrics(context.getDisplayFont()).getHeight());
		setScale(1, DatumDisplay.baseHeight); // scrolling increment
		storeDisplayer.getDisplayInfo().getListDD().clear();
		for (int i = 0; i < region.getNumChildren(); i++) {
			Datum kid = region.getChildAt(i);
			tm.portableDisplays.DatumDisplay dd = tm.portableDisplays.DatumDisplay.getAssociated(kid, this);
			if (dd == null) {
				dd = new tm.portableDisplays.StoreDatumDisplay1(kid, this, false);
			}
			dd.setGrayOut(i < frameBoundary);
			storeDisplayer.getDisplayInfo().getListDD().add(dd);
		}
		layoutManager.layoutDisplay();
		super.refresh();
	}

	// =================================================================
	// buttonPushed overide - used to choose between logical, scaled and
	// binary views
	// =================================================================
	public void buttonPushed(int i) {
		Assert.check(i >= 0 && i < 3);
		// Reset other buttons up
		for (int j = 0; j < 3; j++) {
			if (j != i)
				buttons[j].setButton(false);
			else
				buttons[j].setButton(true);
		}
		view = i;
		refresh();
	}

	protected void mouseJustClicked(MouseEvent evt) {
		super.mouseJustClicked(evt); 
		if (evt == null)
			return;
		if (region == null)
			return;

		Point location = new Point(evt.getX(), evt.getY());
		for (int i = 0; i < region.getNumChildren(); i++) {
			Datum kid = region.getChildAt(i);
			tm.portableDisplays.DatumDisplay dd = tm.portableDisplays.DatumDisplay.getAssociated(kid, this);
			Assert.check(dd != null);
			if (dd.contains(location)) {
				dd.mouseClicked(location);
				break;
			}
		}
	}

	public void notifyOfSave(Configuration config) {
		super.notifyOfSave(config);
		config.setValue("View", Integer.toString(view));
	}

	public void notifyOfLoad(Configuration config) {
		super.notifyOfLoad(config);
		int v;
		String temp = config.getValue("View");
		if (temp != null)
			v = new Integer(temp).intValue();
		else
			v = view;
		buttonPushed(v);
	}

	public String toString() {
		return "StoreDisplay for " + mySubWindow.getTitle();
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
