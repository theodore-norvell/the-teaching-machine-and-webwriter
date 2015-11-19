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


// =================================================================
// =================================================================

/*  Objects can be attached to Datums. An expander 
    1.  tracks whether or not its associated Datum is visibly expanded
    2.  controls the extent of the expander box attached to the displayed Datum.
*/
// =================================================================

public class Expander {
    
/* Expander box dimensions: 
*/
    public static final int EXPAND_X = 2;
    public static final int EXPAND_Y = EXPAND_X;
    public static final int EXPAND_H = DatumDisplay.baseHeight - 2* EXPAND_Y;
    public static final int EXPAND_W = EXPAND_H;
    public static final int EXPAND_OFFSET = EXPAND_W + 2* EXPAND_X;


    private boolean isExpanded;
    
 /* The extent of the expander box relative to parent's drawing co-ordinates.
 */
    Rectangle extent;
    
    public Expander(){
        isExpanded = false;
        extent = new Rectangle(0, 0, EXPAND_H, EXPAND_W);
}
    
    public boolean getExpanded(){return isExpanded;}
    
    public void setExpanded(boolean on){isExpanded = on;}
    
    public void move(int x, int y){
        extent.x = x;
        extent.y = y;
    }
    
    public void shift(int dx, int dy) {
        extent.x += dx;
        extent.y += dy;
    }
    
    public boolean contains(Point p, Point location){
        extent.x += location.x;
        extent.y += location.y;
        boolean does = extent.contains(p);
        extent.x -= location.x;
        extent.y -= location.y;
        return does;
    }
    
	public void draw(Graphics2D screen, Rectangle ownerPlace){
        if (extent == null) return;
	  // Expand box extent  
	    int x = ownerPlace.x + extent.x;
	    int y = ownerPlace.y + extent.y;
	    int w = extent.width;
	    int h = extent.height;
	    
	    screen.drawRect(x, y, w, h);
	    if (isExpanded){
		    StringBox.paintString("-",x+w/2, y + h/2, screen,StringBox.CENTRE,StringBox.MIDDLE);
		}
		else StringBox.paintString("+",x+w/2, y + h/2, screen,StringBox.CENTRE,StringBox.MIDDLE);
	}
	
	public void drawNubbin(Graphics screen, Rectangle ownerPlace, boolean vert){
        if (extent == null) return;
	  // Expand box extent  
	    int x = ownerPlace.x + extent.x;
	    int y = ownerPlace.y + extent.y;
	  // Horizontal nubbin from right edge of expander box to end of expander area
	    screen.drawLine(x + extent.width, y + extent.height/2, x + EXPAND_OFFSET, y + extent.height/2);
	    if (isExpanded && vert) {
	        x += extent.width/2;
	        screen.drawLine(x, y + extent.height, x, y + DatumDisplay.baseHeight);
	    }
	}
}
    
    

