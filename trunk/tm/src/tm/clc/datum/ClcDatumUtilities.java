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

package tm.clc.datum;

import tm.virtualMachine.Console;

public abstract class ClcDatumUtilities
{    
    static public boolean skipWhiteSpace(Console console) {
        while( true ) {
            char ch = console.peekChar(0) ;
            if( ch == '\uffff' ) return false ;
            if( ! isWhiteSpace( ch ) ) break ;
            
            console.consumeChars(1) ; }
        return true ; }
    
    static public boolean isWhiteSpace( char ch ) {
        switch( ch ) {
            case '\n' :
            case ' '  :
            case '\t' :
            case '\r' :
            case '\f' :
                return true ;
            default :
                return false ; } }
}