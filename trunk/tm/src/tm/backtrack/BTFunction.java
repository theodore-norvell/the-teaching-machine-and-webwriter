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

package tm.backtrack;

import java.util.* ;

/** A BTFunction is a function that can be backed up to a
    previous state */
public class BTFunction<K,E> {
	private Hashtable<K,BTValManager<E>> hash ;
	private BTTimeManager timeMan ;

	public BTFunction( BTTimeManager tm ) {
		hash = new Hashtable<K,BTValManager<E>>() ;
		timeMan = tm ;
}

	public E at(K d) {
		BTValManager<E> valMan = hash.get( d ) ;
		if( valMan == null )
		    return null ;
		else
	        return valMan.get( timeMan.getCurrentTime() ) ;
	}

	public void map(K d, E r) {
		BTValManager<E> valMan = hash.get( d ) ;
		/*  r == null && valMan == null
		        Status Quo is fine.
		    r != null && valMan == null
		        Need a value manager.
		        Need to set that value manager with the value.
		    valMan != null
		        Need to set the value manager with the value.
		*/
		if( r != null || valMan != null ) {
		    // Then the status quo is no good.
		    if( valMan == null ) {
		        valMan = new BTValManager<E>() ;
		        hash.put( d, valMan ) ; }
		    valMan.set( timeMan.getCurrentTime(), r ) ; }
    }
}