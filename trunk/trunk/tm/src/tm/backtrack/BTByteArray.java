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

import java.util.Vector ;

import tm.utilities.Assert;
//
//
// BTByteArray
//
//
public class BTByteArray implements Backtrackable {
	private byte[] a ;
	private boolean[] changed ;
	private int size ;
	private Vector< Vector<Entry> > stack ;

	public BTByteArray(BTTimeManager tm, int s) {
	    tm.register( this ) ;
	    size = s ;
	    
	    a = new byte[size] ;
	    for( int i=0 ; i<size ; ++i ) {
	        a[i] = (byte) (i-100) ; }
	    
	    changed = new boolean[size] ;
	    for( int i=0 ; i<size ; ++i ) changed[i] = false ;
	    
	    stack = new Vector< Vector<Entry> >() ;
	    stack.addElement( new Vector<Entry>() ) ;
}

	public void putByte(int i, byte b) {
	    Assert.check( 0 <= i && i < size ) ;
	    if( ! changed[i] ) save( i, a[i] ) ;
	    a[i] = b ;
	}

	public byte getByte(int i) {
	    Assert.check( 0 <= i && i < size ) ;
	    return a[i] ;
    }
    
    private void save( int i, byte b ) {
        Vector<Entry> top = stack.lastElement() ;
        top.addElement( new Entry( i, b ) ) ;
        changed[i] = true ;
    }
        
    /* callback for time manager */
    public void undo() {
        Vector<Entry> top = stack.lastElement() ;
        for(int i=0, sz=top.size() ; i<sz ; ++i ) {
            Entry e = top.elementAt(i) ;
            a[ e.i ] = e.b ;
            changed[ e.i ] = false ; }
        
        stack.removeElementAt( stack.size() - 1 ) ;
        
        top = stack.lastElement() ;
        for(int i=0, sz=top.size() ; i<sz ; ++i ) {
            Entry e = (Entry) top.elementAt(i) ;
            changed[ e.i ] = true ; } }
    
    public void checkpoint() {
        Vector<Entry> top = stack.lastElement() ;
        for(int i=0, sz=top.size() ; i<sz ; ++i ) {
            Entry e = top.elementAt(i) ;
            changed[ e.i ] = false ; }
            
        Vector<Entry> newTop = new Vector<Entry>() ;
        stack.addElement( newTop ) ; }
}

class Entry {
    int i ;
    byte b ;
    
    Entry(int _i, byte _b ) { i = _i ; b = _b ; } ;
}