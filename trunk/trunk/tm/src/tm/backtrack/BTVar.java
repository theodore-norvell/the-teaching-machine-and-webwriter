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

import tm.utilities.Assert;

public class BTVar<T> {
    final BTTimeManager timeMan ;
    Object currentValue ;
    boolean alive ;

	public BTVar(BTTimeManager tm) {
		timeMan = tm ;
		alive = true ;
		timeMan.noteBirth( this ) ;
    }

    public BTVar(BTTimeManager tm, T initialValue) {
		timeMan = tm ;
		alive = true ;
		timeMan.noteBirth( this ) ;
		currentValue = initialValue ;

    }

	public void set(T o) {
		if(!alive) Assert.check(false, "Dead variable is modified.") ;
		timeMan.noteUpdate( this ) ;
		currentValue = o ;
	}

	public void kill() {
		if(!alive) Assert.check(false, "Dead variable is killed again.") ;
		timeMan.noteDeath( this ) ;
		currentValue = null ;
		alive = false ;
	}

	public void revive(T o) {
		if(alive) Assert.check(false, "Live variable is revived.") ;
		timeMan.noteBirth( this ) ;
		currentValue = o ;
		alive = true ;
	}

	@SuppressWarnings("unchecked")
	public T get() {
		if(!alive) Assert.check(false, "Dead variable is accessed.") ;
		return (T) currentValue ;
	}

	public boolean alive() { return alive; }
}
