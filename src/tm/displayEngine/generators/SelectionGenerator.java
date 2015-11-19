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

package tm.displayEngine.generators;

import java.util.Vector;

import tm.displayEngine.DisplayInterface;
import tm.interfaces.Datum;
import tm.interfaces.DisplayContextInterface;

/* Represents a generator put together by an external selection
 * mechanism (such as a mouse). Thus it is generally an arbitrary set
 * of datums. As such it may also be manipulated via function calls.
*/

public class SelectionGenerator extends AbstractGenerator {
	
	private Vector<Datum> theSelection;
	private DisplayContextInterface context;
	private DisplayInterface owner;
	
	public SelectionGenerator(DisplayContextInterface dc, DisplayInterface o){
		owner = o;
		context = dc;
		theSelection = dc.getSelection(o);
	}
	
	public Datum getChildAt(int i) {
		if (theSelection == null) return null;
		return theSelection.elementAt(i);
	}

	public int getNumChildren() {
		if (theSelection == null) return 0;
		return theSelection.size();
	}
	
	public void refresh(){
		theSelection = context.getSelection(owner);	
	}
	
	public String toString(){
		return "Selection Generator with " + getNumChildren() + " datums in the selection.";
	}

	
}
