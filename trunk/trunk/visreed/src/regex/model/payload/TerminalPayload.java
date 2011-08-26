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
import regex.view.TerminalNodeView;
import tm.backtrack.BTTimeManager;

public class TerminalPayload extends RegexPayload {
	private String terminal;
	public String getTerminal(){
		return this.terminal;
	}
	
	public void setTerminal(String terminal){
		this.terminal = terminal;
	}

    public TerminalPayload(){
        super(RegexTag.TERMINAL);
        this.setTerminal("");
    }
	public TerminalPayload(String terminal) {
		super(RegexTag.TERMINAL);
		this.setTerminal(terminal);
	}
	
	public TerminalPayload(char terminal){
		super(RegexTag.TERMINAL);
		this.setTerminal(terminal + "");
	}

    /* (non-Javadoc)
     * @see higraph.model.interfaces.Payload#copy()
     */
    @Override
    public TerminalPayload copy() {
        return this;
    }

	/* (non-Javadoc)
	 * @see regex.model.RegexPayload#format(regex.model.RegexNode)
	 */
	@Override
	public String format(RegexNode currentNode) {
		// no child for terminalNode
		return this.terminal;
	}
    
	/* (non-Javadoc)
	 * @see regex.model.RegexPayload#getDescription()
	 */
	@Override
	public String getDescription(){
	    return "\"" + this.terminal + "\"";
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
        return new TerminalNodeView(sgv, node, timeman);
    }
}
