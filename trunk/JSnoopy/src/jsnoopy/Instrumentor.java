package jsnoopy;

/**
 * <p>Title: JSnoopy</p>
 * <p>Description: Regression testing based on event sequences.</p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Memorial University of Newfoundland</p>
 * @author Theodore S. Norvell
 * @version 1.0
 */

import java.lang.reflect.* ;

abstract public class Instrumentor {

    /** Takes an name, an object, and a Class object.
     *  <P> The Class object must represent an interface and the
     *  object must implement that interface. The name is used to identify
     *  the object and should be unique (no other instrumented objects should
     *  have the same name).
     *  
     *  @param name -- the name of the object (should be unique)
     *  @param baseObject -- the object to be instrumented
     *  @param interfaceToInstrument -- an interface that the proxy should implement.
     *
     *  @return If tracing is on, an instrumented version of the object, that implements the
     *  given interface. But if tracing is not on, simply returns the base object.
     */
    synchronized public <T> T instrument( String name,
                                          T baseObject,
                                          Class<T> interfaceToInstrument ) {
        return instrument( name, baseObject, new Class[] { interfaceToInstrument }, interfaceToInstrument ) ;
    }

    /** Takes an name, an object, and an array of Class objects.
     *  <P> The Class objects must represent interfaces and the
     *  object must implement those interfaces. The name is used to identify
     *  the object and should be unique (no other instrumented objects should
     *  have the same name).
     *
     *  <P> Overrides of this method should be synchronized.
     *  @param name -- the name of the object (should be unique)
     *  @param baseObject -- the object to be instrumented
     *  @param interfacesToInstrument -- a list of interfaces that must be implemented by the proxy. The base object should implement all these interfaces.
     *  @param resultType -- one of the interfaces in interfacesToInstrument. This defines the method's result type.
     *
     *  @return If tracing is on, an instrumented version of the object, that implements the
     *  given interfaces. But if tracing is not on, simply returns the base object.
     */
    abstract public <T> T instrument( String name,
                                          T baseObject,
                                          Class<?>[] interfacesToInstrument,
                                          Class<T> resultType ) ;

    abstract public void appendComment( String str ) ;
}