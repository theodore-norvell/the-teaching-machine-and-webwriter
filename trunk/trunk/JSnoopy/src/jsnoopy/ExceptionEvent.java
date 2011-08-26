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

public class ExceptionEvent extends Event {

    /** The return value as a short formated string */
    protected String exceptionString ;

    ExceptionEvent( String objectName, Method method, Throwable exception ) {
        super( objectName, method ) ;
        this.exceptionString = Format.objectToStringShort( exception ) ;
    }

    public ExceptionEvent( String objectName,
                            String methodName,
                            String exceptionString ) {
        super( objectName, methodName ) ;
        this.exceptionString = exceptionString ;
    }

    void printSelf(PrintStream ps) throws java.io.IOException {
        ps.print( "0<<throw ") ;
        ps.print( Format.formatString( objectName ) );
        ps.print( ".") ;
        ps.print( Format.formatString( methodName ) );
        ps.print( " == " ) ;
        ps.println( Format.formatString( exceptionString ) ) ;
    }

    public boolean equals( Object obj ) {
        if( obj instanceof ExceptionEvent ) {
            ExceptionEvent other = (ExceptionEvent) obj ;
            return objectName.equals(other.objectName)
                && methodName.equals(other.methodName)
                && exceptionString.equals(other.exceptionString) ; }
        else {
            return false ; }
    }
    /**
     * Used for disply to user.
     */
    public String toString() {
        StringBuffer buf = new StringBuffer() ;
        buf.append( "<<throw " ) ;
        buf.append( objectName ) ;
        buf.append( "." ) ;
        buf.append( methodName ) ;
        buf.append( "==" ) ;
        buf.append( exceptionString ) ;
        return buf.toString() ;
    }
}