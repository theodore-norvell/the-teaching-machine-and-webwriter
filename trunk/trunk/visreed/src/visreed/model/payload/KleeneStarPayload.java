package visreed.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import tm.utilities.Assert;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedPayload;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.tag.VisreedTag;
import visreed.view.KleeneStarNodeView;
import visreed.view.VisreedNodeView;

public class KleeneStarPayload extends VisreedPayload {

	public KleeneStarPayload() {
		super(VisreedTag.KLEENE_STAR);
	}
	
    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public KleeneStarPayload copy() {
        return this;
    }

	@Override
	public String format(VisreedNode currentNode) {
		StringBuilder result = new StringBuilder();
		int numOfChildren = currentNode.getNumberOfChildren();
		// KneeneStar -> Sequence
		Assert.check(numOfChildren == 1);
			
		VisreedNode currentChildN = currentNode.getChild(0);
		VisreedPayload currentChildPl = currentChildN.getPayload();
		
		result.append(currentChildPl.format(currentChildN));
		if(result.length() > 1){
            result.insert(0, "(");
    		result.append(")");
		}
		result.append("*");
	
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
        return new KleeneStarNodeView(sgv, node, timeman);
    }
}
