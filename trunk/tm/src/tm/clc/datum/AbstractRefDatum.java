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

package tm.clc.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TyAbstractRef;
import tm.clc.ast.TypeNode;
import tm.interfaces.Datum;
import tm.interfaces.TypeInterface;
import tm.virtualMachine.Memory;
import tm.virtualMachine.Store;




/* Class: AbstractRefDatum.
   Language independant representation of references of all sorts.
   References are used both for variables of reference type
   (as in C++) but also in a lot of places where the virtual
   machine has done a lookup and needs to keep a pointer to
   an object.
*/
abstract public class AbstractRefDatum extends AbstractPointerDatum {

	public static final int size = 4 ;

	public AbstractRefDatum(int a, Datum p, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
		super(a, size, p, m, n, tp, str, timeMan);
    }

	public AbstractRefDatum(int a, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
		this(a, null, m, n, tp, str, timeMan);
	}

	public boolean isNull() {return 0==getValue() ;}

	abstract public void putValueString( String str ) ;

	abstract public String getValueString() ;

	public TypeInterface getPointeeType() { return ((TyAbstractRef) type).getPointeeType() ; }
}