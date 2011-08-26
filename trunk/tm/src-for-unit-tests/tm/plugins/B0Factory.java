/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import tm.plugins.Requirement;

public class B0Factory implements BFactoryInterface {

    private B0Factory() {} 
    
    static public B0Factory createInstance( String parameter ) {
        return new B0Factory() ;
    }
    
    public BPlugInInterface createPlugIn( ) {
        return new B0( ) ;
    }

    public Requirement[] getRequirements() {
        return new Requirement[0];
    }
}
