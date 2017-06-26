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

import java.awt.Component;
import java.util.Vector;

import tm.interfaces.Configurable;
import tm.interfaces.Datum;
import tm.interfaces.PlugIn;
import tm.interfaces.Scriptable;
import tm.subWindowPkg.ToolBar;
/**
 * 
 * The interface for all Teaching Machine displays. Essentially
 * the view side of the TM consists of a displayManager and a set of
 * displays that handle the visualization of the current state of
 * the TM.
 *
 * @author michael bruce-lockhart
 */

public interface DisplayInterface extends PlugIn, Configurable, Scriptable {
	/**
	 * This method is called every time a state change occurs in the Teaching 
	 * Machine. Displays are responsible for pulling the information they
	 * need from the model via the {@link tm.interfaces.CommandInterface CommandInterface}
	 *
	 */
	public void refresh();
	
	/**
	 * Load initial configuration, if one exists, from the configuration file
	 */
	public void loadInitConfig();
	
	/**
	 * 
	 * @return the display toolBar or null if there isn't one
	 */
	public ToolBar getToolBar();
	/**
	 * Interfaces cannot extend classes. Nevertheless, it is required that all
	 * displays be AWT components. Normally satisfied by returning {@code this}.
	 * @return the component that is this display
	 */			
	public Component getWindowComponent();
	/**
	 * This method allows the {@link tm.interfaces.DisplayManagerInterface displayManager}
	 * to discover all datums selected by a user.
	 * @since March, 2007
	 * @return null for non datum displays. The set of datums selected for displays
	 * where datums can be selected (or null if none are).
	 */
	public Vector <Datum> getSelected();
	
	/** Clean up after the display
	 * This method is called just before the display's window component is removed from the display.
	 * One thing it should do is deregister with the configuration manager.
	 */
	public void dispose() ;
}
