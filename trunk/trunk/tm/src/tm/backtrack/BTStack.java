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
// BTStack
//
//
public class BTStack<E> {
    private Vector<BTValManager<E>> s ;
    private BTTimeManager timeMan ;
    private BTVar<Integer> sizeVar ;

    public BTStack(BTTimeManager tm) {
        timeMan = tm ;
        s = new Vector<BTValManager<E>>() ;
        sizeVar = new BTVar<Integer>( timeMan ) ;
        sizeVar.set( new Integer(0) ) ;
    }

    // Invariant: size() <= s.size()

    public void push(E o) {
        int stackSize = size() ;
        BTValManager<E> valMan ;
        if( s.size() == stackSize ) {
            valMan = new BTValManager<E>() ;
            s.addElement( valMan ) ; }
        else {
            valMan = s.elementAt( stackSize ) ; }
        valMan.set( timeMan.getCurrentTime(), o ) ;
        sizeVar.set( new Integer(stackSize + 1 ) ) ;
    }

    public void pop() {Assert.check( size() > 0 ) ;
                       sizeVar.set( new Integer(size() - 1 ) ) ; }

    public int size() {
        Integer I = (Integer) sizeVar.get() ;
        return I.intValue() ; }

    public E top() {return get(size() - 1) ; }

    public E get(int i) {
        Assert.check( 0 <= i && i < size() ) ;
        BTValManager<E> valMan = s.elementAt( i ) ;
        return valMan.get( timeMan.getCurrentTime() ) ;
    }

    public void trimTo( int newSize ) {
        int amountToTrim = size() - newSize ;
        Assert.check( amountToTrim >= 0 ) ;
        for( ; amountToTrim > 0 ; --amountToTrim ) pop() ;
    }
}

