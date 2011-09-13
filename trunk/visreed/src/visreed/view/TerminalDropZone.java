package visreed.view;

import higraph.view.NodeView;
import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;

public class TerminalDropZone extends VisreedDropZone {

	public TerminalDropZone(
			NodeView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> nv,
			BTTimeManager timeMan) {
		super(nv, timeMan);
		// TODO Auto-generated constructor stub
	}

}
