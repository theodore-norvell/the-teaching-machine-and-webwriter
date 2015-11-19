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

package tm.virtualMachine;

import tm.backtrack.BTByteArray;
import tm.backtrack.BTTimeManager;

/** The Memory class implements the memory of the virtual machine.
    The memory can be seen as an array of bytes.

*/
public class Memory extends BTByteArray {

	////////////// P U B L I C   P A R T//////////////////////

	/** Constructor: Create a memory with top as the highest address. */
	public Memory( BTTimeManager timeMan, int top ) {
		super( timeMan, top+1 ) ;
			//{{INIT_CONTROLS
		//}}
}
	//{{DECLARE_CONTROLS
	//}}
}