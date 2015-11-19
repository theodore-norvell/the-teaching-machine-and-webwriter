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

public class MalleableGenerator extends AbstractGenerator {

	private Vector<Datum> theSelection;
	private DisplayContextInterface context;
	private DisplayInterface owner;
	
	public MalleableGenerator(DisplayContextInterface dc, DisplayInterface o){
		if (owner != null) return;  // singleton
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
		
	}
	
	public boolean isMalleable(){
		return true;
	}
	
	/* Implementation of Malleable propertiese */

	public void clear(){theSelection.clear();}
	
	public void add(Datum datum) {
		theSelection.add(datum);
	}
	
	public void add(Vector<Datum> datums) {
		theSelection.addAll(datums);
	}
	
	public void add(AbstractGenerator generator) {
		for(int i = 0; i < generator.getNumChildren(); i++)
			theSelection.add(generator.getChildAt(i));
	}
	
	public void remove(Datum datum){
		theSelection.remove(datum);
	}
	
	public void remove(AbstractGenerator generator) {
		for(int i = 0; i < generator.getNumChildren(); i++)
			theSelection.remove(generator.getChildAt(i));
	}
	
	public String toString(){
		return "Malleable Generator with " + getNumChildren() + " datums added";
	}

}
