//     Copyright 1998--2013 Michael Bruce-Lockhart and Theodore S. Norvell
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

import java.util.Hashtable;

/** A BTFunction is a function that can be backed up to a
previous state */
public class BTFunction<K,E> {
	private Hashtable<K,BTVar<E>> hash ;
	private BTTimeManager timeMan ;

	public BTFunction( BTTimeManager tm ) {
		hash = new Hashtable<K,BTVar<E>>() ;
		timeMan = tm ;
	}

	public E at(K d) {
		BTVar<E> var = hash.get( d ) ;
		if( var == null || ! var.alive )
			return null ;
		else
			return var.get( ) ;
	}

	public void map(K d, E r) {
		BTVar<E> var = hash.get( d ) ;
		if( var == null || ! var.alive ) {
			var = new BTVar<E>(timeMan, r) ;
			hash.put( d, var ) ; }
		else {
			var.set( r ) ;
		}
	}
}
