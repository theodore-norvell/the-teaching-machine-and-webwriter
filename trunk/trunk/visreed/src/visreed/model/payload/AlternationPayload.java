package visreed.model.payload;

import higraph.view.HigraphView;
import tm.backtrack.BTTimeManager;
import visreed.extension.javaCC.parser.JavaCCBuilder;
import visreed.model.VisreedEdge;
import visreed.model.VisreedEdgeLabel;
import visreed.model.VisreedHigraph;
import visreed.model.VisreedNode;
import visreed.model.VisreedSubgraph;
import visreed.model.VisreedWholeGraph;
import visreed.model.tag.VisreedTag;
import visreed.view.AlternationNodeView;
import visreed.view.VisreedNodeView;

public class AlternationPayload extends VisreedPayload {

	public AlternationPayload() {
		super(VisreedTag.ALTERNATION);
	}

    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public AlternationPayload copy() {
        return this;
    }

	@Override
	public String format(VisreedNode currentNode) {
		StringBuilder result = new StringBuilder();
		int numOfChildren = currentNode.getNumberOfChildren();
		
		for(int i = 0; i < numOfChildren; i++){
			if(i > 0){
				result.append("|");
			}
			
			VisreedNode currentChildN = currentNode.getChild(i);
			VisreedPayload currentChildPl = currentChildN.getPayload();
			
			// Alternation -> Sequence{2, }
			result.append(currentChildPl.format(currentChildN));
		}
		
		if(result.length() > 0){
		    result.insert(0, "(");
		    result.append(")");
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
        return new AlternationNodeView(sgv, node, timeman);
    }

	/* (non-Javadoc)
	 * @see visreed.model.payload.VisreedPayload#dump(java.lang.StringBuffer, int)
	 */
	@Override
	public StringBuffer dump(StringBuffer sb, int indentLevel) {
		sb = super.dump(sb, indentLevel);
		int numOfChildren = this.getNode().getNumberOfChildren();
		boolean bracketNeeded = (numOfChildren > 1);

		if(bracketNeeded){
			sb.append("(\n");
		}
		
		for(int i = 0; i < numOfChildren; i++){
			JavaCCBuilder.dumpPrefix(sb, indentLevel);
			if (i == 0){
				sb.append("  ");
			} else {
				sb.append("| ");
			}
			this.getNode().getChild(i).getPayload().dump(sb, 0);
			sb.append("\n");
		}
		
		if(bracketNeeded){
			JavaCCBuilder.dumpPrefix(sb, indentLevel);
			sb.append(")");
		}
		return sb;
	}
}
