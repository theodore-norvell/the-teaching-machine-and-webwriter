////     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
////
//// Licensed under the Apache License, Version 2.0 (the "License");
//// you may not use this file except in compliance with the License. 
//// You may obtain a copy of the License at 
////
////     http://www.apache.org/licenses/LICENSE-2.0 
////
//// Unless required by applicable law or agreed to in writing, 
//// software distributed under the License is distributed on an 
//// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
//// either express or implied. See the License for the specific language 
//// governing permissions and limitations under the License.
//
//package tm.displayEngine;
//
//import java.awt.event.MouseEvent;
//import java.util.Vector;
//
//import telford.common.Point;
//import tm.displayEngine.generators.RegionGenerator;
//import tm.displayEngine.generators.SelectionGenerator;
//import tm.displayEngine.generators.StoreGenerator;
//import tm.interfaces.CommandInterface;
//import tm.interfaces.DataDisplayView;
//import tm.interfaces.Datum;
//import tm.interfaces.ImageSourceInterface;
//import tm.subWindowPkg.SmallToggleButton;
//import tm.subWindowPkg.ToolBar;
//import tm.utilities.Assert;
//
//
///**
// * New class to display linked Datums ("box and arrow view")
// * 
// * This class is built on top of the notion of a LinkedDatumDisplay, a lightweight
// * class of self-drawing objects which represent the display of Datums
// * on the screen. In the linked view of datums a potentially arbitrary set
// * of datums act as a generator for a tree. The generator datums form the root
// * nodes of the tree and are displayed in the leftmost column. Branches occur
// * from every node which has pointers from it.
// * 
// * So far as the linked display is concerned, a node is a "top-level" datum,
// * that is a datum with no parent. A "root node" is any top level node in the
// * generator and "the root node" is the first root node in the generator.
// * 
// * Since Datums are hierarchial, DatumDisplay objects may
// * be expanded or contracted. An unexpanded compound D3 object acts as if it
// * were a scalar node, that is a leaf node.
// * 
// * @author Michael Bruce-Lockhart
// * @since April 12, 2001
// * @see LinkedDatumDisplay
// * @see LinkedD3Iterator
// * @see LinkedLayoutManager
// * @see RegionGenerator
// */
//public class LinkedDisplay1 extends DataVisualizerAdapter1 implements DataDisplayView {
////	private static final int LAST_STEP = 20;    // last step in animation morph
//	private static final int STATIC_BUTTON = 0;  // button nos.
//	private static final int STACK_BUTTON = 1;
//	private static final int SELECTION_BUTTON = 2; // if true, others must be false
//
//// Since buttons are not momentary we need to be able to reset them
//// under program control
//	private SmallToggleButton[] buttons = new SmallToggleButton[3];
//	private ToolBar toolBar;
//	
//	private Boolean stackDown;		// State of other buttons when selection pushed
//	private Boolean staticDown;
//	
//
///*	private int view;           // Current view being used (LOGICAL, SCALED, etc.)
//	private DisplayTree theDisplay = null;
//	private DisplayTree oldDisplay = null;*/
//	
//	private boolean animate = false;    // animation flag for morphing old tree into new
//	
////	private StoreGenerator storeGen;	// Reference to client
//	private SelectionGenerator selectionGen;
////	private AbstractGenerator generator;
////	private DisplayContextInterface context;
//	private LinkedLayoutManager1 layoutManager;
//	
//	
//// =================================================================
//// Constructor
//// =================================================================
//
//	public LinkedDisplay1(DisplayManager dm, String configId) {
//		super(dm, configId);		// Automatic scrollbars
//		
//		CommandInterface cp = context.getCommandProcessor();
//		localGenerator = new StoreGenerator(
//                new RegionGenerator(cp.getStaticRegion()),
//                new RegionGenerator(cp.getStackRegion()));
//		selectionGen = new SelectionGenerator(dm, this);
//		myGenerator = localGenerator;
//		
//	    layoutManager = new LinkedLayoutManager1(this);
//		setScale(1,LinkedDatumDisplay.BASE_HEIGHT);	// scrolling increment
//		
//		// Create and add toolbar for controlling generator mode
//		ImageSourceInterface imageSource = context.getImageSource();
//		buttons[STATIC_BUTTON] = new SmallToggleButton("Static", imageSource);
//		buttons[STATIC_BUTTON].setToolTipText("View static datums");
//		buttons[STACK_BUTTON] = new SmallToggleButton("Stack", imageSource);
//		buttons[STACK_BUTTON].setToolTipText("View stack datums");
//		buttons[SELECTION_BUTTON] = new SmallToggleButton("heapStore", imageSource); // Need a new button gif
//		buttons[SELECTION_BUTTON].setToolTipText("View selected datums");
//		buttons[STACK_BUTTON].setButton(true);
//		toolBar = new ToolBar(buttons);
//		mySubWindow.addToolBar(toolBar);
//	}
//
//	public ToolBar getToolBar() {
//		return toolBar;
//	}
//
//	
//	// Required by the interface. Not used here. Needs to be cleaned up
//
//        public int getDatumView(){
//        return 0;
//    }
//    
///* In Store view, address width is fixed, while the other two are elastic. Name and value widths take
//for example expanding to
//fill a window unless that results in too small a size. Thus field widths are a function of
//the particular view.
//*/
//    public int getNameW(){return 0;}
//    public int getValueW(){return 0;}
//    public int getAddressW(){return 0;}
//    
//    
//    
//
//// =================================================================
//// Over-ride on parent refresh. Refresh is the only signal raised when 
//// the state changes, thus all code that is state dependent must be called
//// from here
//// =================================================================
//    public void refresh() {
//        /* First, make sure there is an LDD for each generator datum */
//        for (int i = 0; i < myGenerator.getNumChildren(); i++) {
//            Datum kid = myGenerator.getChildAt(i);
//            tm.portableDisplays.LinkedDatumDisplay ldd = tm.portableDisplays.LinkedDatumDisplay.getLDD(kid, this);
//            if (ldd == null) {
//                ldd = new tm.portableDisplays.LinkedDatumDisplay(kid, this, 0);
//            }
//        }
//        /* Now, walk the tree. Since the tree structure is defined by the relationship
//            between datums, the tree already pre-exists. However, there is no guarantee
//            either that there is a DatumDisplay object for every datum (new datums may
//            have been created) or that the links are still correct (pointer datums may 
//            have changed their values). Thus walking the tree is a discovery process
//            (it discovers the new structure of the tree) which automatically resets
//            datumDisplays and their associated links to align with the underlying datums.
//         */
//    	myGenerator.refresh();
//        LinkedD3Iterator1 iterator = new LinkedD3Iterator1(myGenerator, this);//myGenerator.getIterator(this);
//        iterator.refresh();
//        linkDisplayer.getDisplayInfo().setIterator(iterator);
//	    layoutManager.layoutDisplay(myGenerator);
//    	super.refresh();
//	}
//    
////    public void systemRefresh(){ context.refresh();}
//
//	
////  =================================================================
////  buttonPushed overide - used to set regions for generating
////  =================================================================
// 	public void buttonPushed(int i) {
// 		if(generatorMode != LOCAL) return;
// 		Assert.check(i >= 0 && i < 3);
// 		if (i==SELECTION_BUTTON){
// 			if (buttons[SELECTION_BUTTON].isSelected()) {
//	 			stackDown = buttons[STACK_BUTTON].isDown();
// 				buttons[STACK_BUTTON].setButton(false);
//	 			staticDown = buttons[STATIC_BUTTON].isDown();
//	 			buttons[STATIC_BUTTON].setButton(false);
// 	 			myGenerator = selectionGen;
// 			} else {
// 				buttons[STACK_BUTTON].setButton(stackDown);
//	 			buttons[STATIC_BUTTON].setButton(staticDown);
//	 			myGenerator = localGenerator;
// 			}
// 			
// 		} else {
// 			buttons[SELECTION_BUTTON].setButton(false);
// 			myGenerator = localGenerator;
// 			((StoreGenerator)localGenerator).buttonPushed(i, buttons[i].isSelected());
// 		}
// 		refresh();
// 	}
// 	
//	
//	
//	
///*  Over-rides method in workArea but call super(evt) first
//to properly handle focus issues
//
//    Walks through the LinkedDatumDisplays until it finds the one that
//    contains the mouse location. Could precalculate in most instances.
//    However, in some modes we may use different depth boxes to reflect
//    the size of the datums, precluding easy precalculation
//    
//*/
//	protected void mouseJustClicked(MouseEvent evt) {
//	    super.mouseJustClicked(evt);     // Workarea handles focus issues
//	    if (/*justGotFocus ||*/ evt == null) return;
//	    
//        Point location = new Point(evt.getX(), evt.getY());
////	        System.out.println(" at (" + location.x + ", " + location.y + ")");
////		Datum kid = generator.getChildAt(i);
////		    DatumDisplay dd = (LinkedDatumDisplay)DatumDisplay.getAssociated(kid,myDataView);
//        LinkedD3Iterator1 iterator  = new LinkedD3Iterator1(myGenerator, this);//myGenerator.getIterator(this);   // Each compound node has its own iterator
////        iterator.reset(); reverted to non-singleton iterator of version 95
//        linkDisplayer.getDisplayInfo().setIterator(iterator);
//        while (!iterator.atEnd()){
//            if (iterator.getNode().contains(location)){
//                iterator.getNode().mouseClicked(location);
//                break;
//            }
//            iterator.increment();
//        }
//        evt.consume();
//}
//	
//	
//	
///////////////////////////////////////////////////////////////////////////
////
//// The Main Paint method
////
//// Problems - resize resizes scroll bar but not bottom + top seems 
////	to reset to 0 when scroll bar used.
////
///////////////////////////////////////////////////////////////////////////
//	
//
//
///*  Parameter over-rides to save/restore view button settings
//*/
//
//    public String toString(){
//        return "LinkedDisplay for " + (myGenerator==null ? "null" : myGenerator.toString()) ;
//    }
//
//    public String getDisplayString(){
//        return "linkedDisplayDatum for " + myGenerator.toString();
//    }
//
//	public Vector<Datum> getSelected() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//
//} // end of LinkedDisplay class
//
