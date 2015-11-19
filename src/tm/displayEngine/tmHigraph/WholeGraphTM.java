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

import java.util.List;

import higraph.model.abstractClasses.AbstractWholeGraph;
import tm.backtrack.BTTimeManager;
import tm.clc.datum.AbstractPointerDatum;
import tm.clc.datum.AbstractRefDatum;
import tm.interfaces.Datum;
import tm.utilities.Assert;

public class WholeGraphTM extends AbstractWholeGraph
    <  NodePayloadTM,
         EdgePayloadTM,
         HigraphTM,
         WholeGraphTM,
         SubgraphTM,
         NodeTM,
         EdgeTM
    >
    implements HigraphTM
 {
    
    public WholeGraphTM( BTTimeManager timeMan ) {
        super( timeMan ) ;
    }

	@Override
	protected EdgeTM constructEdge(NodeTM source, NodeTM target, EdgePayloadTM label) {
		return new EdgeTM(source, target, label, this);
	}

	@Override
	protected NodeTM constructNode(WholeGraphTM higraph, NodePayloadTM payload) {
		return new NodeTM(higraph, payload);
	}

	@Override
	protected NodeTM constructNode(NodeTM original, NodeTM parent) {
		return new NodeTM(original, parent);
	}

	@Override
	protected SubgraphTM constructSubGraph() {
		return new SubgraphTM(this);
	}

	@Override
	public WholeGraphTM getWholeGraph() {
		return this;
	}
	
	public NodeTM getNode(Datum d, boolean deref){	
		if (deref)
			if (d instanceof AbstractPointerDatum)
				d =  ((AbstractPointerDatum)d).deref();
			else
				Assert.scriptingError("argument must be a reference or pointer");
		List<NodeTM> nodeList = getNodes();
		for(int i = 0; i < nodeList.size(); i++){
			NodeTM node = nodeList.get(i);
			if (node.getPayload().getDatum().equals(d))
				return node;
		}
		return null;
	}
	
	public EdgeTM getEdge(NodeTM source, NodeTM target){
		List<EdgeTM> edgeList = getEdges();
		for(int i = 0; i < edgeList.size(); i++){
			EdgeTM edge = edgeList.get(i);
			if (edge.getSource().equals(source) && edge.getTarget().equals(target))
				return edge;
		}
		return null;		
	}


}
