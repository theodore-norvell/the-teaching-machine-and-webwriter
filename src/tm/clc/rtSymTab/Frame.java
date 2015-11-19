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

import tm.backtrack.BTStack;
import tm.backtrack.BTTimeManager;
import tm.clc.analysis.ScopedName;


class Frame {
	
	private Frame _staticLink ;

	private BTStack<VarEntry> _vars ;

	Frame(BTTimeManager timeMan, Frame staticLink ) {
		_staticLink = staticLink ;
		_vars = new BTStack<VarEntry>(timeMan) ;
}

	/** ACCESSORS */

	/** The parents frame */
	Frame staticLink() { return _staticLink ; }
	
	/** How many variables are there */
	int size() { return _vars.size() ; }

	/** lookupByIndex -- look for the top variable with a given scoped name in this frame. */
	
	VarEntry lookupByIndex( ScopedName index ) {
		for( int i=0, sz = _vars.size() ; i < sz ; ++ i ) {
				VarEntry entry = _vars.get( sz-i-1 ) ;
				if( index.equals( entry.getIndex() ) ) {
					return entry ; } }
		return null ; }

	/** lookupByNumber */

	VarEntry lookupByNumber( int i ) {
		return _vars.get( i ) ; }

	int varsInFrame() { return _vars.size() ; }

	/** MUTATORS */

	/** Add a new var entry */

	void addVar( VarEntry varEntry ) { _vars.push( varEntry ) ; }

	/** Delete top variable entry */
	void deleteVar( ) {
		_vars.pop() ; }
}
