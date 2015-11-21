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

import java.awt.*;


/* Updated 2005.11.06 by refactoring names
 * parent class for displays of datums, such as storeDisplay or linkedDisplay
*/

// was StoreDisplayView -- too store centric
public interface DataDisplayView {
    public void refresh();  // refresh data display
//    public void systemRefresh();  // refresh everything
    public Dimension getSize();
   
/* In some views, widths are fixed. In others they are elastic, for example expanding to
fill a window unless that results in too small a size. Thus field widths are a function of
the particular view.
*/
    public int getNameW();
    public int getValueW();
    public int getAddressW();
    
// was getView  --  too generic    
    public int getDatumView(); // LOGICAL, SCALED, BINARY
    public String getDisplayString();
}

