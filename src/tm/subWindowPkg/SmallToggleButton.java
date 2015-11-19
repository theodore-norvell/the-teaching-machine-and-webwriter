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

import java.awt.AWTEventMulticaster;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;

import tm.interfaces.ImageSourceInterface;
import tm.utilities.Assert;

/* This class implements small 3D buttons. A button can be momentary,
in which case it is only depressed when the mouse is over it and
clicked down, or not, in which case it stays down, once a mouse
is clicked on it and is restored by software call.

  Buttons are designed to be used with gif images of WIDTH x WIDTH
  pixels to mark their use.

  ------------------------------------------------------------
  Feb. 98   extensively amended for AWT 1.1

  Sept. '99. Added blocking so that buttons can be blocked by their owners. The
  particular case of interest was subwindows that don't have the focus responding
  when they were clicked. A click on an out-of-focus subwindow should only bring it
  into focus. It should have no other effects. Thus windows that are not in-focus can
  block their buttons. Indeed, block is the default. A button must be actively unblocked
  before it can be used.
  ------------------------------------------------------------
  July 06   moved to JButton
*/
/** A small button used for subWindow control (such as opening or closing a
 * subWindow)
 */
public class SmallToggleButton extends JToggleButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6665517445513669386L;
	// Some standard buttons
    /** predefined window minimize button */    
	public static final int MINIMIZE = 0;
        /** predefined window maximize button */        
	public static final int MAXIMIZE = 1;
        /** predefined window shrink button */        
	public static final int SHRINK = 2;
        /** predefined window close button */        
	public static final int CLOSE = 3;
        /** predefined up arrow */        
	public static final int UPARROW = 4;
        /** predefined down arrow */        
	public static final int DOWNARROW = 5;
        /** predefined left arrow */        
	public static final int LEFTARROW = 6;
        /** predefined right arrow */        
	public static final int RIGHTARROW = 7;
        /** predefined advance button */        
	public static final int ADVANCE = 8;
        /** predefined backup button */        
	public static final int BACKUP = 9;
        /** predefined help button */        
	public static final int HELP = 10;

	private static final int WIDTH = 16;
	private static final int HEIGHT = WIDTH;
	private static final String buttonFiles[] =
		{"Minimize","Maximize", "Shrink", "Close",
		"UpArrow", "DownArrow", "LeftArrow", "RightArrow",
		"Advance", "Backup", "Help"};

	boolean blocked = true;     // button does not respond to mouse

	private String myString;
	/* See note for depressed */
	private Dimension onlySize = new Dimension(WIDTH,HEIGHT);

        /** Create a smallButton
         * @param i The number of a predefined button (between 0 and HELP)
         * @param m true if button is momentary,  false for click on - click off
         * @param c true if the button can be greyed out
         * @param is the image source
         */        
	public SmallToggleButton(int i, /*boolean m, boolean c,*/ ImageSourceInterface is) {
		super();
		Assert.check(i >= 0 && i <= HELP);
		myString = buttonFiles[i];
		setIcon(new ImageIcon(
				is.fetchImage("subWindowPkg/" + myString + ".gif")));
		setSize(onlySize);
	}
        /** Create a smallButton
         * @param s The label for the button
         * @param m true if button is momentary,  false for click on - click off
         * @param c true if the button can be greyed out
         * @param is the image source
         */        
	public SmallToggleButton(String s, /*boolean m, boolean c,*/ ImageSourceInterface is) {
		super();
		myString = s;
		setIcon(new ImageIcon(
				is.fetchImage("subWindowPkg/" + myString + ".gif")));
		setSize(onlySize);
	}


    /** set the button. Only works for non-greyed out click on/click off buttons
     * @param down down if true, up if false
     */    
	public void setButton(boolean down) {
		this.setSelected(down);
	}
	
	public boolean isDown(){return getModel().isSelected();}


    /** block button from responding
     * @param block button blocked if true, unblocked if false
     */    
    public void setBlocked(boolean block){
        blocked = block;
    }

 	public Dimension getPreferredSize(){
		return onlySize;
	}

	public Dimension getMinimumSize(){
		return getPreferredSize();
	}

}