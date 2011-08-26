/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package higraphTests.model;

import higraph.model.abstractClasses.* ;
import higraph.model.interfaces.Payload;

public class TestEdgeLabel
implements Payload<TestEdgeLabel>
{
    
    String str ;
    
    public TestEdgeLabel(String str) {
       this.str = str ;
    }

    public TestEdgeLabel copy() {
        return this;
    }

}
