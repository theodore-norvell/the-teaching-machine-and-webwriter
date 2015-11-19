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

package tm.clc.rtSymTab;

import tm.backtrack.BTTimeManager;
import tm.backtrack.BTVar;
import tm.clc.analysis.ScopedName;
import tm.clc.datum.AbstractDatum;
import tm.interfaces.STEntry;
import tm.utilities.Assert;

class VarEntry {
    String name ;
    ScopedName index ;
    AbstractDatum  datum ;
    BTVar<Integer> highlight ;

    VarEntry( ScopedName idx, String nm, AbstractDatum d, BTTimeManager timeMan ) {
        Assert.check( idx != null && nm != null && d != null ) ;
        index = idx ;
        name = nm ; 
        datum = d ; 
        highlight = new BTVar<Integer>( timeMan ) ;
        highlight.set( new Integer( STEntry.PLAIN  ) ) ; }

    public ScopedName getIndex() { return index ; }
    
    public String getName() { return name ; }

    public AbstractDatum getDatum() { return datum ; }
    
    public void putHighlight(int h) { highlight.set( new Integer(h) ) ; }
    
    public int getHighlight( ) {
        Integer h = highlight.get() ;
        return h.intValue() ;}

    STEntry getSTEntry() {
        return new STEntry( name, datum.getTypeString(), datum.getAddress(),
                            getHighlight() ) ; }
}