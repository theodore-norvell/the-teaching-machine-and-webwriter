package jsnoopy;

/**
 * Title:        JSnoopy
 * Description:  Regression testing based on event sequences.
 * Copyright:    Copyright (c) 2002
 * Company:      Memorial University of Newfoundland
 * @author Theodore S. Norvell
 * @version 1.0
 */

import java.io.* ;
import java.lang.reflect.* ;

public class CallEvent extends Event {
    /** The arguments as formated strings */
    protected String[] arguments ;

    CallEvent( String objectName, Method method, Object[] args ) {
        super( objectName, method ) ;
        final int argLen = args.length ;
        this.arguments = new String[ args.length ] ;
        for( int i=0 ; i<argLen ; ++i ) {
            this.arguments[i] = Format.objectToStringShort( args[i] ) ; }
    }

    public CallEvent( String objectName, String methodName, String[] args ) {
        super( objectName, methodName ) ;
        this.arguments = args ;
    }

    void printSelf(PrintStream ps) throws java.io.IOException {
        ps.print( "0>>") ;
        ps.print( Format.formatString( objectName ) );
        ps.print( ".") ;
        ps.print( Format.formatString( methodName ) );
        ps.print( "(" );
        for( int i=0, len=arguments.length ; i<len ; ++i ) {
            ps.println() ; ps.print("   ") ;
            ps.print( Format.formatString( arguments[i] ) ) ;
            if( i != len-1 ) ps.print( "," ); }
        ps.println( ")" ) ;
    }

    public boolean equals( Object obj ) {
        if( obj instanceof CallEvent ) {
            CallEvent other = (CallEvent) obj ;
            if( objectName.equals(other.objectName)
                && methodName.equals(other.methodName)
                && arguments.length == other.arguments.length ) {
                for(int i=0, len=arguments.length ; i<len ; ++i ) {
                    if( ! arguments[i].equals( other.arguments[i] ) ) {
                        return false ; } }
                return true ; }
            else {
                return false ; } }
        else {
            return false ; }
    }

    /**
     * Used for disply to user.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer() ;
        buf.append( ">>" ) ;
        buf.append( objectName ) ;
        buf.append( "." ) ;
        buf.append( methodName ) ;
        buf.append( "(" ) ;
        for( int i=0, len=arguments.length ; i<len ; ++i ) {
            buf.append(arguments[i] ) ;
            if( i != len-1 ) buf.append( "," ); }
        buf.append( ")" ) ;
        return buf.toString() ;
    }
}