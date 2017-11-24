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

package tm.interfaces;

import java.util.Set;
import java.util.TreeSet;

/** A tag set is an immutable set of tags.
 *  We use tag sets to decide which portions of the code
 *  to display.
 *  
 *  <p>We can query a tagSet (with {@link TagSet#selectionIsValid(String)}) to see if
 *  a boolean expression is valid or not.
 * @author theo
 */
public class TagSet implements TagSetInterface {
    private Set<String> rep = new TreeSet<String> () ;

    private TagSet( ) {
    }

    public TagSet( String str ) {
        str = str.toLowerCase() ;
        rep.add( str ) ;
    }

    public static TagSet union( TagSet a, TagSet b ) {
        if( a.isEmpty() || a==b ) return b ;
        if( b.isEmpty() ) return a ;
        TagSet c = new TagSet(  ) ;
        for( String s : a.rep ) c.rep.add( s ) ;
        for( String s : b.rep ) c.rep.add( s ) ;
        return c ;
    }

    public static TagSet subtract( TagSet a, TagSet b ) {
        if( a.isEmpty() || a==b ) return EMPTY ;
        if( b.isEmpty() ) return a ;
        TagSet c = new TagSet(  ) ;
        for( String s : a.rep ) c.rep.add( s ) ;
        for( String s : b.rep ) c.rep.remove( s ) ;
        return c ;
    }

    private static TagSet EMPTY = new TagSet() ;
    
    public static TagSet getEmpty() { return EMPTY ; }

    public boolean contains( String str ) {
        str = str.toLowerCase() ;
        return rep.contains( str ) ; }

    public int size() {
        return rep.size() ; }

    public String toString() {
        if( rep.size()==0 ) return "EMPTY" ;
        StringBuffer buf = new StringBuffer("{") ;
        boolean comma = false ;
        for( String s : rep ) {
            if( comma ) buf.append(",") ;
            buf.append("'"+s+"'") ;
            comma = true ; }
        buf.append("}") ;
        return buf.toString() ; }

    public boolean selectionIsValid(SelectionInterface selection) {
        return selection.evaluate( this ) ;
    }

    @Override
    public boolean isEmpty() {
        return rep.isEmpty() ;
    }
}