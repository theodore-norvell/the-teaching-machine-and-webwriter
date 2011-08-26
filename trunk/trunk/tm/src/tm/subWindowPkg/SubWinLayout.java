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

import java.awt.*;

/* --------------------------------------------------------------------
CLASS SubWinLayout
	My very own layout manager! Actually it does very little since the sub-windows
	largely take care of themselves. It is really here to allow the windows to do that
	with impunity knowing they will not be interfered with by their (almost) dummy
	layout manager.

  The layout manager is called automatically when the WindowControl frame is resized. Thus
  it is able to check if any window was maximized and resize it to the new maximum.
	--------------------------------------------------------------------
*/


public class SubWinLayout implements LayoutManager {
	
        // Methods
	public SubWinLayout(){
			//{{INIT_CONTROLS
		//}}
}

/* This method is a Kludge, probably added to support BorderLayout. The
Container.add method is used to add components to a container. However the
particular form Container.add(String tag,Component c) results in a call
from container to the layout manager.

While we may not need the string name if we do need to know about the
added Component, we can coerce the notification by demanding that
calls to add should be of form add("",SubWindow)
*/
    public void addLayoutComponent(String name,Component comp) { ; }

/* This is really the layout method. It DOESN'T paint. It is only
 concerned with location. Our windows lay themselves out except when
 the frame is resized. All we do here is make sure a maximized window
 stays maximized.
 */
    public void layoutContainer(Container parent) { 
/*		for (int i = 0; i < parent.getComponentCount(); i++) {
			SubWindow w = (SubWindow)parent.getComponent(i);
			if (w.isMaximized()) {
				w.setBounds(0,0, parent.getSize().width, parent.getSize().height);
				break;		// There can be only one!
			}
		}*/
	
 }

    public Dimension minimumLayoutSize(Container parent) {
		Dimension d = new Dimension(41,41);
		return d;
	}

    public Dimension preferredLayoutSize(Container parent) {
		Dimension d = new Dimension(341,341);
		return d;
	}

    public void removeLayoutComponent(Component comp) { ; }

	//{{DECLARE_CONTROLS
	//}}
}


