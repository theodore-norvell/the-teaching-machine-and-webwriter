package jsnoopy;

/* JSnoopy. (C) Theodore S. Norvell 2002.
*/

import java.lang.ref.WeakReference ;
import java.lang.reflect.* ;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;


/** Creates a proxy objects.
*/

public class Manager extends Instrumentor {

    /** proxyHash maps names (Strings) to weak pointers to proxy objects */
    private HashMap proxyHash ;
    /** recording indicates whether a record should be made of the events */
    private boolean recording ;
    /** trace is a sequence of Events
        Invariant (record != null) == recording */
    private Trace trace ;
    /** The number of active calls */
    private int recordingDepth ;

    private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this) ;;

    static private Manager theSingleton ;


    /** Gets the sole Manager object */
    static public Manager getManager() {
        if( theSingleton == null ) {
            theSingleton = new Manager() ; }
        return theSingleton ;
    }

    private Manager() {
        proxyHash = new HashMap() ;
        recording = false ;
    }

    /** Add a listener for errors that happen during event recording
     *  <P> As the manager records events, exceptions may occur.  We don't
     *  want these to be propagated back to the application, so instead
     *  we catch them and send them to a listener, typically the GUI.
     *  Recording without first setting a listener is a bad idea as
     *  any exceptions thrown by JSnoopy during recording will be masked.
     */
    public void addErrorListener( PropertyChangeListener listener ) {
        propertyChangeSupport.addPropertyChangeListener( "latestError", listener ); }

    private void setLatestError( Throwable latestError ) {

        PropertyChangeEvent evt = new PropertyChangeEvent(this, "latestError", null, latestError ) ;
        propertyChangeSupport.firePropertyChange( evt ) ;

    }

    public synchronized void startRecording() {
        Assert.check( ! recording ) ;
        recording = true ;
        recordingDepth = 0 ;
        trace = new Trace() ; }

    public synchronized Trace stopRecording() {
        Assert.check( recording ) ;
        recording = false ;
        Trace temp = trace ;
        trace = null ;
        return temp ; }

    /** Get the proxy object associated with a name
     *  @returns null if the name has not been associated with an
     *  object, null if the proxy object is no more and the proxy otherwise
     *
     */
    synchronized Object getProxy( String name ) {
        WeakReference wrp = (WeakReference) proxyHash.get( name ) ;
        if( wrp == null ) {
            return null ; }
        else {
            return wrp.get() ; } }

    synchronized public Object instrument( String name,
                                          Object baseObject,
                                          Class[] interfacesToInstrument ) {

        InvocationHandler handler = new RecordingInvocationHandler( name, baseObject ) ;
        Object proxy = Proxy.newProxyInstance(interfacesToInstrument[0].getClassLoader(),
                                              interfacesToInstrument,
                                              handler);
        // Save a weak pointer to the proxy for replay.
        // QUESTION: Should we worry about the case where the name is already in use?
            WeakReference wrp = new WeakReference( proxy ) ;
            proxyHash.put( name, wrp ) ;
        // TODO Fire event to alert listeners.
        return proxy ;
    }

    synchronized public void appendComment( String str ) {
        if( recording ) {
            trace.appendComment( str ) ; }
    }

    synchronized private void callEvent( String objectName, Method method, Object[] args ) {
        try {
            if( recording ) {
                recordingDepth += 1 ;
                if( args==null ) args = new Object[0] ;
                Assert.check( recordingDepth >= 1 ) ;
                if( recordingDepth == 1 ) {
                    trace.add( new PrimaryCallEvent( objectName, method, args ) ) ; }
                else {
                    trace.add( new CallEvent( objectName, method, args ) ) ; } } }
        catch( Throwable e ) {
            setLatestError( e ) ;
        }
    }

    synchronized private void returnEvent( String objectName, Method method, Object rtn ) {
        try {
            if( recording ) {
                recordingDepth -= 1 ;
                Assert.check( recordingDepth >= 0 ) ;
                trace.add( new ReturnEvent( objectName, method, rtn ) ) ; } }
        catch( Throwable e ) {
            setLatestError( e ) ;
        }
    }

    synchronized private void exceptionEvent( String objectName, Method method, Throwable exception ) {
        try {
            if( recording ) {
                recordingDepth -= 1 ;
                Assert.check( recordingDepth >= 0 ) ;
                trace.add( new ExceptionEvent( objectName, method, exception ) ) ; } }
        catch( Throwable e ) {
            setLatestError( e ) ;
        }
    }

    private class RecordingInvocationHandler implements InvocationHandler {
        private Object baseObject ;
        private String objectName ;

        public RecordingInvocationHandler( String objectName, Object baseObject ) {
            this.objectName = objectName ;
            this.baseObject = baseObject ;
        }

        public Object invoke(Object proxy,
                             Method method,
                             Object[] args)
                  throws Throwable {
            callEvent( objectName, method, args ) ;
            Object rtn = null ;
            try {
                rtn =  method.invoke(this.baseObject, args) ;
                returnEvent( objectName, method, rtn ) ; }
            catch( Throwable e ) {
                exceptionEvent( objectName, method, e ) ;
                throw e ; }
            return rtn ;
        }
    }
}