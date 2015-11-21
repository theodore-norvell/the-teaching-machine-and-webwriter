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

/*
 * Created on 12-Nov-2005 by Theodore S. Norvell. 
 */
package tm.displayEngine.generators;

import tm.interfaces.Datum;


/** A standard generator that monitors the stack and the heap. It assumes
 * a pair of buttons has been assigned to it for selecting/deselcting
 * specified by a pair of buttons.
 * 
*/

public class StoreGenerator extends AbstractGenerator {
    private RegionGenerator staticGenerator ;
    private RegionGenerator stackGenerator ;
    private boolean staticOn = false;
    private boolean stackOn = true;
    
    public StoreGenerator(RegionGenerator staticGenerator,
    		RegionGenerator stackGenerator){
        this.staticGenerator = staticGenerator ;
        this.stackGenerator = stackGenerator ;
    }
    
    public int getNumChildren(){
    	int count = 0;
    	if (staticOn) count += staticGenerator.getNumChildren();
    	if (stackOn) count += stackGenerator.getNumChildren();
        return count ;
    }
    
    public Datum getChildAt(int i){
    	int kids = 0;
    	if (staticOn) {
    		kids = staticGenerator.getNumChildren();
    		if (i < kids) return staticGenerator.getChildAt(i);
    		i -= kids;
    	}
    	if (stackOn) {
    		kids = stackGenerator.getNumChildren();
    		if (i < kids) return stackGenerator.getChildAt(i);
    	}
    	return null;
    }
    
    public void buttonPushed(int i, boolean down){
    	if (i==0) staticOn = down;
    	if (i==1) stackOn = down;
    }
    
    public String toString(){
    	String s = "Store Generator ";
    	if (!staticOn && ! stackOn) return "Empty " + s;
    	s += "includes ";
    	if (staticOn){
    		s +=  staticGenerator.toString();
    		if (stackOn) s += " and ";
    	}
    	if (stackOn) s += stackGenerator.toString();
    	return s;
    	}
    	
 }