package regex.model.payload;

import higraph.view.HigraphView;
import regex.model.RegexEdge;
import regex.model.RegexEdgeLabel;
import regex.model.RegexHigraph;
import regex.model.RegexNode;
import regex.model.RegexPayload;
import regex.model.RegexSubgraph;
import regex.model.RegexTag;
import regex.model.RegexWholeGraph;
import regex.view.KleeneStarNodeView;
import regex.view.RegexNodeView;
import tm.backtrack.BTTimeManager;
import tm.utilities.Assert;

public class KleeneStarPayload extends RegexPayload {

	public KleeneStarPayload() {
		super(RegexTag.KLEENE_STAR);
	}
	
    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public KleeneStarPayload copy() {
        return this;
    }

	@Override
	public String format(RegexNode currentNode) {
		StringBuilder result = new StringBuilder();
		int numOfChildren = currentNode.getNumberOfChildren();
		// KneeneStar -> Sequence
		Assert.check(numOfChildren == 1);
			
		RegexNode currentChildN = currentNode.getChild(0);
		RegexPayload currentChildPl = currentChildN.getPayload();
		
		result.append(currentChildPl.format(currentChildN));
		if(result.length() > 1){
            result.insert(0, "(");
    		result.append(")");
		}
		result.append("*");
	
		return result.toString();
	}
    /* (non-Javadoc)
     * @see regex.model.RegexPayload#constructView(higraph.view.HigraphView, regex.model.RegexNode, java.awt.Color, java.awt.Color, java.awt.Stroke, java.awt.geom.RectangularShape, boolean)
     */
    @Override
    public RegexNodeView constructView(
        HigraphView<RegexPayload, RegexEdgeLabel, RegexHigraph, RegexWholeGraph, RegexSubgraph, RegexNode, RegexEdge> sgv,
        RegexNode node, 
        BTTimeManager timeman
    ) {
        return new KleeneStarNodeView(sgv, node, timeman);
    }
}
