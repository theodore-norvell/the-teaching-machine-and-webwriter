/*
 * Created on 10-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

import tm.plugins.Requirement;

public class C2Factory implements CFactoryInterface {

    private String axis;

    private C2Factory(String axis ) { this.axis = axis ; } 
    
    static public C2Factory createInstance( String axis ) {
        return new C2Factory(axis) ;
    }
    
    public CPlugInInterface createPlugIn(Data theData) {
        return new C2( axis, theData ) ;
    }

    public Requirement[] getRequirements() {
        return new Requirement[0];
    }
}

