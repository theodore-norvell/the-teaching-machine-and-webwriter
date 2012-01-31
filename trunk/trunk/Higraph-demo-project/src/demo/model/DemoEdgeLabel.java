/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package demo.model;

import higraph.model.interfaces.Payload;

public class DemoEdgeLabel
implements Payload<DemoEdgeLabel>
{
    
    String str ;
    
    public DemoEdgeLabel(String str) {
       this.str = str ;
    }

    public DemoEdgeLabel copy() {
        return this;
    }

}
