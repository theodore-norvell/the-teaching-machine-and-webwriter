package tm.displayEngine.tmHigraph;

import tm.backtrack.BTTimeManager;
import higraph.view.HigraphView;
import higraph.view.NodeView;

public class NodeViewTM extends NodeView
<NodePayloadTM,
EdgePayloadTM,
HigraphTM,
WholeGraphTM,
SubgraphTM,
NodeTM,
EdgeTM >{
	public NodeViewTM(HigraphView <NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM> view, NodeTM node, BTTimeManager timeMan ){
		super(view, node, timeMan);
	}

}
