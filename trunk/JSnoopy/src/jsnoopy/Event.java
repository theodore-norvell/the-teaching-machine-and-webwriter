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

public abstract class Event {

    /** The name of the object that got the call, as
     *  registered with the manager. */
    protected String objectName ;
    /** The method name (with no argument types) */
    protected String methodName ;

    Event( String objectName, Method method ) {
        this.objectName = objectName ;
        this.methodName = method.getName() ; }

    Event( String objectName, String methodName ) {
        this.objectName = objectName ;
        this.methodName = methodName ; }

    abstract void printSelf( PrintStream ps ) throws IOException ;

    public boolean isInjectable() { return false ; }

    public void inject(Manager mgr)
    throws JSnoopyException {}
}