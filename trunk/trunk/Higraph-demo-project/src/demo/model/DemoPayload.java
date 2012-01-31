/*
 * Created on 2009-09-08 by Theodore S. Norvell. 
 */
package demo.model;

import higraph.model.taggedInterfaces.*;

public class DemoPayload
implements TaggedPayload<DemoTag,DemoPayload>
{
    String str ; 
    final DemoTag tag ;
    
    public DemoPayload( String str, DemoTag tag ) {
        this.str = str ;
        this.tag = tag ;
    }

    public DemoPayload copy() {
        return this;
    }

    @Override
    public DemoTag getTag() {
        return tag;
    }

}
