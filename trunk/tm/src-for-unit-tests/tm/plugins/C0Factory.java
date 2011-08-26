/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import tm.plugins.Requirement;

public class C0Factory implements CFactoryInterface {

    private C0Factory() {} 
    
    static public C0Factory createInstance( String parameter ) {
        return new C0Factory() ;
    }
    
    public CPlugInInterface createPlugIn(Data theData) {
        return new C0( theData ) ;
    }

    public Requirement[] getRequirements() {
        return new Requirement[0];
    }
}
