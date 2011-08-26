/*
 * Created on 28-Jul-2006 by Theodore S. Norvell. 
 */
package tm.plugins;

public class A0Factory implements AFactoryInterface {

    private Requirement[] requirements = new Requirement[] {
            new Requirement("tm.plugins.A:B", BFactoryInterface.class, true, false ),
            new Requirement("tm.plugins.A:C", CFactoryInterface.class, false, true ) } ;
    
    public APlugInInterface createPlugIn() {
        return new A0();
    }

    public Requirement[] getRequirements() {
        return requirements ;
    }
}
