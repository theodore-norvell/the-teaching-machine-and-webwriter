package jsnoopy;

/**
 * <p>Title: JSnoopy</p>
 * <p>Description: Regression testing based on event sequences.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Memorial University of Newfoundland</p>
 * @author Theodore S. Norvell
 * @version 1.0
 */

import java.io.PrintStream;
import java.util.Vector ;

public class Trace {

    private String comment = new String() ;

    private Vector eventVector ;

    public Trace() {
        eventVector = new Vector() ;
    }

    public void setComment( String str ) {
        comment = str ; }

    public void appendComment( String str ) {
        comment += str ; }

    public String getComment() {
        return comment ; }

    public void add( Event e ) {
        eventVector.addElement( e ); }

    public Event get( int i ) {
        return (Event) eventVector.elementAt( i ) ; }

    public int size() {
        return eventVector.size() ; }

    public void run( Manager mgr, int delay )
    throws JSnoopyException {
        for( int i=0, sz=size() ; i < sz ; ++i ) {
            Event e = get(i) ;
            if( e.isInjectable() ) {
                e.inject( mgr ) ;
                try {
                    Thread.sleep( delay ); }
                catch( InterruptedException ex ) {} } }
    }

    public void printSelf( PrintStream ps )
    throws java.io.IOException {
        ps.println(  Format.formatString("JSnoopy 0.0"));
        ps.println( Format.formatString(comment) );
        for( int i=0, sz=size() ; i < sz ; ++i ) {
            jsnoopy.Event ev = get(i) ;
            ev.printSelf( ps ) ; } }

    /** Compare two traces.
     *  Comments are ignored.
     * @return -1 if they are the same or the index where they first differ
     */
    public int compare( Trace other ) {
        int sz = Math.max( size(), other.size()) ;
        for( int i=0 ; i < sz ; ++i ) {
            Event e0 = get(i) ;
            Event e1 = other.get(i) ;
            if( ! e0.equals( e1 ) ) return i ; }
        if( size() != other.size() ) return sz ;
        return -1 ;
    }
}