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
import tm.clc.ast.TyAbstractClass;
import tm.clc.ast.TyAbstractClassDeclared;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractDatum;
import tm.clc.datum.AbstractObjectDatum;
import tm.clc.datum.AbstractPointerDatum;
import tm.interfaces.Datum;
import tm.javaLang.analysis.Java_ScopedName;
import tm.javaLang.ast.TyJavaArray;
import tm.utilities.Assert;
import tm.virtualMachine.Memory;
import tm.virtualMachine.VMState;

/*******************************************************************************
Class: ObjectDatum

Overview:
This class represents an object or structure in memory.

Review:          2002 08 05     Michael Burton and Jonathan Anderson
*******************************************************************************/

public class ObjectDatum extends AbstractObjectDatum {
    public ObjectDatum(int add, Datum p, Memory m, String n, 
            TyAbstractClass tp, BTTimeManager timeMan) {
        super(add, p, m, n, tp, timeMan);
    }
    
    public ObjectDatum(int add, Datum p, Memory m, String n, 
            TyAbstractClass tp, int numberOfElements, BTTimeManager timeMan) {
        super(add, p, m, n, tp, numberOfElements, timeMan);
    }
    
    public ArrayDatum getStringArrayDatum(){
		AbstractDatum stringRef = getField(0);
		Assert.check(stringRef instanceof AbstractPointerDatum);
		Datum target = ((AbstractPointerDatum)stringRef).deref();
		Assert.check(target instanceof ArrayDatum);
		return(ArrayDatum)target;   	
    }

    public String getValueString(){
        return type.getTypeString()+"{..}";
    }
}