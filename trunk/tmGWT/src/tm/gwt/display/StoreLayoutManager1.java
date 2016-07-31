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

package tm.gwt.display;

import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;
import tm.interfaces.StoreLayoutManagerI;
import tm.portableDisplays.DatumDisplay;
  
  
  public class StoreLayoutManager1 implements StoreLayoutManagerI{
    
    private StoreDisplay1 target;
    
    public StoreLayoutManager1(StoreDisplay1 t){
    	target = t;
    }
    
    public void layoutDisplay(){
        int oldWidth = 150;//target.getViewportSize().width;         
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
		    Datum kid = region.getChildAt(i);
		    DatumDisplay dd = DatumDisplay.getAssociated(kid,target);
		    // This suppresses errors to Java console caused by asynchronous repaint requests
		    if(dd == null) return; // asynchronous request, no refresh, punt
            dd.move(x,y);
            dd.resize(nameWidth,0,valueWidth,ADDRESS_W);    // height will be sized automatically
            y += dd.getSize().height;
            if (y < target.getFirstY())
                firstDatum = i;
            if (y > target.getLastY() && lastDatum == (kids-1))
                lastDatum = i;
		}
//		target.setPreferredSize(new Dimension(nameWidth + valueWidth + ADDRESS_W, y+1));
//		target.setOnScreen(firstDatum, lastDatum);
    }
  }
