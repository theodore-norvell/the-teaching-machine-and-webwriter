package jsnoopy;

/* JSnoopy. (C) Theodore S. Norvell 2002.
*/

import java.lang.reflect.* ;

/** The extention of Intrumentor is used in production code. It does not create a proxy.
*/

class DummyInstrumentor extends Instrumentor {

    @Override synchronized public <T> T instrument( String name,
            										T baseObject,
            										Class<?>[] interfacesToInstrument,
            										Class<T> resultType ) {
        return baseObject ;
    }

    @Override synchronized public void appendComment( String str ) {
    }
}