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
import java.awt.Graphics2D;
import tm.interfaces.DisplayContextInterface;


/*	A very simpleminded display. All it does is paint a string
representing the contents of a program counter. It could possibly
be combined into a single class with the expressionEngine
*/

public class PCDisplay extends DisplayAdapter{
	/**
	 * 
	 */
	private static final long serialVersionUID = -2305992297895605099L;
	public static final int LEFTMARGIN = 4;
	public static final int TOPMARGIN = 2;
	String theLocation = null;

	public PCDisplay(DisplayManager dm, String configId) {
	    super(dm, configId);
//		setPreferredSize(this.getViewportSize());
	}


// =================================================================
// Graphical overrides
	//	  
	//
// =================================================================
	public void drawArea(Graphics2D g){
		g.setFont(context.getDisplayFont());

		g.setColor(Color.black);
		g.drawString (commandProcessor.getPCLocation(), LEFTMARGIN,
		TOPMARGIN + g.getFontMetrics().getAscent());
	}


}
