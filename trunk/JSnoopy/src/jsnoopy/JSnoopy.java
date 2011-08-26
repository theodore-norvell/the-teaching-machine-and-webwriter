package jsnoopy;

/* JSnoopy. (C) Theodore S. Norvell 2002.
*/

import java.lang.reflect.* ;

/**
 *  Produces instrumented objects and collects data from instrumented
 *  objects.
 */
abstract public class JSnoopy {
    static private Instrumentor theInstrumentor = null ;
    static private final int INIT=0 , ACTIVE=1, PASSIVE=2 ;
    static private int state = INIT ;

    static public void setActive( boolean active) {
        Assert.check( state==INIT
                    || state==ACTIVE && active
                    || state==PASSIVE && !active,
                       "Bad call to setActive." ) ;
        if( state==INIT && active ) {
            // The Manager object is
            // constructed through reflection so that it
            // need not pollute production code.
            Class managerClass = null ;
            boolean ok ;
            try {
                managerClass = Class.forName("jsnoopy.Manager") ;
                Class[] noClasses = new Class[0] ;
                Method getManager = managerClass.getMethod( "getManager", noClasses ) ;
                Object[] noArgs = new Object[0] ;
                theInstrumentor = (Instrumentor) getManager.invoke( null,  noArgs) ;
                ok = true ;}
            catch( InvocationTargetException e ) {
                Assert.check( false, "Could not build Manager. Exception is "+e.getTargetException() ) ;
                ok = false ; }
            catch( Exception e ) {
                Assert.check( false, "Could not build Manager. Exception is "+e ) ;
                ok = false ; }

            /** The GUI is also constructed through reflection. */
            if( ok ) {
                try {
                    // Get the constructor for the main window
                        Class mainWindowClass = Class.forName("jsnoopy.swingui.MainWindow") ;
                        Class[] paramTypes = new Class[]{ managerClass } ;
                        Constructor constructor = mainWindowClass.getConstructor( paramTypes ) ;

                    // Construct a main window
                    Object[] initArgs = new Object[]{ theInstrumentor } ;
                    constructor.newInstance( initArgs ) ; }
                catch( InvocationTargetException e ) {
                    Assert.check( false, "Could not build Manager. Exception is "+e.getTargetException() ) ; }
                catch( Exception e ) {
                    Assert.check( false, "Could not build GUI. Exception is "+e ) ; }
                state = ACTIVE ; } }
        else if( state == INIT ) { // !active
            theInstrumentor = new DummyInstrumentor() ;
            state = PASSIVE ; }
    }

    /** Gets the sole Instrumentor object.
     *  <P> Precondition: setActive must already have been called. </P>
     *  <P> If setActive(true) was called, then the instrumentor will
     *      be one that builds proxy objects capable of recording
     *      calls and return. </P>
     *  <P> If setActive(false) was called, then the instrumentor will
     *      be one that simply returns the object that it is passed. </P>
     **/
    static public Instrumentor getInstrumentor() {
        Assert.check( state==ACTIVE || state==PASSIVE,
                       "setActive must be called before getInstrumentor" ) ;
        return theInstrumentor ;
    }

    /** Add to the comment field of the current trace.
     *
     */
    static public void appendComment( String str ) {
        Assert.check( state!=INIT, "setActive must be called before addComment" ) ;
        theInstrumentor.appendComment( str ) ; }

    /** Add a line to the comment field of the current trace.
     *
     */
    static public void appendCommentLn( String str ) {
        appendComment( str+"\n" ) ; }
}