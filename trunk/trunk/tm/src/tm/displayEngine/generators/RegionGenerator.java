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

import tm.interfaces.Datum;
import tm.interfaces.RegionInterface;


/* This is a simple wrapper around a region - actually a facade. It has a much
simpler interface than a region but its real purpose (not yet implemented) is to
allow an arbitrary list of datums to be used as a generator instead of a specific
region (like the stack). Regions model memory regions and thus carry the implication
that they span a particular address space. A generator is simply any set of Datums
*/

public class RegionGenerator extends AbstractGenerator {
    private RegionInterface region;
    
    public RegionGenerator(RegionInterface r){
        region = r;
    }
    
    public int getNumChildren(){
        return region.getNumChildren();
    }
    
    public Datum getChildAt(int i){
        return region.getChildAt(i);
    }
    
    public String toString(){
        return "Region Generator for " + region.toString();
    }
    
}
    
