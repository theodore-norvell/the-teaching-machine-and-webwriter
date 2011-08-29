package visreed.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.tag.VisreedTag;
import visreed.view.SequenceNodeView;
import visreed.view.VisreedNodeView;

public class SequencePayload extends VisreedPayload {

	public SequencePayload() {
		super(VisreedTag.SEQUENCE);
	}

    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public SequencePayload copy() {
        return this;
    }
    

	@Override
	public String format(VisreedNode currentNode) {
		StringBuilder result = new StringBuilder();
		int numOfChildren = currentNode.getNumberOfChildren();
		
		for(int i = 0; i < numOfChildren; i++){
			if(i > 0){
				result.append(";");
			}
			
			VisreedNode currentChildN = currentNode.getChild(i);
			VisreedPayload currentChildPl = currentChildN.getPayload();
			
			result.append(currentChildPl.format(currentChildN));
		}
		return result.toString();
	}
    /* (non-Javadoc)
     * @see visreed.model.VisreedPayload#constructView(higraph.view.HigraphView, visreed.model.RegexNode, java.awt.Color, java.awt.Color, java.awt.Stroke, java.awt.geom.RectangularShape, boolean)
     */
    @Override
    public VisreedNodeView constructView(
        HigraphView<VisreedPayload, VisreedEdgeLabel, VisreedHigraph, VisreedWholeGraph, VisreedSubgraph, VisreedNode, VisreedEdge> sgv,
        VisreedNode node, 
        BTTimeManager timeman
    ) {
        return new SequenceNodeView(sgv, node, timeman);
    }
}
