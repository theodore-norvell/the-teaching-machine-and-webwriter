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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;

import tm.interfaces.StateInterface ;


// =================================================================
// =================================================================

/* Skeletal class for displaying output. The output display acts
like a simple scrollable output. Once lines are put into it they cannot
be retreived. The display can be scrolled if needed but has a limited
capacity


2000.01.06: Support for colour staining for inputs added. Embed INPUT_MARK
    in output string to turn following chars red. Embed NORMAL_MARK to
    turn chars back to blck.
*/
// =================================================================

public class OldConsoleDisplay extends DisplayAdapter {

	/**
		 * 
		 */
		private static final long serialVersionUID = 7066880211879266535L;
		//Printing modes
	    private static final char MARKER_BOUND = StateInterface.INPUT_MARK;

	    private final static int LEFT_MARGIN = 10;
	    private final static int TOP_MARGIN = 10;
	    private final static int TABSPACE = 4;

	    private int numLines = 0;
	    private int advances[];




	    public OldConsoleDisplay(DisplayManager dm, String configId){
	        super(dm, configId);
	    }



//	 =================================================================
//	 Resource Interface Methods
//	 =================================================================

	    public void refresh(){
	        // Must be connected.
	        if( commandProcessor == null ) return ;
	        Graphics g = getGraphics();
	        if (g == null) return;
	        g.setFont(context.getCodeFont());
	        FontMetrics fm = g.getFontMetrics();
	        advances = fm.getWidths();


	        int n = commandProcessor.getNumConsoleLines();
	        if (n != numLines) {
	            int width = 0;
	            int theWidth = 0;
	            String theLine = null;

	            setScale(1,(fm != null) ? fm.getHeight() + 2 : 12);

	        // Find the rightMost edge of the longest line and use it to set
	        // world co-ordinates
	            for (int i = 0; i < n; i++) {
	                theLine = commandProcessor.getConsoleLine(i);
	                if(theLine != null) {
	                    theWidth = stringWidth(theLine, g);
	                    if (theWidth > width) width = theWidth;
	                }
	            }
	            setPreferredSize (new Dimension(width+LEFT_MARGIN,n*getVScale()));
	            numLines = n;
	        }
	        super.refresh();
	        g.dispose();
	    }

//	 Replaces fontMetrics.stringWidth - takes colour changes into account
	    private int stringWidth(String theLine, Graphics screen){
	        int theWidth = 0;
	        if (theLine.length() > 0) {
	            String expanded = expandTabs(theLine);
	            for( int i = 0 ; i < expanded.length(); ++ i ) {
	                char c = expanded.charAt(i);
	                if ( c >= MARKER_BOUND)
	                    screen.setColor(c==StateInterface.INPUT_MARK ? Color.red : Color.black);
	                else
	                    theWidth += advances[c];
	            }
	        }
	        return theWidth;
	    }
//	 =================================================================
//	 Graphics Methods
//	 =================================================================

	    public void drawArea(Graphics2D g){
	        if( commandProcessor == null ) return ;
	        g.setFont(context.getCodeFont());
	        FontMetrics fm = g.getFontMetrics();
	        int baseLine = TOP_MARGIN;

	    //	if (getLastShowing() > 0)
	            for (int i = 0; i< numLines; i++) {
	                baseLine += fm.getAscent();
	                String theLine = commandProcessor.getConsoleLine(i);
	                if(theLine != null)
	                    drawLine(expandTabs(theLine), LEFT_MARGIN, baseLine, g);
	            }
	    }

//	 Draws a single line, taking mode changes into account
	    private void drawLine(String theLine, int x, int y, Graphics screen) {
	    // Must convert expression to array of characters as drawChars can only work with
	    // char arrays
	        if (theLine.length() > 0) {
	            char tempArray[] = theLine.toCharArray();
	            for( int i = 0; i < theLine.length(); ++ i ) {
	                char c = tempArray[i];
	                if ( c >= MARKER_BOUND)
	                    screen.setColor(c==StateInterface.INPUT_MARK ? Color.red : Color.black);
	                else {
	                    screen.drawChars(tempArray,i,1,x,y);
	                    x += advances[c];
	                }
	            }
	        }
	    }

	    private String expandTabs( String theLine ) {
	        int column = 0 ;
	        StringBuffer buf = new StringBuffer() ;
	        for( int i=0 , sz = theLine.length() ; i < sz ; ++i ) {
	            char c = theLine.charAt(i) ;
	            if( c >= MARKER_BOUND ) {
	                buf.append(c) ; }
	            else if( c == '\t' ) {
	                column = (column / TABSPACE  + 1) * TABSPACE ;
	                while( buf.length() < column ) buf.append(' ') ; }
	            else {
	                column += 1 ;
	                buf.append(c) ; } }
	        return buf.toString() ; }

}
