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