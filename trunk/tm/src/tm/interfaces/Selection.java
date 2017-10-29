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

/*
 * Created on 2009-06-03 by Theodore S. Norvell. 
 */
package tm.interfaces;

import java.io.Serializable ;

public class Selection implements SelectionInterface, Serializable {

    public enum TokenType{ AND, OR, NOT, TRUE, FALSE, TAG }
    
    private TokenType type ;
    
    private String tag ;
    
    private Selection[] children ;
    
    private Selection() {} ;
    
    public Selection( TokenType t ) { type = t ; }
    
    public Selection( String tag ) {
        type = TokenType.TAG ;
        this.tag = tag ; }
    
    public Selection( TokenType t, Selection child ) {
        type = t ;
        children = new Selection[] { child } ; 
    }
    
    public Selection( TokenType t, Selection left, Selection right ) {
        type = t ;
        children = new Selection[] { left, right } ; 
    }
    
    public boolean isValidForEmptyTagSet() {
        switch( type ) {
        case TRUE : return true ;
        case FALSE : return false ;
        case TAG: return false ;
        case NOT: return ! children[0].isValidForEmptyTagSet() ;
        case AND:
            if( children[0].isValidForEmptyTagSet() )
                return children[1].isValidForEmptyTagSet() ;
            else return false ;
        case OR:
            if( children[0].isValidForEmptyTagSet() ) return true ;
            else return children[1].isValidForEmptyTagSet() ;
        default: /*Unreachable*/; return false ;
        }
    }
    
    public boolean evaluate( TagSetInterface tagSet ) {
        switch( type ) {
        case TRUE : return true ;
        case FALSE : return false ;
        case TAG: return tagSet.contains( tag ) ;
        case NOT: return ! children[0].evaluate(tagSet) ;
        case AND:
            if( children[0].evaluate(tagSet) )
                return children[1].evaluate(tagSet) ;
            else return false ;
        case OR:
            if( children[0].evaluate(tagSet) ) return true ;
            else return children[1].evaluate(tagSet) ;
        default: /* Unreachable*/ return false ;
        }
    }
    
    /** Convert to a string in infix notation. */
    public String toString( ) {
        return toString( 0 ) ;
    }
    
    /** Convert to a string as an operand of an operator with precedence p.
    * OR - 1, And - 2, Not 3 */
    public String toString( int p ) {
        switch( type ) {
        case TRUE : return "true" ;
        case FALSE : return "false" ;
        case TAG: return tag ;
        case NOT: return "!" + children[0].toString(3) ;
        case AND:
            String x = children[0].toString( 2 ) + " & " + children[1].toString( 2 ) ;
            if( p > 2)
                return   "(" + x + ")";
            else return x ;
        case OR:
            String y = children[0].toString( 1 ) + " | " + children[1].toString( 1 ) ;
            if( p > 1)
                return "(" + y + ")" ;
            else return y ;
        default: /*Unreachable*/ return "" ;
        }
    }
}
