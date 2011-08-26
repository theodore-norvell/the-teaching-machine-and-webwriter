/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package higraphTests.model;

import higraph.model.abstractClasses.* ;
import higraph.model.interfaces.Payload;

public class TestPayload
implements Payload<TestPayload>
{
    String str ; 
    
    public TestPayload( String str ) {
        this.str = str ;
    }

    public TestPayload copy() {
        return this;
    }

}
