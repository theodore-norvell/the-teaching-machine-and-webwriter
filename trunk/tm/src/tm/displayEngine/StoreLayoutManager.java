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
  import java.awt.Dimension;

import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;
  
  
  public class StoreLayoutManager {
    
	private static final int ADDRESS_W = 55;  // minimum widths for display boxes
	private static final int VALUE_W = 150;  // value boxes can be less since insets taken OUT of this width
	private static final int NAME_W = 120;  // memory display boxes
    
    
    private StoreDisplay target;
//    private Dimension mySize = new Dimension(0,0);
    
    public StoreLayoutManager(StoreDisplay t){
    	target = t;
    }
    
    public void layoutDisplay(/*DisplayTree displayTree*/){


/*  Work out the base sizes which are somewhat elastic.

    All datumDisplays have three fields, the name, the value and the address. For top level
    datums (no parent) these fill the width of the storeDisplay, as follows:
    
    The address width is fixed.
    Value takes 40% of what remains unless it drops below a minimum
    Name takes 60%, unless it drops below a minimum.
    
    So if the minimums are observed, there is no horizontal scrollBar . Otherwise there is.
    
    The situation is the same for child datums except that value boxes get increasingly smaller
    as the nesting level increases.
    
    The nameBox is located at extent.x, while the addressBox is at extent.x + nameBox.extent.width +
    valueBoxWidth where valueBoxWidth is the width of the valueBox at nesting level zero.
    
    Top level valueBoxes occur at extent.x + nameBox.extent.width. Others are right justified within
    the space between the name and address boxes, leaving a gap on their left for expanders and
    leaders.
    
    The layout manager locates and sizes toplevel datumDisplays only.
    
*/
  

// width before refresh: Store is refreshed after layout		
        int oldWidth = target.getViewportSize().width;         
        int nameWidth = (int)(.4 * (double)(oldWidth - ADDRESS_W));
		int valueWidth = oldWidth-nameWidth-ADDRESS_W; // Base width with no room for expanders
		if (nameWidth < NAME_W) nameWidth = NAME_W;
		if (valueWidth < VALUE_W) valueWidth = VALUE_W;
        

        int x = 0;
        int y = 0;
            
        RegionInterface region = target.getRegion();
        
        int kids = region.getNumChildren();
        
        int firstDatum = 0;      // Assume all showing
        int lastDatum = kids - 1;

		for (int i = 0; i < kids; i++) {
//			System.out.println("Laying out Datum " + i);
		    Datum kid = region.getChildAt(i);
		    DatumDisplay dd = DatumDisplay.getAssociated(kid,target);
		    // This suppresses errors to Java console caused by asynchronous repaint requests
		    if(dd == null) return; // asynchronous request, no refresh, punt
            dd.move(x,y);
            dd.resize(nameWidth,0,valueWidth,ADDRESS_W);    // height will be sized automatically
            Expander expander = dd.getExpander();
            y += dd.getSize().height;
            if (y < target.getFirstY())
                firstDatum = i;
            if (y > target.getLastY() && lastDatum == (kids-1))
                lastDatum = i;
		}
		target.setPreferredSize(new Dimension(nameWidth + valueWidth + ADDRESS_W, y+1));
		target.setOnScreen(firstDatum, lastDatum);
    }
    

  }
