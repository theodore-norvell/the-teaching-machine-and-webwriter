/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import tm.plugins.Requirement;

public class B1Factory implements BFactoryInterface {

    private B1Factory() {} 
    
    static public B1Factory createInstance( String parameter ) {
        return new B1Factory() ;
    }
    
    public BPlugInInterface createPlugIn( ) {
        return new B1( ) ;
    }

    public Requirement[] getRequirements() {
        return new Requirement[] { new Requirement("tm.plugins.B1:D", DFactoryInterface.class, true, false)  } ;
    }
}