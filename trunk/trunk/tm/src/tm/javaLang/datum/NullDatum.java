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

package tm.javaLang.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.interfaces.TypeInterface;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.Store;

/** This class represents the value of the literal "null".
 *
 * @author Theo
 */
public class NullDatum extends tm.clc.datum.AbstractPointerDatum {
    public static final int size = 4;

    public NullDatum(int a, Datum p, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
        super(a, size, p, m, n, tp, str, timeMan);
        putValue(0);
    }

    public NullDatum(int a, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
        this(a, null, m, n, tp, str, timeMan);
    }

    protected NullDatum(NullDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new NullDatum(this);
    }

    public TypeInterface getPointeeType() {
        Assert.check( false ) ;
        return null;
    }

    public boolean isNull() { return true;	}

    public String getValueString() {
        return "null" ; }
}