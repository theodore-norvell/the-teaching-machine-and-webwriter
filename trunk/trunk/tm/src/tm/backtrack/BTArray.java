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

import tm.utilities.Assert;
//
//
// BTArray
//
//
public class BTArray<E> {
	private Object[] a ;
	private BTTimeManager timeMan ;
	private int size ;

	public BTArray(BTTimeManager tm, int s) {
	    timeMan = tm ;
	    size = s ;
	    a = new Object[size] ;
}

	public void put(int i, E o) {
	    Assert.check( 0 <= i && i < size ) ;
	    BTValManager<E> valMan ;
	    if( a[i] == null ) {
	        valMan = new BTValManager<E>( ) ;
	        a[i] = valMan ; }
	    else {
	        valMan = (BTValManager<E>) a[i] ; }
	    valMan.set(timeMan.getCurrentTime(), o ) ;
	}

	public E get(int i) {
	    Assert.check( 0 <= i && i < size ) ;
	    BTValManager<E> valMan = (BTValManager<E>) a[i] ;
	    if( valMan == null ) {
	        return null ; }
	    else {
    		return valMan.get( timeMan.getCurrentTime() ) ; }
    }
}