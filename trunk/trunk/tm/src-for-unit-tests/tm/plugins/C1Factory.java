/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import tm.plugins.Requirement;

public class C1Factory implements CFactoryInterface {

    private C1Factory() {} 
    
    static public C1Factory createInstance( String parameter ) {
        return new C1Factory() ;
    }
    
    public CPlugInInterface createPlugIn(Data theData) {
        return new C1( theData ) ;
    }

    public Requirement[] getRequirements() {
        return new Requirement[0];
    }
}