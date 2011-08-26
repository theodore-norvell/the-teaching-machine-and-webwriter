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

/** A class for objects that can be backed up
 */
public class BTVar<T> {
    private BTTimeManager timeMan ;
    private BTValManager<T> valMan ;

	public BTVar(BTTimeManager tm) {
		timeMan = tm ;
		valMan = null ;
    }

    public BTVar(BTTimeManager tm, T initialValue) {
        timeMan = tm ;
        valMan = new BTValManager<T>( ) ;
        valMan.set( timeMan.getCurrentTime(), initialValue ) ;
    }

	public void set(T o) {
	    if( valMan == null ) {
	        valMan = new BTValManager<T>( ) ; }
		valMan.set( timeMan.getCurrentTime(), o ) ; }

	public T get() {
	    if( valMan == null ) {
	        return null ; }
	    else {
		    return valMan.get( timeMan.getCurrentTime() ) ; }
	}
}