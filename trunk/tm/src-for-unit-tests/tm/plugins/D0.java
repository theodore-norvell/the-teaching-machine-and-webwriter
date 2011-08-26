/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import tm.plugins.Requirement;

/** This is an example of a singleton plug-in that is its own factory.
 * I only recommend this for singletons. */
public class D0 implements DFactoryInterface, DPlugInInterface {

    private static D0 theSingleton;

    private D0() {} 
    
    static public DFactoryInterface createInstance( String parameter ) {
        if( theSingleton == null ) theSingleton = new D0() ;
        return theSingleton ;
    }
    
    public DPlugInInterface createPlugIn() {
        if( theSingleton == null ) theSingleton = new D0() ;
        return theSingleton ;
    }

    public Requirement[] getRequirements() {
        return new Requirement[0];
    }
}
