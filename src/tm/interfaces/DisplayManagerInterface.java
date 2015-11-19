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

package tm.interfaces;
import java.awt.Component;


/**
 * The DisplayManagerInterface is actually the main interface for the view as seen
 * by the model. DM's actually have a second interface for the use of the displays
 * they manage called the DisplayContextInterface. Perhaps when we get rid of CVS I'll
 * refactor this so DMInterface incorporates DMJack and DMPlug
 *
 * @author mpbl
 */
public interface DisplayManagerInterface extends PlugIn, Configurable{
	public void createAllDisplays();
    public void refresh();
    public Component getComponent();
    /** Clean up after your self. Called when display manager is about to be destroyed.*/
    public void dispose() ;
}
