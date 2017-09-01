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

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import tm.interfaces.ImageSourceInterface;

/* Customization of sub-windows is designed to be wholly embodied in this abstract
class. It is not an interface for it provides at least one service to make sure
that all subwindows come into focus properly if they get clicked on while they
are in the background. This can't be done through a interface (as far as I know)
since whatever display is specialized from abstractDisplay must call its corresponding
subWindow whenever the mouse is clicked on it and IT DOES NOT HAVE THE FOCUS. Interfaces
define functions which must be available but the implication is they wait around until
they are called. However, displays must take the initiative and call subWindow when
necessary.

    Given one necessary common service, a convenience common service has also been added.
AbstractDisplay manages the update process using double buffering.

    The SubWindow package has been designed so that each SubWindow has an titleBar and a
workArea. The workArea is where the application specific components are to be added.
Thus the idea is to extend the workArea for each application or each special subwindow,
and not the subWindow. Thus messages from the parent subWindow's toolbar are automatically
routed to the workArea.
2001.11.30: Trying to remove Unix bug that make it impossible to resize windows bigger and
    also move bug that has window move against point where mouse clicked, often to top left
    corner. Struck me that the focus event should not be mousePressed but rather mouseClicked.
    Whether that solves the bug is moot but still true.

2001.01.23: Removed finalize to support rekindling of closed
    windows in the event a new configuration is loaded.
    
99.12.13: Added mutable window titling. The title given at construction is used as an immutable
    name. A separate title can now be specified in the configuration file. Thus, for beginning students
    we might want to use only one store window (the Stack) but label it simply "Memory". Later
    as different types of memory are introduced, we use the more specialised labels.
*/



abstract public class WorkArea extends JPanel implements Scrollable, WorkAreaInterface {
	
/**
	 * 
	 */
	private static final long serialVersionUID = 6714156506043873572L;
    private Dimension mySize = null;
	protected SubWindow mySubWindow = null;
	protected JScrollPane myWorkPane = null;
//	protected String configId;
	
// These are private to ensure that a change in them forces a change in
// scrollbars as well
	private int verticalScale, horizontalScale;		// from world to view co-ordinates 

	

	public WorkArea(ImageSourceInterface imageSource) {
	    mySubWindow = new SubWindow(imageSource);
	    myWorkPane = mySubWindow.getWorkPane();
	    horizontalScale = 1;
	    verticalScale = 1;
	    mySize = new Dimension(0,0);

	    mySubWindow.setVisible(true);
	    
		setBackground(Color.white);
		this.setOpaque(true);
		this.setDoubleBuffered(false);
//		ScriptManager.getManager().register(this);
		
		addMouseListener(new MouseAdapter() {
		    public void mouseClicked(MouseEvent evt) {
		        mouseJustClicked(evt);
		    }});
		
	}
	
	public Component getWindowComponent(){return mySubWindow;}
	
	
/*  Displays derived from this class that need to intercept mouse events can
    over-ride this method.
    
*/
	protected void mouseJustClicked(MouseEvent evt) {
	}
	
	
/* refresh - update methods. Refresh is the message used within the subWindow package
    for external requests to update a window because something may have changed. 
    update is the standard awt update method
*/
	
	public void refresh(){
//		drawArea((Graphics2D)getGraphics());
		repaint();
//		System.out.println("refreshing workArea " + this);
/*		Graphics screen = getGraphics();
		paintComponent(screen);*/
	}

	
 /* Unless a redraw has been triggered paint simply brings forward the existing
    offscreen image */
	public void paintComponent(Graphics screen) {
		if (screen == null) return;
	    super.paintComponent(screen);
//		System.out.println("drawing " + mySubWindow.getTitle());
	    drawArea((Graphics2D)screen);
	}
	

/*  The scrollPane uses preferred size to calculate image sizes and do scrollbar
    manipulations. So here we use the actual image size (rather than the scrollPane
    viewport size) and let the scrollPane think we're drawing full size.*/

	public void setPreferredSize(int width, int height){
        if (mySize.width != width || mySize.height != height) {
    	// size has changed
    	    mySize.width = width;
    	    mySize.height = height;
    	    revalidate();   // I need relaying out
    	    myWorkPane.doLayout();
    	}
	}
	
	public void setPreferredSize(Dimension d){
	    setPreferredSize(d.width, d.height);
	}
	
	

	public void setScale(int hScale, int vScale){
	    horizontalScale = hScale;
	    verticalScale = vScale;
	}
	
	public int getHScale(){ return horizontalScale;}
	
	public int getVScale(){ return verticalScale;}

	public Point getScrollPosition(){
		return new Point(myWorkPane.getHorizontalScrollBar().getValue(),
				myWorkPane.getVerticalScrollBar().getValue());
	}
	
	public Dimension getViewportExtent(){
		return myWorkPane.getViewport().getExtentSize();
	}

	public Dimension getViewportSize(){
		return myWorkPane.getViewport().getSize();
	}
	
	
/* Scrollpanes are designed to work with getPreferredSize so children of the workArea
class should calculate their proper size and return it as the preferred size. Scrollpanes
will then size object to fit inside pane if it is smaller or to its preferred size
(for clipping and scrolling if it is larger).
*/
	public Dimension getMinimumSize(){
	    return getPreferredSize();
	}
	
	public Dimension getPreferredSize(){
		Dimension preferred = new Dimension(mySize);
		if (preferred.height < getViewportSize().height)
			preferred.height = getViewportSize().height;
		if (preferred.width < getViewportSize().width)
			preferred.width = getViewportSize().width;
	    return preferred;
	}
	
	public Dimension getPreferredScrollableViewportSize() {
		return getPreferredSize();
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
		// Over-ride in derived classes
		if (orientation == SwingConstants.HORIZONTAL)
	        return horizontalScale;
	    else
	        return verticalScale;
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
		// Over-ride in derived classes
		//System.out.println("Visible rectangle: " + visibleRect.toString());
		if (orientation == SwingConstants.HORIZONTAL)
	        return (visibleRect.width-horizontalScale);
	    else
	        return (visibleRect.height - verticalScale);
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}



/* Over-ride methods. These are designed to be implemented in subclasses.
*/

/** This method deals with any buttons pushed. 
*/
	@Override
    public void buttonPushed(int i) {
        /*DBG System.out.println("Button " + Integer.toString(i) + " has been pushed.");/*DBG*/
	}
	
/**  subClasses should implement this method INSTEAD OF OVERRIDING PAINT. WorkArea
    uses paint and update, working in tandem, to deliver consistent, rapid double
    buffering. These methods call drawArea to do the actual drawing/painting

*/
	abstract public void drawArea(Graphics2D screen) ;
	
    /**
     * Returns the parameter String of this Container.
     */
    protected String paramString() {
	    String str = super.paramString() + ": " + toString();
	    str = str + " " + mySize.width + "x" + mySize.height;
	    return str;
    }
    
// 99.12.13 Changed to return name (invariant) instead of title(mutable)
    
    /* 10.06.17 Over-ride in sub classes. Originally returned
     * getName which is inherited from Component class but
     * returns null if the component has not yet been added
     * to a container. Thus System.out.printlns gave the impression
     * that a workArea had not yet been constructed when in fact
     * it had simply not been added to the subWindow
     * 
     */
	
	public String toString(){ return "workArea";}
	
	@Override
    public Component getSwingComponent() {
	    return this ;
	}
}


