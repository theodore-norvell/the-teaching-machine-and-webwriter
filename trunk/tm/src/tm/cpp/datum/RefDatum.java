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

package tm.cpp.datum;

import tm.backtrack.BTTimeManager;
import tm.clc.ast.TypeNode;
import tm.clc.datum.AbstractRefDatum;
import tm.interfaces.Datum;
import tm.virtualMachine.Memory;
import tm.virtualMachine.Store;


/* Class: RefDatum.
   References for C++.
*/
public class RefDatum extends AbstractRefDatum {

	private String targetName ;

	public RefDatum(int a, Datum p, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
		super(a, p, m, n, tp, str, timeMan);
    }

	public RefDatum(int a, Memory m, String n, TypeNode tp, Store str, BTTimeManager timeMan) {
		this(a, null, m, n, tp, str, timeMan);
	}

	public void putValueString( String str ) {
		targetName = str ; }

	public String getValueString() {
		if( targetName != null ) 
			return targetName ;
		else {
			int v = getValue() ;
			return "(*" + v + ")"; } }
}