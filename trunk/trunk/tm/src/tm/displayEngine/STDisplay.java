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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import tm.interfaces.DisplayContextInterface;
import tm.interfaces.STEntry;


/*==================================================================
Class: STDisplay

Overview:
The Display panel for the SymbolTable class (which is the cross-reference between identifier
names and their locations in memory).

99.12.08 Minor improvements added. Symbol table now automatically expands to fill the available
    width by expanding the type area. Horizontal scroll bars are used if the width drops
    below a minimum. Also, any strings that flow outside their cells are clipped.
===================================================================
*/

public class STDisplay extends DisplayAdapter{
	private static final long serialVersionUID = 676949149026767852L;
	
    private int numEntries = 0;
    
	private static int entryHeight = 16;
	private static final int OFFSET = 3;
	private static final int Y_OFF = 2;     // y offset 
	private static final int W_NAME = 100;  // width of drawing boxes
	private static final int W_TYPE = 100;  // this is minimum width since type box expands to fill space     
	private static final int W_ADDRESS = 50;       
	private static final int MY_WIDTH = W_NAME + W_TYPE + W_ADDRESS + 50;       




	public STDisplay(DisplayManager dm, String configId) {
	    super(dm, configId);
		setScale(1,entryHeight);	// Unit scroll increment
}


	public void refresh(){
	    int n = commandProcessor.getNumSTEntries();
	    int height = getToolkit().getFontMetrics(context.getDisplayFont()).getHeight() + 2;
	    if (n != numEntries || height != entryHeight) { // n is the total no. entries in the ST
	        entryHeight = height;
		    setPreferredSize(new Dimension(MY_WIDTH,n * entryHeight));   // Fixed width. Should autosize!!!!
		    numEntries = n;
	    }
	    super.refresh();
	}
	

// Graphics portion ---------------------------------------------------


	public void drawArea(Graphics2D screen) {
	/*  a method to draw this component; each symbol table item is drawn
	within a box, and each box contains the name, owner and address of 
	an identifier 
	*/

		if (commandProcessor == null) return;
                int globals = commandProcessor.varsInGlobalFrame();
                int locals = commandProcessor.varsInCurrentFrame();
		
		screen.setFont(context.getDisplayFont());
		FontMetrics fm = screen.getFontMetrics();
		int ascent = fm.getAscent();
	
		Point scroll = getScrollPosition();
		
		int yCurr = /*-scroll.y*/ 0;       // current y location
		int typeWidth = getSize().width - W_NAME - W_ADDRESS;
		if (typeWidth < W_TYPE) typeWidth = W_TYPE;
		
		for (int i = 0; i< numEntries; i++) {
			STEntry s = commandProcessor.getSymTabEntry(i);
			if (s.getHighlight() == STEntry.HIGHLIGHTED) {
				screen.setPaint(context.getHighlightColor());
				screen.fill(new Rectangle(/*-scroll.x*/0,yCurr,getSize().width, entryHeight));
			}
            if ( (i >= globals) && (i < numEntries - locals)) {
				screen.setPaint(Color.lightGray);
				screen.fill(new Rectangle(/*-scroll.x*/0,yCurr,getSize().width, entryHeight));
             }
			screen.setPaint(Color.black);
	// Draw the name in a box
	        drawStringBox(screen, s.getName(), /*-scroll.x*/0, yCurr, W_NAME, ascent);
	// And the type
	        drawStringBox(screen, s.getType(), W_NAME/*-scroll.x*/, yCurr, typeWidth, ascent);
	// finally, the address
			Integer tempAdd = new Integer(s.getAddress());
	        drawStringBox(screen, tempAdd.toString(), W_NAME+typeWidth/* -scroll.x*/, yCurr, W_ADDRESS, ascent);
			yCurr += entryHeight;
		}
		
	}
	
	private void drawStringBox(Graphics2D screen, String s, int x, int y, int w, int ascent) {
		screen.drawRect(x, y, w, entryHeight);
		Shape hold = screen.getClip();
		screen.setClip(x, y, w, entryHeight);
		screen.drawString(s,x + OFFSET,y + Y_OFF + ascent);
		screen.setClip(hold);
	}



	//{{DECLARE_CONTROLS
	//}}
}	 // End of STDisplay class
