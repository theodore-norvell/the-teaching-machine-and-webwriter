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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector ;

import tm.utilities.Assert;

/** A Vector-like class that can be backtracked
 *@author theo
 *
 */
public class BTVector<E> implements Iterable<E> {
    private Vector<BTVar<E>> v ;
    private BTVar<Integer> sizeVar ;
    private BTTimeManager timeMan ;

    public BTVector(BTTimeManager tm) {
        timeMan = tm ;
        v = new Vector<BTVar<E>>() ;
        sizeVar = new BTVar<Integer>(timeMan) ;
        sizeVar.set(0) ;
    }

    public int size() {
        return sizeVar.get().intValue() ;
    }
    
    public E get( int locn ) {
        Assert.check( 0 <= locn && locn < size() ) ;
        return v.elementAt( locn ).get() ;
    }

    @Deprecated public E elementAt( int locn ) {
        Assert.check( 0 <= locn && locn < size() ) ;
        return v.elementAt( locn ).get() ;
    }
    
    public void set( int locn, E value ) {
        Assert.check( 0 <= locn && locn < size() ) ;
        v.elementAt( locn ).set( value ) ;
    }

    @Deprecated public void setElementAt( E value, int locn ) {
        Assert.check( 0 <= locn && locn < size() ) ;
        v.elementAt( locn ).set( value ) ;
    }

    public void insertElementAt(E o, int locn) {
        int size = size() ;
        Assert.check( 0 <= locn && locn <= size ) ;
        if( v.size() == size ) v.add( new BTVar<E>( timeMan ) ) ;
        Assert.check( size < v.size() ) ;
        for( int i = size-1 ; i >= locn ; --i ) {
            v.get(i+1).set( v.get(i).get() ) ; }
        v.get(locn).set( o ) ;
        sizeVar.set( new Integer( size+1 ) ) ;
    }

    public void removeElementAt( int locn ) {
        int size = size() ;
        Assert.check( 0 <= locn && locn < size ) ;
        for( int i = locn ; i < size-1 ; ++i ) {
            v.get(i).set( v.get(i+1).get() ) ; }
        v.get(size-1).set( null ) ;
        sizeVar.set( new Integer( size-1 ) ) ;
    }
    
    public void add( E o ) {
        insertElementAt( o, size() ) ;
    }
    
    @Deprecated public void addElement( E o ) {
        insertElementAt( o, size() ) ;
    }
    
    /** Remove the first occurrence of o if there is one. Otherwise do nothing. */
    public void remove( E o ) {
        int size = size() ;
        int i ;
        for( i = 0 ; i < size ; ++i ) if( o == v.elementAt( i ).get() ) break ;
        if( i < size ) removeElementAt( i ) ;
    }
    
    public boolean contains( E o ) {
        return lastIndexOf( o ) >= 0 ;
    }
    
    public int lastIndexOf( E o ) {
        int size = size() ;
        int i = size - 1 ;
        for( ; i >= 0 ; --i ) 
            if( (o==null ? get(i)==null : o.equals(get(i))) ) return i ;
        return -1 ;
    }

    /** Return an iterator for a *copy* of this vector.
     * Unlike most implementations of the "iterator" method, this one
     * first copies the current values of the vector to a List and
     * then returns an iterator for that copy. 
     * <p> This means, for example that you can not use the "remove"
     * method of the iterator to remove something from the original
     * vector. On the other hand, it also means that here will be
     * no ConcurrentModificationExceptions, if you try to modify the
     * vector while iterating over it.
     * @see java.util.List
     */
    @Override
    public Iterator<E> iterator() {
     // To avoid concurrent modification problems, we first make
     // a fresh copy of the vector.
        return toList().iterator() ;
    }
    
    public List<E> toList() {
        int size = size() ;
        ArrayList<E> result = new ArrayList<E>(size) ;
        for( int i = 0 ; i < size ; ++i ) {
            result.add( v.elementAt( i ).get() ) ; }
        return result ;
    }
}