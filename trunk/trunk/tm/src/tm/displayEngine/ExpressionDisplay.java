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
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.util.Vector;

import tm.interfaces.DisplayContextInterface;
import tm.interfaces.ImageSourceInterface;
import tm.subWindowPkg.SmallButton;
import tm.subWindowPkg.ToolBar;

public class ExpressionDisplay extends DisplayAdapter {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3372985987803053351L;
	private static final int LEFTMARGIN = 4;
	private static final int TOPMARGIN = 2;

	public static final char MARKER1 = '\uffff';	
	public static final char MARKER2 = '\ufffe';	
	public static final char MARKER3 = '\ufffd';	
	public static final char MARKER4 = '\ufffc';
	public static final char ENDMARKER = '\ufffb';
	

// Font Control
/*	static int sizes[] = {6,7,8,9,10,11,12,13,14,15,16,18,20,22,24,28,32,48,64};
	Choice nameList, styleList, sizeList;
	Font myFont;
	static String fontList[] = null;
*/
	String theExpression = "";
	int advances[];		// Character sizes

	public ExpressionDisplay(DisplayManager dm, String configId) {
		super(dm, configId);
		ImageSourceInterface imageSource = context.getImageSource();
		SmallButton[] buttons = new SmallButton[2];
		buttons[0] = new SmallButton(SmallButton.BACKUP, imageSource);
		buttons[0].setToolTipText("Backup Expression Engine");
		buttons[1] = new SmallButton(SmallButton.ADVANCE, imageSource);
		buttons[1].setToolTipText("Step Expression Engine");
		toolBar = new ToolBar(buttons, "West");
		mySubWindow.addToolBar(toolBar);

//		System.out.println("ExpressionDisplay size is " + getSize());
		setPreferredSize(this.getViewportSize());
	}



// =================================================================
// Resource Interface  
// =================================================================

		
	public void refresh(){
		theExpression = commandProcessor.getExpression();
//		System.out.println("Refreshing expression");
		super.refresh();
	    
	}

	
	public void buttonPushed(int i) {
		if (i == 0) commandProcessor.goBack();
		if (i == 1) commandProcessor.goForward();
	}
	

// =================================================================
// Graphical overrides
	//	  
	//
// =================================================================
	public void drawArea(Graphics2D g){
//		System.out.println("Expression drawArea ");
		int advance = LEFTMARGIN;
		g.setFont(context.getDisplayFont());

	// Must convert expression to array of characters as drawChars can only work with
	// char arrays
		if (theExpression.length() > 0) {
//			System.out.println("Expression: " + theExpression);
			char tempArray[] = theExpression.toCharArray();
  
//			g.setFont(myFont);
			FontMetrics fm = g.getFontMetrics();
			advances = fm.getWidths();	// Widths of every character
			int baseline = TOPMARGIN + fm.getAscent();

			g.setColor(Color.black);
		    
			Color currColor = Color.black ;
			boolean currUnderline = false ;
			Vector attrStack = new Vector() ;
			for( int i = 0, sz = theExpression.length() ; i < sz ; ++ i ) {
				char c = tempArray[i];		// Next character
				switch (c) {
				    case MARKER1:
				        attrStack.addElement( currColor ) ;
				        currColor = Color.red ;
				        g.setColor( currColor ) ;
					    break;
					case MARKER2:
				        attrStack.addElement( new Boolean(currUnderline) ) ;
				        currUnderline = false ;
					    break;
					case MARKER3:
				        attrStack.addElement( new Boolean(currUnderline) ) ;
				        currUnderline = true ;
					    break;
					case MARKER4:
				        attrStack.addElement( currColor ) ;
				        currColor = Color.blue ;
				        g.setColor( currColor ) ;
					    break;
					case ENDMARKER:
					    Object temp = attrStack.elementAt( attrStack.size() - 1 ) ;
					    if( temp instanceof Color ) {
					        currColor = (Color) temp ;
					        g.setColor( currColor ) ; }
					    else {
					        currUnderline = ((Boolean)temp).booleanValue() ; }
					    attrStack.removeElementAt( attrStack.size() - 1 ) ;
					    break ;
					default:
//						System.out.println("drawing " + tempArray[i] + " at (" + advance + ", " + baseline + ")");						
					    g.drawChars(tempArray,i,1,advance,baseline);
					    if( currUnderline ) {
					        g.setColor( Color.black ) ;
					        g.fillRect( advance, baseline+2, advances[c], +3);
					        g.setColor( currColor ) ; }
					    advance += advances[c];
				}  // Switch
			}  // For loop
		}   // End if we have an expression
	}   // End drawArea


}
