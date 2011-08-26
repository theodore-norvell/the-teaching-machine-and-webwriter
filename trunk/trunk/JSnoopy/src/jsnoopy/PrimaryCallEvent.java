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

import jsnoopy.parser.Parser;

public class PrimaryCallEvent extends CallEvent {

    protected String fullMethodName ;
    protected String[] fullArgs ;

    PrimaryCallEvent( String objectName, Method method, Object[] args )
    throws JSnoopyException {
        super( objectName, method, args ) ;
        this.fullMethodName = Format.methodToString(method) ;
        this.fullArgs = new String[ args.length ] ;
        for( int i=0 ; i<args.length ; ++i ) {
            this.fullArgs[i] = Format.objectToStringFull( args[i] ) ; }
    }

    public PrimaryCallEvent( String objectName,
                      String methodName,
                      String[] arguments,
                      String fullMethodName,
                      String[] fullArgs ) {
        super( objectName, methodName, arguments ) ;
        this.fullMethodName = fullMethodName ;
        this.fullArgs = fullArgs ;
    }

    void printSelf(PrintStream ps) throws java.io.IOException {
        ps.print( "0>>>") ;
        ps.print( Format.formatString( objectName ) ) ;
        ps.print( ".") ;
        ps.print( Format.formatString( methodName ) ) ;
        ps.print( "(" );
        for( int i=0, len=arguments.length ; i<len ; ++i ) {
            ps.println() ; ps.print("   ");
            ps.print( Format.formatString( arguments[i] ) ) ;
            if( i != len-1 ) ps.print( "," ); }
        ps.println( ")" );

        ps.print( Format.formatString( fullMethodName ) ) ;
        ps.print( "(" );
        for( int i=0, len=arguments.length ; i<len ; ++i ) {
            ps.println() ; ps.print("      ");
            ps.print( Format.formatString( fullArgs[i] ) ) ;
            if( i != len-1 ) ps.print( "," ); }
        ps.println( ")" ) ;
    }

    /** This is for debugging. Should be removed someday
     *
     */
    void dumpMethods( Object obj ) {
        Class klass = obj.getClass() ;
        Method[] meths = klass.getMethods() ;
        for( int i=0 ; i<meths.length ; ++i ) {
            Method m = meths[i] ;
            System.out.println( m );
            System.out.println( "  Name is "+m.getName() );
            Class[] paramTypes = m.getParameterTypes() ;
            for( int j=0 ; j<paramTypes.length ; ++j ) {
                Class paramType = paramTypes[j] ;
                System.out.println( "  Param type "+j+" is "+paramType.getName() );
            }
        }
    }
    public boolean isInjectable() { return true ; }

    public void inject( Manager mgr )
    throws JSnoopyException {
        try {
            Object obj = mgr.getProxy( objectName ) ;
            if( obj == null ) {
                throw new JSnoopyException(
                    "Proxy object missing! Either it has not been created or"
                    +" it has been garbage collected. Object name is: "
                    +objectName ) ; }
            //dumpMethods( obj ) ;
            Method m = Parser.parseMethod( fullMethodName, obj.getClass() ) ;
            Object[] args = Parser.parseArgs( fullArgs ) ;
            m.invoke( obj, args ) ; }
        catch( Throwable e ) {
            throw JSnoopyException.wrap( e ) ; }
    }
}