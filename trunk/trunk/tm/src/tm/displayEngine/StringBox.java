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

import java.awt.*;

import tm.utilities.Debug;

/* New class to handle the display of a string inside a cell. Introduced in Fall, 1999
    to replace ad hoc methods used in storeDisplay, DatumDisplay and/or STDisplay.
    
    This class is a lightweight class. It was very tempting to make it hierarchial
    giving it the expanders and so on that are in DatumDisplay. After all, cells
    appear inside of cells. However that would affect a lot of code that is currently
    working very well. Plus it seems to me that expansiveness is a property of DatumDisplays
    rather than boxes.
    
    So boxes just draw strings (clipped to the box) and ultimately will be able to respond
    to mouse clicks to handle the display of strings too big for the box.
    Expanders, leaders, etc, are no concern of the box (even though they appear to be in
    the box). They are overlaid over the box
    
    See Datum Display class for more information on it.
*/

public class StringBox extends Object {
// String alignments, horizontal, then vertical	
	public static final int LEFT = 0;
	public static final int CENTRE = 1;
	public static final int RIGHT = 2;
	public static final int TOP = 3;
	public static final int MIDDLE = 4;
	public static final int BOTTOM = 5;

/* This is a bit of a Kludge. Not being a component I get no access to
    any graphics context. Once the draw method on any StringBox object is called
    I should have a graphics context. Since all I need is fontMetrics for
    establishing the left edge of the String this should do.
    The context is updated by each call to draw. 
*/
    private Graphics2D theScreen;     // Since I am not a component I need to rememeber for font metrics
    
    private String theString;
    private boolean outlined;   // to draw a box or not to draw a box
    private Rectangle extent;   // relative to object that owns it
    private Point offset;       // of string, within the box
    
    private int alignH;
    private int alignV;
    
    public StringBox(String s, boolean o, int hor, int vert){
        extent = new Rectangle (0,0,0,0);
        theString = s;
        outlined = o;
        alignH = hor;
        alignV = vert;
        offset = new Point(0,0);
    }
    
    public void move(int x, int y){
        extent.x = x;
        extent.y = y;
    }
    
    public void resize(int w, int h){
        extent.width = w;
        extent.height = h;
    }
    
    public void nudge(int x,int y){
        offset.x = x;
        offset.y = y;
    }

// Added March, 2001, to support pointer drawing    
    public Rectangle getExtent(){return new Rectangle(extent);}

/* 2001.03.22 Returns the position of the left edge of the string relative
    to the left edge of the extent. May not work if a graphics context has
    not been established by a call to draw ANY object.
*/
    public int getStringLeft(){
        if(theScreen == null) return 0;
		FontMetrics fm = theScreen.getFontMetrics();
        int x = 0;
        
        switch (alignH){
			case LEFT:
			    x = offset.x;
			    break;	
			case RIGHT:			
				x = extent.width - fm.stringWidth(theString) - offset.x;  // oops start from right side.
				break;
			case CENTRE:  // ignore offset
				x = (extent.width - fm.stringWidth(theString))/2;
				break;
		}
		return x;
    }
    
    public void setString(String str){
        theString = str;
    }

/*  Someone thinks the mouse clicked inside of me. Do something about letting user see the full string.
    If mouse clicked on an overlay (e.g. an expander) too bad. I don't know about expanders, I'm going
    to do my thing. If you don't want my peaches, don't shake my tree!
*/
    public void mouseClicked(Point p){
        /*DBG System.out.println("checking mouseClick on" + toString());/*DBG*/
        if (extent.contains(p))
            Debug.getInstance().msg(Debug.DISPLAY, theString);    // Have to do better than that!
    }
    
    public void draw(Graphics2D screen, Point ownerPosition, Color fill){
        if (screen == null) return;
        theScreen = screen;
        int x = ownerPosition.x + extent.x;
        int y = ownerPosition.y + extent.y;
        if (fill != null) {
            Color holdC = screen.getColor();
            screen.setColor(fill);
            screen.fill(new Rectangle(x, y, extent.width, extent.height));
            screen.setColor(holdC);
        }
        if (outlined) screen.draw(new Rectangle(x, y, extent.width, extent.height));
        if (theString != null && theString != "") {
            Shape holdS = screen.getClip();  // This worries me as being very expensive
            screen.setClip(x, y, extent.width, extent.height);
            switch (alignH){
			    case LEFT:
			        x += offset.x;
			        break;	
			    case RIGHT:			
				    x += extent.width-offset.x;  // oops start from right side.
				    break;
			    case CENTRE:
				    x += offset.x + (extent.width - offset.x)/2;
				    break;
			}
            switch (alignV){
			    case TOP:
			        y += offset.y;
			        break;
			    case BOTTOM:			
				    y += extent.height;  // oops start at bottom.
				    break;
			    case MIDDLE:
				    y += offset.y + (extent.height-offset.y)/2;
				    break;
			}
            paintString(theString,x, y, screen, alignH, alignV);
            screen.setClip(holdS);
        }
    }
    
    public String toString(){return "StringBox: " + theString;}
    
 // Utility method available to rest of the package          
	static void paintString(String s, int x, int y, Graphics2D screen, int j, int p) {
		FontMetrics fm = screen.getFontMetrics();
		switch (j) {	// Justification
			case LEFT: break;	// Start right at x
			case RIGHT:			// End at x
				x -=  fm.stringWidth(s);
				break;
			case CENTRE:
				x -= fm.stringWidth(s)/2;
				break;
		}
		switch (p) {	//vertical Position
			case TOP:			// Ascent at y
				y += fm.getAscent();
				break;
			case MIDDLE:		// centre at y
				y += fm.getHeight()/2 - fm.getDescent();
				break;
			case BOTTOM:
				y -= fm.getDescent();
		}
		screen.drawString(s,x,y);
	}
}    
        