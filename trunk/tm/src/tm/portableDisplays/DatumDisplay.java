//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

package tm.portableDisplays;

import telford.common.Dimension;
import telford.common.Font;
import telford.common.Graphics;
import telford.common.Kit;
import telford.common.Line;
import telford.common.Point;
import telford.common.Rectangle;
import tm.displayEngine.D3Iterator1;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;

/*  Class to display the contents of a single Datum. DatumDisplay
    objects are very lightweight. Each is associated with a particular 
    view of a single datum.
 
    Extensively ammended March, 2001. The DatumDisplay Class has been broken up
    into multiple classes. DatumDisplay iteslf is now abstract. It functions partly
    as an interface for a whole family of DatumDisplay classes, partly as a repository
    for code common to its concrete descendents. The default code now generally assumes
    the datum it represents is scalar. All code having to  do with the display of
    compound datums has been moved to a descendent DatumDisplayCompound class. This
    is also abstract.
    
    Concrete classes for particular views (e.g. linear store, linked) are derived
    from BOTH abstract classes. Thus, each view will require at least two concrete
    DatumDisplay classes, one for scalar and one for compound datums.
    
    The second major change is that DatumDisplay objects used to be transient,
    existing only as long as a Datum was in view. Persistent information was kept
    in the expander which was attached directly to the Datum. Now, DatumDisplay
    objects are attached to the Datum (to allow for multiple views) and expanders
    are attached to the DatumDisplay objects. Thus DatumDisplay objects are more
    persistent.

*/

public abstract class DatumDisplay extends Object {

	static final int LAST_STEP = 20;

	// birthOrders
	public static final int ONLY = 0;
	public static final int OLDEST = 1;
	public static final int IN_BETWEEN = 2;
	public static final int YOUNGEST = 3;

	protected static Font addressFont = Kit.getKit().getFont("Dialog", 0, 12);
	protected static Font nameFont = Kit.getKit().getFont("Dialog", 1, 12);
	protected static Font valueFont = Kit.getKit().getFont("Dialog", 0, 12);

	/*
	 * Sizes
	 */
	protected static int STRING_OFFSET = 5; // String offset from box edge
	public static int baseHeight = 14; // Used to set scroll inc as well

	/*
	 * constants for categorizing where the mouse clicked. See checkClick()
	 * method
	 */
	public static final int NOT_IN_BOX = 0;
	public static final int IN_BOX = 1;
	public static final int EXPANDER_HIT = 2;

	/*
	 * static value providing functions. Most of them provide x positions for
	 * the internal components which are likely to vary from sub-class to
	 * sub-class. They should be over-ridden in the concrete classes.
	 */
	public static int getNameX(int nestLevel) {
		return 0;
	}

	public static int getValueX(int nestLevel) {
		return 0;
	}

	public static int getAddressX(int nestLevel) {
		return 0;
	}

	public static int getDescenderX(int nestLevel) {
		return 0;
	}

	public static int getExpanderX(int nestLevel) {
		return 0;
	}

	public static int getCompoundXLeft(int nestLevel) {
		return 0;
	}

	public static int getCompoundXRight(int nestLevel) {
		return 0;
	}

	/*
	 * Reference to the datum associated with this datumDisplay.
	 */
	protected Datum myDatum;
	protected Expander myExpander;

	/*
	 * Reference to the toplevel display of this datumDisplay.
	 */
	protected DataDisplayView myDataView = null; // Change this to a
													// memoryDisplay

	protected int nestLevel;

	/*
	 * References to the boxes that hold the various strings that define the
	 * datum
	 */
	protected StringBox nameBox;
	protected StringBox addressBox;
	protected StringBox valueBox; // Primary value box

	/*
	 * If the associated datum is the child of another datum, specifies whether
	 * it is a oldest, middle, or youngest child. Leaders are drawn slightly
	 * differently
	 */
	protected int birthOrder;

	/*
	 * The extent of the displayDatum in its parent's drawing (pixel)
	 * co-ordinates
	 */
	protected Rectangle extent;

	/* is this datum grayed out? */
	protected boolean grayOut = false;
	// Have I been selected?
	protected boolean selected = false;

	// =================================================================
	// Constructor
	// =================================================================

	/*
	 * 3/08/2001: Major change. Since every Datum in a region being displayed
	 * has to have a DatumDisplay attached, whether or not it is actually being
	 * displayed (i.e. on screen), DatumDisplays should be able to discover
	 * their own nesting level and their birth order. This can make creating
	 * them considerable easier since currently any Datum that is discovered to
	 * have no DatumDisplay attached gets one attached. The point of discovery
	 * may not have easy access to the nesting level. Further, when a
	 * DatumDisplay is constructed, it initializes expander boxes per the new
	 * expand argument.
	 */

	public DatumDisplay(Datum datum, DataDisplayView displayView, boolean expand) {
		myDataView = displayView;
		myDatum = datum;
		Datum parent = datum.getParent();
		// System.out.println("0: datum is " + datum + " and displayView is " +
		// displayView);
		if (parent == null) { // Top level datum
			nestLevel = 0;
			birthOrder = ONLY;
		} else {
			DatumDisplay pdd = getAssociated(parent, myDataView);
			// parent is not part of display so effectively 0?????
			nestLevel = (pdd == null) ? 0 : pdd.nestLevel + 1;
			if (parent.getNumChildren() == 1)
				birthOrder = ONLY;
			else if (datum.getBirthOrder() == 0)
				birthOrder = OLDEST;
			else if (datum.getBirthOrder() == parent.getNumChildren() - 1)
				birthOrder = YOUNGEST;
			else
				birthOrder = IN_BETWEEN;
		}
		if (myDatum.getNumChildren() > 0) {
			myExpander = new Expander();
			myExpander.setExpanded(expand);
		} else
			myExpander = null; // Default for scalars

		// Size doesn't matter as they will be resized by layout routine
		extent = new Rectangle(0, 0, 0, 0);
		// Hook me into the datum
		myDatum.setProperty(myDataView.getDisplayString(), this);
		nameBox = new StringBox(datum.getName(), false, StringBox.RIGHT, StringBox.MIDDLE);
		valueBox = new StringBox(datum.getValueString(), true, StringBox.LEFT, StringBox.MIDDLE);
	}

	// Service routine to return the DatumDisplay associated with a Datum
	public static DatumDisplay getAssociated(Datum datum, DataDisplayView view) {
		return (DatumDisplay) (datum.getProperty(view.getDisplayString()));
	}

	public D3Iterator1 getIterator() {
		return new D3Iterator1(this, myDataView);
	}

	public static void updateFonts(Font baseFont, int height) {
		addressFont = Kit.getKit().getFont(baseFont.getName(), 0, baseFont.getSize());
		nameFont = Kit.getKit().getFont(baseFont.getName(), 1, baseFont.getSize());
		valueFont = Kit.getKit().getFont(baseFont.getName(), 0, baseFont.getSize());
		baseHeight = height + 1;
	}

	public Point getLocation() {
		return new Point(extent.x, extent.y);
	}

	public Dimension getSize() {
		return new Dimension(extent.width, extent.height);
	}

	public boolean contains(Point p) {
		return extent.contains(p);
	}

	public Datum getDatum() {
		return myDatum;
	}

	public Expander getExpander() {
		return myExpander;
	}

	public void move(int x, int y) {
		extent.x = x;
		extent.y = y;

		/*
		 * move kids, because all datums are located absolutely but NOT the
		 * expander which is located relatively
		 */
		if (myExpander != null && myExpander.getExpanded()) {
			y = 0; // new relative positon of kids
			for (int i = 0; i < myDatum.getNumChildren(); i++) {
				DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
				kid.move(extent.x, extent.y + y);
				y += kid.extent.height;
			}
		}
	}

	/*
	 * Resize is responsible for locating all subcomponents properly. In this
	 * default code, components are laid out from left to right starting with
	 * the nameBox. After it comes a gap for expanders (whether or not the
	 * DatumDisplay object has one) followed by the valueBox, finally by an
	 * address box if one exists. Resize governs their PLACEMENT. Draw handles
	 * their appearance.
	 */
	public void resize(int nWidth, int vGap, int vWidth, int aWidth) {
		vGap += Expander.EXPAND_OFFSET; // add my expander offset to gap
		vWidth -= Expander.EXPAND_OFFSET; // and reduce my value width to
											// compensate
		extent.width = nWidth + vGap + vWidth + aWidth;
		extent.height = baseHeight; // fine for scalars in logical mode
		nameBox.move(0, 0); // Change
		nameBox.resize(nWidth, baseHeight);
		nameBox.nudge(STRING_OFFSET, 0);
		valueBox.move(nWidth + vGap, 0); // Change
		valueBox.resize(vWidth, baseHeight);
		valueBox.nudge(STRING_OFFSET, 0);
		if (addressBox != null) {
			addressBox.move(nWidth + vGap + vWidth, 0); // Change
			addressBox.resize(aWidth, baseHeight);
			addressBox.nudge(STRING_OFFSET, 0);
		}
		if (myExpander != null) { // Compound Datum
			myExpander.move(nWidth + vGap - Expander.EXPAND_OFFSET + Expander.EXPAND_X, Expander.EXPAND_Y);
			if (myExpander.getExpanded()) {
				int y = 0; // Kids relative vertical position
				for (int i = 0; i < myDatum.getNumChildren(); i++) {
					DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
					kid.resize(nWidth, vGap, vWidth, aWidth);
					kid.move(extent.x, extent.y + y);
					y += kid.extent.height;
				}
				extent.height = y; // reset height to sum of kid's heights
			}
		}

	}

	/*
	 * A mouse click has occurred, probably inside my box. I need to check if my
	 * expander has been hit. If not I need to check on all my kids in case one
	 * of their expanders has been hit. Expander boxes will be in different
	 * locations so only 1 can be hit. Any hit requires a complete refresh of
	 * the base display.
	 */
	public void mouseClicked(Point p) {
		if (extent.contains(p)) {
			boolean clickSelect = false;
			// Did it hit my expander?
			if (myExpander == null)
				clickSelect = true;
			else {
				if (myExpander.contains(p, getLocation())) { 
					myExpander.setExpanded(!myExpander.getExpanded());
					myDataView.refresh();
				} else if (myExpander.getExpanded()) { // Only search kids if I
														// am expanded
					for (int i = 0; i < myDatum.getNumChildren(); i++) {
						DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
						kid.mouseClicked(p);
					}
				} else
					clickSelect = true; // neither on an expander or a kid
			}
			if (clickSelect) {
				selected = !selected;
				myDataView.refresh(); // Changed June 8, 2007 from a system wide
										// refresh
			}
		}
	}

	/*
	 * Recursive check on whether expander box is hit. If so changes state of
	 * box and requests any height changes necessary. DON'T BELIEVE THIS IS USED
	 */
	public boolean expanderHit(Point p) {
		if (myExpander == null)
			return false;
		if (myExpander.contains(p, getLocation())) {
			myExpander.setExpanded(!myExpander.getExpanded());
			return true;
		}
		if (myExpander.getExpanded()) { // Only search kids if I am expanded
			for (int i = 0; i < myDatum.getNumChildren(); i++) {
				DatumDisplay kid = getAssociated(myDatum.getChildAt(i), myDataView);
				if (kid.expanderHit(p))
					return true;
			}
		}
		return false;
	}

	public void setGrayOut(boolean on) {
		grayOut = on;
	}

	public boolean isSelected() {
		if (selected)
			return true;
		if (myDatum.getParent() == null)
			return false;
		return getAssociated(myDatum.getParent(), myDataView).isSelected();
	}

	public boolean isGrayedOut() {
		return grayOut;
	}

	public void draw(Graphics screen) {
		if (screen == null)
			return;

		if (myExpander == null)
			drawScalar(screen);
		else
			drawCompound(screen);
	}

	protected void drawCompound(Graphics screen) {

		// We need local co-ordinates for accumulating
		Rectangle localR = new Rectangle(extent);

		// Draw the expander before accumulating
		myExpander.draw(screen, localR);

		// Draw box around the whole datum value area
		localR.x += nameBox.getExtent().width;
		localR.width = valueBox.getExtent().x + valueBox.getExtent().width - nameBox.getExtent().width;
		screen.drawRect(localR.x, localR.y, localR.getWidth(), localR.getHeight());

		localR.x += Expander.EXPAND_OFFSET * nestLevel;

		if (myExpander.getExpanded()) { // Draw leaders, descenders and kids
			int kids = myDatum.getNumChildren();
			int xDesc = localR.x + Expander.EXPAND_OFFSET / 2; // descenders
																// drop from
																// middle
			int yEnd; // end of descender
			int xStart, xEnd; // Leader start and end
			Line line;
			for (int i = 0; i < kids; i++) {
				DatumDisplay dd = (DatumDisplay) myDatum.getChildAt(i).getProperty(myDataView.getDisplayString());
				int depth = dd.extent.height;
				int yLeader = localR.y + baseHeight / 2; // Leader vertical
															// position

				if (i > 0) { // Not the first kid
					xStart = xDesc; // leaders start on descender
					yEnd = localR.y + (i == (kids - 1) ? baseHeight / 2 : depth); // last
																					// descender
																					// stops
																					// at
																					// leader
					line = new Line(new Point(xDesc, localR.y), new Point(xDesc, yEnd));
					screen.draw(line); // descender
				} else { // first kid is special
					if (kids > 1) // no descender for only kid, otherwise start
									// at bottom of expander
					{
						line = new Line(new Point(xDesc, localR.y + Expander.EXPAND_X + Expander.EXPAND_H),
								new Point(xDesc, localR.y + depth));
						screen.draw(line);
					}
					xStart = localR.x + Expander.EXPAND_X + Expander.EXPAND_W; // leader
																				// from
																				// right
																				// of
																				// expander
				}
				xEnd = localR.x + Expander.EXPAND_OFFSET * (dd.getExpander() == null ? 2 : 1);
				line = new Line(new Point(xStart, yLeader), new Point(xEnd, yLeader));
				screen.draw(line);// Leader
				dd.draw(screen);
				localR.y += depth;

			}
		} else // Draw it the same way you would a scalar
			drawScalar(screen);
	}

	protected abstract void drawScalar(Graphics screen);

	public String toString() {
		return "DatumDisplay for " + myDatum.toString();
	}

}