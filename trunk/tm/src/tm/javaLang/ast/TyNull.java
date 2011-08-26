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

package tm.javaLang.ast;

import tm.clc.ast.TyAbstractPointer;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.javaLang.datum.NullDatum;
import tm.utilities.Assert;
import tm.virtualMachine.MemRegion;
import tm.virtualMachine.VMState;

/** This class represents the type of the Java null pointer constant.
 * @author Theo
*/

public class TyNull extends TyAbstractPointer implements TyJava {

    private static TyNull singleton ;

    static TyNull get() {
        if( singleton == null ) singleton = new TyNull() ;
        return singleton ; }

    private TyNull() { super(); }

    public AbstractDatum makeMemberDatum(VMState vms, int address,
                                         AbstractDatum parent, String name) {
        NullDatum d = new NullDatum(address, parent, vms.getMemory(),
                                          name, this, vms.getStore(), vms.getTimeManager());
        return d;
    }

    public AbstractDatum makeDatum(VMState vms, MemRegion mr, String name) {
        int address = mr.findSpace(NullDatum.size);
        NullDatum d = new NullDatum(address, null, vms.getMemory(), name,
                                          this, vms.getStore(), vms.getTimeManager());
        vms.getStore().addDatum(d);
        return d;
    }

    public int getNumBytes() { return NullDatum.size; }

    public String getTypeString(){ return "null"; }

    public String elementId(){
        Assert.check(false);
        return "";
    }

    public String typeId() { return "null"; }

    public void addToEnd(TypeNode t) {
        Assert.check( false );
    }

    public boolean isReachableByWideningFrom(TyJava fromType) {
        return false;
    }
    
    public String toString(){ return "null";}
}