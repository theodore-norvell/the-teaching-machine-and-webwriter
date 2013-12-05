/**
 * STRINGNodeView.java - play.higraph.view - PLAY
 * 
 * Created on 2012-03-21 by Kai Zhu
 */
package play.higraph.view;

import java.awt.Color;

import higraph.view.HigraphView;
import play.executor.Environment;
import play.higraph.model.PLAYEdge;
import play.higraph.model.PLAYEdgeLabel;
import play.higraph.model.PLAYHigraph;
import play.higraph.model.PLAYNode;
import play.higraph.model.PLAYPayload;
import play.higraph.model.PLAYSubgraph;
import play.higraph.model.PLAYWholeGraph;
import tm.backtrack.BTTimeManager;

/**
 * @author Kai Zhu
 * 
 */
public class STRINGNodeView extends PLAYNodeView {

	private String s;
	private Environment e;
	private PLAYNode n;
	private PLAYSubgraph sg;

    /**
     * @param v
     * @param node
     * @param timeMan
     */
    public STRINGNodeView(
	    HigraphView<PLAYPayload, PLAYEdgeLabel, PLAYHigraph, PLAYWholeGraph, PLAYSubgraph, PLAYNode, PLAYEdge> v,
	    PLAYNode node, BTTimeManager timeMan) {
	super(v, node, timeMan);
	super.setNodeSize(120, 30);
	super.setColor(Color.GREEN);
	super.setFillColor(null);
	super.label.setShow(true);
    }
    
    //added by ravneet
    public String execute(Environment env,PLAYNode node,PLAYSubgraph sgraph){
		e = env;
		s = null;
		sg = sgraph;
		n = node;
		
		highlight(n);
		System.out.println("inside num execute");
		int children = n.getNumberOfChildren();
		System.out.println("children = "+children);
		
		if(children==0){
			s = n.getPayload().getPayloadValue();
		}
		
		return s;
		
	}


}
