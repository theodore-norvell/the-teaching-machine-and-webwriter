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

package tm.displayEngine.tmHigraph;

import higraph.model.abstractClasses.AbstractNode;

public class NodeTM extends AbstractNode
    <  NodePayloadTM,
         EdgePayloadTM,
         HigraphTM,
         WholeGraphTM,
         SubgraphTM,
         NodeTM,
         EdgeTM
    >
{
	
    protected NodeTM(WholeGraphTM higraph, NodePayloadTM payload) {
        super(higraph, payload);
    }
    
    protected NodeTM(NodeTM original, NodeTM parent) {
        super( original, parent ) ;
    }
	
    @Override
    protected NodeTM getThis() { return this ; }
    
    public String toString(){
    	return "node(" + payload.get().getName() + ")";
    }

}
