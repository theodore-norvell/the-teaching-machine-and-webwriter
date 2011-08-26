//     Copyright 1998--2010 Michael Bruce-Lockhart and Theodore S. Norvell
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License. 
// You may obtain a copy of the License at 
//
//     http://www.apache.org/licenses/LICENSE-2.0 
//
// Unless required by applicable law or agreed to in writing, 
// software distributed under the License is distributed on an 
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
// either express or implied. See the License for the specific language 
// governing permissions and limitations under the License.

/*
 * Created on 2009-09-07 by Theodore S. Norvell. 
 */
package higraph.model.abstractTaggedClasses;

import higraph.model.abstractClasses.*;
import higraph.model.interfaces.*;
import higraph.model.taggedInterfaces.*;

public abstract class AbstractTaggedEdge
    <   T extends Tag<T,NP>,
        NP extends TaggedPayload<T,NP>,
        EP extends Payload<EP>,
        HG extends AbstractTaggedHigraph<T,NP,EP,HG,WG,SG,N,E>,
        WG extends AbstractTaggedWholeGraph<T,NP,EP,HG,WG,SG,N,E>,
        SG extends AbstractTaggedSubgraph<T,NP,EP,HG,WG,SG,N,E>,
        N extends AbstractTaggedNode<T,NP,EP,HG,WG,SG,N,E>, 
        E extends AbstractTaggedEdge<T,NP,EP,HG,WG,SG,N,E>
    >
extends AbstractEdge<NP,EP,HG,WG,SG,N,E>
implements TaggedEdge<T,NP,EP,HG,WG,SG,N,E>
{

    protected AbstractTaggedEdge(N source, N target, EP label, WG higraph) {
        super(source, target, label, higraph);
    }
}
