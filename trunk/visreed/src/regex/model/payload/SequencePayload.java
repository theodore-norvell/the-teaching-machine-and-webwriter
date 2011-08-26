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
import regex.view.RegexNodeView;
import regex.view.SequenceNodeView;
import tm.backtrack.BTTimeManager;

public class SequencePayload extends RegexPayload {

	public SequencePayload() {
		super(RegexTag.SEQUENCE);
	}

    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public SequencePayload copy() {
        return this;
    }
    

	@Override
	public String format(RegexNode currentNode) {
		StringBuilder result = new StringBuilder();
		int numOfChildren = currentNode.getNumberOfChildren();
		
		for(int i = 0; i < numOfChildren; i++){
			if(i > 0){
				result.append(";");
			}
			
			RegexNode currentChildN = currentNode.getChild(i);
			RegexPayload currentChildPl = currentChildN.getPayload();
			
			result.append(currentChildPl.format(currentChildN));
		}
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
        return new SequenceNodeView(sgv, node, timeman);
    }
}
