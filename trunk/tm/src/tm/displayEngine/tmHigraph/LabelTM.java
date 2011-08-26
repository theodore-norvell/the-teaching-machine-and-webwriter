package tm.displayEngine.tmHigraph;

import tm.backtrack.BTTimeManager;
import higraph.view.ComponentView;
import higraph.view.Label;

public class LabelTM extends Label<
NodePayloadTM,
EdgePayloadTM,
HigraphTM,
WholeGraphTM,
SubgraphTM,
NodeTM,
EdgeTM > {
	public LabelTM(String id, int position, ComponentView <NodePayloadTM,EdgePayloadTM,HigraphTM,WholeGraphTM,SubgraphTM,NodeTM,EdgeTM> view, BTTimeManager timeMan ){
		super(id, position, view, timeMan);
	}
}
