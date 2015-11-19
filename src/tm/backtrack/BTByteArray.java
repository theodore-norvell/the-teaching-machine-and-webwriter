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

import tm.utilities.Assert ;
/** An array of bytes that can support undo and redo */
public class BTByteArray {
	BTTimeManager timeMan ;
	byte[] a ;
	int size ;

	public BTByteArray(BTTimeManager tm, int s) {
		timeMan = tm ;
		size = s ;
		a = new byte[size] ;
	}

	public void putByte(int i, byte b) {
		//if( !alive ) Assert.check(false) ; 
		if(! ( 0 <= i && i < size) ) Assert.check( false ) ;
		timeMan.notePutByte(this, i ) ;
		a[i] =  b ;
	}

	public byte getByte(int i) {
		//if( !alive ) Assert.check(false) ; 
		if(! ( 0 <= i && i < size) ) Assert.check( false ) ;
		return a[i] ;
	}
}
