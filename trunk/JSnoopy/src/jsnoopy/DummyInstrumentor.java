package jsnoopy;

/* JSnoopy. (C) Theodore S. Norvell 2002.
*/

import java.lang.reflect.* ;

/** The extention of Intrumentor is used in production code. It does not create a proxy.
*/

class DummyInstrumentor extends Instrumentor {

    synchronized public Object instrument( String name,
                                          Object baseObject,
                                          Class[] interfacesToInstrument ) {
        return baseObject ;
    }

    synchronized public void appendComment( String str ) {
    }
}