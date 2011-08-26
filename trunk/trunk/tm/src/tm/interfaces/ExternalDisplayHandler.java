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

import java.awt.Graphics2D;

/**
 * External displays are used to show snapshots of the TM displays, generally
 * in response to requests to the ScriptManager. Such displays are not part of
 * the TM, per se.
 * 
 * @author mpbl 
 *
 */
public interface ExternalDisplayHandler {
	/**
	 * The handler should be able to handle multiple external displays, distinguishing them
	 * by individual id strings
	 * 
	 * @param displayId a unique string identifying a particular external display
	 * @return the 2D graphics context of the specified external display
	 */
	public Graphics2D getGraphics(String displayId);

}
