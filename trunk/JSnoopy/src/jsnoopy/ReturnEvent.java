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

public class ReturnEvent extends Event {

    /** The return value as a short formated string */
    protected String rtnString ;

    ReturnEvent( String objectName, Method method, Object rtn ) {
        super( objectName, method ) ;
        this.rtnString = Format.objectToStringShort( rtn ) ;
    }

    public ReturnEvent( String objectName, String methodName, String rtnString ) {
        super( objectName, methodName ) ;
        this.rtnString = rtnString ;
    }

    void printSelf(PrintStream ps) throws java.io.IOException {
        ps.print( "0<<return ") ;
        ps.print( Format.formatString( objectName ) );
        ps.print( ".") ;
        ps.print( Format.formatString( methodName ) );
        ps.print( " == " ) ;
        ps.println( Format.formatString( rtnString ) ) ;
    }

    public boolean equals( Object obj ) {
        if( obj instanceof ReturnEvent ) {
            ReturnEvent other = (ReturnEvent) obj ;
            return objectName.equals(other.objectName)
                && methodName.equals(other.methodName)
                && rtnString.equals( other.rtnString ) ; }
        else {
            return false ; }
    }

    /**
     * Used for disply to user.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer() ;
        buf.append( "<<return " ) ;
        buf.append( objectName ) ;
        buf.append( "." ) ;
        buf.append( methodName ) ;
        buf.append( "==" ) ;
        buf.append( rtnString ) ;
        return buf.toString() ;
    }
}