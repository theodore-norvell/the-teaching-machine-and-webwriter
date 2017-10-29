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
import java.awt.event.*;

import javax.swing.AbstractButton;
import javax.swing.JToolBar;

/* --------------------------------------------------------------------
CLASS ToolBar
	Implements a simple toolbar containing a number of buttons
	--------------------------------------------------------------------
*/

public class ToolBar extends JToolBar implements ActionListener{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7389340619003401902L;
	private WorkAreaInterface owner;		// Custom tool events will be sent to the workArea
	private String border;

	public ToolBar(AbstractButton buttons[]) {
		this(buttons, "North");
	}
	
	public ToolBar(AbstractButton buttons[], String side) {
		super();
		setFloatable(false);
		setOrientation(HORIZONTAL);
		
		if (side == "East" || side == "West" || side == "South")
			border = side;
		else
			border = "North";


		if (buttons != null){
			for (int i = 0; i < buttons.length; i++){
				add(buttons[i]);
				buttons[i].addActionListener(this);
			}
		}
	}
	
	public String getSide(){return border;}
	

	public void setReportTo(WorkAreaInterface a){
	    owner = a;
	}

	public void actionPerformed(ActionEvent evt){
	    for (int i = 0; i < getComponentCount(); i++)
	       if (getComponent(i) == evt.getSource())
	          owner.buttonPushed(i);
	}
	
	public String toString(){return border + " toolBar";}

}

