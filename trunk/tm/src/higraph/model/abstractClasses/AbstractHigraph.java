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
 * Created on Oct 30, 2010 by Theodore S. Norvell. 
 */
package higraph.model.abstractClasses;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import higraph.model.interfaces.*;
import tm.backtrack.*;

public interface AbstractHigraph 
    <   NP extends Payload<NP>,
        EP extends Payload<EP>,
        HG extends AbstractHigraph<NP,EP,HG,WG,SG,N,E>,
        WG extends AbstractWholeGraph<NP,EP,HG,WG,SG,N,E>,
        SG extends AbstractSubgraph<NP,EP,HG,WG,SG,N,E>,
        N extends AbstractNode<NP,EP,HG,WG,SG,N,E>, 
        E extends AbstractEdge<NP,EP,HG,WG,SG,N,E>
    >
    extends Higraph<NP,EP,HG,WG,SG,N,E>
{
}
