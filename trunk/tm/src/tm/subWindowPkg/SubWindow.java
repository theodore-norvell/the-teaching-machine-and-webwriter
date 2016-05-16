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

package tm.subWindowPkg;

import java.awt.BorderLayout;

import javax.swing.AbstractButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JViewport;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import tm.interfaces.ImageSourceInterface;

/** <p>A Class representing a subWindow within a window. SubWindows are confined within
 * their parent window.</p>
 *
 * @author Michael Bruce-Lockhart
 * @see WorkArea
 */
public class SubWindow extends JInternalFrame {

/**
	 * 
	 */
	private static final long serialVersionUID = -8039438939872323140L;

/** The area inside the edges, containing the workPane + toolBar */
    private JPanel contentPane;
/** A scrollable pane that contains the workArea */
	private JScrollPane workPane;
/** The actual drawing surface for the display from which all displays are
 *  derived */
	private WorkArea workArea;
/** The current location of the mouse */	
//	private Point mouseAt = new Point(0,0); // Make sure it is initialized
	private final static int MIN_DIM = 40;

//===================================================================================
// Constructor
//===================================================================================
  
        /** <p>Create a subWindow which is now just a JInternalFrame. SubWindows are
         * specialized by adding a {@link workArea} after the subWindow is created.</p>
         * @param is: the ImageSourceInterface ? from which the buttons gifs come?
         * @param scrollPolicy: the scrolling policy for the workPane
         */        
	public SubWindow(ImageSourceInterface is/*,
						int vertScrollPolicy, int horizScrollPolicy*/) {
		super(null,true, false, true, true);
// Build the inner area for the center which will contain a workArea and possibly a toolbar
		contentPane = new JPanel(new BorderLayout());

// Create the specialized scrollPane that holds the actual workArea		
		workPane = new JScrollPane(/*vertScrollPolicy, horizScrollPolicy*/);


// Add title bar & workPane to inside area
		contentPane.add("Center",workPane);

/*  Now put it all together & make this known to the workArea which
    had to be created first.
*/
		setContentPane(contentPane);

	}
	

        /** <p>Each subWindow has a {@link WorkArea} which is where the subWindow is
         * specialised. Both objects must know of the other but must be constructed one at
         * a time. Thus a SubWindow is constructed first, then its {@link WorkArea} then the
         * {@link WorkArea} must be added to the SubWindow.</p>
         * @param workArea the {@link WorkArea} associated with the subWindow
         */        
	public void addWorkArea(WorkArea wa){
		workArea = wa;
	    workPane.setViewportView(workArea);
/**
 * TO BE DONE: If I try to use the new BLIT_SCROLL_MODE clipping doesn't work
 * properly with storeDisplays and the STDisplay (although its fine with the
 * codeDisplay. The blitting algorithm queues all graphics changes and then
 * paints them at once. While store displays start out well behaved they can
 * appear to stop clipping sometimes when another subwindow is brought into
 * focus. To duplicate the problem, change mode to blit mode, run the TM and
 * load the arrayToPntr program from cpp-conformance-tests. StepOver to load
 * the first two or three variables into the stack, then expand the first
 * variable which is a large array. Scroll down so the array is clipped top and
 * bottom. Now click on a the heap and (if it is below the stack), the array
 * will overflow the bottom. Click on the symtab and it will overflow the top.
 * Resizing the stack subwindow has no effect. Its contents now appear to
 * be being written on the screen behind, as if the parent graphics context is
 * being used. A dump of the clip rectangle from the store paint routine shows
 * the clip to be set properly, arguing the correct context is being used.
 * 
 * Part of the problem appears to be reconciling my legacy
 * code (which has its own viewport model) with the new Swing approaches (which
 * have an explicit viewport model). WorkArea should be carefully scrutinized
 * and possibly revised so that it works properly with blit mode. Moreover
 * the calling of layout managers (particularly for the store) should be examined
 * (see drawArea method for StoreDisplay) to make sure layout is redone for all
 * events that would require it instead of just on a size change (e.g., should
 * a scroll change force a relayout). Finally, my preclipping of stores is not
 * handling compound datums properly. firstDatum, lastDatum refer to unexpanded
 * datums even when datums are expanded. size of the view is changing properly
 * so there's no reason first and last datum can't be more accurate.
 */
	    workPane.getViewport().setScrollMode(JViewport.BACKINGSTORE_SCROLL_MODE);
	}
	
	public void addWorkArea(WorkArea wa, ToolBar toolBar){
		addWorkArea(wa);
		if (toolBar != null) addToolBar(toolBar);
	}

	
	public void addToolBar(ToolBar toolBar){
		contentPane.add(toolBar.getSide(),toolBar);
        toolBar.setReportTo(workArea);		
	}
	
        /** get the subWindow's workPane
         * @return the workPane associated with the subWindow
         */        
	public JScrollPane getWorkPane(){ return workPane;}
	    
	    
	public String getShortName() {return getTitle();}
}
