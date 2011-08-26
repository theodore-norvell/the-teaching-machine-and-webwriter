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
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.interfaces.Datum;
import tm.interfaces.TypeInterface;
import tm.javaLang.analysis.Java_ScopedName;
import tm.javaLang.ast.TyJavaArray;
import tm.javaLang.ast.TyPointer;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.Store;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: PointerDatum

Overview:
This class represents a Pointer stored in memory.

Review:          2002 08 05     Michael Burton and Jonathan Anderson
*******************************************************************************/

public class PointerDatum extends tm.clc.datum.AbstractPointerDatum {
    public static final int size = 4;

    public PointerDatum(int a, Datum p, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
        super(a, size, p, m, n, tp, str, timeMan);
    }

    public PointerDatum(int a, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
        this(a, null, m, n, tp, str, timeMan);
    }

    protected PointerDatum(PointerDatum original){
    	super(original);
    }
    
    public Datum copy(){
    	return new PointerDatum(this);
    }

    public TypeInterface getPointeeType() {
        return ((TyPointer) type).getPointeeType();
    }

    public boolean isNull() { return 0==getValue();	}

    public String getValueString() {
        if( isNull() ) return "null" ;
        else {
            //Datum d = this.deref() ;
            //if( d == null ) return "???" ;
            //else return this.deref().getValueString();
            return ""+getValue() ;
        } }
}