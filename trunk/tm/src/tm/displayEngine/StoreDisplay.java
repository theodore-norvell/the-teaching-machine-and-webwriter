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

package tm.displayEngine;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.Vector;

import tm.configuration.Configuration;
import tm.interfaces.DataDisplayView;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.ImageSourceInterface;
import tm.interfaces.RegionInterface;
import tm.subWindowPkg.SmallToggleButton;
import tm.subWindowPkg.ToolBar;
import tm.utilities.Assert;
import tm.utilities.Debug;


/*    
    Designed to be specialised. I suspect we will want a simple heap and stack specialisation
    to handle special aspects of those memories (e.g. holes in the heap, defunct data on the stack
    still pointed to by a pointer). I am even hopeful box & arrow can use this code, simply
    by applying a new layoutManager and overloading the drawArea routine!
    
    Ammended March, 2001. Instead of attaching expanders to Datums, we now attach DatumDisplays
    directly. This is done to support linked view. Now it is possible to have different views of
    same datum so we simply attach a DatumDisplay object for each view to the Datum. This allows
    displays to be in different states in different views. Expanders are now attached to DatumDisplay
    objects
*/

/** New class to display the contents of a memory store. Introduced in Fall, 1999
 *    to replace older memory store display technique.
 *
 *    This class is built on top of the notion of a {@link DatumDisplay}, a lightweight
 *    class of self-drawing objects which represent the display of datums
 *    on the screen. Since Datums are hierarchial, DatumDisplay objects may
 *    be expanded or contracted, which affects the store display inasmuch as
 *    it changes the total number of DatumDisplay objects available for display.
 */
public class StoreDisplay extends DisplayAdapter implements DataDisplayView {

	private static final long serialVersionUID = 9057830260011019321L;
	
//	 There are 3 separate views. The values correspond to the button order
		static final int LOGICAL = 0;	// each variable gets a similar box
		static final int SCALED = 1;	// box depth reflects variable's size	
		static final int BINARY = 2;	// bit patterns for each byte	

		private static final int LEFT = 0;	
		private static final int CENTRE = 1;	
		private static final int RIGHT = 2;	
		private static final int TOP = 3;	
		private static final int MIDDLE = 4;	
		private static final int BOTTOM = 5;	
//	 Since buttons are not momentary we need to be able to reset them
//	 under program control
		private SmallToggleButton buttons[] = new SmallToggleButton[3];

		private int view;           // Current view being used (LOGICAL, SCALED, etc.)
		private int topIndex = 0;     // index of top datumDisplay showing
		private int firstDatum;     // first one showing
		private int lastDatum;    //  last datumDisplay on screen
		
		private Dimension mySize;  // So draw can tell if window resized
		

		private RegionInterface region;	// Reference to client
		private StoreLayoutManager layoutManager;
		
		

//	 =================================================================
//	 Constructor
//	 =================================================================

	        /** Constructor
	         * @param dc The displayManager which owns this display
	         * @param r the Region of memory represented by this display
	         * @param w the subWindow which holds the display
	         * @param b the array of buttons for the display
	         */        
		public StoreDisplay(DisplayManager dm, String configId) {
			super(dm, configId);		// Automatic scrollbars
//			System.out.println("storeDisplay configId is " + configId);
			if (configId.equalsIgnoreCase("Heap"))
				region = commandProcessor.getHeapRegion();
			else if (configId.equalsIgnoreCase("Stack"))
				region = commandProcessor.getStackRegion();
			else if (configId.equalsIgnoreCase("Static"))
				region = commandProcessor.getStaticRegion();
			else if (configId.equalsIgnoreCase("Scratch"))
				region = commandProcessor.getScratchRegion();
			else Assert.error("No store display for region " + configId);
			
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
						
//			setScale(1,DatumDisplay.baseHeight);	// scrolling increment
		    layoutManager = new StoreLayoutManager(this);
		    mySize = new Dimension(getSize());
		}

	       /** what kind of store view is currently being used?
	         * @return LOGICAl SCALED or BINARY
	         */        
	  	public int getDatumView(){
		    return view;
		}
	  	
		
	/* In Store view, address width is fixed, while the other two are elastic. Name and value widths take
	for example expanding to
	fill a window unless that results in too small a size. Thus field widths are a function of
	the particular view.
	*/
	   public int getNameW(){return 0;}
	    public int getValueW(){return 0;}
	    public int getAddressW(){return 0;}
	    
	    
		
		
		RegionInterface getRegion(){
		    return region;
		}
		
		int getFirstY(){
		    return getScrollPosition().y;
		}
		
		int getLastY(){
		    return getFirstY() + getViewportSize().height-1;
		}
		
		void setOnScreen(int first, int last) {
		    firstDatum = first;
		    lastDatum = last;
		}

//	 =================================================================
//	 Over-ride on parent refresh. Refresh is the only signal raised when 
//	 the state changes, thus all code that is state dependent must be called
//	 from here
//	 =================================================================
	    public void refresh() {
			if (region == null) return;
	        int frameBoundary = region.getFrameBoundary();
	        DatumDisplay.updateFonts(context.getDisplayFont(),getToolkit().getFontMetrics(context.getDisplayFont()).getHeight());
			setScale(1,DatumDisplay.baseHeight);	// scrolling increment
			for (int i = 0; i < region.getNumChildren(); i++) {
			    Datum kid = region.getChildAt(i);
			    DatumDisplay dd = DatumDisplay.getAssociated(kid,this);
			    if (dd == null) {
			        dd = new StoreDatumDisplay(kid, this, false);
//	redundant - constructor does it
//                  kid.setProperty(getDisplayString(), dd);
			    }
//			    System.out.println("DatumDisplay "  + dd.toString() + " should be associated with " + kid.toString());
//			    System.out.println("DatumDisplay "  + (DatumDisplay.getAssociated(kid,this)).toString() + " IS associated with " + kid.toString());
	            dd.setGrayOut(i < frameBoundary);  
			}
		    layoutManager.layoutDisplay();
	    	super.refresh();
		}
	    
//	    public void systemRefresh(){ context.refresh();}
		
		
//	 =================================================================
//	 buttonPushed overide - used to choose between logical, scaled and
//	 binary views
//	 =================================================================
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
		
		
	/*  Over-rides method in workArea but call super(evt) first
	to properly handle focus issues

	    Walks through the DatumDisplays until it finds the one that
	    contains the mouse location. Could precalculate in most instances.
	    However, in some modes we may use different depth boxes to reflect
	    the size of the datums, precluding easy precalculation
	    
	*/
		protected void mouseJustClicked(MouseEvent evt) {
		    super.mouseJustClicked(evt);     // Workarea handles focus issues
		    if (/*justGotFocus ||*/ evt == null) return;
			if (region == null) return;
		    
	        Point location = evt.getPoint();
			for (int i = 0; i < region.getNumChildren(); i++) {
			    Datum kid = region.getChildAt(i);
			    DatumDisplay dd = DatumDisplay.getAssociated(kid, this);
			    Assert.check (dd != null);
	           if (dd.contains(location)){
	               dd.mouseClicked(location);
	               break;
	           }
			}
//	        evt.consume();
	}
		
		
		
////////////////////////////////////////////////////////////////////////	/
//
//	 The Main Paint method
//
//
////////////////////////////////////////////////////////////////////////	/
		public void drawArea(Graphics2D screen) {  
		/*  a method to draw this component; what is drawn depends upon
			the current view.
		*/
		    if (screen == null || region == null /*|| theDisplay == null*/) return;
		    Debug.getInstance().msg(Debug.DISPLAY, "drawing " + this);
		    
		    if (mySize != getSize()) {
		        layoutManager.layoutDisplay();
		        mySize = getSize();
		    }
/*		    System.out.println(toString() + " view size: " + getSize().width + " x " + getSize().height);
		    System.out.println(toString() + " viewport size: " + getViewportSize().width + " x " + getViewportSize().height);
		    System.out.println(toString() + "firstDatum,lastDatum = (" + firstDatum + ", " + lastDatum + ")");
		    System.out.println(toString() + " clip Rectangle: @(" + screen.getClipBounds().x + ", " + screen.getClipBounds().y + ") dim - " + 
		    		screen.getClipBounds().width + " x " + screen.getClipBounds().height);
*/
		    
			for (int i = firstDatum; i <= lastDatum; i++) {
			    Datum kid = region.getChildAt(i);
			    DatumDisplay dd = DatumDisplay.getAssociated(kid, this);
			    if (dd == null) return;  // Punt - asynchronous paint without refresh
			    dd.draw(screen);
			}
		}	// End of paint() method


	/*  Parameter over-rides to save/restore view button settings
	*/
		public void notifyOfSave(Configuration config){
		    super.notifyOfSave(config);
		    config.setValue("View", Integer.toString(view));
		}
		
		public void notifyOfLoad(Configuration config){
		    super.notifyOfLoad(config);
		    int v;
		    String temp = config.getValue("View");
		    if (temp != null) v = new Integer(temp).intValue();
		    else v = view;
		    buttonPushed(v);
		}

	    public String toString(){
	        return "StoreDisplay for " + mySubWindow.getTitle();
	    }

	    public String getDisplayString(){
	        return "storeNode";
	    }

/**
 * At the moment only top level datums are included in the selection
 */
		public Vector<Datum> getSelected() {
//			System.out.println("getSelected() called on " + toString());
			Vector<Datum> selection = new Vector<Datum>();
			for (int i = 0; i < region.getNumChildren(); i++){
				Datum datum = region.getChildAt(i);
				DatumDisplay dd = DatumDisplay.getAssociated(datum, this); 
				if (dd != null && dd.isSelected())
					selection.add(datum);
			}
			return selection;
		}
	
}
